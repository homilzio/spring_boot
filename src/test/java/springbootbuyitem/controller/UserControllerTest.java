package springbootbuyitem.controller;

import com.training.springbootbuyitem.BuyItemApplication;
import com.training.springbootbuyitem.configuration.JwtTokenUtil;
import com.training.springbootbuyitem.entity.model.Role;
import com.training.springbootbuyitem.entity.model.User;
import com.training.springbootbuyitem.enums.EnumUserState;
import com.training.springbootbuyitem.repository.jpa.UserJpaRepository;
import com.training.springbootbuyitem.service.UserDetailsServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = BuyItemApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Before
    public void setup(){

        Set<Role> roles = new HashSet<Role>();

        roles.add(Role.builder()
                .roleId(2)
                .name("USER")
                .build());


        User user = User.builder()
                .name("user teste controller")
                .age(22)
                .state(EnumUserState.ACTIVE)
                .password(encoder.encode("123"))
                .username("user 357 teste controller")
                .roles(roles)
                .build();

        userJpaRepository.save(user);
    }


    @Test
    public void shouldNotAllowAccessToUnauthenticatedUsers() throws Exception {
        mockMvc.perform(get("/users/logoutAllUsers")).andExpect(status().isUnauthorized());
    }

    @Test
    public void validateAuthToken() throws Exception {

        UserDetails userDetails = userDetailsService.loadUserByUsername("user 357 teste controller");

        final String token =  jwtTokenUtil.generateToken(userDetails);

        Assert.assertTrue(jwtTokenUtil.validateToken(token, userDetails));
    }

    @Test
    public void permitAccessWithToken() throws Exception {

        UserDetails userDetails = userDetailsService.loadUserByUsername("user 357 teste controller");

        final String token =  jwtTokenUtil.generateToken(userDetails);

        mockMvc.perform(get("/items/all?pageNo=0&pageSize=10&sortBy=stock").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    public void onlyAdminCanAccess() throws Exception {

        UserDetails userDetails = userDetailsService.loadUserByUsername("user 357 teste controller");

        final String token =  jwtTokenUtil.generateToken(userDetails);

        mockMvc.perform(get("/users/logoutAllUsers").header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    public void authenticateWithWrongCredentials() throws Exception {

        UserDetails userDetails = userDetailsService.loadUserByUsername("user 357 teste controller");

        final String json = "{\"username\":  \""+userDetails.getUsername()+"\", " +
                " \"password\": \"489409840984098\"}";

        mockMvc.perform(post("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                                    .andExpect(status().isInternalServerError());
    }

    @Test
    public void authenticateWithCredentials() throws Exception {

        UserDetails userDetails = userDetailsService.loadUserByUsername("user 357 teste controller");

        final String json = "{\"username\":  \""+userDetails.getUsername()+"\", " +
                " \"password\": \"123\"}";

        mockMvc.perform(post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }

}
