package fabio.dev.url_shortener.repositorios;

import fabio.dev.url_shortener.dtos.UrlRespuesta;
import fabio.dev.url_shortener.modelos.Url;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UrlRepositorio extends JpaRepository<Url,Integer> {
    Optional<Url> findBySlug(String slug);
}
