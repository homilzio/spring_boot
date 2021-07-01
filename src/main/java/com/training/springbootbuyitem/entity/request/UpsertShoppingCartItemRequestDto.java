package com.training.springbootbuyitem.entity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigInteger;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpsertShoppingCartItemRequestDto {

    @NotNull
    @PositiveOrZero
    private Long itemId;
    @NotNull
    @PositiveOrZero
    private BigInteger quantity;
}
