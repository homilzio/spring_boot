package com.training.springbootbuyitem.repository.interfaces;

import com.training.springbootbuyitem.entity.model.Item;

public interface ItemCustom {

    public Item getItemByID(Long id);
}
