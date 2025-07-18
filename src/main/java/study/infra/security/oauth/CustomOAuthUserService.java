package study.infra.security.oauth;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import study.demo.domain.User;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuthUserService extends DefaultOAuth2UserService {

    private final HttpSession session;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // 사용자 정보를 추상화한 LoginUser 객체 생성
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, oAuth2User.getAttributes()); // 팩토리에서 소셜 별 각각 구현
        // 세션에 저장
        session.setAttribute("loginUser", oAuth2UserInfo);

        return oAuth2UserInfo;
    }
}
