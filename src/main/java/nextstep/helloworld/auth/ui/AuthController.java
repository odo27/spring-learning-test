package nextstep.helloworld.auth.ui;

import nextstep.helloworld.auth.application.AuthService;
import nextstep.helloworld.auth.application.AuthorizationException;
import nextstep.helloworld.auth.dto.AuthInfo;
import nextstep.helloworld.auth.dto.MemberResponse;
import nextstep.helloworld.auth.dto.TokenRequest;
import nextstep.helloworld.auth.dto.TokenResponse;
import nextstep.helloworld.auth.infrastructure.AuthorizationExtractor;
import nextstep.helloworld.auth.infrastructure.BasicAuthorizationExtractor;
import nextstep.helloworld.auth.infrastructure.BearerAuthorizationExtractor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@RestController
public class AuthController {
    private static final String SESSION_KEY = "USER";
    private static final String USERNAME_FIELD = "email";
    private static final String PASSWORD_FIELD = "password";
    private AuthorizationExtractor<String> bearerAuthorizationExtractor = new BearerAuthorizationExtractor();
    private AuthorizationExtractor<AuthInfo> basicAuthorizationExtractor = new BasicAuthorizationExtractor();
    private AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * ex) request sample
     * <p>
     * POST /login/session HTTP/1.1
     * content-type: application/x-www-form-urlencoded; charset=ISO-8859-1
     * host: localhost:55477
     * <p>
     * email=email@email.com&password=1234
     */
    @PostMapping("/login/session")
    public ResponseEntity sessionLogin(HttpServletRequest request, HttpSession session) {
        Map<String, String[]> paramMap = request.getParameterMap();
        String email = paramMap.get(USERNAME_FIELD)[0];
        String password = paramMap.get(PASSWORD_FIELD)[0];

        if (authService.checkInvalidLogin(email, password)) {
            throw new AuthorizationException();
        }

        session.setAttribute(SESSION_KEY, email);
        return ResponseEntity.ok().build();
    }

    /**
     * ex) request sample
     * <p>
     * GET /members/me HTTP/1.1
     * cookie: JSESSIONID=E7263AC9557EF658C888F02EEF840A19
     * accept: application/json
     */
    @GetMapping("/members/me")
    public ResponseEntity findMyInfo(HttpSession session) {
        String email = (String) session.getAttribute(SESSION_KEY);
        MemberResponse member = authService.findMember(email);
        return ResponseEntity.ok().body(member);
    }

    /**
     * ex) request sample
     * <p>
     * POST /login/token HTTP/1.1
     * accept: application/json
     * content-type: application/json; charset=UTF-8
     * <p>
     * {
     * "email": "email@email.com",
     * "password": "1234"
     * }
     */
    @PostMapping("/login/token")
    public ResponseEntity tokenLogin(@RequestBody TokenRequest tokenRequest) {
        TokenResponse tokenResponse = authService.createToken(tokenRequest);
        return ResponseEntity.ok().body(tokenResponse);
    }

    /**
     * ex) request sample
     * <p>
     * GET /members/you HTTP/1.1
     * authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbEBlbWFpbC5jb20iLCJpYXQiOjE2MTAzNzY2NzIsImV4cCI6MTYxMDM4MDI3Mn0.Gy4g5RwK1Nr7bKT1TOFS4Da6wxWh8l97gmMQDgF8c1E
     * accept: application/json
     */
    @GetMapping("/members/you")
    public ResponseEntity findYourInfo(HttpServletRequest request) {
        String token = bearerAuthorizationExtractor.extract(request);
        MemberResponse member = authService.findMemberByToken(token);
        return ResponseEntity.ok().body(member);
    }

    /**
     * ex) request sample
     * <p>
     * GET /members/my HTTP/1.1
     * authorization: Basic ZW1haWxAZW1haWwuY29tOjEyMzQ=
     * accept: application/json
     */
    @GetMapping("/members/my")
    public ResponseEntity findMyInfo(HttpServletRequest request) {
        AuthInfo authInfo = basicAuthorizationExtractor.extract(request);
        String email = authInfo.getEmail();
        String password = authInfo.getPassword();

        if (authService.checkInvalidLogin(email, password)) {
            throw new AuthorizationException();
        }

        MemberResponse member = authService.findMember(email);
        return ResponseEntity.ok().body(member);
    }
}
