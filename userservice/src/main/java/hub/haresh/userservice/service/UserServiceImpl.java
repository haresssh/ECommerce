package hub.haresh.userservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import hub.haresh.userservice.dto.SendEmailDto;
import hub.haresh.userservice.exceptions.InvalidTokenException;
import hub.haresh.userservice.exceptions.UserAlreadyExistsException;
import hub.haresh.userservice.exceptions.UserNotFoundException;
import hub.haresh.userservice.model.Token;
import hub.haresh.userservice.model.User;
import hub.haresh.userservice.repository.TokenRepository;
import hub.haresh.userservice.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private TokenRepository tokenRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private KafkaTemplate<String, String> kafkaTemplate;
    private ObjectMapper objectMapper;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(
            UserRepository userRepository,
            TokenRepository tokenRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            KafkaTemplate kafkaTemplate) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public User signUp(String name, String email, String password) {
        logger.info("SignUp request received for email: {}", email);
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            throw new UserAlreadyExistsException("User already exists with email: " + email);
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setHashedPassword(bCryptPasswordEncoder.encode(password));

        SendEmailDto sendEmailDto = new SendEmailDto();
        sendEmailDto.setFromEmail("hp494343@gmail.com");
        sendEmailDto.setToEmail(email);
        sendEmailDto.setSubject("Welcome");
        sendEmailDto.setBody("Welcome to Haresh Hub");

        String sendEmailDtoString = null;
        try {
            sendEmailDtoString = objectMapper.writeValueAsString(sendEmailDto);
        } catch (Exception ex) {
            logger.error("Something went wrong while converting to string", ex);
        }

        logger.info("Sending email event: {}", sendEmailDtoString);
        kafkaTemplate.send("emailSend", sendEmailDtoString);

        return userRepository.save(user);
    }

    @Override
    public Token login(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User not found with email: " + email);
        }

        User user = userOptional.get();
        if (!bCryptPasswordEncoder.matches(password, user.getHashedPassword())) {
            throw new RuntimeException("Invalid password");
        }

        Token token = createToken(user);
        return tokenRepository.save(token);
    }

    @Override
    @Cacheable(value = "tokens", key = "#tokenValue")
    public User validate(String tokenValue) {
        System.out.println("Validating");
        Optional<Token> tokenOptional = tokenRepository
                .findByValueAndDeletedAndExpiryAtGreaterThan(
                        tokenValue,
                        false,
                        new Date());

        if (tokenOptional.isEmpty()) {
            throw new InvalidTokenException("Invalid or expired token");
        }

        Token token = tokenOptional.get();
        return token.getUser();
    }

    @Override
    public void logout(String tokenValue) {
        Optional<Token> optionalToken = tokenRepository
                .findByValueAndDeleted(tokenValue, false);

        if (optionalToken.isEmpty()) {
            return;
        }

        Token token = optionalToken.get();
        token.setDeleted(true);
        tokenRepository.save(token);
    }

    private Token createToken(User user) {
        Token token = new Token();
        token.setUser(user);
        token.setValue(RandomStringUtils.randomAlphanumeric(128));

        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        Date dateAfter30Days = calendar.getTime();

        token.setExpiryAt(dateAfter30Days);
        token.setDeleted(false);

        return token;
    }

    @Override
    @Cacheable(value = "users", key = "#userId")
    public User getUserDetails(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }
        return userOptional.get();
    }
}
