package nextstep.helloworld.mvc.mapping;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/param-header")
public class ParamHeaderController {

    @GetMapping(path = "/message")
    public ResponseEntity<String> message() {

        return ResponseEntity.ok().body("message");
    }

    @GetMapping(path = "/message", params = "name=hello")
    public ResponseEntity<String> messageForParam() {
        return ResponseEntity.ok().body("hello");
    }

    @GetMapping(path = "/message", headers = "HEADER=hi")
    public ResponseEntity<String> messageForHeader() {
        return ResponseEntity.ok().body("hi");
    }

    @GetMapping(path = "/message", headers = "HEADER=a", params = "name=b")
    public ResponseEntity<String> messageForHeader2() {
        return ResponseEntity.ok().body("test");
    }
}
