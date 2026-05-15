package fabio.dev.url_shortener;

import fabio.dev.url_shortener.controladores.Controlador;
import fabio.dev.url_shortener.dtos.ActualizarRespuesta;
import fabio.dev.url_shortener.dtos.UrlRespuesta;
import fabio.dev.url_shortener.modelos.Url;
import fabio.dev.url_shortener.servicios.UrlServicio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(Controlador.class)
public class ControladorTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UrlServicio urlServicio;

    @Autowired
    private ObjectMapper objectMapper;

    private Url url;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        url = new Url();
        url.setId(1);
        url.setOriginalUrl("https://www.google.com");
        url.setSlug("uzdkr8");
        url.setFechaRegistro(LocalDateTime.now().toString());
        url.setFechaModificacion(LocalDateTime.now().toString());
        url.setVecesAccedido(0);

    }

    @Test
    void registrarUrl_DebeRetornar201ConUrlCreada() throws Exception {
        UrlRespuesta respuesta = new UrlRespuesta(
                url.getId(),
                url.getOriginalUrl(),
                url.getSlug(),
                url.getFechaRegistro(),
                url.getFechaModificacion(),
                url.getVecesAccedido()
        );

        when(urlServicio.crearShortUrl("https://www.google.com")).thenReturn(respuesta);

        mockMvc.perform(post("/acortador/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("https://www.google.com"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.slug").value("uzdkr8"))
                .andExpect(jsonPath("$.originalUrl").value("https://www.google.com"));
    }

    @Test
    void actualizarUrl_DebeRetornar200ConUrlActualizada() throws Exception {

        ActualizarRespuesta urlActulizar = new ActualizarRespuesta(
                "https://www.youtube.com",
                false,
                false
        );

        UrlRespuesta respuesta = new UrlRespuesta(
                1,
                "https://www.youtube.com",
                url.getSlug(),
                url.getFechaRegistro(),
                url.getFechaModificacion(),
                url.getVecesAccedido()
        );

        when(urlServicio.actualizarShortUrl(1, urlActulizar)).thenReturn(respuesta);

        mockMvc.perform(patch("/acortador/1/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(urlActulizar)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.slug").value("uzdkr8"))
                .andExpect(jsonPath("$.originalUrl").value("https://www.youtube.com"));
    }


    @Test
    void eliminarUrl_DebeRetornar200ConUrlEliminada() throws Exception {

        doNothing().when(urlServicio).EliminarUrl(1);
        mockMvc.perform(delete("/acortador/1/"))
                .andExpect(status().isNoContent());
    }

}