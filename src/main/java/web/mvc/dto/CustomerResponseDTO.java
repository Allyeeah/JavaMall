package web.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import web.mvc.domain.Customer;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDTO {
    private String userId;
    private String userName;
    private String regDate;

    public static CustomerResponseDTO from(Customer customer) {
        return new CustomerResponseDTO(
                customer.getUserId(),
                customer.getUserName(),
                customer.getRegDate()
        );
    }
}
