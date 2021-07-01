package com.training.springbootbuyitem.entity.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomSessionManagerImpl {
    private User user;
    private List<ShoppingCartItem> shoppingCart;
    private String key;
    private String sessionID;

    static public CustomSessionManagerImpl createCustomSessionForUserOrSession(
            User user,
            List<ShoppingCartItem> shoppingCart,
            HttpSession session,
            Map<String, CustomSessionManagerImpl> customSessionManagerMap) {

        String key = user != null ? user.getUsername() : session.getId();
        CustomSessionManagerImpl customSession = CustomSessionManagerImpl.builder()
                .user(user)
                .shoppingCart(shoppingCart)
                .key(key)
                .sessionID(session.getId())
                .build();
        customSessionManagerMap.put(key, customSession);

        return customSession;
    }

    static public CustomSessionManagerImpl getCustomSessionForUserOrSession(
            Authentication auth,
            HttpSession session,
            Map<String, CustomSessionManagerImpl> customSessionManagerMap) {

        CustomSessionManagerImpl customSession = auth != null ? customSessionManagerMap.get(auth.getName().toLowerCase()) : null;
        if(customSession == null)
            customSession = customSessionManagerMap.get(session.getId());

        return customSession;
    }


}
