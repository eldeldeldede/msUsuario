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

@RestController
@RequestMapping("api/v1/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios(){
        try {
            List<Usuario> usuarios = service.listarUsuarios();
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("id/{id}")
    public ResponseEntity<Usuario> buscarUsuario(@PathVariable Integer id){
        try {
            Usuario usuario = service.buscarUsuario(id);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("email/{email}")
    public ResponseEntity<Usuario> buscarUsuarioPorEmail(@PathVariable String email){
        try {
            Usuario usuario = service.buscarUsuarioPorEmail(email);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Usuario> guardarUsuario(Usuario usuario){
        try {
            Usuario nuevoUsuario = service.crearUsuario(usuario);
            return ResponseEntity.ok(nuevoUsuario);
        } catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Integer id,@RequestBody Usuario usuario){
        try {
            Usuario usuarioActualizado = service.actualizarUsuario(id, usuario);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("dto/{id}")
    public ResponseEntity<UsuarioDTO> buscarDTO(@PathVariable Integer id){
        try {
            UsuarioDTO usuarioDTO = service.buscarDTO(id);
            return ResponseEntity.ok(usuarioDTO);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }


}
