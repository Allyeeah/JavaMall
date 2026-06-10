package web.mvc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import web.mvc.domain.Customer;
import web.mvc.dto.CustomerRegisterDTO;
import web.mvc.exception.AddException;
import web.mvc.exception.DuplicatedException;
import web.mvc.jwt.JwtUtil;
import web.mvc.repository.CustomerRepository;
import web.mvc.service.CustomerServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    @DisplayName("회원가입 성공")
    void register_success() throws AddException {
        // given
        CustomerRegisterDTO dto = new CustomerRegisterDTO("user1", "1234", "홍길동");
        given(customerRepository.existsById("user1")).willReturn(false);
        given(customerRepository.save(any(Customer.class))).willAnswer(i -> i.getArgument(0));

        // when
        Customer result = customerService.register(dto);

        // then
        assertThat(result.getUserId()).isEqualTo("user1");
        assertThat(result.getUserName()).isEqualTo("홍길동");
    }

    @Test
    @DisplayName("회원가입 실패 - 아이디 중복")
    void register_duplicated() {
        // given
        CustomerRegisterDTO dto = new CustomerRegisterDTO("user1", "1234", "홍길동");
        given(customerRepository.existsById("user1")).willReturn(true);

        // when & then
        assertThatThrownBy(() -> customerService.register(dto))
                .isInstanceOf(DuplicatedException.class)
                .hasMessageContaining("이미 사용 중인 아이디");
    }

    @Test
    @DisplayName("로그인 성공 - JWT 토큰 반환")
    void login_success() {
        // given
        Customer customer = new Customer("user1", "1234", "홍길동", "2026-01-01");
        given(customerRepository.findByUserIdAndUserPwd("user1", "1234")).willReturn(Optional.of(customer));
        given(jwtUtil.generateToken("user1")).willReturn("mock.jwt.token");

        // when
        String token = customerService.login("user1", "1234");

        // then
        assertThat(token).isEqualTo("mock.jwt.token");
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 비밀번호")
    void login_fail() {
        // given
        given(customerRepository.findByUserIdAndUserPwd("user1", "wrong")).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> customerService.login("user1", "wrong"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("아이디 또는 비밀번호");
    }
}
