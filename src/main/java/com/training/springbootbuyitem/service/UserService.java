package com.training.springbootbuyitem.service;

import com.training.springbootbuyitem.entity.model.CustomSessionManagerImpl;
import com.training.springbootbuyitem.entity.model.User;
import com.training.springbootbuyitem.enums.EnumUserState;
import com.training.springbootbuyitem.repository.UserRepository;
import com.training.springbootbuyitem.repository.jpa.UserJpaRepository;
import com.training.springbootbuyitem.service.interfaces.IUserService;
import com.training.springbootbuyitem.utils.properties.ItemStorageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserService implements IUserService {

    final static Logger logger = LoggerFactory.getLogger(UserService.class);


    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemStorageProperties itemStorageProperties; //Let's use the same one for now since it's just notification and email stuff

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomSessionService customSessionService;

    /**
     * @JavaDoc RestTemplate is a synchronous Http Client which is supported by Pivotal development team take into
     * consideration this client is deprecated and shall not be supported for LTS use instead the newly Http Client
     * WebClient which is capable of synchronous & asynchronous invocations check some code samples at:
     * https://spring.io/guides/gs/consuming-rest/
     */
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<User> list(Integer pageNo, Integer pageSize, String sortBy) {
        logger.info("Find all users");

        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<User> pageResult = userJpaRepository.findAll(paging);

        return pageResult.hasContent() ? pageResult.getContent() : new ArrayList<User>();
    }

    @Override
    public User get(Long id) {
        return userRepository.getUserByID(id);
    }

    @Override
    public List<User> get(List<Long> ids) {
        logger.info("Listing all users with ids {}", ids);
        return userJpaRepository.findAllById(ids);
    }

    @Override
    public void delete(Long id) {
        logger.warn("Deleting user with id {}", id);
        userJpaRepository.delete(get(id));
    }

    @Override
    public User update(User updatedUser) {
        logger.debug("[DEBUG] Updating user with user {}", updatedUser);

        User persistedUser = get(updatedUser.getPersonId());
        if (StringUtils.hasText(updatedUser.getName())) {
            persistedUser.setName(updatedUser.getName());
        }
        if (updatedUser.getAge() != -1) {
            persistedUser.setAge(updatedUser.getAge());
        }
        if(StringUtils.hasText(updatedUser.getState().name())){
            persistedUser.setState(updatedUser.getState());
        }

        if(userJpaRepository.countByUsername(updatedUser.getUsername()) == 0)
            persistedUser.setUsername(updatedUser.getUsername());
        persistedUser.setRoles(updatedUser.getRoles());
        persistedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));

        userJpaRepository.save(persistedUser);
        return persistedUser;
    }

    @Override
    public User save(User entity) throws Exception {
        logger.info("Setting user with id {} state to active", entity.getPersonId());
        entity.setState(EnumUserState.ACTIVE);
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        if(userJpaRepository.countByUsername(entity.getUsername()) == 0)
            return userJpaRepository.save(entity);
        else
            throw new Exception("User already Exists");
    }


    @Override
    public String userInfo(CustomSessionManagerImpl customSessionManager) {
        logger.info("Showing authenticated user");
        StringBuilder msg = new StringBuilder();
        msg.append("Authenticated User Or Session: ").append(customSessionManager.getKey()).append("\n");
        msg.append("\nAll Authenticated Users or Sessions: \n");
        for(Map.Entry<String, CustomSessionManagerImpl> session : customSessionService.getAllCustomSessions().entrySet()){
            msg.append(session.getKey()).append("\n");
        }
        return msg.toString();
    }


}
