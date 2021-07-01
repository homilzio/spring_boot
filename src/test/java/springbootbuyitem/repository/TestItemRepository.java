package springbootbuyitem.repository;

import com.training.springbootbuyitem.BuyItemApplication;
import com.training.springbootbuyitem.entity.model.Item;
import com.training.springbootbuyitem.repository.jpa.ItemJpaRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;

@SpringBootTest(classes = BuyItemApplication.class)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@Transactional
public class TestItemRepository {

	@Autowired
	private ItemJpaRepository itemRepository;

	@Sql("/delete_all.sql")
	@Test
	public void createItemTest() {
		Item item = new Item("my item");
		item.setPriceTag(BigDecimal.ONE);
		item.setStock(BigInteger.ONE);
		Item itemSaved = itemRepository.save(item);
		Assert.assertEquals (item.getName(), ("my item"));
	}

	// TODO

	//@Test
	//public void getItemsTest() {
	//	assertThat(itemRepository.findAll().size(), is(1));
	//}

/*	@Sql("/delete_all.sql")
	@Test(expected = DataIntegrityViolationException.class)
	public void createDuplicateItemTest() {
		itemRepository.save(Item.builder().name("my item").priceTag(BigDecimal.ONE).stock(BigInteger.ONE).build());
		itemRepository.save(Item.builder().name("my item").priceTag(BigDecimal.ONE).stock(BigInteger.ONE).build());
	}

*/
}
