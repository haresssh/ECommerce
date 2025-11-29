package hub.haresh.userservice.dto;

import hub.haresh.userservice.model.Token;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto {
    private Token token;
}