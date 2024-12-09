package edutech.backend;

import edutech.backend.config.JwtConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(JwtConfig.class)  // Enable the binding of properties in JwtConfig
@SpringBootApplication  // Automatically scans the package 'education.plateform.edutech'
public class EduTechBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EduTechBackendApplication.class, args);
	}
}
