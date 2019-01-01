package site.pushy.landlords;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("site.pushy.landlords.dao")
public class LandlordsApplication {

	public static void main(String[] args) {
		SpringApplication.run(LandlordsApplication.class, args);
	}

}

