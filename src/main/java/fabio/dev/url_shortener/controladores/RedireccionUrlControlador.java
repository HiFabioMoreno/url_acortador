package fabio.dev.url_shortener.controladores;


import fabio.dev.url_shortener.servicios.UrlServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class RedireccionUrlControlador {

    private final UrlServicio urlServicio;

    public RedireccionUrlControlador(UrlServicio urlServicio) {
        this.urlServicio = urlServicio;
    }

    @Operation(
            summary = "Redirigir a la URL original",
            description = "Redirige al usuario hacia la URL original asociada al slug proporcionado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "302",
                    description = "Redirección realizada correctamente hacia la URL original"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se encontró una URL asociada al slug proporcionado"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor"
            )
    })
    @GetMapping("/{slug}/")
    public ResponseEntity<Void> redericcionar(
            @Parameter(
                    description = "Identificador único de la URL acortada",
                    example = "1sol4h"
            )
            @PathVariable String slug) {

        String urlOriginal = urlServicio.buscarUrl(slug);

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, urlOriginal)
                .build();
    }

}