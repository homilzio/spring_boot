package com.training.springbootbuyitem.entity.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetShoppingCartItemResponseDto {

    private String name;
    private String description;
    private BigDecimal priceTag;
    private BigInteger quantity;


}
