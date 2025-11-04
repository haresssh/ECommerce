package hub.haresh.userservice.controller;


import hub.haresh.userservice.dto.*;
import hub.haresh.userservice.model.Token;
import hub.haresh.userservice.model.User;
import hub.haresh.userservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public SignUpResponseDTO signup(@RequestBody SignUpRequestDTO requestDto) {
        User user = userService.signUp(
                requestDto.getName(),
                requestDto.getEmail(),
                requestDto.getPassword()
        );

        SignUpResponseDTO responseDto = new SignUpResponseDTO();
        responseDto.setUser(user);

        return responseDto;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO loginRequestDto) {
        Token token = userService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
        LoginResponseDTO responseDto = new LoginResponseDTO();
        responseDto.setToken(token);

        return responseDto;
    }

    @PostMapping("/validate")
    public UserDTO validate(@RequestHeader("Authorization") String token) {
        User user = userService.validate(token);
        return UserDTO.fromUser(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequestDTO logoutRequestDto) {
        userService.logout(logoutRequestDto.getToken());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}