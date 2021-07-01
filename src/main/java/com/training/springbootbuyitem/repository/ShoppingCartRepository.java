package com.training.springbootbuyitem.repository;

import com.training.springbootbuyitem.repository.interfaces.ShoppingCartCustom;
import com.training.springbootbuyitem.repository.jpa.ShoppingCartJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ShoppingCartRepository implements ShoppingCartCustom {
    final static Logger logger = LoggerFactory.getLogger(ShoppingCartRepository.class);

    @Autowired
    private ShoppingCartJpaRepository jpaRepository;


}
