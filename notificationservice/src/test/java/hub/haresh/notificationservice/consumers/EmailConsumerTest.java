package hub.haresh.notificationservice.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hub.haresh.notificationservice.dtos.SendEmailDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailConsumerTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private EmailConsumer emailConsumer;

    private SendEmailDto sendEmailDto;

    @BeforeEach
    void setUp() {
        sendEmailDto = new SendEmailDto();
        sendEmailDto.setFromEmail("test@example.com");
        sendEmailDto.setToEmail("user@example.com");
        sendEmailDto.setSubject("Test Subject");
        sendEmailDto.setBody("Test Body");
    }

    @Test
    void handleSendEmailEvent_Success() throws JsonProcessingException {
        String message = "{\"fromEmail\":\"test@example.com\",\"toEmail\":\"user@example.com\",\"subject\":\"Test Subject\",\"body\":\"Test Body\"}";
        when(objectMapper.readValue(message, SendEmailDto.class)).thenReturn(sendEmailDto);

        emailConsumer.handleSendEmailEvent(message);

        verify(objectMapper, times(1)).readValue(message, SendEmailDto.class);
    }

    @Test
    void handleSendEmailEvent_JsonProcessingException() throws JsonProcessingException {
        String message = "invalid-json";
        when(objectMapper.readValue(message, SendEmailDto.class)).thenThrow(JsonProcessingException.class);

        emailConsumer.handleSendEmailEvent(message);

        verify(objectMapper, times(1)).readValue(message, SendEmailDto.class);
    }
}
