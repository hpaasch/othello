package com.compozed.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NO_CONTENT, reason="No such user")  // 404
public class UserNotFoundException extends RuntimeException {
}
