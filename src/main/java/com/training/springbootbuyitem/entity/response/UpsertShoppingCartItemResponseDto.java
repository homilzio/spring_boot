package com.training.springbootbuyitem.entity.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpsertShoppingCartItemResponseDto {

    private Long itemID;
    private BigInteger quantity;
}
