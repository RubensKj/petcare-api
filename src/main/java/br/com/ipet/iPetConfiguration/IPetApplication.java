package br.com.ipet.iPetConfiguration;

import br.com.ipet.Property.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

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
}