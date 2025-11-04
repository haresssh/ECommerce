package hub.haresh.userservice.dto;

import hub.haresh.userservice.model.Role;
import hub.haresh.userservice.model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDTO {
    private String name;
    private String email;
    private List<Role> roles;

    public static UserDTO fromUser(User user) {
        if (user == null) {
            return null;
        }

        UserDTO userDto = new UserDTO();
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setRoles(user.getRoles());

        return userDto;
    }
}
