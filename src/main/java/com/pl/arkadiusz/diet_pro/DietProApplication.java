package com.pl.arkadiusz.diet_pro;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;



@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.pl.arkadiusz.diet_pro"})
public class DietProApplication  {

    public static void main(String[] args) {
        SpringApplication.run(DietProApplication.class, args);
    }



}
