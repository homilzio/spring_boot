package com.training.springbootbuyitem.repository;

import com.training.springbootbuyitem.entity.model.Item;
import com.training.springbootbuyitem.enums.EnumEntity;
import com.training.springbootbuyitem.error.EntityNotFoundException;
import com.training.springbootbuyitem.repository.interfaces.ItemCustom;
import com.training.springbootbuyitem.repository.jpa.ItemJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ItemRepository implements ItemCustom {
    final static Logger logger = LoggerFactory.getLogger(ItemRepository.class);

    @Autowired
    private ItemJpaRepository jpaRepository;

    @Override
    public Item getItemByID(Long id) {
        logger.info("Getting Item with id {}", id);
        return jpaRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(EnumEntity.ITEM.name(), id));
    }
}
