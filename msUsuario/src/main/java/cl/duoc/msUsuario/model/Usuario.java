package cl.duoc.msUsuario.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
@Schema(description = "Representa un usuario en el sistema")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID unico del usuario, es autoincrementable dentro de la base de datos, por lo que al momento de crearse puede llevar un null el input",
            examples = {"1"})
    private Integer id;

    @Column(nullable = false)
    @Schema(description = "Nombre o username del usuario. No puede ser nulo.",
        examples = {"pedro420-z"})
    private String nombre;

    @Column(nullable = false, unique = true)
    @Schema(description = "Correo electrónico del usuario. No puede ser nulo.",
            examples = {"pedro420z@gmail.cl"})
    private String email;

    @Column(nullable = false)
    @Schema(description = "Contraseña del usuario, necesaria para iniciar sesión en el sistema. No puede ser nula.",
            examples = {"pedRinho420%"})
    private String password;

    @ManyToOne
    @JoinColumn(name = "rol_id")
    @Schema(description = "Roles de cada usuario. Puede ser cliente o empleado.")
    private Rol rol;
}
