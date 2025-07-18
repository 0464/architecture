package study.demo.domain;

import lombok.Data;
import study.infra.security.oauth.OAuth2UserInfo;

@Data
public class User {

    private String id;
    private String name;
    private String email;

    public User(OAuth2UserInfo userInfo) {
        this.id = userInfo.getId();
        this.name = userInfo.getName();
        this.email = userInfo.getEmail();
    }
}
