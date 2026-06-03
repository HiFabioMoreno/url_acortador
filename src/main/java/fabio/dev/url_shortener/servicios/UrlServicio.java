package fabio.dev.url_shortener.servicios;

import fabio.dev.url_shortener.dtos.ActualizarRespuesta;
import fabio.dev.url_shortener.dtos.UrlRespuesta;
import fabio.dev.url_shortener.dtos.UrlSolicitud;
import fabio.dev.url_shortener.excepciones.InvalidInputException;
import fabio.dev.url_shortener.modelos.Url;
import fabio.dev.url_shortener.repositorios.UrlRepositorio;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static fabio.dev.url_shortener.modelos.Url.GeneradorUrl;

@Service
public class UrlServicio {

    private UrlRepositorio urlRepositorio;
    private final Logger logger = LoggerFactory.getLogger(UrlServicio.class);

    public UrlServicio(UrlRepositorio urlRepositorio) {
        this.urlRepositorio = urlRepositorio;
    }

    @Transactional
    public UrlRespuesta crearShortUrl(@NonNull UrlSolicitud urlSolicitud) {

        logger.info("Creando slog para url: {}", urlSolicitud.url());

        Url shortUrl = new Url();
        shortUrl.setOriginalUrl(urlSolicitud.url());
        shortUrl.setSlug(GeneradorUrl());
        shortUrl.setFechaModificacion(shortUrl.getFechaRegistro());
        shortUrl.setVecesAccedido(0);

        urlRepositorio.save(shortUrl);

        logger.info("Url procesada y guardada exitosamente");

        return new UrlRespuesta(
                shortUrl.getId(),
                shortUrl.getOriginalUrl(),
                shortUrl.getSlug(),
                shortUrl.getFechaRegistro(),
                shortUrl.getFechaModificacion(),
                shortUrl.getVecesAccedido());
    }

    public ArrayList<UrlRespuesta> ListarUrls() {
        logger.info("Listando todos los urls");

        List<Url> urls = urlRepositorio.findAll();

        ArrayList<UrlRespuesta> urlsList = new ArrayList<>();

        urls.forEach(url -> {
            urlsList.add(new UrlRespuesta(
                    url.getId(),
                    url.getOriginalUrl(),
                    url.getSlug(),
                    url.getFechaRegistro(),
                    url.getFechaModificacion(),
                    url.getVecesAccedido()));
        });

        return urlsList;
    }

    @Transactional
    public UrlRespuesta actualizarShortUrl(Integer id,ActualizarRespuesta actualizarRespuesta)  {

        logger.info("Actualizando url : {}", id);

        if (id == null || id < 0) {
            throw new InvalidInputException("El id debe ser mayor a cero");
        }

        if (actualizarRespuesta == null) {
            throw new InvalidInputException("La solicitud no puede estar vacia");
        }

        Url url = urlRepositorio.findById(id).orElseThrow( () -> new RuntimeException("El url no existe"));

        if (actualizarRespuesta.url() != null){
            url.setOriginalUrl(actualizarRespuesta.url());
        }

        if (actualizarRespuesta.cambiarSlug()) {
            url.setSlug(GeneradorUrl());
        }

        if (actualizarRespuesta.esClicked()) {
            Integer vecesAccedido = url.getVecesAccedido();
            url.setVecesAccedido(vecesAccedido + 1);
        }

        urlRepositorio.save(url);

        logger.info("Url actualizada exitosamente");

        return new UrlRespuesta(
                url.getId(),
                url.getOriginalUrl(),
                url.getSlug(),
                url.getFechaRegistro(),
                url.getFechaModificacion(),
                url.getVecesAccedido());

    }

    @Transactional
    public void EliminarUrl(Integer id){
        logger.info("Eliminando url : {}", id);

        if (id == null || id < 0) {
            throw new InvalidInputException("El id debe ser mayor a cero");
        }

        if (!urlRepositorio.existsById(id)) {
           throw new EntityNotFoundException("El url no existe");
        }

        urlRepositorio.deleteById(id);

        logger.info("Url eliminada exitosamente");

    }

}
