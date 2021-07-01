package com.training.springbootbuyitem.service;

import com.training.springbootbuyitem.entity.model.CustomSessionManagerImpl;
import com.training.springbootbuyitem.entity.model.Item;
import com.training.springbootbuyitem.entity.model.ShoppingCartItem;
import com.training.springbootbuyitem.entity.model.User;
import com.training.springbootbuyitem.entity.response.GetShoppingCartItemResponseDto;
import com.training.springbootbuyitem.repository.ItemRepository;
import com.training.springbootbuyitem.repository.ShoppingCartRepository;
import com.training.springbootbuyitem.repository.UserRepository;
import com.training.springbootbuyitem.repository.jpa.ShoppingCartJpaRepository;
import com.training.springbootbuyitem.repository.jpa.UserJpaRepository;
import com.training.springbootbuyitem.service.interfaces.IShoppingCartService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class ShoppingCartService implements IShoppingCartService {

    final static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private ShoppingCartJpaRepository shoppingCartJpaRepository;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;


    @Override
    public List<GetShoppingCartItemResponseDto> get(CustomSessionManagerImpl customSessionManager) throws Exception {

        //Get shopping cart from session
        List<ShoppingCartItem> shoppingCart;
        List<GetShoppingCartItemResponseDto> shoppingCartFormattedItems = new ArrayList<>();
        shoppingCart = customSessionManager.getShoppingCart();
        for (ShoppingCartItem shoppingCartItem : shoppingCart) {
            Item item = itemRepository.getItemByID(shoppingCartItem.getItemId());

            GetShoppingCartItemResponseDto formattedItem = mapper.map(item,GetShoppingCartItemResponseDto.class);
            mapper.map(shoppingCartItem, formattedItem);

            shoppingCartFormattedItems.add(formattedItem);

        }
        return shoppingCartFormattedItems;
    }

    @Override
    public void delete(Long itemId, CustomSessionManagerImpl customSessionManager) throws Exception {

        //Delete from both session and database. Only deletes from database if the user is authenticated
        User user = customSessionManager.getUser();
        List<ShoppingCartItem> updatedListOfShoppingCartItems = new ArrayList<>();
        if(user != null) {
            ShoppingCartItem persistedShoppingCartItem = shoppingCartJpaRepository.findShoppingCartItemsByUserIdAndItemId(user.getPersonId(), itemId);
            logger.warn("Deleting item  {}", persistedShoppingCartItem);
            shoppingCartJpaRepository.delete(persistedShoppingCartItem);
        }
        for(ShoppingCartItem item: customSessionManager.getShoppingCart()){
            if (!item.getItemId().equals(itemId))
                updatedListOfShoppingCartItems.add(item);
        }
        customSessionManager.setShoppingCart(updatedListOfShoppingCartItems);

    }

    @Override
    public ShoppingCartItem update(ShoppingCartItem updatedShoppingCartItem, CustomSessionManagerImpl customSessionManager) throws Exception {

        //Update both session and database. Only updated database if the user is authenticated
        User user = customSessionManager.getUser();
        if(user != null) {
            logger.debug("[DEBUG] Updating Shopping Cart Item with item {}", updatedShoppingCartItem);

            ShoppingCartItem persistedShoppingCartItem = shoppingCartJpaRepository.findShoppingCartItemsByUserIdAndItemId(user.getPersonId(), updatedShoppingCartItem.getItemId());
            persistedShoppingCartItem.setQuantity(updatedShoppingCartItem.getQuantity());

            shoppingCartJpaRepository.saveAndFlush(persistedShoppingCartItem);
        }
        customSessionManager.getShoppingCart().stream()
                .filter(item -> item.getItemId().equals(updatedShoppingCartItem.getItemId()))
                .forEach(item -> item.setQuantity(updatedShoppingCartItem.getQuantity()));
        return updatedShoppingCartItem;
    }

    @Override
    public ShoppingCartItem create(ShoppingCartItem shoppingCartItem, CustomSessionManagerImpl customSessionManager) {

        //Creates an item both in the session and in the database. Only creates in the database if the user is authenticated

        User user = customSessionManager.getUser();
        if(user != null) {
            shoppingCartItem.setUserId(user.getPersonId());
            //Won't call update() because then it would make the same query twice
            ShoppingCartItem updatedShoppingCartItem = shoppingCartJpaRepository.findShoppingCartItemsByUserIdAndItemId(shoppingCartItem.getUserId(), shoppingCartItem.getItemId());

            //If the item already exists it will update the item
            if(updatedShoppingCartItem != null) {
                updatedShoppingCartItem.setQuantity(shoppingCartItem.getQuantity());
                shoppingCartJpaRepository.save(updatedShoppingCartItem);
                customSessionManager.getShoppingCart().stream()
                        .filter(item -> item.getItemId().equals(updatedShoppingCartItem.getItemId()))
                        .forEach(item -> item.setQuantity(updatedShoppingCartItem.getQuantity()));
            } else {
                shoppingCartJpaRepository.save(shoppingCartItem);
                customSessionManager.getShoppingCart().add(shoppingCartItem);
            }
        } else {
            AtomicBoolean updatedItem = new AtomicBoolean(false);
            customSessionManager.getShoppingCart().stream()
                    .filter(item -> item.getItemId().equals(shoppingCartItem.getItemId()))
                    .forEach(item -> {
                        item.setQuantity(shoppingCartItem.getQuantity());
                        updatedItem.set(true);
                    });
            if (!updatedItem.get()) customSessionManager.getShoppingCart().add(shoppingCartItem);
        }
        return shoppingCartItem;

    }






}
