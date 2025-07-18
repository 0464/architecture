package study.infra.security.oauth;

import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public interface OAuth2UserInfo extends OAuth2User {

    String getId();
    String getName();
    String getEmail();
    Map<String, Object> getAttributes();

}
