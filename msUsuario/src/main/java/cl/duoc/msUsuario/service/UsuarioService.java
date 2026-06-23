package cl.duoc.msUsuario.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.duoc.msUsuario.dto.UsuarioDTO;
import cl.duoc.msUsuario.model.Usuario;
import cl.duoc.msUsuario.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repo;
    
    public List<Usuario> listarUsuarios(){
        return repo.findAll();
    }

    public Usuario buscarUsuario(Integer id){
        return repo.findById(id).orElseThrow(()-> new RuntimeException("El usuario no existe"));
    }

    public Usuario buscarUsuarioPorEmail(String email){
        return repo.findByEmail(email).orElseThrow(()-> new RuntimeException("El usuario no existe"));
    }

    public Usuario crearUsuario(Usuario usuario){
        return repo.save(usuario);
    }

    public Usuario actualizarUsuario(Integer id, Usuario usuarioActualizado){
        Usuario usuario = repo.findById(id).orElseThrow(()-> new RuntimeException("El usuario no existe"));
        usuario.setNombre(usuarioActualizado.getNombre());
        usuario.setEmail(usuarioActualizado.getEmail());;
        usuario.setPassword(usuarioActualizado.getPassword());
        usuario.setRol(usuarioActualizado.getRol());

        return repo.save(usuario);
    }

    public void eliminarUsuario(Integer id){
        if(repo.existsById(id)){
            repo.deleteById(id);
        } else {
            throw new RuntimeException("El usuario no existe");
        }
    }

    public UsuarioDTO buscarDTO(Integer id){
        Usuario usuario = buscarUsuario(id);
        return new UsuarioDTO(usuario.getId(), usuario.getNombre(), usuario.getEmail(), usuario.getRol().getNombre());
    }

    public UsuarioDTO buscarDTOPorEmail(String email){
        Usuario usuario = buscarUsuarioPorEmail(email);
        return new UsuarioDTO(usuario.getId(), usuario.getNombre(), usuario.getEmail(), usuario.getRol().getNombre());
    }

}
