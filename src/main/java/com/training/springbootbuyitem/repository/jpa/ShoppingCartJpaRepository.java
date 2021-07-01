package com.training.springbootbuyitem.repository.jpa;

import com.training.springbootbuyitem.entity.model.ShoppingCartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCartJpaRepository extends JpaRepository<ShoppingCartItem, Long> {

    @Query("SELECT s FROM ShoppingCartItem s WHERE s.userId = ?1")
    List<ShoppingCartItem> findShoppingCartItemsByUserID(Long userID);

    @Query("SELECT s FROM ShoppingCartItem s WHERE s.userId = ?1 and s.itemId = ?2")
    ShoppingCartItem findShoppingCartItemsByUserIdAndItemId(Long userID, Long itemID);

    @Query("SELECT s FROM ShoppingCartItem s")
    List<ShoppingCartItem> findAllShoppingCartItems();

}
