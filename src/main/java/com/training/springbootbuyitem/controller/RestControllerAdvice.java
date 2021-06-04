package com.training.springbootbuyitem.controller;

import com.training.springbootbuyitem.constant.BuyItemConstant;
import com.training.springbootbuyitem.entity.response.ErrorMessage;
import com.training.springbootbuyitem.error.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@Slf4j
@ControllerAdvice
public class RestControllerAdvice {


	private static final Logger LOGGER = LoggerFactory.getLogger(RestControllerAdvice.class);
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ErrorMessage> handleNotFoundError(Exception e) {
		//return buildErrorMessageResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
		return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorMessage> handleBadRequest(Exception e) {
		//return buildErrorMessageResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
		return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorMessage> handleConflict(Exception e) {
		//return buildErrorMessageResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
		return new ResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorMessage> handleInternalError(Exception e) {
		//return buildErrorMessageResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private ResponseEntity<ErrorMessage> buildErrorMessageResponseEntity(String msg, HttpStatus httpStatus) {
		LOGGER.error(msg);
		LOGGER.info(msg);
		LOGGER.debug(msg);
		LOGGER.warn(msg, httpStatus.getReasonPhrase());

		return new ResponseEntity<ErrorMessage>();
	}

}
