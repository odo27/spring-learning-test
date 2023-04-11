package nextstep.helloworld.mvc.exceptions;

import nextstep.helloworld.mvc.exceptions.exception.CustomException;
import nextstep.helloworld.mvc.exceptions.exception.HelloException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice(annotations = RestController.class)
public class HelloAdvice {

    @ExceptionHandler({HelloException.class})
    public ResponseEntity<String> handle() {
        return ResponseEntity.badRequest().body("HelloException");
    }

    @ExceptionHandler({CustomException.class})
    public ResponseEntity<String> handle2() {

        return ResponseEntity.badRequest().body("CustomException");
    }
}
