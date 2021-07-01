package com.training.springbootbuyitem.repository.interfaces;

import com.training.springbootbuyitem.entity.model.User;
import org.springframework.security.core.Authentication;

public interface UserCustom {

    public User getUserByID(Long id);

    public boolean isUserAuthorized(Authentication authentication, Long userId);
}
