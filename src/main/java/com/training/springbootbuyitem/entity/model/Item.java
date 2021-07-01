package com.training.springbootbuyitem.entity.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Proxy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
@Proxy(lazy = false)
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Item extends Auditable implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long itemUid;
	@Column(unique = true)
	private String name;
	private String state;
	private String description;
	private String market;
	private BigInteger stock;
	private BigDecimal priceTag;

	public Item(String name){
		this.name = name;
	}
	public Long getItemUid() {
		return itemUid;
	}

	public void setItemUid(Long itemUid) {
		this.itemUid = itemUid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDescription() {
		return description;
	}

	public String getMarket() {
		return market;
	}

	public void setMarket(String market) {
		this.market = market;
	}

	public BigInteger getStock() {
		return stock;
	}

	public void setStock(BigInteger stock) {
		this.stock = stock;
	}

	public BigDecimal getPriceTag() {
		return priceTag;
	}

	public void setPriceTag(BigDecimal priceTag) {
		this.priceTag = priceTag;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
