package cl.duoc.msUsuario.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import cl.duoc.msUsuario.model.Rol;
import cl.duoc.msUsuario.model.Usuario;
import cl.duoc.msUsuario.service.UsuarioService;

@WebMvcTest(UsuarioController.class)//levanta solo la capa web, no la bd
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mock; //mock que simula las peticiones HTTP

    @MockitoBean 
    private UsuarioService service;

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



}
