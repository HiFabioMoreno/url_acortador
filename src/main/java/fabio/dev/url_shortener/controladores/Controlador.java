package fabio.dev.url_shortener.controladores;

import fabio.dev.url_shortener.dtos.ActualizarRespuesta;
import fabio.dev.url_shortener.dtos.UrlRespuesta;
import fabio.dev.url_shortener.dtos.UrlSolicitud;
import fabio.dev.url_shortener.servicios.UrlServicio;
import jakarta.persistence.Temporal;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/acortador")
@Validated
public class Controlador {

    private final UrlServicio urlServicio;

    public Controlador(UrlServicio urlServicio) {
        this.urlServicio = urlServicio;
    }

    @GetMapping("/")
    public ResponseEntity<ArrayList<UrlRespuesta>> ListarUrls() {
        return ResponseEntity.ok(urlServicio.ListarUrls());
    }

    @PostMapping("/")
    public ResponseEntity<UrlRespuesta> registrarUrl(@Valid @RequestBody UrlSolicitud urlSolicitud) {
        return ResponseEntity.status(HttpStatus.CREATED).body(urlServicio.crearShortUrl(urlSolicitud));
    }

    @PatchMapping("/{id}/")
    public ResponseEntity<UrlRespuesta> actualizarUrl(@PathVariable @Positive Integer id, @Valid @RequestBody ActualizarRespuesta dto) {
        return ResponseEntity.ok(urlServicio.actualizarShortUrl(id,dto));
    }

    @DeleteMapping("/{id}/")
    public ResponseEntity<Void> eliminarUrl(@PathVariable @Positive Integer id) {
        urlServicio.EliminarUrl(id);
        return ResponseEntity.noContent().build();
    }

}