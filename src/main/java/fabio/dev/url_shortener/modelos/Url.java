package fabio.dev.url_shortener.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

@Data
@Entity
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El url no puede estar vacio")
    @Column(unique = true)
    private String originalUrl;

    @NotBlank(message = "El slug no puede estar vacio")
    @Column(unique = true)
    private String slug;

    @Column(name = "fechaRegistro", nullable = false, updatable = false)
    private String fechaRegistro;

    @Column(name = "fechaModificacion", nullable = false)
    private String fechaModificacion;

    @Positive(message = "El contador de accesos no puede ser negativo")
    private Integer vecesAccedido;

    @PrePersist
    void onCreate() {

        String str = "04-08-2026 12:30";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(str, formatter);

        this.fechaRegistro = dateTime.toString();
    }

    @PreUpdate
    void onUpdate() {
        String str = "04-08-2026 12:30";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(str, formatter);

        this.fechaRegistro = dateTime.toString();
    }

    public static String GeneradorUrl(){

        StringBuilder sb = new StringBuilder();

        String chars = "abcdefghijklmopqrstuvwxyz0123456789";
        ArrayList<String> array = new ArrayList<>();

        for(String str : chars.split("")){
            array.add(str);
        }

        Collections.shuffle(array);

        for(int i = 0; i <= 5; i++){
            sb.append(array.get(i).toString());
        }

        return sb.toString();
    }

}
