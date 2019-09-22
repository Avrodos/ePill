package com.doccuty.epill.authentication;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//see: https://stackoverflow.com/questions/45546/how-do-i-return-a-403-forbidden-in-spring-mvc
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {
	private static final long serialVersionUID = 1L;
}