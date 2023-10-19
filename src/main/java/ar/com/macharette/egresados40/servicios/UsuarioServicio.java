package ar.com.macharette.egresados40.servicios;


import ar.com.macharette.egresados40.entidades.Usuario;
import ar.com.macharette.egresados40.enumeraciones.Rol;
import ar.com.macharette.egresados40.exepciones.MiException;
import ar.com.macharette.egresados40.repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioServicio implements UserDetailsService {
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Transactional
    public void registrar(String nombre, String userName, String password, String password2) throws MiException {
        //lo primero es llamar al metodo validar
        validar (nombre, userName, password, password2);
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setUserName(userName);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        //asignamos por defecto que los usuarios son de rol user
        usuario.setRol(Rol.USER);
        //guardamos el usuario
        usuarioRepositorio.save(usuario);

    }

    public  void validar(String nombre, String userName,String password, String password2) throws MiException{
        if (nombre.isEmpty() || nombre == null){
            throw  new MiException("nombre de usuario vacio o incorrecto");
        }
        if (userName.isEmpty() || userName == null){
            throw  new MiException("el usuario no puede estar vacio");
        }

        if (password.isEmpty() || password == null) {
            throw new MiException("la contraseña no puede estar vacia ");
        }

        if (!password.equals(password2)){
            throw new MiException("las contraseñas no coinciden");
        }

    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepositorio.buscarPorUserName(userName);
        if (usuario != null){
          //si el usuario es distinto de nulo lo transformamos a un usuario del dominio de spring
            List<GrantedAuthority> permisos = new ArrayList();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_"+usuario.getRol().toString());//ROLE_USER
            permisos.add(p);
            //aqui ya sabemos que el usuario ingreso a la plataforma y le dimos los persmisos
            //atraoamos la sesion del usuario y llamos al request
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            //guardamos el objeto en una httpsesion
            HttpSession session = attr.getRequest().getSession(true);
            //seteamos en esta sesion los atributos
            session.setAttribute("usuariosession", usuario);




           return new User(usuario.getUserName(), usuario.getPassword(), permisos );

        }else {
            return null;
        }





    }
}
