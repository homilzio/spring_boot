package springbootbuyitem.repository;

import com.training.springbootbuyitem.BuyItemApplication;
import com.training.springbootbuyitem.entity.model.ShoppingCartItem;
import com.training.springbootbuyitem.repository.ShoppingCartRepository;
import com.training.springbootbuyitem.repository.jpa.ShoppingCartJpaRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;

@SpringBootTest(classes = BuyItemApplication.class)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@Transactional
public class TestShoppingCartRepository {
    @Autowired
    private ShoppingCartJpaRepository shoppingCartJpaRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;


    @Sql("/sql_test_shopping_cart.sql")
    @Test
    public void findShoppingCartItemsByUserIDTest() {
        Long USER_ID = 1L;
        String expected = "6:10|3:50|1:10|"; // Item_id:quantity|Item_id:quantity

        List<ShoppingCartItem> shoppingCartItemList = shoppingCartJpaRepository.findShoppingCartItemsByUserID(USER_ID);
        StringBuilder result = new StringBuilder();
        for(ShoppingCartItem shoppingCartItem: shoppingCartItemList){
            result.append(shoppingCartItem.getItemId()).append(":").append(shoppingCartItem.getQuantity()).append("|");
        }
        Assert.assertEquals(expected, result.toString());
    }

    @Sql("/sql_test_shopping_cart.sql")
    @Test
    public void findShoppingCartItemsByUserIdAndItemIdTest() {
        Long USER_ID = 1L;
        Long ITEM_ID = 6L;
        String expected = "6:10"; // Item_id:quantity|Item_id:quantity

        ShoppingCartItem shoppingCartItemList = shoppingCartJpaRepository.findShoppingCartItemsByUserIdAndItemId(USER_ID,ITEM_ID);
        Assert.assertEquals(expected, shoppingCartItemList.getItemId() + ":" + shoppingCartItemList.getQuantity());
    }

    @Sql("/sql_test_shopping_cart.sql")
    @Test(expected = IncorrectResultSizeDataAccessException.class)
    public void findShoppingCartItemsByUserIdAndItemIdTestException() {
        Long USER_ID = 1L;
        Long ITEM_ID = 6L;
        String expected = "6:10"; // Item_id:quantity|Item_id:quantity

        ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
        shoppingCartItem.setUserId(USER_ID);
        shoppingCartItem.setItemId(ITEM_ID);
        shoppingCartItem.setQuantity(new BigInteger("30"));
        shoppingCartJpaRepository.saveAndFlush(shoppingCartItem);

        ShoppingCartItem shoppingCartItemList = shoppingCartJpaRepository.findShoppingCartItemsByUserIdAndItemId(USER_ID,ITEM_ID);
        Assert.assertEquals(expected, shoppingCartItemList.getItemId() + ":" + shoppingCartItemList.getQuantity());
    }

}
