package springbootbuyitem.service;

import com.training.springbootbuyitem.BuyItemApplication;
import com.training.springbootbuyitem.entity.model.Item;
import com.training.springbootbuyitem.error.EntityNotFoundException;
import com.training.springbootbuyitem.service.ItemService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.assertTrue;

@SpringBootTest(classes = BuyItemApplication.class)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class ItemServiceTest {

	@Autowired
	private ItemService itemService;

	private static final long ID = 1L;


	@Test
	public void save() {

		Item item = Item.builder()
				.name("banana")
				.priceTag(BigDecimal.ONE)
				.stock(BigInteger.valueOf(5))
				.build();


		item = itemService.save(item);

		assertTrue(item.getItemUid() != null);
	}

	@Test(expected = EntityNotFoundException.class)
	public void errorGet() {

		itemService.get(943423L);

	}

	@Test(expected = EntityNotFoundException.class)
	public void notFoundDelete(){

		itemService.delete(943423L);

	}

	@Test(expected = Exception.class)
	public void negativeDispatch() throws Exception {

		Item item = Item.builder()
				.name("item 85")
				.priceTag(BigDecimal.ONE)
				.stock(BigInteger.valueOf(5))
				.build();


		item = itemService.save(item);

		itemService.dispatch(item.getItemUid(), -55);

	}

	@Test(expected = Exception.class)
	public void dispatchQuantityMoreThanCurrentStock() throws Exception {

		Item item = Item.builder()
				.name("item 90")
				.priceTag(BigDecimal.ONE)
				.stock(BigInteger.valueOf(5))
				.build();


		item = itemService.save(item);

		itemService.dispatch(item.getItemUid(), 6);

	}

	@Test
	public void dispatch() throws Exception {

		Item item = Item.builder()
				.name("item 145")
				.priceTag(BigDecimal.ONE)
				.stock(BigInteger.valueOf(5))
				.build();


		item = itemService.save(item);

		itemService.dispatch(item.getItemUid(), 4);

		Item itemUpdated = itemService.get(item.getItemUid());

		Assert.assertTrue(itemUpdated.getStock().intValue() < item.getStock().intValue());

	}

	@Test
	public void block() throws Exception {

		Item item = Item.builder()
				.name("item 225")
				.priceTag(BigDecimal.ONE)
				.stock(BigInteger.valueOf(5))
				.build();


		item = itemService.save(item);

		itemService.block(item.getItemUid(), 4);

		Item itemUpdated = itemService.get(item.getItemUid());

		Assert.assertTrue(itemUpdated.getStock().intValue() < item.getStock().intValue());

	}

	@Test
	public void restock() throws Exception {

		Item item = Item.builder()
				.name("item 92")
				.priceTag(BigDecimal.ONE)
				.stock(BigInteger.valueOf(5))
				.build();


		item = itemService.save(item);

		itemService.restock(item.getItemUid(), 4);

		Item itemUpdated = itemService.get(item.getItemUid());

		Assert.assertTrue(itemUpdated.getStock().intValue() > item.getStock().intValue());

	}

	@Test(expected = Exception.class)
	public void negativeRestock() throws Exception {

		Item item = Item.builder()
				.name("item 757")
				.priceTag(BigDecimal.ONE)
				.stock(BigInteger.valueOf(5))
				.build();


		item = itemService.save(item);

		itemService.restock(item.getItemUid(), -55);

	}


}