package ar.com.macharette.egresados40.repositorio;

import ar.com.macharette.egresados40.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio  extends JpaRepository<Usuario, String> {
    //metodo retorne un usuario segun si id

    @Query("SELECT u FROM Usuario u WHERE u.userName = :userName")
    public Usuario buscarPorUserName(@Param("userName") String userName);


}
