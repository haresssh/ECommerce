package hub.haresh.userservice.service;

import hub.haresh.userservice.model.Token;
import hub.haresh.userservice.model.User;

public interface UserService {
    public User signUp(String name, String email, String password);

    public Token login(String email, String password);

    public User validate(String token);

    public void logout(String token);

    public User getUserDetails(Long userId);
}