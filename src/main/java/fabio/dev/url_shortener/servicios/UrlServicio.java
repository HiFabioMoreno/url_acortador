package fabio.dev.url_shortener.servicios;

import fabio.dev.url_shortener.dtos.ActualizarRespuesta;
import fabio.dev.url_shortener.dtos.UrlRespuesta;
import fabio.dev.url_shortener.modelos.Url;
import fabio.dev.url_shortener.repositorios.UrlRepositorio;
import jakarta.transaction.Transactional;
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
    public UrlRespuesta crearShortUrl(String url) {

        logger.info("Creando url para : {}", url);


        if (url == null || url.isEmpty()){
            throw new RuntimeException("El url no puede ser vacio");
        }

        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            throw new RuntimeException("Ingeresa un url valido");
        }

        Url shortUrl = new Url();
        shortUrl.setOriginalUrl(url);
        shortUrl.setSlug(GeneradorUrl());
        shortUrl.setFechaModificacion(shortUrl.getFechaRegistro());
        shortUrl.setVecesAccedido(0);

        urlRepositorio.save(shortUrl);

        logger.info("Url acortada y guardada exitosamente");

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
            throw new RuntimeException("El id debe ser mayor a cero");
        }

        if (actualizarRespuesta == null) {
            throw new RuntimeException("La solicitud no puede ser nula");
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
            throw new RuntimeException("El id debe ser mayor a cero");
        }

        if (!urlRepositorio.existsById(id)) {
           throw new RuntimeException("El url no existe");
        }

        urlRepositorio.deleteById(id);

        logger.info("Url eliminada exitosamente");

    }

}
