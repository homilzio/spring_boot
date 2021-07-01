package com.training.springbootbuyitem.service.interfaces;

import com.training.springbootbuyitem.entity.model.CustomSessionManagerImpl;
import com.training.springbootbuyitem.entity.model.ShoppingCartItem;
import com.training.springbootbuyitem.entity.response.GetShoppingCartItemResponseDto;

import java.util.List;

public interface IShoppingCartService {

    List<GetShoppingCartItemResponseDto> get(CustomSessionManagerImpl customSessionManager) throws Exception;

    void delete(Long itemId, CustomSessionManagerImpl customSessionManager) throws Exception;

    ShoppingCartItem update(ShoppingCartItem updatedShoppingCartItem, CustomSessionManagerImpl customSessionManager) throws Exception;

    ShoppingCartItem create(ShoppingCartItem shoppingCartItem, CustomSessionManagerImpl customSessionManager) throws Exception;
}
