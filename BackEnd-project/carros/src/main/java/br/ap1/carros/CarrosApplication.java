package br.ap1.carros;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;


@SpringBootApplication
@EntityScan(basePackages = "br.ap1.carros.model") // Ajuste o pacote se necessário
public class CarrosApplication {
    public static void main(String[] args) {
        SpringApplication.run(CarrosApplication.class, args);
    }
}
