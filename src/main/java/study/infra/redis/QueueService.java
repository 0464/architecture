package study.infra.redis;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueueService {

    private final RedisTemplate<String, String> redisTemplate;
    private final Map<String, SseEmitter> clients = new ConcurrentHashMap<>(); // 클라이언트별 emitter 저장소

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> task;

    private static final String QUEUE_NAME = "queue-stack";
    private static final String ALLOWED_NAME = "allowed-users";

    /**
     * 클라이언트가 연결 요청하면 Emitter 생성 후 반환
     */
    public SseEmitter subscribe(String userId) {

        SseEmitter emitter = new SseEmitter(600_000L); // 10분동안 유효
        clients.put(userId, emitter);
        emitter.onTimeout(() -> clients.remove(userId));
        emitter.onCompletion(() -> clients.remove(userId));

        try {
            String connectMessage = "✅ 연결됨";
            broadcast("connect", connectMessage, emitter); // SSE 전송
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }

    /**
     * 사용자를 대기열에 추가
     */
    public void joinQueue(String userId) {

        if (!isUserInQueue(userId)) {
            redisTemplate.opsForList().rightPush(QUEUE_NAME, userId);
        }
    }

    /**
     * 사용자 대기열 여부 확인
     */
    public boolean isUserInQueue(String userId) {

        List<String> queue = redisTemplate.opsForList().range(QUEUE_NAME, 0, -1);
        return queue != null && queue.contains(userId);
    }

    /**
     * 스케쥴러 정지
     */
    public void schedulerStop() {
        if (isRunning()) {
            task.cancel(true);
            log.debug("🔴 큐 소비 중지됨");
        } else {
            log.debug("⚠️ 중지할 작업 없음");
        }
    }

    /**
     * 뷰 이동 결정
     */
    public boolean view(String userId) {

        // 대기순번 큐에서 사라졌어도, 사전 등록된 허용 목록에서 검증
        Boolean allowed = redisTemplate.opsForSet().isMember(ALLOWED_NAME, userId);

        if (Boolean.TRUE.equals(allowed)) {
            redisTemplate.opsForSet().remove(ALLOWED_NAME, userId);
        }

        return Boolean.TRUE.equals(allowed);
    }

    // 테스트용 큐 데이터이니 나중에 init 클래스 삭제
    @PostConstruct
    public void init() {
        // 기존 데이터 제거
        redisTemplate.delete(QUEUE_NAME);
        // 50개의 랜덤 UUID를 큐에 추가
        IntStream.range(0, 10).forEach(i -> {
            String randomUserId = UUID.randomUUID().toString();
            redisTemplate.opsForList().rightPush(QUEUE_NAME, randomUserId);
        });
    }

    /**
     * 대기열 스케쥴러 시작
     */
    public void processQueue() {
        if (!isRunning()) {
            task = scheduler.scheduleAtFixedRate(() -> {
                Long queueSize = redisTemplate.opsForList().size(QUEUE_NAME);
                String userId = redisTemplate.opsForList().leftPop(QUEUE_NAME);

                // 큐가 비어있지 않으면
                if (userId != null) {

                    final String globalMessage = "남은 대기순번: " + queueSize;
                    // 모든 클라이언트에게 대기순번 전송
                    clients.forEach((clientId, emitter) -> {
                        try {
                            emitter.send(SseEmitter.event().data(globalMessage)); // SSE 전송
                        }
                        catch (IOException e) {
                            // 대기순번 전송에 문제가 생기면
                            emitter.complete();
                            clients.remove(clientId);
                        }
                    });

                    SseEmitter emitter = clients.get(userId);
                    // 클라이언트 연결이 끊기지 않았다면
                    if (emitter != null) {
                        try {
                            String allowedMessage = "✅ 입장 가능";
                            broadcast("move", allowedMessage, emitter); // SSE 전송
                            redisTemplate.opsForSet().add(ALLOWED_NAME, userId); // 현재 순서의 클라이언트를 접속 권한 허용 리스트에 추가
                            clients.remove(userId); // 사용 완료 후 연결 제거
                        } catch (IOException e) {
                            // 입장가능 전송에 문제가 생기면
                            emitter.completeWithError(e);
                            clients.remove(userId);
                        }
                    }
                    // 연결이 끊긴 클라이언트의 큐를 삭제
                    clients.remove(userId);
                }

            }, 0, 1, TimeUnit.SECONDS); // 1초 간격 스케쥴러
        }
    }

    /**
     * 사용자 순서 확인: 맨 앞인지?
     */
    public boolean isTurn(String userId, String queueName) {
        String first = redisTemplate.opsForList().index(queueName, 0);
        return userId.equals(first);
    }

    /**
     * 클라이언트에게 메시지를 이벤트리스너로 전송
     */
    public void broadcast(String mid, String message, SseEmitter emitter) throws IOException {
        emitter.send(SseEmitter.event().name(mid).data(message));
    }

    /**
     * 스케줄러 실행 여부 반환
     * @return {@code boolean}
     */
    public boolean isRunning() {
        return task != null && !task.isCancelled() && !task.isDone();
    }

}
