package ar.com.macharette.egresados40;


import ar.com.macharette.egresados40.exepciones.MiException;
import ar.com.macharette.egresados40.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SeguridadWeb extends WebSecurityConfigurerAdapter {
@Autowired
public UsuarioServicio usuarioServicio;

//encriptar contraseña
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        //configurar el manejador de seguridad
        auth.userDetailsService(usuarioServicio)
        //autenticar la contraseña
                .passwordEncoder(new BCryptPasswordEncoder());

    }
//debe tener si o si el mismo nombre configure
@Override
protected void configure(HttpSecurity http) throws Exception {
    http
            .authorizeRequests()
            .antMatchers("/admin/*").hasAnyRole("ADMIN", "MASTER")
            .antMatchers("/inicio").hasAnyRole("USER", "ADMIN", "MASTER")
            .antMatchers("/css/*", "/js/*", "/img/*", "/**").permitAll()
            .and()
            .formLogin()
            .loginPage("/login")
            .loginProcessingUrl("/logincheck")
            .usernameParameter("userName")
            .passwordParameter("password")
            .defaultSuccessUrl("/inicio")
            .permitAll()
            .and()
            .logout()
            .logoutUrl("/logout")
            .logoutSuccessUrl("/")
            .permitAll()
            .and()
            .csrf()
            .disable();
}

}






