package cl.duoc.msUsuario.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.duoc.msUsuario.dto.UsuarioDTO;
import cl.duoc.msUsuario.model.Rol;
import cl.duoc.msUsuario.model.Usuario;
import cl.duoc.msUsuario.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock //NO ES EL REPO REAL, SOLO SERÁ UNA SIMULACIÓN DEL REPO
    private UsuarioRepository usuarioRepository;

    @InjectMocks //el servicio REAL con el repo simulado inyectado
    private UsuarioService usuarioService;

    private Usuario usuarioEjemplo;

    @BeforeEach
    void setUp(){
        
        usuarioEjemplo = new Usuario();
        usuarioEjemplo.setId(1);
        usuarioEjemplo.setNombre("Pedro Cid");
        usuarioEjemplo.setPassword("123456");
        usuarioEjemplo.setEmail("pedro420z@email.cl");
        usuarioEjemplo.setRol(new Rol(1, "Admin"));

    }

    // ---------- listarUsuarios ----------

    @Test
    void listarUsuarios_retornaLista(){
        //ARRANGE: el repo retorna una lista con un usuario
        when(usuarioRepository.findAll()).thenReturn(List.of(usuarioEjemplo));

        //ACT
        List<Usuario> resultado = usuarioService.listarUsuarios();

        //ASSERT
        assertEquals(1, resultado.size());
        assertEquals("Pedro Cid", resultado.get(0).getNombre());
    }

    // ---------- buscarUsuario por id ----------

    @Test
    public void buscarUsuario_encontrado(){
        //ARRANGE: preparamos la prueba, le decimos que hacer
        Optional<Usuario> optionalUsuario = Optional.of(usuarioEjemplo);
        when(usuarioRepository.findById(1)).thenReturn(optionalUsuario);

        //ACT: llamamos el metodo real
        Usuario resultado = usuarioService.buscarUsuario(1);

        //ASSERT: verificamos si el usuario que retornó es el correcto
        //        (valor que deberia tener, origen)
        assertEquals(1, resultado.getId());
        assertEquals("Pedro Cid", resultado.getNombre());

    }

    @Test
    public void buscarUsuario_noEncontrado(){
        //ARRANGE: preparamos la prueba pero para que retorne un doctor vacio
        Optional<Usuario> usuarioVacio = Optional.empty();
        when(usuarioRepository.findById(99)).thenReturn(usuarioVacio);

        //ACT + ASSERT: verificamos si lanza la excepcion correcta
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.buscarUsuario(99);
        });

        assertEquals("El usuario no existe", exception.getMessage());

    }

    // ---------- buscarUsuarioPorEmail ----------

    @Test
    void buscarUsuarioPorEmail_encontrado(){
        //ARRANGE
        when(usuarioRepository.findByEmail("pedro420z@email.cl")).thenReturn(Optional.of(usuarioEjemplo));

        //ACT
        Usuario resultado = usuarioService.buscarUsuarioPorEmail("pedro420z@email.cl");

        //ASSERT
        assertEquals("Pedro Cid", resultado.getNombre());
        assertEquals("pedro420z@email.cl", resultado.getEmail());
    }

    @Test
    void buscarUsuarioPorEmail_noEncontrado(){
        //ARRANGE: el correo no existe en el repo
        when(usuarioRepository.findByEmail("noexiste@email.cl")).thenReturn(Optional.empty());

        //ACT + ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.buscarUsuarioPorEmail("noexiste@email.cl");
        });

        assertEquals("El usuario no existe", exception.getMessage());
    }

    // ---------- crearUsuario ----------

    @Test
    void guardar(){
        //ARRANGE: configuramos que el repository retorne el usuario guardado
        when(usuarioRepository.save(usuarioEjemplo)).thenReturn(usuarioEjemplo);

        //ACT: 
        Usuario resultado = usuarioService.crearUsuario(usuarioEjemplo);

        //ASSERT:
        assertEquals("Pedro Cid", resultado.getNombre());

    }

    // ---------- actualizarUsuario ----------

    @Test
    void actualizarUsuario_exitoso(){
        //ARRANGE: el usuario existe y llegan datos nuevos para actualizar
        Usuario datosActualizados = new Usuario();
        datosActualizados.setNombre("Pedro Actualizado");
        datosActualizados.setEmail("nuevo@email.cl");
        datosActualizados.setPassword("654321");
        datosActualizados.setRol(new Rol(2, "Cliente"));

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioEjemplo));
        when(usuarioRepository.save(usuarioEjemplo)).thenReturn(usuarioEjemplo);

        //ACT
        Usuario resultado = usuarioService.actualizarUsuario(1, datosActualizados);

        //ASSERT: el usuario original fue mutado con los nuevos datos
        assertEquals("Pedro Actualizado", resultado.getNombre());
        assertEquals("nuevo@email.cl", resultado.getEmail());
        assertEquals("654321", resultado.getPassword());
        assertEquals("Cliente", resultado.getRol().getNombre());
        verify(usuarioRepository, times(1)).save(usuarioEjemplo);
    }

    @Test
    void actualizarUsuario_noEncontrado(){
        //ARRANGE: el usuario 99 no existe
        when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

        //ACT + ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.actualizarUsuario(99, usuarioEjemplo);
        });

        assertEquals("El usuario no existe", exception.getMessage());

        //nunca debe intentar guardar si el usuario no existe
        verify(usuarioRepository, times(0)).save(any(Usuario.class));
    }

    // ---------- eliminarUsuario ----------

    @Test
    void eliminarExitoso(){
        //ARRANGE: el usuario existe
        when(usuarioRepository.existsById(1)).thenReturn(true);

        //ASSERT: no debe lanzar error/exception
        assertDoesNotThrow(() -> usuarioService.eliminarUsuario(1));

        //verificamos que el deleteByID fue exitoso solo una vez
        verify(usuarioRepository, times(1)).deleteById(1);
    }

    @Test
    void eliminar_noExiste(){
        // ARRANGE: configuramos el mock para que simule que el usuario NO existe
        when(usuarioRepository.existsById(99)).thenReturn(false);

        // ACT & ASSERT: verificamos que lance la excepción esperada
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.eliminarUsuario(99);
        });

        // Verificamos que el mensaje de error sea el correcto (ajústalo si tu servicio usa otro mensaje)
        assertEquals("El usuario no existe", exception.getMessage());
        
        // Opcional: aseguramos que NUNCA se intente borrar si no existe
        verify(usuarioRepository, times(0)).deleteById(99);
    }

    // ---------- buscarDTO ----------

    @Test
    void buscarDTO_exitoso(){
        //ARRANGE: el usuario existe, así que buscarUsuario(id) internamente lo encuentra
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioEjemplo));

        //ACT
        UsuarioDTO resultado = usuarioService.buscarDTO(1);

        //ASSERT: el DTO debe tener los mismos datos que el usuario
        assertEquals("Pedro Cid", resultado.getNombre());
    }

    @Test
    void buscarDTO_noEncontrado(){
        //ARRANGE: el usuario no existe, buscarUsuario(id) lanzará la excepción
        when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

        //ACT + ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.buscarDTO(99);
        });

        assertEquals("El usuario no existe", exception.getMessage());
    }

    @Test
    void buscarDTOPorEmail_exitoso(){
        //ARRANGE: el usuario existe, así que buscarUsuarioPorEmail(email) internamente lo encuentra
        when(usuarioRepository.findByEmail("pedro420z@email.cl")).thenReturn(Optional.of(usuarioEjemplo));

        //ACT
        UsuarioDTO resultado = usuarioService.buscarDTOPorEmail("pedro420z@email.cl");

        //ASSERT
        assertEquals("Pedro Cid", resultado.getNombre());
    }

    @Test
    void buscarDTOPorEmail_noEncontrado(){
        //ARRANGE: el usuario no existe, buscarUsuarioPorEmail(email) lanzará la excepción
        when(usuarioRepository.findByEmail("correo@email.cl")).thenReturn(Optional.empty());

        //ACT + ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.buscarDTOPorEmail("correo@email.cl");    
        });

        assertEquals("El usuario no existe", exception.getMessage());
    }

}