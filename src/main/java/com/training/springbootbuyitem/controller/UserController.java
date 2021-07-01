package com.training.springbootbuyitem.controller;

import com.training.springbootbuyitem.controller.interfaces.IUserController;
import com.training.springbootbuyitem.entity.model.CustomSessionManagerImpl;
import com.training.springbootbuyitem.entity.model.User;
import com.training.springbootbuyitem.entity.request.CreateUserRequestDto;
import com.training.springbootbuyitem.entity.response.GetUserResponseDto;
import com.training.springbootbuyitem.entity.response.UpdateUserResponseDto;
import com.training.springbootbuyitem.error.ForbiddenException;
import com.training.springbootbuyitem.service.CustomSessionService;
import com.training.springbootbuyitem.service.UserService;
import com.training.springbootbuyitem.utils.annotation.ServiceOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@RefreshScope
@RestController
public class UserController implements IUserController {


    @Autowired
    private UserService userService;


    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CustomSessionService customSessionService;

    @RequestMapping("/users")
    public String home(){
        return "Users Go Here";
    }


    @Override
    @RequestMapping("/users/createUser")
    public ResponseEntity<CreateUserRequestDto> createUser(CreateUserRequestDto request) throws Exception {

        return new ResponseEntity<>(mapper.map(userService.save(mapper.map(request, User.class)), CreateUserRequestDto.class), HttpStatus.CREATED);
    }

    @Override
    @GetMapping("/users/allUsers")
    @ServiceOperation("listUsers")
    public ResponseEntity<List<GetUserResponseDto>> listUsers(@RequestParam(defaultValue = "0") Integer pageNo,
                                                              @RequestParam(defaultValue = "10") Integer pageSize,
                                                              @RequestParam(defaultValue = "name") String sortBy) {
        return new ResponseEntity<>(userService.list(pageNo, pageSize, sortBy).stream().map(i -> mapper.map(i, GetUserResponseDto.class)).collect(Collectors.toList()), HttpStatus.OK);
    }

    @Override
    @PatchMapping("/users/updateUser/{id}")
    @ServiceOperation("updateUser")
    public ResponseEntity<UpdateUserResponseDto> updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        user.setPersonId(id);
        return new ResponseEntity<>(mapper.map(userService.update(user), UpdateUserResponseDto.class), HttpStatus.OK);
    }

    @RequestMapping("/users/infoAuth")
    @ServiceOperation("userInfo")
    public String userInfo(Authentication auth, HttpSession session) {
        CustomSessionManagerImpl customSessionManager = customSessionService.getOrCreateCustomSession(auth, session);
        return userService.userInfo(customSessionManager);
    }

    @PostMapping("/users/logoutAllUsers")
    @ServiceOperation("logoutAllUsers")
    public void logoutAllUsers() {
        customSessionService.clearAllSessions();
    }

    @PostMapping("/users/logout")
    @ServiceOperation("logout")
    public void logout(Authentication auth, HttpSession session) throws ForbiddenException {
        customSessionService.logoutFromSession(auth, session);
    }



}
