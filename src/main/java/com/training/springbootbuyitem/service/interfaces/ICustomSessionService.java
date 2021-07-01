package com.training.springbootbuyitem.service.interfaces;

import com.training.springbootbuyitem.entity.model.CustomSessionManagerImpl;
import com.training.springbootbuyitem.error.ForbiddenException;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpSession;
import java.util.Map;

public interface ICustomSessionService {
    CustomSessionManagerImpl getOrCreateCustomSession(Authentication auth, HttpSession session);

    Map<String, CustomSessionManagerImpl> getAllCustomSessions();

    void clearAllSessions();

    void logoutFromSession(Authentication auth, HttpSession session) throws ForbiddenException;

    CustomSessionManagerImpl findCustomSessionBySessionId(String sessionId);
}
