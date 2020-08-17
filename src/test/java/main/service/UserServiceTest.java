package main.service;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import main.dto.LoginDto;
import main.dto.RegisterForm;
import main.model.Post;
import main.model.Role;
import main.model.User;
import main.model.enums.ModerationStatus;
import main.repository.CaptchaCodeRepository;
import main.repository.PostRepository;
import main.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceTest {

  @Autowired
  UserService userService;

  @MockBean
  UserRepository userRepository;

  @MockBean
  AuthenticationService authenticationService;

  @MockBean
  PostRepository postRepository;

  @MockBean
  MailSender mailSender;

  @MockBean
  CaptchaCodeRepository captchaCodeRepository;

  @Test
  void loadUserByUsername() {
    Mockito.doReturn(Optional.of(new User(1, 1, LocalDateTime.now(), "vanya",
        "some@mail.ru", "123456", "123456", "123.jpr", new Role(1))))
        .when(userRepository).findByEmail("some@mail.ru");
    UserDetails user = userService.loadUserByUsername("some@mail.ru");
    Assertions.assertEquals("some@mail.ru", user.getUsername());
  }


  @Test
  void saveUser() {
    RegisterForm registerForm = new RegisterForm();
    registerForm.setE_mail("tarakan@mail.ru");
    registerForm.setCaptcha("123456");
    registerForm.setCaptcha_secret("123456");
    registerForm.setName("vanya");
    registerForm.setPassword("12345");
    JsonNode jsonNode = userService.saveUser(registerForm);
    Assertions.assertFalse(jsonNode.get("result").asBoolean());
    Assertions.assertEquals("Пароль короче 6-ти символов",
        jsonNode.get("errors").get("password").asText());
  }

  @Test
  void login() {
    Mockito.doReturn(Optional.of(new User(1, 1, LocalDateTime.now(), "vanya",
        "some@mail.ru", "$2a$10$FLwXXL.MI88B.UCf5zgHbek0Qk3k.oSqhzAUyyMPJFkYWOddpuLqu",
        "123456", "123.jpr", new Role(1))))
        .when(userRepository).findByEmail("some@mail.ru");
    LoginDto loginDto = new LoginDto();
    loginDto.setE_mail("some@mail.ru");
    loginDto.setPassword("123456");
    JsonNode jsonNode = userService.login(loginDto);
    Assertions.assertTrue(jsonNode.get("result").asBoolean());
    Assertions.assertTrue(jsonNode.get("user").get("settings").asBoolean());
  }

  @Test
  void check() {
    JsonNode jsonNode = userService.check();
    Assertions.assertFalse(jsonNode.get("result").asBoolean());
  }

  @Test
  void restore() {
    Mockito.doReturn(Optional.of(new User(1, 1, LocalDateTime.now(), "vanya",
        "some@mail.ru", "$2a$10$FLwXXL.MI88B.UCf5zgHbek0Qk3k.oSqhzAUyyMPJFkYWOddpuLqu",
        "123456", "123.jpr", new Role(1))))
        .when(userRepository).findByEmail("some@mail.ru");
    JsonNode jsonNode = userService.restore("some@mail.ru");
    Mockito.verify(mailSender, Mockito.times(1))
        .send("some@mail.ru", "Code", "/login/change-password/123456");
    Assertions.assertTrue(jsonNode.get("result").asBoolean());
  }

  @Test
  void postNewPassword() {
  }

  @Test
  void postNewProfile() {
  }

  @Test
  void getMyStatistics() throws Exception {
    Mockito.doReturn(new User(1, 1, LocalDateTime.now(), "vanya",
        "some@mail.ru", "123456", "123456", "123.jpr", new Role(1)))
        .when(authenticationService).getCurrentUser();
    JsonNode jsonNode = userService.getMyStatistics();
    Assertions.assertEquals(0, jsonNode.get("postsCount").asInt());
  }

  @Test
  void getAllStatistics() {
    Post post = newPost();
    Mockito.doReturn(List.of(post)).when(postRepository).findAll();
    JsonNode jsonNode = userService.getAllStatistics();
    Assertions.assertEquals(2, jsonNode.get("viewsCount").asInt());
  }

  @Test
  void logout() {
  }

  @Test
  void getSettings() {
  }

  @Test
  void putSettings() {
  }

  @Test
  void getCaptcha() throws IOException {
    JsonNode jsonNode = userService.getCaptcha();
    Mockito.verify(captchaCodeRepository, Mockito.times(1))
        .save(ArgumentMatchers.isNotNull());
    Assertions.assertNotNull(jsonNode.get("secret").asText());
  }

  private Post newPost() {
    return new Post(1, 1, ModerationStatus.NEW, new User(1, 1, LocalDateTime.now(), "vanya",
        "some@mail.ru", "123456", "123456", "123.jpr", new Role(1)),
        new User(1, 1, LocalDateTime.now(), "vanya",
            "some@mail.ru", "123456", "123456", "123.jpr", new Role(1)),
        null, LocalDateTime.now(), "Hello World", "SSSSSSSSSSSSSSSSS", 2);
  }
}