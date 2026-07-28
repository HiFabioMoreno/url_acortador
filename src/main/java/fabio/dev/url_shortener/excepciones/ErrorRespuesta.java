package fabio.dev.url_shortener.excepciones;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ErrorRespuesta {

    private LocalDateTime timestamp;
    private Integer status;
    private String error;
    private String errorCode;
    private String mensaje;
    private String mensajeDesarrollador;
    private String path;
    private List<FieldError> errors;
    private List<ConstraintViolationError> violacionesDeRestricciones;

    @Data
    @AllArgsConstructor
    public static class FieldError {
        private String field;
        private Object rejectedValue;
        private String message;
    }

    @Data
    @AllArgsConstructor
    public static class ConstraintViolationError {
        private Object rootBean;
        private String message;
    }
}
