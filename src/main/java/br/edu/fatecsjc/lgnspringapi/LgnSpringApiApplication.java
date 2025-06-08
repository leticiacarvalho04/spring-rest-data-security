package br.edu.fatecsjc.lgnspringapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({
    "br.edu.fatecsjc.lgnspringapi",
    "br.edu.fatecsjc.lgnspringapi.resource.advice"
})
public class LgnSpringApiApplication {

    public static void main(String[] args) {
        runApp(args);
    }

    public static void runApp(String[] args) {
        SpringApplication.run(LgnSpringApiApplication.class, args);
    }
}
