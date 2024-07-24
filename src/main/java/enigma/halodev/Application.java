package enigma.halodev;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Configuration
    public static class CloudinaryConfig {
		@Value("${cloudinary.url}")
		private String cloudinaryUrl;

		@Bean
		public Cloudinary cloudinaryConfig() {
			final Cloudinary cloudinary = new Cloudinary(cloudinaryUrl);
			cloudinary.config.secure = true;
			return cloudinary;
		}
	}
}
