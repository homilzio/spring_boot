package com.training.springbootbuyitem.controller;

import com.training.springbootbuyitem.controller.interfaces.IBuyController;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RefreshScope
@RestController
public class BuyController implements IBuyController {

	@Autowired
	private ItemService itemService;

//	@RequestMapping("/")
//	public String home(){
//		return "This is what i was looking for";
//	}

	/**
	 * @JavaDoc ModelMapper is a mapping tool easily configurable to accommodate most application defined entities check
	 * some configuration example at: http://modelmapper.org/user-manual/
	 */
	@Autowired
	private ModelMapper mapper;

	@Override
	@PostMapping("/items")
	@ServiceOperation("createItem")
	public ResponseEntity<CreateItemResponseDto> createItem(@RequestBody @Valid CreateItemRequestDto request) {
			return new ResponseEntity<>(mapper.map(itemService.save(mapper.map(request, Item.class)), CreateItemResponseDto.class)
									.add(linkTo(methodOn(BuyController.class).listItems(0,10,"name")).withSelfRel()), HttpStatus.CREATED);
	}


	@Override
	@GetMapping("items/{id}")
	@ServiceOperation("getItem")
	public ResponseEntity<GetItemResponseDto> getItem(@PathVariable("id") Long id) {

		GetItemResponseDto getItemResponseDto = mapper.map(itemService.get(id), GetItemResponseDto.class);

		getItemResponseDto.add(linkTo(methodOn(BuyController.class).listItems(0, 10, "name")).withSelfRel());

			return new ResponseEntity<>(getItemResponseDto, HttpStatus.OK);
	}

	@Override
	@PatchMapping("items/{id}")
	@ServiceOperation("updateItem")
	public ResponseEntity<UpdateItemResponseDto> updateItem(@PathVariable("id") Long id, @RequestBody Item item) {
		item.setItemUid(id);
			return new ResponseEntity<>(mapper.map(itemService.update(item), UpdateItemResponseDto.class), HttpStatus.OK);
	}

	@Override
	@DeleteMapping("items/{id}")
	@ServiceOperation("deleteItem")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<HttpStatus> deleteItem(@PathVariable("id") Long id) {
			itemService.delete(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@Override
	@GetMapping("items/all")
	@ServiceOperation("listItems")
	public ResponseEntity<List<GetItemResponseDto>> listItems(@RequestParam(defaultValue = "0") Integer pageNo,
															  @RequestParam(defaultValue = "10") Integer pageSize,
															  @RequestParam(defaultValue = "name") String sortBy) {

		List<GetItemResponseDto> itemsResponseDto = itemService.list(pageNo, pageSize, sortBy)
																.stream()
																.map(i -> mapper.map(i, GetItemResponseDto.class))
																.collect(Collectors.toList());


		return new ResponseEntity<>(itemsResponseDto.stream()
										.map(this::addGetByIdLink)
										.collect(Collectors.toList()), HttpStatus.OK);
	}


	private GetItemResponseDto addGetByIdLink(GetItemResponseDto getItemResponseDto){

		getItemResponseDto.add(linkTo(methodOn(BuyController.class).getItem(getItemResponseDto.getItemUid())).withSelfRel());

		return getItemResponseDto;
	}

	@Override
	@GetMapping("items/ids")
	@ServiceOperation("listItemsById")
	public ResponseEntity<List<GetItemResponseDto>> listItemsByIds(@RequestBody List<Long> ids) {

		List<Item> items = itemService.get(ids);

		return new ResponseEntity<>(items
				.stream()
				.map(i -> mapper.map(i, GetItemResponseDto.class))
				.collect(Collectors.toList()), HttpStatus.OK);

	}

	@Override
	@PostMapping("items/{id}/dispatch")
	@ServiceOperation("dispatchItem")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<HttpStatus> dispatchItem(@PathVariable("id") Long id,
			@RequestBody DispatchItemRequestDto request) throws Exception {
			itemService.dispatch(id, request.getQuantity());
			return new ResponseEntity<>(HttpStatus.OK);
	
	}

	@Override
	@ServiceOperation("blockItem")
	@RequestMapping(value = "items/{id}/block", method = RequestMethod.POST, produces = "application/json")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<HttpStatus> blockItem(@PathVariable("id") Long id,
			@RequestBody DispatchItemRequestDto request) throws Exception {
			itemService.block(id, request.getQuantity());
			return new ResponseEntity<>(HttpStatus.OK);

	}

	@Override
	@ServiceOperation("blockItem")
	@RequestMapping(value = "items/{id}/{user}/block", method = RequestMethod.POST, produces = "application/json")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<HttpStatus> blockItemForUser(@PathVariable("id") Long id, @PathVariable("user") Long userId,
			@RequestBody DispatchItemRequestDto request) throws Exception {
			itemService.block(id, request.getQuantity());
			return new ResponseEntity<>(HttpStatus.OK);

	}

	@Override
	@PostMapping("items/{id}/restock")
	@ServiceOperation("restockItem")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<HttpStatus> restockItem(@PathVariable("id") Long id,
			@RequestBody RestockItemRequestDto request) throws Exception {
			itemService.restock(id, request.getQuantity());
			return new ResponseEntity<>(HttpStatus.OK);
	}

	@Override
	@PatchMapping("items/updateList")
	@ServiceOperation("updateList")
	public ResponseEntity<List<UpdateItemResponseDto>> updateList(@RequestBody List<Item> items) {
		return new ResponseEntity<>(itemService
				.updateListItems(items)
				.stream()
				.map(i -> mapper.map(i, UpdateItemResponseDto.class))
				.collect(Collectors.toList()), HttpStatus.OK);
	}



}
