package web.mvc.service;

import web.mvc.domain.Customer;
import web.mvc.dto.CustomerRegisterDTO;
import web.mvc.exception.AddException;

public interface CustomerService {
    Customer register(CustomerRegisterDTO dto) throws AddException;
    String login(String userId, String userPwd); // JWT 토큰 반환
}
