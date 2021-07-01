package com.training.springbootbuyitem.entity.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.math.BigInteger;

@Proxy(lazy = false)
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shopping_cart_items")
public class ShoppingCartItem extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shoppingCartID;

    @Column(name = "user_id")
    @JoinColumn(name = "person_id", referencedColumnName = "user")
    private Long userId;

    @Column(name = "item_id")
    @JoinColumn(name = "item_uid", referencedColumnName = "item")
    private Long itemId;

    @Column(name = "quantity")
    private BigInteger quantity;

    @Override
    public boolean equals(Object object) {
        if (object == this)
            return true;
        if (!(object instanceof ShoppingCartItem))
            return false;
        ShoppingCartItem otherShoppingCartItem = (ShoppingCartItem)object;
        return this.userId.equals(otherShoppingCartItem.getUserId())
                        && this.itemId.equals(otherShoppingCartItem.getItemId())
                        && this.quantity == otherShoppingCartItem.getQuantity();
    }
}
