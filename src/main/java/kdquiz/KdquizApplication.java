package kdquiz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@EnableScheduling
@SpringBootApplication
public class KdquizApplication {

	public static void main(String[] args) {
		SpringApplication.run(KdquizApplication.class, args);
	}



}
