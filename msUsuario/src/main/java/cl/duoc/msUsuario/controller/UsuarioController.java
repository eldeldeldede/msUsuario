package cl.duoc.msUsuario.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.msUsuario.dto.UsuarioDTO;
import cl.duoc.msUsuario.model.Usuario;
import cl.duoc.msUsuario.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/v1/usuarios")
@Tag(name = "Usuarios", description = "Operaciones sobre usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping
    @Operation(
        summary = "Obtener la lista de usuarios registrados.",
        description = "Retorna la lista de usuarios registrados en el sistema del Rent A Car. Estos usuarios pueden ser empleados o clientes, dependiendo del rol."
    )
    public ResponseEntity<List<Usuario>> listarUsuarios(){
        try {
            List<Usuario> usuarios = service.listarUsuarios();
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("id/{id}")
    @Operation(summary = "Buscar usuario por ID.", 
               description = "Retorna un usuario según el ID proporcionado y debe retornar solo un usuario."
    )
    public ResponseEntity<Usuario> buscarUsuario(@PathVariable Integer id){
        try {
            Usuario usuario = service.buscarUsuario(id);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("email/{email}")
    @Operation(
        summary = "Buscar usuario por correo electrónico.",
        description = "Retorna un usuario según el correo electrónico proporcionado en el input. Debe retornar un único usuario."
    )
    public ResponseEntity<Usuario> buscarUsuarioPorEmail(@PathVariable String email){
        try {
            Usuario usuario = service.buscarUsuarioPorEmail(email);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(
        summary = "Guarda un usuario en la base de datos.",
        description = "Se le dan datos mediante la estructura JSon, datos que luego serán guardados en la base de datos, creando un nuevo usuario."
    )
    public ResponseEntity<Usuario> guardarUsuario(@RequestBody Usuario usuario){
        try {
            Usuario nuevoUsuario = service.crearUsuario(usuario);
            return ResponseEntity.ok(nuevoUsuario);
        } catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }

    @PutMapping("{id}")
    @Operation(
        summary = "Actualiza un usuario en la base de datos.",
        description = "Recibe los datos del usuario a actualizar en formato JSon, y luego actualiza el usuario con la id indicada al momento de ingresar al sitio."
    )
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Integer id,@RequestBody Usuario usuario){
        try {
            Usuario usuarioActualizado = service.actualizarUsuario(id, usuario);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("dto/{id}")
    @Operation(
        summary = "Retorna el DTO de un usuario.",
        description = "Recibe el id del usuario del cual se requiere el DTO. Este DTO sirve para enviar los datos de usuario a otros microservicios que lo requieran."
    )
    public ResponseEntity<UsuarioDTO> buscarDTO(@PathVariable Integer id){
        try {
            UsuarioDTO usuarioDTO = service.buscarDTO(id);
            return ResponseEntity.ok(usuarioDTO);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("dto/email/{email}")
    @Operation(
        summary = "Retorna el DTO de un usuario por correo electrónico.",
        description = "Recibe el correo electrónico del usuario del cual se requiere el DTO. Este DTO sirve para enviar los datos de usuario a otros microservicios que lo requieran."
    )
    public ResponseEntity<UsuarioDTO> buscarDTOPorEmail(@PathVariable String email){
        try {
            UsuarioDTO usuarioDTO = service.buscarDTOPorEmail(email);
            return ResponseEntity.ok(usuarioDTO);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}
