package com.training.springbootbuyitem.controller.interfaces;

import com.training.springbootbuyitem.entity.request.UpsertShoppingCartItemRequestDto;
import com.training.springbootbuyitem.entity.response.GetShoppingCartItemResponseDto;
import com.training.springbootbuyitem.entity.response.UpsertShoppingCartItemResponseDto;
import com.training.springbootbuyitem.utils.annotation.ServiceOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

public interface IShoppingCartController {

    @PostMapping("/shoppingCart/addShoppingCartItem")
    @ServiceOperation("addShoppingCartItem")
    ResponseEntity<UpsertShoppingCartItemResponseDto> addShoppingCartItem(Authentication auth, HttpSession session, @RequestBody @Valid UpsertShoppingCartItemRequestDto request) throws Exception;

    @PatchMapping("/shoppingCart/updateShoppingCartItem")
    @ServiceOperation("updateShoppingCartItem")
    ResponseEntity<UpsertShoppingCartItemResponseDto> updateShoppingCartItem(Authentication auth, HttpSession session, @RequestBody @Valid UpsertShoppingCartItemRequestDto request) throws Exception;

    @GetMapping("/shoppingCart/getShoppingCart")
    @ServiceOperation("getShoppingCart")
    ResponseEntity<List<GetShoppingCartItemResponseDto>> getShoppingCart(Authentication auth, HttpSession session) throws Exception;

    @DeleteMapping("/shoppingCart/deleteShoppingCartItem/{itemID}")
    @ServiceOperation("deleteShoppingCartItem")
    ResponseEntity<HttpStatus> deleteShoppingCartItem(@PathVariable("itemID") Long itemID, Authentication auth, HttpSession session) throws Exception;
}
