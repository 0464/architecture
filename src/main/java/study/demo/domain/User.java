package study.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import study.infra.security.oauth.OAuth2UserInfo;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class User implements OAuth2User {

    private String oauthId;
    private String name;
    private String email;
    private String provider;
    private String uuid;

    public User(OAuth2UserInfo userInfo) {
        this.uuid = UUID.randomUUID().toString();
        this.name = userInfo.getName();
        this.email = userInfo.getEmail();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
}
