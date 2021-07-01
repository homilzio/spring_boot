package com.training.springbootbuyitem.repository;

import com.training.springbootbuyitem.entity.model.User;
import com.training.springbootbuyitem.enums.EnumEntity;
import com.training.springbootbuyitem.error.EntityNotFoundException;
import com.training.springbootbuyitem.repository.interfaces.UserCustom;
import com.training.springbootbuyitem.repository.jpa.UserJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository implements UserCustom {
    final static Logger logger = LoggerFactory.getLogger(UserRepository.class);

    @Autowired
    private UserJpaRepository jpaRepository;

    @Override
    public boolean isUserAuthorized(Authentication authentication, Long userId){
        String requesterUsername = authentication.getName();
        String shoppingCartUsername = getUserByID(userId).getUsername();
        return requesterUsername.equalsIgnoreCase(shoppingCartUsername);
    }

    @Override
    public User getUserByID(Long id) {
        logger.info("Getting user with id {}", id);
        return jpaRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(EnumEntity.USER.name(), id));
    }
}
