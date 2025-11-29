package hub.haresh.notificationservice.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hub.haresh.notificationservice.dtos.SendEmailDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class EmailConsumer {

    private ObjectMapper objectMapper;

    public EmailConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "emailSend", groupId = "notification-service")
    public void handleSendEmailEvent(String message) {
        try {
            SendEmailDto sendEmailDTO = objectMapper.readValue(message, SendEmailDto.class);
            System.out.println("Received email event:");
            System.out.println(sendEmailDTO.toString());
            // In a real application, we would send the email here using JavaMailSender
        } catch (JsonProcessingException e) {
            System.out.println("Error processing message: " + message);
            e.printStackTrace();
        }
    }
}
