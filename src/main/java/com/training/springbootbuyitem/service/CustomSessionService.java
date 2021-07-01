package com.training.springbootbuyitem.service;


import com.training.springbootbuyitem.entity.model.CustomSessionManagerImpl;
import com.training.springbootbuyitem.entity.model.ShoppingCartItem;
import com.training.springbootbuyitem.entity.model.User;
import com.training.springbootbuyitem.error.ForbiddenException;
import com.training.springbootbuyitem.repository.jpa.ShoppingCartJpaRepository;
import com.training.springbootbuyitem.repository.jpa.UserJpaRepository;
import com.training.springbootbuyitem.service.interfaces.ICustomSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomSessionService implements ICustomSessionService {

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private ShoppingCartJpaRepository shoppingCartJpaRepository;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private Map<String, CustomSessionManagerImpl> customSessionManagerMap;

    @Override
    public CustomSessionManagerImpl getOrCreateCustomSession(Authentication auth, HttpSession session) {
        CustomSessionManagerImpl customSession = CustomSessionManagerImpl.getCustomSessionForUserOrSession(auth, session, customSessionManagerMap);
        if (customSession == null) {
            User user = null;
            List<ShoppingCartItem> shoppingcart = new ArrayList<>();
            if (auth != null) {
                user = userJpaRepository.findUserByUsername(auth.getName().toLowerCase());
                shoppingcart = shoppingCartJpaRepository.findShoppingCartItemsByUserID(user.getPersonId());
            }
            customSession = CustomSessionManagerImpl.createCustomSessionForUserOrSession(user, shoppingcart, session, customSessionManagerMap);
        }
        return customSession;
    }

    @Override
    public Map<String, CustomSessionManagerImpl> getAllCustomSessions() {
        return customSessionManagerMap;
    }

    @Override
    public void clearAllSessions() {
        customSessionManagerMap = new HashMap<>();
    }

    @Override
    public void logoutFromSession(Authentication auth, HttpSession session) throws ForbiddenException {
        if (auth != null) {
            CustomSessionManagerImpl customSessionManager = getOrCreateCustomSession(auth, session);
            String key = customSessionManager.getKey();
            customSessionManagerMap.remove(key);
        } else {
            throw new ForbiddenException("Unauthenticated Users cannot Logout");
        }
    }

    @Override
    public CustomSessionManagerImpl findCustomSessionBySessionId(String sessionId) {
        for (Map.Entry<String, CustomSessionManagerImpl> customSession : customSessionManagerMap.entrySet()) {
            if (customSession.getValue().getSessionID().equalsIgnoreCase(sessionId))
                return customSession.getValue();
        }
        return null;
    }
}


