package com.training.springbootbuyitem.service.interfaces;

import java.util.List;

public interface ICrudService<T extends Object> {


	List<T> list(Integer pageNo, Integer pageSize, String sortBy);

	T get(Long id);

	List<T> get(List<Long> ids);

	void delete(Long id);

	T update(T entity);

	T save(T entity) throws Exception;

}
