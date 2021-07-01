package com.training.springbootbuyitem.controller;

import com.training.springbootbuyitem.controller.interfaces.IShoppingCartController;
import com.training.springbootbuyitem.entity.model.CustomSessionManagerImpl;
import com.training.springbootbuyitem.entity.model.ShoppingCartItem;
import com.training.springbootbuyitem.entity.request.UpsertShoppingCartItemRequestDto;
import com.training.springbootbuyitem.entity.response.GetShoppingCartItemResponseDto;
import com.training.springbootbuyitem.entity.response.UpsertShoppingCartItemResponseDto;
import com.training.springbootbuyitem.service.CustomSessionService;
import com.training.springbootbuyitem.service.ShoppingCartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RefreshScope
@RestController
public class ShoppingCartController implements IShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private ModelMapper mapper;

   @Autowired
   private CustomSessionService customSessionService;


    @Override
    public ResponseEntity<UpsertShoppingCartItemResponseDto> addShoppingCartItem(Authentication auth, HttpSession session, UpsertShoppingCartItemRequestDto request) throws Exception {
        CustomSessionManagerImpl customSessionManager = customSessionService.getOrCreateCustomSession(auth, session);
        return new ResponseEntity<>(mapper.map(shoppingCartService.create(mapper.map(request, ShoppingCartItem.class),customSessionManager), UpsertShoppingCartItemResponseDto.class), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<UpsertShoppingCartItemResponseDto> updateShoppingCartItem(Authentication auth, HttpSession session, UpsertShoppingCartItemRequestDto request) throws Exception {
        CustomSessionManagerImpl customSessionManager = customSessionService.getOrCreateCustomSession(auth, session);
        return new ResponseEntity<>(mapper.map(shoppingCartService.update(mapper.map(request, ShoppingCartItem.class), customSessionManager), UpsertShoppingCartItemResponseDto.class), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<GetShoppingCartItemResponseDto>> getShoppingCart(Authentication auth, HttpSession session) throws Exception {
        CustomSessionManagerImpl customSessionManager = customSessionService.getOrCreateCustomSession(auth, session);
        return new ResponseEntity<>(shoppingCartService.get(customSessionManager), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<HttpStatus> deleteShoppingCartItem(Long itemID,Authentication auth, HttpSession session) throws Exception {
        CustomSessionManagerImpl customSessionManager = customSessionService.getOrCreateCustomSession(auth, session);
        shoppingCartService.delete(itemID,customSessionManager);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
