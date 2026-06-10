package web.mvc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.Customer;
import web.mvc.dto.CustomerRegisterDTO;
import web.mvc.exception.AddException;
import web.mvc.exception.DuplicatedException;
import web.mvc.jwt.JwtUtil;
import web.mvc.repository.CustomerRepository;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public Customer register(CustomerRegisterDTO dto) throws AddException {
        if (customerRepository.existsById(dto.getUserId())) {
            throw new DuplicatedException("이미 사용 중인 아이디입니다: " + dto.getUserId());
        }
        Customer customer = new Customer(
                dto.getUserId(),
                dto.getUserPwd(),
                dto.getUserName(),
                LocalDate.now().toString()
        );
        return customerRepository.save(customer);
    }

    @Override
    public String login(String userId, String userPwd) {
        customerRepository.findByUserIdAndUserPwd(userId, userPwd)
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다."));
        return jwtUtil.generateToken(userId);
    }
}
