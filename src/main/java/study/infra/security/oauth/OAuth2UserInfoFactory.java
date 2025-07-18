package study.infra.security.oauth;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {

        return switch (registrationId.toLowerCase()) {
            case "google" -> new GoogleOAuth2UserInfo(attributes);
            case "naver" -> new NaverOAuth2UserInfo(attributes);
            // case "apple": return new AppleOAuth2UserInfo(attributes);
            default -> throw new IllegalArgumentException("지원하지 않는 소셜 로그인입니다: " + registrationId);
        };

    }
}
