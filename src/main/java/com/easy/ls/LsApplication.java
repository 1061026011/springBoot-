package com.easy.ls;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@ServletComponentScan
@Controller
public class LsApplication {

	public static void main(String[] args) {
		SpringApplication.run(LsApplication.class, args);
	}

	@RequestMapping("/toOracle")
	public String toOracle(){
		return "test/test";
	}
}
