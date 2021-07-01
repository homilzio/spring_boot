package com.training.springbootbuyitem.controller;

import com.training.springbootbuyitem.constant.BuyItemConstant;
import com.training.springbootbuyitem.entity.response.ErrorMessage;
import com.training.springbootbuyitem.error.EntityNotFoundException;
import com.training.springbootbuyitem.error.ForbiddenException;
import com.training.springbootbuyitem.error.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class RestControllerAdvice {

	final static Logger logger = LoggerFactory.getLogger(RestControllerAdvice.class);


	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ErrorMessage> handleNotFoundError(Exception e) {
		logger.error("Handle Entity Not Found Exception Launched");
		String message = "Entity Not Found";
		return buildErrorMessageResponseEntity(message, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorMessage> handleBadRequest(Exception e) {
		logger.error("Handle Method Argument Not Valid Exception Launched");
		String message = "Bad Request";

		if(e.getMessage().contains("CreateUserRequestDto")) message = "Improper Date";

		return buildErrorMessageResponseEntity(message, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorMessage> handleConflict(Exception e) {
		logger.error("Handle Data Integrity Violation Exception Launched");
		String message = "Data Integrity Violation";

		return buildErrorMessageResponseEntity(message, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorMessage> handleInternalError(Exception e) {
		logger.error("Handle Internal Error Exception Launched");
		String message = "Internal Server Error";

		return buildErrorMessageResponseEntity(message, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<ErrorMessage> handleUnauthorizedError(Exception e) {
		logger.error("Handle Internal Error Exception Launched");
		String message = "Unauthorized Access";

		return buildErrorMessageResponseEntity(message, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(InvalidDataAccessApiUsageException.class)
	public ResponseEntity<ErrorMessage> handleInvalidDataAccessException(Exception e) {
		logger.error("Invalid Data Access Exception Launched");
		String message = "Role not properly formatted, please pass the ID";

		return buildErrorMessageResponseEntity(message, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<ErrorMessage> handleForbiddenError(Exception e) {
		logger.error("Handle Forbidden Exception Launched");
		String message = "Forbidden Access";

		return buildErrorMessageResponseEntity(message, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private ResponseEntity<ErrorMessage> buildErrorMessageResponseEntity(String msg, HttpStatus httpStatus) {

		log.error(msg);
		return new ResponseEntity<>(
				ErrorMessage.builder()
						.message(msg)
						.code(httpStatus.value())
						.traceId(MDC.get(BuyItemConstant.TRACE_ID))
						.operation(MDC.get(BuyItemConstant.OPERATION))
						.build(),
				httpStatus);

		//throw new NotImplementedException();
	}

}
