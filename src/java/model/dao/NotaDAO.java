/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import beans.Nota;
import java.util.ArrayList;
import java.util.List;
import model.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;

/**
 *
 * @author Manolomon
 */
public class NotaDAO {
    public static List<Nota> getUserNotes(Integer idUsuario) {
        List<Nota> list = new ArrayList<Nota>();
        SqlSession conn = null;
        try {
            conn = MyBatisUtils.getSession();
            list = conn.selectList("Nota.getUserNotes", idUsuario);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return list;
    }
    
    public static int deleteNote(Integer idNota) {
        int filas = 0;
        if (idNota != null) {    
            SqlSession conn = null;
            try {
                conn = MyBatisUtils.getSession();
                filas = conn.delete("Nota.delete", idNota);
                conn.commit(); // Para INSERT, UPDATE y DELETE
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.close();
                }
            }
        }
        return filas;
    }
    
    public static int updateNote(Nota note) {
        int filas = 0;
        if (note != null) {    
            SqlSession conn = null;
            try {
                conn = MyBatisUtils.getSession();
                filas = conn.update("Nota.update", note);
                conn.commit(); // Para INSERT, UPDATE y DELETE
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.close();
                }
            }
        }
        return filas;
    }
    
    public static int saveNote(Nota note) {
        int filas = 0;
        if (note != null) {    
            SqlSession conn = null;
            try {
                conn = MyBatisUtils.getSession();
                filas = conn.insert("Nota.save", note);
                conn.commit(); // Para INSERT, UPDATE y DELETE
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.close();
                }
            }
        }
        return filas;
    }
    
    
}
