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
    public SignUpResponseDto signup(@RequestBody SignUpRequestDto requestDto) {
        User user = userService.signUp(
                requestDto.getName(),
                requestDto.getEmail(),
                requestDto.getPassword());

        SignUpResponseDto responseDto = new SignUpResponseDto();
        responseDto.setUser(user);

        return responseDto;
    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto loginRequestDto) {
        Token token = userService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
        LoginResponseDto responseDto = new LoginResponseDto();
        responseDto.setToken(token);

        return responseDto;
    }

    @PostMapping("/validate")
    public UserDto validate(@RequestHeader("Authorization") String token) {
        User user = userService.validate(token);
        return UserDto.fromUser(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequestDto logoutRequestDto) {
        userService.logout(logoutRequestDto.getToken());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserDetails(@PathVariable("id") Long id) {
        User user = userService.getUserDetails(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(UserDto.fromUser(user), HttpStatus.OK);
    }
}