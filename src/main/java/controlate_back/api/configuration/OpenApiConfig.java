package controlate_back.api.configuration;

import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // Define el esquema de seguridad (Bearer Token)
        SecurityScheme bearerAuthScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        // Asocia el esquema de seguridad a las rutas protegidas
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("BearerAuth");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("BearerAuth", bearerAuthScheme))
                .addSecurityItem(securityRequirement) // Asocia el esquema a todas las rutas protegidas
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("API de Controlate")
                        .description("API que gestiona el backend de la aplicación web Controlate")
                        .version("1.0.0")
                        .contact(new io.swagger.v3.oas.models.info.Contact()
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
