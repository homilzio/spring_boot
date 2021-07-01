package com.training.springbootbuyitem.entity.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessage {

	private String traceId;
	private String operation;
	private int code;
	private String message;

}
