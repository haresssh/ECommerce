package hub.haresh.userservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendEmailDTO {
    private String fromEmail;
    private String toEmail;
    private String subject;
    private String body;
}
