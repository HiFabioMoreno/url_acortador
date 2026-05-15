package fabio.dev.url_shortener;

import fabio.dev.url_shortener.dtos.ActualizarRespuesta;
import fabio.dev.url_shortener.dtos.UrlRespuesta;
import fabio.dev.url_shortener.modelos.Url;
import fabio.dev.url_shortener.repositorios.UrlRepositorio;
import fabio.dev.url_shortener.servicios.UrlServicio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UrlServicioTests {

	@Mock
	private UrlRepositorio urlRepositorio;

	@InjectMocks
	private UrlServicio urlServicio;

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
	@DisplayName("Deberia guardar y acortar exitosamente un url")
	public void deberiaGuardarUrlExistosamente()  {

		when(urlRepositorio.save(any(Url.class))).thenAnswer(i -> {
				Url u = i.getArgument(0);
				u.setFechaRegistro(LocalDateTime.now().toString());
				return u;
		});

		UrlRespuesta nuevaUrl = urlServicio.crearShortUrl("https://www.google.com");

		assertEquals("https://www.google.com",nuevaUrl.originalUrl());
		assertNotNull(nuevaUrl.fechaRegistro());
		assertNotNull(nuevaUrl.slug());
		assertEquals(0, nuevaUrl.vecesAccedidio());

		verify(urlRepositorio, times(1)).save(any(Url.class));

	}

	@Test
	@DisplayName("Deberia dar NullPointerException por ser url null")
	public void deberiaDarErrorPorUrlNull() {
		RuntimeException ex = assertThrows(RuntimeException.class, () ->
				urlServicio.crearShortUrl(null)
		);

		assertEquals("El url no puede ser vacio", ex.getMessage());

		verify(urlRepositorio, never()).save(any());
	}

	@Test
	@DisplayName("Deberia dar BadRequestException por url vacia")
	public void deberiaDarErrorPorUrlVacia() {

		RuntimeException ex = assertThrows(RuntimeException.class, () ->
				urlServicio.crearShortUrl("")
		);

		assertEquals("El url no puede ser vacio", ex.getMessage());

		verify(urlRepositorio, never()).save(any());

	}

	@Test
	@DisplayName("Deberia dar BadRequestException por url no valida")
	public void deberiaDarErrorPorUrlNoValida() {

		RuntimeException ex = assertThrows(
				RuntimeException.class, () -> urlServicio.crearShortUrl("www.googl.com")
		);

		assertEquals("Ingeresa un url valido", ex.getMessage());

		verify(urlRepositorio, never()).save(any());

	}

	@Test
	@DisplayName("Deberia mostrar todos los urls guardados exitosamente")
	public void deberiaMostrarTodosLosUrlsGuardadosExitosamente(){

		ArrayList<Url> urls = new ArrayList<>();
		urls.add(url);

		when(urlRepositorio.findAll()).thenReturn(urls);

		urlServicio.ListarUrls();

		assertEquals(1, urls.size());

		verify(urlRepositorio, times(1)).findAll();

	}

	@Test
	@DisplayName("Deberia actualizar el contador de un url exitosamente")
	public void deberiaActualizarElContadorDelUrlExistosamente() {

		when(urlRepositorio.findById(anyInt())).thenReturn(Optional.of(this.url));
		when(urlRepositorio.save(any(Url.class))).thenReturn(this.url);

		UrlRespuesta urlRespuesta = urlServicio.actualizarShortUrl(
				1, new ActualizarRespuesta(null, false, true)
		);

		assertEquals(1,urlRespuesta.vecesAccedidio());
		assertEquals("https://www.google.com",urlRespuesta.originalUrl());

		verify(urlRepositorio, times(1)).findById(anyInt());
		verify(urlRepositorio, times(1)).save(any(Url.class));

	}

	@Test
	@DisplayName("Deberia actualizar el url exitosamente")
	public void deberiaActualizarElUrlExistosamente() {

		when(urlRepositorio.findById(anyInt())).thenReturn(Optional.of(this.url));
		when(urlRepositorio.save(any(Url.class))).thenReturn(this.url);

		UrlRespuesta urlRespuesta = urlServicio.actualizarShortUrl(
				1, new ActualizarRespuesta("https://www.youtube.com/", false, false)
		);

		assertEquals(0,urlRespuesta.vecesAccedidio());
		assertEquals("https://www.youtube.com/",urlRespuesta.originalUrl());

		verify(urlRepositorio, times(1)).findById(anyInt());
		verify(urlRepositorio, times(1)).save(any(Url.class));

	}

	@Test
	@DisplayName("Deberia actualizar el slug exitosamente")
	public void deberiaActualizarSlugExistosamente() {

		when(urlRepositorio.findById(anyInt())).thenReturn(Optional.of(this.url));
		when(urlRepositorio.save(any(Url.class))).thenReturn(this.url);

		UrlRespuesta urlRespuesta = urlServicio.actualizarShortUrl(
				1, new ActualizarRespuesta(null, true, false)
		);

		assertEquals(0,urlRespuesta.vecesAccedidio());
		assertEquals("https://www.google.com",urlRespuesta.originalUrl());
		assertNotEquals("uzdkr8", urlRespuesta.slug());

		verify(urlRepositorio, times(1)).findById(anyInt());
		verify(urlRepositorio, times(1)).save(any(Url.class));

	}

	@Test
	@DisplayName("Deberia dar RuntimeException por dto null al actualizar")
	public void deberiaDarRuntimeExceptionPorDtoNullActualizar() {

		RuntimeException err = assertThrows(RuntimeException.class, () -> urlServicio.actualizarShortUrl(1, null));

		assertEquals("La solicitud no puede ser nula", err.getMessage());
		verify(urlRepositorio, never()).findById(anyInt());
		verify(urlRepositorio, never()).save(any(Url.class));

	}

	@Test
	@DisplayName("Deberia dar RuntimeException por id null al actualizar")
	public void deberiaDarRuntimeExceptionActualizar() {

		RuntimeException err = assertThrows(RuntimeException.class, () -> urlServicio.actualizarShortUrl(null, null));

		assertEquals("El id debe ser mayor a cero", err.getMessage());

		verify(urlRepositorio, never()).findById(anyInt());
		verify(urlRepositorio, never()).save(any(Url.class));

	}

	@Test
	@DisplayName("Deberia dar RuntimeException por id negativo al actualizar")
	public void deberiaDarRuntimeExceptionPorIdNegativoAlActualizar() {

		RuntimeException err = assertThrows(RuntimeException.class, () -> urlServicio.actualizarShortUrl(-1, null));

		assertEquals("El id debe ser mayor a cero", err.getMessage());

		verify(urlRepositorio, never()).findById(anyInt());
		verify(urlRepositorio, never()).save(any(Url.class));

	}

	@Test
	@DisplayName("Deberia eliminar exitosamente un url")
	public void deberiaEliminarUnUrlExitosamente() {
		when(urlRepositorio.existsById(anyInt())).thenReturn(true);

		urlServicio.EliminarUrl(1);

		verify(urlRepositorio, times(1)).deleteById(anyInt());

	}

	@Test
	@DisplayName("Deberia dar RuntimeException por id negativo al eliminar un url")
	public void deberiaFallarAlEliminarUnUrlPorIdNegativo() {

		RuntimeException err = assertThrows(
				RuntimeException.class, () -> urlServicio.EliminarUrl(-1)
		);

		assertEquals("El id debe ser mayor a cero", err.getMessage());

		verify(urlRepositorio, never()).existsById(anyInt());
		verify(urlRepositorio, never()).deleteById(anyInt());
	}

	@Test
	@DisplayName("Deberia dar RuntimeException por id null al eliminar un url")
	public void deberiaFallarAlEliminarUnUrlPorSerNull() {

		RuntimeException err = assertThrows(
				RuntimeException.class, () -> urlServicio.EliminarUrl(null)
		);

		assertEquals("El id debe ser mayor a cero", err.getMessage());

		verify(urlRepositorio, never()).existsById(anyInt());
		verify(urlRepositorio, never()).deleteById(anyInt());
	}

	@Test
	@DisplayName("Deberia dar RuntimeException por url no existente")
	public void deberiaFallarAlEliminarUnUrlQueNoExiste() {

		RuntimeException err = assertThrows(
				RuntimeException.class, () -> urlServicio.EliminarUrl(1000)
		);

		assertEquals("El url no existe", err.getMessage());


		verify(urlRepositorio, times(1)).existsById(anyInt());
		verify(urlRepositorio, never()).deleteById(anyInt());
	}

}