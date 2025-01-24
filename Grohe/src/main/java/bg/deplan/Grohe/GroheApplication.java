package bg.deplan.Grohe;

import bg.deplan.Grohe.model.DTOs.PreOrderDTO;
import bg.deplan.Grohe.model.DTOs.PreOrderExcelDTO;
import bg.deplan.Grohe.service.PreOrderService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;


@SpringBootApplication
public class GroheApplication {

	public static void main(String[] args) {

		SpringApplication.run(GroheApplication.class, args);

	}
}
