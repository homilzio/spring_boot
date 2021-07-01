package com.training.springbootbuyitem.service;

import com.training.springbootbuyitem.entity.model.Item;
import com.training.springbootbuyitem.enums.EnumEntity;
import com.training.springbootbuyitem.enums.EnumItemState;
import com.training.springbootbuyitem.error.EntityNotFoundException;
import com.training.springbootbuyitem.repository.jpa.ItemJpaRepository;
import com.training.springbootbuyitem.service.interfaces.IItemService;
import com.training.springbootbuyitem.utils.properties.ItemStorageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService implements IItemService {

	final static Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private ItemJpaRepository itemRepository;

	@Autowired
	private ItemStorageProperties itemStorageProperties;

	/**
	 * @JavaDoc RestTemplate is a synchronous Http Client which is supported by Pivotal development team take into
	 * consideration this client is deprecated and shall not be supported for LTS use instead the newly Http Client
	 * WebClient which is capable of synchronous & asynchronous invocations check some code samples at:
	 * https://spring.io/guides/gs/consuming-rest/
	 */
	@Autowired
	private RestTemplate restTemplate;

	@Override
	@Cacheable("items")
	public List<Item> list(Integer pageNo, Integer pageSize, String sortBy) {
		logger.info("Find all items");

		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

		Page<Item> pageResult = itemRepository.findAll(paging);

		return pageResult.hasContent() ? pageResult.getContent() : new ArrayList<Item>();
	}



	@Override
	@Cacheable("items")
	public Item get(Long id) {
		logger.info("Getting item with id {}", id);
		return itemRepository.findById(id).orElseThrow(() ->
				new EntityNotFoundException(EnumEntity.ITEM.name(), id));
	}

	@Override
	@Cacheable("items")
	public List<Item> get(List<Long> ids) {
		logger.info("Getting items with ids {}", ids);
		return itemRepository.findAllById(ids);
	}

	@CacheEvict(value="items",  allEntries = true)
	public List<Item> updateListItems(List<Item> items){

		logger.debug("Updating items {}", items);

		return items.parallelStream()
				.map(this::update)
				.collect(Collectors.toList());
	}

	@Override
	@CacheEvict(value="items",  allEntries = true)
	public void delete(Long id) {
		logger.warn("Deleting item with id {}", id);
		itemRepository.delete(get(id));
	}

	@Override
	@CacheEvict(value="items",  allEntries = true)
	public Item update(Item item) {

		logger.debug("[DEBUG] Updating item {}", item);

		Item persistedItem = get(item.getItemUid());
		if (!StringUtils.hasText(item.getName())) {
			persistedItem.setName(item.getName());
		}
		if (!StringUtils.isEmpty(item.getDescription())) {
			persistedItem.setDescription(item.getDescription());
		}
		if (!StringUtils.isEmpty(item.getMarket())) {
			persistedItem.setMarket(item.getMarket());
		}
		if (item.getStock() != null && item.getStock().intValue() >= 0) {
			persistedItem.setStock(item.getStock());
		}
		if (item.getPriceTag() != null && item.getPriceTag().longValue() >= 0.0) {
			persistedItem.setPriceTag(item.getPriceTag());
		}
		save(persistedItem);
		return persistedItem;
	}

	@Override
	@CacheEvict(value="items",  allEntries = true)
	public Item save(Item item) {
		logger.info("Setting item with id {} state to AVAILABLE", item.getItemUid());
		item.setState(EnumItemState.AVAILABLE.name());
		return itemRepository.save(item);
	}


	@Override
	@CacheEvict(value="items",  allEntries = true)
	public void restock(Long id, Integer quantity) throws Exception {
		Item item = get(id);

		if(quantity < 0)
			throw  new Exception("Quantity can't be negative");

		item.setStock(item.getStock().add(BigInteger.valueOf(quantity)));
		save(item);
	}

	//TODO create the dispatch method that use "quantity"  items from item stock for the item represented by id
	@Override
	@CacheEvict(value="items",  allEntries = true)
	public void dispatch(Long id, Integer quantity) throws Exception {
		logger.warn("Dispatching item with id {}", id);
		Item item = get(id);

		if(quantity < 0)
			throw  new Exception("Quantity can't be negative");
		if(quantity > item.getStock().intValue())
			throw new Exception("Quantity informed ("+quantity+") can't be greater than current stock ("+item.getStock().intValue()+")");

		item.setStock(item.getStock().subtract(BigInteger.valueOf(quantity)));
		save(item);
	}

	@Override
	@CacheEvict(value="items",  allEntries = true)
	public void block(Long id, Integer quantity) throws Exception {
		logger.warn("Blocking item with id {}",id);
		Item item = get(id);

		if(quantity < 0)
			throw  new Exception("Quantity can't be negative");
		if(quantity > item.getStock().intValue())
			throw new Exception("Quantity informed ("+quantity+") can't be greater than current stock ("+item.getStock().intValue()+")");

		item.setStock(item.getStock().subtract(BigInteger.valueOf(quantity)));
		save(item);
	}
}
