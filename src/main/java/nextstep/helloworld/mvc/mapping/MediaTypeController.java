package nextstep.helloworld.mvc.mapping;

import nextstep.helloworld.mvc.domain.User;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/media-type")
public class MediaTypeController {

    @PostMapping(path = "/users", consumes = "application/json")
    public ResponseEntity createUser(@RequestBody User user) {
        Long id = 1L;
        return ResponseEntity.created(URI.create("/users/" + id)).build();
    }

    @GetMapping(path = "/users", produces = "application/json")
    @ResponseBody
    public ResponseEntity<List<User>> showUser() {
        List<User> users = Arrays.asList(
                new User("이름", "email"),
                new User("이름", "email")
        );
        return ResponseEntity.ok().body(users);
    }

    @GetMapping(path = "/users", produces = "text/html")
    @ResponseBody
    public String userPage() {
        System.out.println("==============aaaaaaaaaaaaaaa===============");
        return "user page";
    }
}
