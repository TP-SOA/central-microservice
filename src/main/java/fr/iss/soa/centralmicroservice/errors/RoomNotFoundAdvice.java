package fr.iss.soa.centralmicroservice.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RoomNotFoundAdvice {

	@ResponseBody
	@ExceptionHandler(RoomNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String roomNotFoundHandler(RoomNotFoundException e) {
		return e.getMessage();
	}
}
