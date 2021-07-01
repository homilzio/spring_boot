package com.training.springbootbuyitem.service.interfaces;

import com.training.springbootbuyitem.entity.model.Item;

public interface IItemService extends ICrudService<Item> {

	void restock(Long id, Integer quantity) throws Exception;

	void dispatch(Long id, Integer quantity) throws Exception;

	void block(Long id, Integer quantity) throws Exception;

}
