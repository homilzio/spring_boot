package springbootbuyitem.service;

import com.training.springbootbuyitem.BuyItemApplication;
import com.training.springbootbuyitem.entity.model.CustomSessionManagerImpl;
import com.training.springbootbuyitem.entity.model.ShoppingCartItem;
import com.training.springbootbuyitem.entity.response.GetShoppingCartItemResponseDto;
import com.training.springbootbuyitem.repository.jpa.ShoppingCartJpaRepository;
import com.training.springbootbuyitem.service.CustomSessionService;
import com.training.springbootbuyitem.service.ShoppingCartService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;

@SpringBootTest(classes = BuyItemApplication.class)
@RunWith(SpringRunner.class)
@Transactional
@ActiveProfiles("test")
public class ShoppingCartServiceTest {

    @Autowired
    private ShoppingCartJpaRepository shoppingCartJpaRepository;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private CustomSessionService customSessionService;

    @WithMockUser(username = "Oretoh", password = "password", roles = "USER")
    @Sql("/sql_test_shopping_cart.sql")
    @Test
    public void getFromUser() throws Exception {
        String expected = "item 6:10|item 3:50|item 1:10|";
        Long USER_ID = 1L;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MockHttpSession session = new MockHttpSession();

        customSessionService.logoutFromSession(auth, session);
        CustomSessionManagerImpl customSessionManager = customSessionService.getOrCreateCustomSession(auth, session);

        List<GetShoppingCartItemResponseDto> output = shoppingCartService.get(customSessionManager);
        StringBuilder result = new StringBuilder();
        for(GetShoppingCartItemResponseDto shoppingCartItem: output){
            result.append(shoppingCartItem.getName()).append(":").append(shoppingCartItem.getQuantity()).append("|");
        }

        Assert.assertEquals(expected, result.toString());
    }

    //@WithMockUser(username = "Oretoh", password = "password", roles = "USER")
    //@Sql("/sql_test_shopping_cart.sql")
    //@Test(expected = UnauthorizedException.class)
    //public void getFromUserUnauthorized() throws Exception {
    //    Long USER_ID = 2L;
    //    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    //    MockHttpSession session = new MockHttpSession();
//
    //    CustomSessionManagerImpl customSessionManager = customSessionService.getOrCreateCustomSession(auth, session);
    //    List<GetShoppingCartItemResponseDto> output = shoppingCartService.get(customSessionManager);
    //}

    @WithMockUser(username = "Oretoh", password = "password", roles = "USER")
    @Sql("/sql_test_shopping_cart.sql")
    @Test
    public void deleteEntry() throws Exception {
        String expected = "item 3:50|item 1:10|";
        Long USER_ID = 1L;
        Long ITEM_ID = 6L;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MockHttpSession session = new MockHttpSession();

        customSessionService.logoutFromSession(auth, session);
        CustomSessionManagerImpl customSessionManager = customSessionService.getOrCreateCustomSession(auth, session);

        shoppingCartService.delete(ITEM_ID,customSessionManager);
        List<GetShoppingCartItemResponseDto> output = shoppingCartService.get(customSessionManager);
        StringBuilder result = new StringBuilder();
        for(GetShoppingCartItemResponseDto shoppingCartItem: output){
            result.append(shoppingCartItem.getName()).append(":").append(shoppingCartItem.getQuantity()).append("|");
        }

        Assert.assertEquals(expected, result.toString());
    }

    //@WithMockUser(username = "Oretoh", password = "password", roles = "USER")
    //@Sql("/sql_test_shopping_cart.sql")
    //@Test(expected = UnauthorizedException.class)
    //public void deleteEntryUnauthorized() throws Exception {
    //    Long USER_ID = 2L;
    //    Long ITEM_ID = 6L;
    //    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//
    //    shoppingCartService.delete(USER_ID,ITEM_ID,auth);
    //}

    @WithMockUser(username = "Oretoh", password = "password", roles = "USER")
    @Sql("/sql_test_shopping_cart.sql")
    @Test
    public void updateItem() throws Exception {
        String expected =  "item 6:30|item 3:50|item 1:10|";;
        Long USER_ID = 1L;
        Long ITEM_ID = 6L;
        BigInteger QUANTITY = new BigInteger("30");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MockHttpSession session = new MockHttpSession();

        customSessionService.logoutFromSession(auth, session);
        CustomSessionManagerImpl customSessionManager = customSessionService.getOrCreateCustomSession(auth, session);

        ShoppingCartItem updatedShoppingCartItem =  ShoppingCartItem.builder()
                .userId(USER_ID)
                .itemId(ITEM_ID)
                .quantity(QUANTITY)
                .build();
        shoppingCartService.update(updatedShoppingCartItem, customSessionManager);
        List<GetShoppingCartItemResponseDto> output = shoppingCartService.get(customSessionManager);
        StringBuilder result = new StringBuilder();
        for(GetShoppingCartItemResponseDto shoppingCartItem: output){
            result.append(shoppingCartItem.getName()).append(":").append(shoppingCartItem.getQuantity()).append("|");
        }

        Assert.assertEquals(expected, result.toString());
    }

    //@WithMockUser(username = "Oretoh", password = "password", roles = "USER")
    //@Sql("/sql_test_shopping_cart.sql")
    //@Test(expected = UnauthorizedException.class)
    //public void updateItemUnauthorized() throws Exception {
    //    Long USER_ID = 2L;
    //    Long ITEM_ID = 6L;
    //    BigInteger QUANTITY = new BigInteger("30");
    //    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//
    //    ShoppingCartItem updatedShoppingCartItem =  ShoppingCartItem.builder()
    //            .userId(USER_ID)
    //            .itemId(ITEM_ID)
    //            .quantity(QUANTITY)
    //            .build();
    //    shoppingCartService.update(updatedShoppingCartItem, auth);
    //}

    @WithMockUser(username = "Oretoh", password = "password", roles = "USER")
    @Sql("/sql_test_shopping_cart.sql")
    @Test
    public void addItem() throws Exception {
        String expected = "item 6:10|item 3:50|item 1:10|item 4:17|";
        Long USER_ID = 1L;
        Long ITEM_ID = 4L;
        BigInteger QUANTITY = new BigInteger("17");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MockHttpSession session = new MockHttpSession();

        customSessionService.logoutFromSession(auth, session);
        CustomSessionManagerImpl customSessionManager = customSessionService.getOrCreateCustomSession(auth, session);

        ShoppingCartItem inputShoppingCartItem =  ShoppingCartItem.builder()
                .userId(USER_ID)
                .itemId(ITEM_ID)
                .quantity(QUANTITY)
                .build();
        shoppingCartService.create(inputShoppingCartItem, customSessionManager);
        List<GetShoppingCartItemResponseDto> output = shoppingCartService.get(customSessionManager);
        StringBuilder result = new StringBuilder();
        for(GetShoppingCartItemResponseDto shoppingCartItem: output){
            result.append(shoppingCartItem.getName()).append(":").append(shoppingCartItem.getQuantity()).append("|");
        }
        System.out.println("[OUTPUT]"+result.toString());
        Assert.assertEquals(expected, result.toString());
    }

    //@WithMockUser(username = "Oretoh", password = "password", roles = "USER")
    //@Sql("/sql_test_shopping_cart.sql")
    //@Test(expected = UnauthorizedException.class)
    //public void addItemUnauthorized() throws Exception {
    //    Long USER_ID = 2L;
    //    Long ITEM_ID = 4L;
    //    BigInteger QUANTITY = new BigInteger("17");
    //    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//
    //    ShoppingCartItem inputShoppingCartItem =  ShoppingCartItem.builder()
    //            .userId(USER_ID)
    //            .itemId(ITEM_ID)
    //            .quantity(QUANTITY)
    //            .build();
    //    shoppingCartService.create(inputShoppingCartItem, auth);
//
    //}
}
