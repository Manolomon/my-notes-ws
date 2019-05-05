package ws;

import beans.Respuesta;
import beans.RespuestaUsuario;
import beans.Usuario;
import gateway.sms.JaxSms;
import java.util.Random;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import model.dao.UsuarioDAO;

/**
 * REST Web Service
 *
 * @author Manolomon
 */
@Path("usuarios")
public class UsuariosWS {

    @Context
    private UriInfo context;

    public UsuariosWS() {
    }
    
    private Respuesta validarRegistro(Usuario usuario) {
        Respuesta res = new Respuesta();
        res.setError(false);
        res.setErrorcode(0);
        if (usuario.getNombre() == null || usuario.getNombre().isEmpty()) {
            res.setError(true);
            res.setErrorcode(1);
            res.setMensaje("El nombre no puede estar vacío");
        }
        if (usuario.getTelefono() == null || usuario.getTelefono().isEmpty()) {
            res.setError(true);
            res.setErrorcode(2);
            res.setMensaje("El telefono no puede estar vacío");
        }
        if (!usuario.getTelefono().matches("^\\d{10}$")) {
            res.setError(true);
            res.setErrorcode(3);
            res.setMensaje("El valor del telefono debe contener 10 digitos");
        }
        if (usuario.getPassword() == null || usuario.getPassword().isEmpty()) {
            res.setError(true);
            res.setErrorcode(4);
            res.setMensaje("La contraseña no puede estar vacía");
        }
        return res;
    }

    @POST
    @Path("registrar")
    @Produces(MediaType.APPLICATION_JSON)
    /**
     * Codigos de error del metodo
     * 1 - Nombre vacio
     * 2 - Telefono vacio
     * 3 - Telefono no son 10 digitos
     * 4 - Contraseña vacía
     * 5 - Fallo en envio de SMS
     */
    public Respuesta registrarUsuario(
            @FormParam("nombre") String nombre,
            @FormParam("telefono") String telefono,
            @FormParam("password") String password
    ){
        Respuesta res = new Respuesta();
        Usuario user = new Usuario();
        user.setNombre(nombre);
        user.setTelefono(telefono);
        user.setPassword(password);
        res = validarRegistro(user);
        if (res.getErrorcode() == 0) {
            user.setIdEstatus(1);
            Random random = new Random();
            String token = String.format("%04d", random.nextInt(10000));
            user.setTokenAcceso(token);
            res = UsuarioDAO.registrarUsuario(user);
            if (!res.isError()) {
                JaxSms jax = new JaxSms();
                String rSms = jax.enviar(telefono, "Su token de acceso es: " + token);
                System.out.println("rsms: "+rSms);
                if (rSms == null || rSms.isEmpty() || Integer.parseInt(rSms)!=3) {
                    res.setError(true);
                    res.setErrorcode(4);
                    res.setMensaje("Fallo al enviar mensaje con token");
                }
            }
        }     
        return res;
    }
    
    @POST
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    public RespuestaUsuario login(
        @FormParam("telefono") String telefono,
        @FormParam("password") String password
    ) {
        RespuestaUsuario respuestaFinal = new RespuestaUsuario();
        Respuesta res = new Respuesta();
        Usuario usuario = new Usuario();
        usuario.setTelefono(telefono);
        usuario.setPassword(password);
        if (telefono == null || telefono.trim().isEmpty()) {
            res.setError(true);
            res.setErrorcode(2);
            res.setMensaje("El telefono no puede estar vacío");
            
            respuestaFinal.setRespuesta(res);
            respuestaFinal.setUsuario(null);
        }
        if (password == null || password.trim().isEmpty()) {
            res.setError(true);
            res.setErrorcode(4);
            res.setMensaje("La contraseña no puede estar vacía");
            
            respuestaFinal.setRespuesta(res);
            respuestaFinal.setUsuario(null);
        }
        if(!res.isError()) {
            respuestaFinal = UsuarioDAO.login(usuario);
        }
        return respuestaFinal;
    }
    
    @POST
    @Path("validarregistro")
    @Produces(MediaType.APPLICATION_JSON)
    /**
     * Codigos de error
     * 2 - Telefono vacio
     * 3 - Telefono no son 10 digitos
     * 5 - Token vacia
     * 6 - Token no coincide con telefono
     */
    public RespuestaUsuario validarUsuario(
            @FormParam("telefono") String telefono,
            @FormParam("token") String token
    ){
        RespuestaUsuario respuestaFinal = new RespuestaUsuario();
        Respuesta res = new Respuesta();
        Usuario usuario = new Usuario();
        usuario.setTelefono(telefono);
        usuario.setTokenAcceso(token);
        if (token == null || token.trim().isEmpty()) {
            res.setError(true);
            res.setErrorcode(7);
            
            res.setMensaje("El token de validación no puede estar vacío");
            respuestaFinal.setRespuesta(res);
            respuestaFinal.setUsuario(null);
        }
        if(!res.isError()) {
            respuestaFinal = UsuarioDAO.validarRegistro(usuario);
        }
        return respuestaFinal;
    }
}
