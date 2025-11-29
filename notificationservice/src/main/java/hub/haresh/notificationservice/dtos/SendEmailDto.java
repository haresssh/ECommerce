package hub.haresh.notificationservice.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SendEmailDto {
    private String fromEmail;
    private String toEmail;
    private String subject;
    private String body;
}
