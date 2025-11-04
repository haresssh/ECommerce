package hub.haresh.userservice.security.service;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import hub.haresh.userservice.model.Role;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@JsonDeserialize
@Setter
@NoArgsConstructor
public class CustomGrantedAuthority implements GrantedAuthority {
    private String authority;

    public CustomGrantedAuthority(Role role) {
        this.authority = role.getName();
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
