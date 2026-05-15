package fabio.dev.url_shortener.controladores;

import fabio.dev.url_shortener.dtos.ActualizarRespuesta;
import fabio.dev.url_shortener.dtos.UrlRespuesta;
import fabio.dev.url_shortener.servicios.UrlServicio;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/acortador")
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
    public ResponseEntity<UrlRespuesta> registrarUrl(@RequestBody String url) {
        return ResponseEntity.status(HttpStatus.CREATED).body(urlServicio.crearShortUrl(url));
    }

    @PatchMapping("/{id}/")
    public ResponseEntity<UrlRespuesta> actualizarUrl(@PathVariable Integer id, @Valid @RequestBody ActualizarRespuesta dto) {
        return ResponseEntity.ok(urlServicio.actualizarShortUrl(id,dto));
    }

    @DeleteMapping("/{id}/")
    public ResponseEntity<Void> actualizarUrl(@PathVariable Integer id) {
        urlServicio.EliminarUrl(id);
        return ResponseEntity.noContent().build();
    }

}