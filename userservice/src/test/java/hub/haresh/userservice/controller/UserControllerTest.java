package hub.haresh.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hub.haresh.userservice.dto.SignUpRequestDto;
import hub.haresh.userservice.model.User;
import hub.haresh.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
    }

    @Test
    void signup_Success() throws Exception {
        SignUpRequestDto requestDto = new SignUpRequestDto();
        requestDto.setName("Test User");
        requestDto.setEmail("test@example.com");
        requestDto.setPassword("password");

        when(userService.signUp(anyString(), anyString(), anyString())).thenReturn(user);

        mockMvc.perform(post("/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.id").value(1L))
                .andExpect(jsonPath("$.user.name").value("Test User"));
    }

    @Test
    void getUserDetails_Success() throws Exception {
        when(userService.getUserDetails(1L)).thenReturn(user);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void getUserDetails_NotFound() throws Exception {
        when(userService.getUserDetails(1L)).thenReturn(null);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isNotFound());
    }
}
