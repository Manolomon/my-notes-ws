package ws;

import beans.Mensaje;
import beans.Nota;
import beans.Respuesta;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import model.dao.NotaDAO;

/**
 * REST Web Service
 *
 * @author Manolomon
 */
@Path("notas")
public class NotasWS {

    @Context
    private UriInfo context;

    public NotasWS() {
    }

    @POST
    @Path("enviar")
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta enviar() {
        Respuesta res = new Respuesta();
        return res;
    }
    
    @GET
    @Path("getmismensajes")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Mensaje> getMisMensajes() {
        List<Mensaje> mensajes = new ArrayList<Mensaje>();
        
        return mensajes;
    }
    
    @DELETE
    @Path("eliminar")
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta eliminar() {
        Respuesta res = new Respuesta();
        
        return res;
    }
    
    @GET
    @Path("getmensajesenviados")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Mensaje> getMensajesEnviados() {
       List<Mensaje> mensajes = new ArrayList<Mensaje>();
        
       return mensajes; 
    }
    
    @GET
    @Path("getusernotes/{idUsuario}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Nota> getUserNotes(
      @PathParam("idUsuario") Integer idUsuario
    ) {
        System.out.println("El id del usuario es: " + idUsuario);
        return NotaDAO.getUserNotes(idUsuario);
    }
    
}
