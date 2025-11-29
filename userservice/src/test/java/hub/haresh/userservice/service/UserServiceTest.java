package hub.haresh.userservice.service;

import hub.haresh.userservice.model.User;
import hub.haresh.userservice.repository.TokenRepository;
import hub.haresh.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setHashedPassword("hashedPassword");
    }

    @Test
    void signUp_Success() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(bCryptPasswordEncoder.encode("password")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.signUp("Test User", "test@example.com", "password");

        assertNotNull(result);
        assertEquals("Test User", result.getName());
        assertEquals("test@example.com", result.getEmail());
        verify(kafkaTemplate, times(1)).send(eq("emailSend"), anyString());
    }

    @Test
    void signUp_UserAlreadyExists() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        User result = userService.signUp("Test User", "test@example.com", "password");

        assertNull(result);
        verify(userRepository, never()).save(any(User.class));
        verify(kafkaTemplate, never()).send(anyString(), anyString());
    }

    @Test
    void getUserDetails_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserDetails(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getUserDetails_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        User result = userService.getUserDetails(1L);

        assertNull(result);
    }
}
