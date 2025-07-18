package study.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CommonMapConverter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 단일 객체 변환
     */
    public CommonMap convertToMap(Object entity) {
        Map<String, Object> map = objectMapper.convertValue(entity, new TypeReference<>() {});
        return new CommonMap(map);
    }

    /**
     * 리스트 변환
     */
    public List<CommonMap> convertToMapList(List<?> entityList) {
        return entityList.stream()
                .map(this::convertToMap)
                .collect(Collectors.toList());
    }
}
