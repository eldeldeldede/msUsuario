package cl.duoc.msUsuario.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Test
    void guardar(){
        //ARRANGE: configuramos que el repository retorne el usuario guardado
        when(usuarioRepository.save(usuarioEjemplo)).thenReturn(usuarioEjemplo);

        //ACT: 
        Usuario resultado = usuarioService.crearUsuario(usuarioEjemplo);

        //ASSERT:
        assertEquals("Pedro Cid", resultado.getNombre());

    }

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


}
