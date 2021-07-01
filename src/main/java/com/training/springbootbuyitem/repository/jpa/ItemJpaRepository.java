package com.training.springbootbuyitem.repository.jpa;

import com.training.springbootbuyitem.entity.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemJpaRepository extends JpaRepository<Item, Long> {

}
