package fabio.dev.url_shortener.dtos;

public record ActualizarRespuesta(
        String url,
        boolean cambiarSlug,
        boolean esClicked
) {
}
