package fabio.dev.url_shortener.repositorios;

import fabio.dev.url_shortener.modelos.Url;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlRepositorio extends JpaRepository<Url,Integer> {
}
