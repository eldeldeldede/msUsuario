package cl.duoc.msUsuario.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
@Schema(description = "Representa un rol de usuario dentro del sistema.")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID unico del rol de usuario, es autoincrementable dentro de la base de datos, por lo que al momento de crearse el input puede ser null",
            examples = {"1"})
    private Integer id;

    @Column(nullable = false)
    @Schema(description = "El nombre del rol de usuario. No puede ser null.",
            examples = {"Cliente"})
    private String nombre;
}
