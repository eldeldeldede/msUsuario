package cl.duoc.msUsuario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.msUsuario.model.Rol;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer>{

}
