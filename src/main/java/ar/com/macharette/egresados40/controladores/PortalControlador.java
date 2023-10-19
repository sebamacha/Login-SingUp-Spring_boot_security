package ar.com.macharette.egresados40.controladores;


import ar.com.macharette.egresados40.entidades.Usuario;
import ar.com.macharette.egresados40.exepciones.MiException;
import ar.com.macharette.egresados40.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class PortalControlador {


    //crear una instancia unica
    @Autowired
    private UsuarioServicio usuarioServicio;
    @GetMapping("/")
    public String index(){
        //retorna al index
        return "login.html";
    }


    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_MASTER')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session) {

        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        if (logueado.getRol().toString().equals("ADMIN")) {
            return "redirect:/admin/dashboard";
        }
        if (logueado.getRol().toString().equals("MASTER")) {
            return "redirect:/admin/master";
        }

        return "inicio.html";
    }

    @GetMapping("/registrar")
    public String registrar(){
        return "registro.html";
    }
    @GetMapping("/login")
    public String login(@RequestParam(required = false)String error, ModelMap modelo ){
        if (error != null){
            modelo.put("error", "usuario o contraseña invalida");
        }

        return "login.html";
    }

    @PostMapping("/registro")
    public String registro (@RequestParam String nombre, @RequestParam String userName, @RequestParam String password,
                            @RequestParam String password2, ModelMap modelo) throws MiException {
        //llamar al metito registrar del servicio
        try{
        usuarioServicio.registrar(nombre, userName, password, password2);
        //si esto ocurre mandamos mensaje de exito a traves del modelo
            modelo.put("exito", "usuario registrado");
            //retornar al index
            return "index.html";
        }
        catch (MiException ex){

            modelo.put("error",ex.getMessage());
            //si ay un error que nos guardae los datos anteriormente guardados
            modelo.put("nombre", nombre);
            modelo.put("userName", userName);
            return "registro.html";
        }

    }

}
