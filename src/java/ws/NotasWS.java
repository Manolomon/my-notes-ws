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
import javax.ws.rs.FormParam;
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

    @GET
    @Path("getusernotes/{idUsuario}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Nota> getUserNotes(@PathParam("idUsuario") Integer idUsuario) {
        System.out.println("El id del usuario es: " + idUsuario);
        return NotaDAO.getUserNotes(idUsuario);
    }
    
    @POST
    @Path("agregar")
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta saveNote(
        @FormParam("idUsuario") Integer idUsuario,
        @FormParam("titulo") String titulo,
        @FormParam("contenido") String contenido
    ) {
        Respuesta respuesta = new Respuesta();
        Nota note = new Nota();
        note.setIdUsuario(idUsuario);
        note.setTitulo(titulo);
        note.setContenido(contenido);
        int filas = 0;
        filas = NotaDAO.saveNote(note);
        if (filas > 0) {
            respuesta.setMensaje("Nota guardada correctamente");
        } else {
            respuesta.setError(true);
            respuesta.setErrorcode(98);
            respuesta.setMensaje("No se pudo guardar la nota");
        }
        return respuesta;
    }
    
    @PUT
    @Path("actualizar")
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta updateNote(
        @FormParam("idNota") Integer idNota,
        @FormParam("titulo") String titulo,
        @FormParam("contenido") String contenido
    ) {
        Respuesta respuesta = new Respuesta();
        Nota note = new Nota();
        note.setIdNota(idNota);
        note.setTitulo(titulo);
        note.setContenido(contenido);
        int filas = 0;
        filas = NotaDAO.updateNote(note);
        if (filas > 0) {
            respuesta.setMensaje("Nota actualizada correctamente");
        } else {
            respuesta.setError(true);
            respuesta.setErrorcode(98);
            respuesta.setMensaje("No se pudo actualizar la nota");
        }
        return respuesta;
    }

    @DELETE
    @Path("eliminar")
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta deleteNote(
        @FormParam("idNota") Integer idNota
    ) {
        Respuesta respuesta = new Respuesta();
        int filas = 0;
        filas = NotaDAO.deleteNote(idNota);
        if (filas > 0) {
            respuesta.setMensaje("Registro eliminado correctamente");
        } else {
            respuesta.setError(true);
            respuesta.setErrorcode(98);
            respuesta.setMensaje("No se pudo eliminar el registro");
        }
        return respuesta;
    }
    
}
