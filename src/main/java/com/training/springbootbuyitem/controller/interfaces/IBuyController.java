package com.training.springbootbuyitem.controller.interfaces;

import com.training.springbootbuyitem.entity.model.Item;
import com.training.springbootbuyitem.entity.request.CreateItemRequestDto;
import com.training.springbootbuyitem.entity.request.DispatchItemRequestDto;
import com.training.springbootbuyitem.entity.request.RestockItemRequestDto;
import com.training.springbootbuyitem.entity.response.CreateItemResponseDto;
import com.training.springbootbuyitem.entity.response.GetItemResponseDto;
import com.training.springbootbuyitem.entity.response.UpdateItemResponseDto;
import com.training.springbootbuyitem.utils.annotation.ServiceOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

public interface IBuyController {

	@PostMapping
	@ServiceOperation("createItem")
	ResponseEntity<CreateItemResponseDto> createItem(@RequestBody @Valid CreateItemRequestDto request);

	@GetMapping("items/{id}")
	@ServiceOperation("getItem")
	ResponseEntity<GetItemResponseDto> getItem(@PathVariable("id") Long id);

	@PatchMapping("items/{id}")
	@ServiceOperation("updateItem")
	ResponseEntity<UpdateItemResponseDto> updateItem(@PathVariable("id") Long id, @RequestBody Item item);

	@DeleteMapping("items/{id}")
	@ServiceOperation("deleteItem")
	ResponseEntity<HttpStatus> deleteItem(@PathVariable("id") Long id);

	@GetMapping
	@ServiceOperation("listItems")
	ResponseEntity<List<GetItemResponseDto>> listItems(@RequestParam(defaultValue = "0") Integer pageNo,
													   @RequestParam(defaultValue = "10") Integer pageSize,
													   @RequestParam(defaultValue = "name") String sortBy);

	@GetMapping
	@ServiceOperation("listItemsByIds")
	ResponseEntity<List<GetItemResponseDto>> listItemsByIds(List<Long> ids);

	@PostMapping("items/{id}/dispatch")
	@ServiceOperation("dispatchItem")
	ResponseEntity<HttpStatus> dispatchItem(@PathVariable("id") Long id,
                                            @RequestBody DispatchItemRequestDto request) throws Exception;

	@PostMapping("items/{id}/blockItem")
	@ServiceOperation("blockItem")
	ResponseEntity<HttpStatus> blockItem(@PathVariable("id") Long id,
                                            @RequestBody DispatchItemRequestDto request) throws Exception;

	@PostMapping("items/{id}/{user}/blockItemForUser")
	@ServiceOperation("blockItemForUser")
	ResponseEntity<HttpStatus> blockItemForUser(@PathVariable("id") Long id, @PathVariable("user") Long userId,
                                            @RequestBody DispatchItemRequestDto request) throws Exception;

	@PostMapping("items/{id}/restock")
	@ServiceOperation("restockItem")
	ResponseEntity<HttpStatus> restockItem(@PathVariable("id") Long id,
                                           @RequestBody RestockItemRequestDto request) throws Exception;

	@PatchMapping("items/updateList")
	@ServiceOperation("updateList")
	ResponseEntity<List<UpdateItemResponseDto>> updateList(List<Item> items);


}
