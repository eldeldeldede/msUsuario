package cl.duoc.msUsuario.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import cl.duoc.msUsuario.dto.UsuarioDTO;
import cl.duoc.msUsuario.model.Rol;
import cl.duoc.msUsuario.model.Usuario;
import cl.duoc.msUsuario.service.UsuarioService;

@WebMvcTest(UsuarioController.class)//levanta solo la capa web, no la bd
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mock; //mock que simula las peticiones HTTP

    @MockitoBean 
    private UsuarioService service;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Usuario usuarioEjemplo;

    private UsuarioDTO dtoEjemplo; 

    @BeforeEach
    void setUp(){
        usuarioEjemplo = new Usuario();
        usuarioEjemplo.setId(1);
        usuarioEjemplo.setNombre("Pedro Cid");
        usuarioEjemplo.setPassword("123456");
        usuarioEjemplo.setEmail("pedro420z@email.cl");
        usuarioEjemplo.setRol(new Rol(1, "Admin"));
        dtoEjemplo =new UsuarioDTO(1, "Pedro Cid", "pedro420z@email.cl", "Admin");

    }

    // ---------- listarUsuarios ----------

    @Test
    public void listarUsuarios_retorna200conLista() throws Exception{
        //ARRANGE
        when(service.listarUsuarios()).thenReturn(List.of(usuarioEjemplo));

        //ACT + ASSERT
        mock.perform(get("/api/v1/usuarios"))
            .andExpect(status().isOk());
    }

    @Test
    public void listarUsuarios_retornaNoContentSiHayError() throws Exception{
        //ARRANGE: el service falla al listar
        when(service.listarUsuarios()).thenThrow(new RuntimeException("Error al listar"));

        //ACT + ASSERT
        mock.perform(get("/api/v1/usuarios"))
            .andExpect(status().isNoContent());
    }

    // ---------- buscarUsuario por id ----------

    @Test
    public void buscarUsuario_retorna200() throws Exception{
        //ARRANGE: el service debe retornar el usuario
        when(service.buscarUsuario(1)).thenReturn(usuarioEjemplo);

        //ACT + ASSERT
        mock.perform(get("/api/v1/usuarios/id/1"))
            .andExpect(status().isOk())
            ;
    }

    @Test
    public void buscarUsuario_retorna404() throws Exception{
        //ARRANGE: buscamos un usuario con id 99 y tira un error
        when(service.buscarUsuario(99)).thenThrow(new RuntimeException("El usuario no existe"));

        //ACT + ASSERT
        mock.perform(get("/api/v1/usuarios/id/99")).andExpect(status().isNotFound());        
    }

    // ---------- buscarUsuarioPorEmail ----------

    @Test
    public void buscarUsuarioPorEmail_retorna200() throws Exception{
        //ARRANGE
        when(service.buscarUsuarioPorEmail("pedro420z@email.cl")).thenReturn(usuarioEjemplo);

        //ACT + ASSERT
        mock.perform(get("/api/v1/usuarios/email/pedro420z@email.cl"))
            .andExpect(status().isOk());
    }

    @Test
    public void buscarUsuarioPorEmail_retorna404() throws Exception{
        //ARRANGE: el correo no existe
        when(service.buscarUsuarioPorEmail("noexiste@email.cl"))
            .thenThrow(new RuntimeException("El usuario no existe"));

        //ACT + ASSERT
        mock.perform(get("/api/v1/usuarios/email/noexiste@email.cl"))
            .andExpect(status().isNotFound());
    }

    // ---------- guardarUsuario (POST) ----------

    @Test
    public void guardarUsuario_retorna200() throws Exception{
        //ARRANGE: se usa any() porque el objeto deserializado del JSON
        //nunca será == al objeto usuarioEjemplo (son instancias distintas)
        when(service.crearUsuario(any(Usuario.class))).thenReturn(usuarioEjemplo);

        //ACT + ASSERT
        mock.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioEjemplo)))
            .andExpect(status().isOk());
    }

    @Test
    public void guardarUsuario_retornaNoContentSiHayError() throws Exception{
        //ARRANGE: el service falla al crear
        when(service.crearUsuario(any(Usuario.class)))
            .thenThrow(new RuntimeException("Error al crear usuario"));

        //ACT + ASSERT
        mock.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioEjemplo)))
            .andExpect(status().isNoContent());
    }

    // ---------- actualizarUsuario (PUT) ----------

    @Test
    public void actualizarUsuario_retorna200() throws Exception{
        //ARRANGE
        when(service.actualizarUsuario(1, usuarioEjemplo)).thenReturn(usuarioEjemplo);

        //ACT + ASSERT
        mock.perform(put("/api/v1/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioEjemplo)))
            .andExpect(status().isOk());
    }

    @Test
    public void actualizarUsuario_retorna404() throws Exception{
        //ARRANGE: el usuario 99 no existe
        when(service.actualizarUsuario(99, usuarioEjemplo))
            .thenThrow(new RuntimeException("El usuario no existe"));

        //ACT + ASSERT
        mock.perform(put("/api/v1/usuarios/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioEjemplo)))
            .andExpect(status().isNotFound());
    }

    // ---------- buscarDTO ----------

    @Test
    public void buscarDTO_retorna200() throws Exception{
        //ARRANGE
        when(service.buscarDTO(1)).thenReturn(dtoEjemplo);

        //ACT + ASSERT
        mock.perform(get("/api/v1/usuarios/dto/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre").value("Pedro Cid"));
    }

    @Test
    public void buscarDTO_retorna404() throws Exception{
        //ARRANGE
        when(service.buscarDTO(99)).thenThrow(new RuntimeException("El usuario no existe"));

        //ACT + ASSERT
        mock.perform(get("/api/v1/usuarios/dto/99"))
            .andExpect(status().isNotFound());
    }

}