package br.com.ipet.iPetConfiguration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebConfiguration extends WebMvcConfigurationSupport {

    @Override
    protected void addCorsMappings(CorsRegistry registry) {
        //"https://aw-petcare-client.herokuapp.com", "https://aw-petcare-business.herokuapp.com"
        registry.addMapping("/api/**").allowedOrigins("http://localhost:3000").allowedOrigins("https://aw-petcare-client.herokuapp.com").allowedOrigins("https://aw-petcare-business.herokuapp.com");
        registry.addMapping("/**").allowedOrigins("http://localhost:3000").allowedOrigins("https://aw-petcare-client.herokuapp.com").allowedOrigins("https://aw-petcare-business.herokuapp.com");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}