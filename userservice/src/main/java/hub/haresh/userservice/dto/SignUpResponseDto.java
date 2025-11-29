package hub.haresh.userservice.dto;

import hub.haresh.userservice.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpResponseDto {
    private User user;
}
