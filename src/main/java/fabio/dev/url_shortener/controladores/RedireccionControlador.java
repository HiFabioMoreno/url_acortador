package fabio.dev.url_shortener.controladores;


import fabio.dev.url_shortener.servicios.UrlServicio;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class RedireccionControlador {

    private final UrlServicio urlServicio;

    public RedireccionControlador(UrlServicio urlServicio) {
        this.urlServicio = urlServicio;
    }

    @GetMapping("/{slug}/")
    public ResponseEntity<Void> redericcionar(@PathVariable String slug) {

        String urlOriginal = urlServicio.buscarUrl(slug);

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, urlOriginal)
                .build();
    }

}