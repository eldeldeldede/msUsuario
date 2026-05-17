package cl.duoc.msUsuario.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cl.duoc.msUsuario.model.Rol;
import cl.duoc.msUsuario.model.Usuario;
import cl.duoc.msUsuario.repository.RolRepository;
import cl.duoc.msUsuario.repository.UsuarioRepository;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDataBase(UsuarioRepository usuarioRepo,
                                   RolRepository rolRepo){
                                    return args -> {
                                        if(rolRepo.count() > 0){
                                        System.out.println("Datos ya cargados en la base de datos");
                                        }else{
                                            Rol rol1 = new Rol(null, "ADMIN");
                                            Rol rol2 = new Rol(null, "EJECUTIVO");
                                            Rol rol3 = new Rol(null, "CLIENTE");

                                            rolRepo.save(rol1);
                                            rolRepo.save(rol2);
                                            rolRepo.save(rol3);

                                            Usuario usuario1 = new Usuario(null, "Pedro420-z", "pedrinho420@email.cl", "123456", rol1);
                                            Usuario usuario2 = new Usuario(null, "BenCu", "bencu@email.cl", "123456", rol2);
                                            Usuario usuario3 = new Usuario(null, "GonBah", "gonbah@email.cl", "123456", rol3);

                                            usuarioRepo.save(usuario1);
                                            usuarioRepo.save(usuario2);
                                            usuarioRepo.save(usuario3);
                                            
                                        }
                                    };
                                   }
    
}
