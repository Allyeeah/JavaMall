package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.mvc.domain.Customer;
import web.mvc.dto.CustomerRegisterDTO;
import web.mvc.dto.LoginRequest;
import web.mvc.exception.AddException;
import web.mvc.service.CustomerService;

import java.util.Map;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody CustomerRegisterDTO dto) throws AddException {
        Customer customer = customerService.register(dto);
        return ResponseEntity.ok(Map.of(
                "message", "회원가입 성공",
                "userId", customer.getUserId()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        String token = customerService.login(req.getUserId(), req.getUserPwd());
        return ResponseEntity.ok(Map.of(
                "message", "로그인 성공",
                "token", token
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // JWT는 서버에서 무효화 불가 - 클라이언트가 토큰을 삭제해야 함
        return ResponseEntity.ok(Map.of("message", "로그아웃 되었습니다. 클라이언트에서 토큰을 삭제하세요."));
    }
}
