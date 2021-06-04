package com.training.springbootbuyitem.controller;

import com.training.springbootbuyitem.entity.model.Item;
import com.training.springbootbuyitem.entity.request.CreateItemRequestDto;
import com.training.springbootbuyitem.entity.request.DispatchItemRequestDto;
import com.training.springbootbuyitem.entity.request.RestockItemRequestDto;
import com.training.springbootbuyitem.entity.response.CreateItemResponseDto;
import com.training.springbootbuyitem.entity.response.GetItemResponseDto;
import com.training.springbootbuyitem.entity.response.UpdateItemResponseDto;
import com.training.springbootbuyitem.service.ItemService;
import com.training.springbootbuyitem.utils.annotation.ServiceOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RefreshScope
@RestController
public class BuyController implements IBuyController {

	@Autowired
	private ItemService itemService;

	@RequestMapping("/")
	public String home(){
		return "This is what i was looking for";
	}

	/**
	 * @JavaDoc ModelMapper is a mapping tool easily configurable to accommodate most application defined entities check
	 * some configuration example at: http://modelmapper.org/user-manual/
	 */
	@Autowired
	private ModelMapper mapper;

	@Override
	@PostMapping("/createitem")
	@ServiceOperation("createItem")
	public ResponseEntity<CreateItemResponseDto> createItem(@RequestBody @Valid CreateItemRequestDto request) {
			return new ResponseEntity<>(mapper.map(itemService.save(mapper.map(request, Item.class)), CreateItemResponseDto.class), HttpStatus.CREATED);
	}

	@Override
	@GetMapping("/{id}")
	@ServiceOperation("getItem")
	public ResponseEntity<GetItemResponseDto> getItem(@PathVariable("id") Long id) {
			return new ResponseEntity<>(mapper.map(itemService.get(id), GetItemResponseDto.class), HttpStatus.OK);
	}

	@Override
	@PatchMapping("/{id}")
	@ServiceOperation("updateItem")
	public ResponseEntity<UpdateItemResponseDto> updateItem(@PathVariable("id") Long id, @RequestBody Item item) {
		item.setItemUid(id);
			return new ResponseEntity<>(mapper.map(itemService.update(item), UpdateItemResponseDto.class), HttpStatus.OK);
	}

	@Override
	@DeleteMapping("/{id}")
	@ServiceOperation("deleteItem")
	public ResponseEntity<HttpStatus> deleteItem(@PathVariable("id") Long id) {
			itemService.delete(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@Override
	@GetMapping("/all")
	@ServiceOperation("listItems")
	public ResponseEntity<List<GetItemResponseDto>> listItems() {
		return new ResponseEntity<>(itemService.list().stream().map(i -> mapper.map(i, GetItemResponseDto.class)).collect(
				Collectors.toList()), HttpStatus.OK);
	}

	@Override
	@PostMapping("/{id}/dispatch")
	@ServiceOperation("dispatchItem")
	public ResponseEntity<HttpStatus> dispatchItem(@PathVariable("id") Long id,
			@RequestBody DispatchItemRequestDto request) {
			itemService.dispatch(id, request.getQuantity());
			return new ResponseEntity<>(HttpStatus.OK);
	
	}

	@Override
	@ServiceOperation("blockItem")
	@RequestMapping(value = "/{id}/block", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<HttpStatus> blockItem(@PathVariable("id") Long id,
			@RequestBody DispatchItemRequestDto request) {
			itemService.block(id, request.getQuantity());
			return new ResponseEntity<>(HttpStatus.OK);

	}

	@Override
	@ServiceOperation("blockItem")
	@RequestMapping(value = "/{id}/{user}/block", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<HttpStatus> blockItemForUser(@PathVariable("id") Long id, @PathVariable("user") Long userId,
			@RequestBody DispatchItemRequestDto request) {
			itemService.block(id, request.getQuantity());
			return new ResponseEntity<>(HttpStatus.OK);

	}

	@Override
	@PostMapping("/{id}/restock")
	@ServiceOperation("restockItem")
	public ResponseEntity<HttpStatus> restockItem(@PathVariable("id") Long id,
			@RequestBody RestockItemRequestDto request) {
			itemService.restock(id, request.getQuantity());
			return new ResponseEntity<>(HttpStatus.OK);
	}

}
