package model.dao;

import beans.Respuesta;
import beans.RespuestaUsuario;
import beans.Usuario;
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

    public static RespuestaUsuario validarRegistro(Usuario usuario) {
        RespuestaUsuario resFinal = new RespuestaUsuario();
        Usuario nuevoUsuario = new Usuario();
        Respuesta res = new Respuesta();
        SqlSession conn = null;
        try {
            conn = MyBatisUtils.getSession();
            nuevoUsuario = conn.selectOne("Usuario.validarRegistro", usuario);
            if (nuevoUsuario != null) {
                conn.update("Usuario.actualizarEstatus", nuevoUsuario.getIdUsuario());
                conn.commit();
                res.setError(false);
                res.setErrorcode(0);
                res.setMensaje("Validación de registro de usuario realizada correctamente");
            } else {
                res.setError(true);
                res.setErrorcode(8);
                res.setMensaje("Token no coincide con el telefono");
            }
            resFinal.setRespuesta(res);
            resFinal.setUsuario(nuevoUsuario);
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
    
    public static RespuestaUsuario login(Usuario usuario) {
        RespuestaUsuario resFinal = new RespuestaUsuario();
        Usuario nuevoUsuario = new Usuario();
        Respuesta res = new Respuesta();
        SqlSession conn = null;
        try {
            conn = MyBatisUtils.getSession();
            nuevoUsuario = conn.selectOne("Usuario.login", usuario);
            if (nuevoUsuario != null) {
                res.setError(false);
                res.setErrorcode(0);
                res.setMensaje("Sesión Iniciada");
            } else{
                res.setError(true);
                res.setErrorcode(9);
                res.setMensaje("Usuario o contraseña erróneos");
            }
            resFinal.setRespuesta(res);
            resFinal.setUsuario(nuevoUsuario);
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
