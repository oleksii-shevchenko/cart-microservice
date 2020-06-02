package dev.flanker.cart.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "A targeted cart is empty.")
public class EmptyCartException extends RuntimeException {
}
