package co.edu.uniquindio.talleruno;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;

import java.util.Scanner;

@SpringBootApplication
public class Cliente implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(Cliente.class);

	private final IntegracionService integracionService;

	public Cliente(IntegracionService integracionService) {
		this.integracionService = integracionService;
	}

	public static void main(String[] args) {
		SpringApplication.run(Cliente.class, args);
	}

	@Override
	public void run(String... args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Por favor, ingrese su nombre: ");
		String nombre = "Alejandro" ;

		try {
			String token = integracionService.obtenerToken(nombre);
			logger.info("Token obtenido: {}", token);

			String saludo = integracionService.obtenerSaludo(nombre, token.trim());
			logger.info("Respuesta del servicio de saludo: {}", saludo);

			System.out.println("Respuesta final: " + saludo);
		} catch (Exception e) {
			logger.error("Error al procesar la solicitud", e);
			System.out.println("Ocurrió un error al procesar su solicitud.");
		}
	}
}