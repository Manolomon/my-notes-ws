package model.dao;

import beans.Respuesta;
import beans.RespuestaUsuarioMensaje;
import beans.Usuario;
import java.util.HashMap;
import model.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;

/**
 *
 * @author Manolomon
 */
public class UsuarioDAO {
    public static Respuesta registrarUsuario(Usuario usuario) {
        Respuesta res = new Respuesta();
        res.setError(false);
        SqlSession conn = null;
        try {
            conn = MyBatisUtils.getSession();
            String telefono = usuario.getTelefono();
            Integer validacion = conn.selectOne("Usuario.buscarNumero", telefono);
            if (validacion == 1) {
                res.setError(true);
                res.setErrorcode(6);
                res.setMensaje("Ya hay un usuario registrado con ese número");
            } else {
                int fa = 0;
                fa = conn.insert("Usuario.registrar", usuario);
                conn.commit();
                if (fa == 1) {
                    res.setErrorcode(0);
                    res.setMensaje("Usuario registrado en BD");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            res.setError(true);
            res.setErrorcode(99);
            res.setMensaje("Excepcion: " + ex.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return res;
    }

    public static RespuestaUsuarioMensaje validarRegistro(String telefono, String token) {
        RespuestaUsuarioMensaje resFinal = new RespuestaUsuarioMensaje();
        Usuario usuario = new Usuario();
        Respuesta res = new Respuesta();
        SqlSession conn = null;
        try {
            conn = MyBatisUtils.getSession();
            HashMap<String, Object> parametros = new HashMap<String, Object>();
            parametros.put("telefono", telefono);
            parametros.put("token", token);
            usuario = conn.selectOne("Usuario.validarRegistro", parametros);
            if (usuario != null) {
                conn.update("Usuario.actualizarEstatus", usuario.getIdUsuario());
                res.setError(false);
                res.setErrorcode(0);
                res.setMensaje("Validación de registro de usuario realizada correctamente");
            } else {
                res.setError(true);
                res.setErrorcode(6);
                res.setMensaje("Token no coincide con el telefono");
            }
            resFinal.setRespuesta(res);
            resFinal.setUsuario(usuario);
        } catch (Exception ex) {
            ex.printStackTrace();
            res.setError(true);
            res.setErrorcode(99);
            res.setMensaje("Excepcion: " + ex.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

        return resFinal;
    }
}
