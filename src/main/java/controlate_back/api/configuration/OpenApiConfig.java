package controlate_back.api.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Controlate")
                        .description("API que gestiona el backend de la aplicacion web Controlate")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Óscar Velasco Portela")
                                .email("oscar.velpor@educa.jcyl.es")
                                .url("https://controlate-front.vercel.app"))
                        .license(new io.swagger.v3.oas.models.info.License()
                                .name("Licencia")
                                .url("https://controlate-front.vercel.app")))
                .externalDocs(new io.swagger.v3.oas.models.ExternalDocumentation()
                        .description("Documentación Adicional")
                        .url("https://controlate-front.vercel.app"));
    }
}
