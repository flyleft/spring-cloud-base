package me.jcala.consumer.ribbon.controller;

import me.jcala.consumer.ribbon.TestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author flyleft
 */
@RestController
public class GatewayTestController {

    private static final Logger logger = LoggerFactory.getLogger(GatewayTestController.class);

    private TestClient testClient;

    @Autowired
    public GatewayTestController(TestClient testClient) {
        this.testClient = testClient;
    }

    @GetMapping("/oauth/api/user")
    public ResponseEntity<String> test() {
        return new ResponseEntity<>("{\"error\": \"invalid_token\",\"error_description\": \"Invalid access token: f82898a8-b515-4aeb-a564-36aa9c92c443\"}", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/consumer/hello")
    public String hello() {
        return "abc";
    }

    @PostMapping("/v1/users/{id}")
    public String user(@PathVariable long id, HttpServletRequest request, @RequestHeader HttpHeaders headers, @RequestBody String body) {
        System.out.println(body);
        return ":" + id;
    }

}
