package springbootbuyitem.service;

import com.training.springbootbuyitem.BuyItemApplication;
import com.training.springbootbuyitem.entity.model.User;
import com.training.springbootbuyitem.enums.EnumUserState;
import com.training.springbootbuyitem.error.EntityNotFoundException;
import com.training.springbootbuyitem.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = BuyItemApplication.class)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UserServiceTest {


    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder encoder;


    @Test
    public void listAll(){
        Assert.assertTrue(userService.list(0,10, "name").size() > 0);
    }

    @Test
    public void save() throws Exception {

        User user = User.builder()
                        .name("user 357")
                        .age(22)
                        .state(EnumUserState.ACTIVE)
                        .password(encoder.encode("123"))
                        .username("user 357")
                        .build();

        user = userService.save(user);

        Assert.assertTrue(user.getPersonId() != null && user.getPersonId() > 0);
    }

    @Test(expected = Exception.class)
    public void saveUserAlreadyExist() throws Exception {

        User user = User.builder()
                .name("user 11")
                .age(22)
                .state(EnumUserState.ACTIVE)
                .password(encoder.encode("123"))
                .username("user")
                .build();

        user = userService.save(user);

        User anotherUser = User.builder()
                            .name("user 11")
                            .age(22)
                            .state(EnumUserState.ACTIVE)
                            .password(encoder.encode("123"))
                            .username("user")
                            .build();

        anotherUser = userService.save(anotherUser);

    }


    @Test(expected = EntityNotFoundException.class)
    public void delete(){
        userService.delete(983409832L);
    }


}
