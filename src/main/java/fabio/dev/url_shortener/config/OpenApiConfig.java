package fabio.dev.url_shortener.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Info;

@OpenAPIDefinition
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI baseOpenAPI () {
        return  new  OpenAPI ().info( new  Info ()
                        .title( "Acortador de URLs API" )
                        .version( "1.0" ).description( "Esta API expone endpoint para para manejar urls largas" ));
    }
}