package com.training.springbootbuyitem.entity.response;

import com.training.springbootbuyitem.entity.model.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateItemResponseDto extends RepresentationModel<CreateItemResponseDto> {

	private Long itemUid;

	public Long getItemUid() {
		return itemUid;
	}

	public void setItemUid(Long itemUid) {
		this.itemUid = itemUid;
	}
}
