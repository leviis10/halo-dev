package enigma.halodev.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ApplicationConfig {
    @Value("${cloudinary.url}")
    private String cloudinaryUrl;

    @Bean
    public RestClient restClient() {
        return RestClient.create();
    }

    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(5);
    }

    @Bean
    public Cloudinary cloudinaryConfig() {
        final Cloudinary cloudinary = new Cloudinary(cloudinaryUrl);
        cloudinary.config.secure = true;
        return cloudinary;
    }
}
