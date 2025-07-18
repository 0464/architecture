package study.infra.security.oauth;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class NaverOAuth2UserInfo implements OAuth2UserInfo {

    private final Map<String, Object> response;

    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        this.response = (Map<String, Object>) attributes.get("response");
    }

    @Override
    public String getId() {
        return response.get("id").toString();
    }

    @Override
    public String getName() {
        return response.get("name").toString();
    }

    @Override
    public String getEmail() {
        return response.get("email").toString();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return response;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
}
