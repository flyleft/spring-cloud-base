package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableEurekaClient
public class WsServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WsServerApplication.class, args);
	}

	@GetMapping("/say")
	public String sayWs(){
		return "websocket";
	}

}
