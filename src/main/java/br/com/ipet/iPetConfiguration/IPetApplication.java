package br.com.ipet.iPetConfiguration;

import br.com.ipet.Property.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@Configuration
@ComponentScan(basePackages = {"br.com.ipet.Security.JWT", "br.com.ipet.iPetConfiguration", "br.com.ipet.Services", "br.com.ipet.Controllers"})
@EnableConfigurationProperties({
        FileStorageProperties.class
})
@EnableJpaRepositories(basePackages = "br.com.ipet.Repository")
@EntityScan("br.com.ipet.Models")
public class IPetApplication {

    public static void main(String[] args) {
        SpringApplication.run(IPetApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**").allowedOrigins("http://localhost:3000").allowedOrigins("https://aw-petcare-client.herokuapp.com").allowedOrigins("https://aw-petcare-business.herokuapp.com").allowedOrigins("http://aw-petcare-client.herokuapp.com").allowedOrigins("http://aw-petcare-business.herokuapp.com");
                registry.addMapping("/**").allowedOrigins("http://localhost:3000").allowedOrigins("https://aw-petcare-client.herokuapp.com").allowedOrigins("https://aw-petcare-business.herokuapp.com").allowedOrigins("http://aw-petcare-client.herokuapp.com").allowedOrigins("http://aw-petcare-business.herokuapp.com");
            }
        };
    }
}