package study.demo.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "access_logs")
@NoArgsConstructor
public class AccessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ip;
    private String uri;
    private String method;
    private String userAgent;
    private String queryParams;
    private LocalDateTime timestamp;

    public AccessLog(String ip, String uri, String method, String userAgent, String queryParams) {
        this.ip = ip;
        this.uri = uri;
        this.method = method;
        this.userAgent = userAgent;
        this.queryParams = queryParams;
        this.timestamp = LocalDateTime.now();
    }
}
