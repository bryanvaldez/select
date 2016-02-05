package pe.gob.onpe.eva.seleccion.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import pe.gob.onpe.animations.OnpeMessagesTypes;
import pe.gob.onpe.eva.common.dto.EmpleadoDTO;
import pe.gob.onpe.eva.common.dto.LoginDTO;
import pe.gob.onpe.eva.model.Usuario;
import pe.gob.onpe.eva.dao.FactoryDao;
import pe.gob.onpe.eva.enums.Profile;
import pe.gob.onpe.eva.model.Cargo;
import pe.gob.onpe.eva.model.Empleado;
import pe.gob.onpe.eva.model.Perfil;
import pe.gob.onpe.eva.seleccion.constant.ConstantsSeleccion;
import pe.gob.onpe.eva.seleccion.service.iface.IUsuarioService;
//import static pe.gob.onpe.eva.perso.constant.UserConstants.*;

/**
 *
 * @author Jose Limachi
 */
public class UserService implements IUsuarioService {

    FactoryDao oFactor = new FactoryDao();
    private String msgPopup;
    private int msgType;

    @Override
    public void changeStatusBackup(Usuario usuario, Integer status) {
        Usuario user = new Usuario();

        if (usuario.getUsuarioPk() != 0) {
            user.setUsuarioPk(usuario.getUsuarioPk());

            try {
                oFactor.getConexionDao().conexionOpen();
                List<Usuario> lstUser = oFactor.getUsuarioDao().fetchAll(user);
                Usuario userLog = lstUser.get(0);

                userLog.setEstado(status);

                oFactor.getUsuarioDao().update(userLog);
            } catch (Exception e) {

            }
            oFactor.getConexionDao().conexionClose();
        }

    }

    @Override
    public void logout(Usuario usuario) {
        Usuario user = new Usuario();

        if (usuario.getUsuarioPk() != 0) {
            user.setUsuarioPk(usuario.getUsuarioPk());

            try {
                oFactor.getConexionDao().conexionOpen();
                List<Usuario> lstUser = oFactor.getUsuarioDao().fetchAll(user);
                Usuario userLog = lstUser.get(0);

                userLog.setEstado(ConstantsSeleccion.N_LOGIN_AVAILABLE);

                oFactor.getUsuarioDao().update(userLog);
            } catch (Exception e) {

            }
            oFactor.getConexionDao().conexionClose();
        }
    }

    @Override
    public LoginDTO validateLogin(Usuario usuario) {

        //User validations
        LoginDTO response = new LoginDTO();
        Usuario usuarioObject = null;

        //This user is obtained by employePK
        Usuario usu = new Usuario();
        usu.setUsuario(usuario.getUsuario());

        ArrayList<Usuario> listaUsuarios = new ArrayList<>();
        try {
            // open conection
            oFactor.getConexionDao().conexionOpen();
            // data list
            listaUsuarios = oFactor.getUsuarioDao().fetchAll(usu);
        } catch (Exception e) {
            msgType = OnpeMessagesTypes.TYPE_ERROR;
            msgPopup = ConstantsSeleccion.MSG_EXCEPTION_001 + e.getMessage();
        } finally {
            // close conection
            oFactor.getConexionDao().conexionClose();
        }

        //If user by user exists
        if (!listaUsuarios.isEmpty()) {

            usu = listaUsuarios.get(0);
            if (usu.getEstado() != ConstantsSeleccion.N_LOGIN_BLOCKED) {

                if (usu.getClave().equals(usuario.getClave())) {

                    Perfil perfil = new Perfil();
                    perfil.setPerfilPk(usu.getPerfilPk());

                    ArrayList<Perfil> listaPerfil = new ArrayList<>();
                    try {
                        // open conection
                        oFactor.getConexionDao().conexionOpen();
                        // data list
                        listaPerfil = oFactor.getPerfilDao().fetchAll(perfil);
                    } catch (Exception e) {
                        msgType = OnpeMessagesTypes.TYPE_ERROR;
                        msgPopup = ConstantsSeleccion.MSG_EXCEPTION_001 + e.getMessage();
                    } finally {
                        // close conection
                        oFactor.getConexionDao().conexionClose();
                    }

                    if (listaPerfil.get(0).getEstado() == ConstantsSeleccion.ESTADO_PERFIL_ACTIVO) {

                        if (usu.getPerfilPk() == Profile.perfilSGIST.getId()) {

                            switch (usu.getEstado()) {

                                case ConstantsSeleccion.N_LOGIN_AVAILABLE: {

                                    usu.setEstado(ConstantsSeleccion.N_LOGIN_UNAVAILABLE);                                    
                                    usu.setDModUser(usu.getUsuarioPk());
                                    usu.setUltimoIngreso(new Date());

                                    try {
                                        // open conection
                                        oFactor.getConexionDao().conexionOpen();
                                        // execute command
                                        oFactor.getUsuarioDao().update(usu);
                                    } catch (Exception e) {
                                        msgType = OnpeMessagesTypes.TYPE_ERROR;
                                        msgPopup = ConstantsSeleccion.MSG_EXCEPTION_001 + e.getMessage();
                                    } finally {
                                        // close conection
                                        oFactor.getConexionDao().conexionClose();
                                    }

                                    usuarioObject = usu;
                                    //usuarioObject.setClave("");//set password vacio

                                }
                                break;

                                case ConstantsSeleccion.N_LOGIN_UNAVAILABLE: {

                                    if (ConstantsSeleccion.NUM_FAILED_LOGINS == ConstantsSeleccion.LOGIN_FAILED) {
                                        //This user is blocked by 5 intents
                                        try {
                                            usu.setEstado(ConstantsSeleccion.N_LOGIN_BLOCKED);
                                            // open conection
                                            oFactor.getConexionDao().conexionOpen();
                                            // execute command
                                            oFactor.getUsuarioDao().update(usu);
                                        } catch (Exception e) {
                                            msgType = OnpeMessagesTypes.TYPE_ERROR;
                                            msgPopup = ConstantsSeleccion.MSG_EXCEPTION_001 + e.getMessage();
                                        } finally {
                                            // close conection
                                            oFactor.getConexionDao().conexionClose();
                                        }
                                        msgPopup = ConstantsSeleccion.MSG_ERROR_NUM_FAILED_LOGINS;
                                        msgType = OnpeMessagesTypes.TYPE_ERROR;
                                    } else {
                                        msgPopup = ConstantsSeleccion.MSG_USUARIO_SESSION_ACTIVE;
                                        msgType = OnpeMessagesTypes.TYPE_INFORMATION;
                                        //++ConstantsSeleccion.NUM_FAILED_LOGINS;
                                    }
                                }
                                break;

                                case ConstantsSeleccion.N_LOGIN_NEW: {
                                    usu.setEstado(ConstantsSeleccion.N_LOGIN_UNAVAILABLE);                                    
                                    usu.setDModUser(usu.getUsuarioPk());
                                    try {
                                        // open conection
                                        oFactor.getConexionDao().conexionOpen();
                                        // execute command
                                        oFactor.getUsuarioDao().update(usu);
                                    } catch (Exception e) {
                                        msgType = OnpeMessagesTypes.TYPE_ERROR;
                                        msgPopup = ConstantsSeleccion.MSG_EXCEPTION_001 + e.getMessage();
                                    } finally {
                                        // close conection
                                        oFactor.getConexionDao().conexionClose();
                                    }

                                    usuarioObject = usu;
                                    usuarioObject.setEstado(ConstantsSeleccion.N_LOGIN_NEW);//Change the login status to new state to validate the change password
                                    usuarioObject.setClave("");//set password vacio

                                }
                                break;

                                default: {
                                    msgPopup = ConstantsSeleccion.MSG_ERROR_NUM_FAILED_LOGINS;
                                    msgType = OnpeMessagesTypes.TYPE_ERROR;
                                }
                            }

                        } else {
                            if (ConstantsSeleccion.NUM_FAILED_LOGINS == ConstantsSeleccion.LOGIN_FAILED) {
                                //This user is blocked by 5 intents
                                try {
                                    usu.setEstado(ConstantsSeleccion.N_LOGIN_BLOCKED);
                                    // open conection
                                    oFactor.getConexionDao().conexionOpen();
                                    // execute command
                                    oFactor.getUsuarioDao().update(usu);
                                } catch (Exception e) {
                                    msgType = OnpeMessagesTypes.TYPE_ERROR;
                                    msgPopup = ConstantsSeleccion.MSG_EXCEPTION_001 + e.getMessage();
                                } finally {
                                    // close conection
                                    oFactor.getConexionDao().conexionClose();
                                }
                                msgPopup = ConstantsSeleccion.MSG_ERROR_NUM_FAILED_LOGINS;
                                msgType = OnpeMessagesTypes.TYPE_ERROR;
                            } else {
                                msgPopup = ConstantsSeleccion.MSG_ERROR_VAL_PERFIL;
                                msgType = OnpeMessagesTypes.TYPE_ERROR;
                                //++ConstantsSeleccion.NUM_FAILED_LOGINS;
                            }
                        }

                    } else {

                        if (ConstantsSeleccion.NUM_FAILED_LOGINS == ConstantsSeleccion.LOGIN_FAILED) {
                            //This user is blocked by 5 intents
                            try {
                                usu.setEstado(ConstantsSeleccion.N_LOGIN_BLOCKED);
                                // open conection
                                oFactor.getConexionDao().conexionOpen();
                                // execute command
                                oFactor.getUsuarioDao().update(usu);
                            } catch (Exception e) {
                                msgType = OnpeMessagesTypes.TYPE_ERROR;
                                msgPopup = ConstantsSeleccion.MSG_EXCEPTION_001 + e.getMessage();
                            } finally {
                                // close conection
                                oFactor.getConexionDao().conexionClose();
                            }
                            msgPopup = ConstantsSeleccion.MSG_ERROR_NUM_FAILED_LOGINS;
                            msgType = OnpeMessagesTypes.TYPE_ERROR;
                        } else {
                            msgPopup = ConstantsSeleccion.MSG_ERROR_PERFIL_INACTIVO;
                            msgType = OnpeMessagesTypes.TYPE_INFORMATION;
                            //++ConstantsSeleccion.NUM_FAILED_LOGINS;
                        }
                    }
                } else {

                    if (ConstantsSeleccion.NUM_FAILED_LOGINS == ConstantsSeleccion.LOGIN_FAILED) {
                        //This user is blocked by 5 intents
                        try {
                            usu.setEstado(ConstantsSeleccion.N_LOGIN_BLOCKED);
                            // open conection
                            oFactor.getConexionDao().conexionOpen();
                            // execute command
                            oFactor.getUsuarioDao().update(usu);
                        } catch (Exception e) {
                            msgType = OnpeMessagesTypes.TYPE_ERROR;
                            msgPopup = ConstantsSeleccion.MSG_EXCEPTION_001 + e.getMessage();
                        } finally {
                            // close conection
                            oFactor.getConexionDao().conexionClose();
                        }
                        msgType = OnpeMessagesTypes.TYPE_ERROR;
                        msgPopup = ConstantsSeleccion.MSG_ERROR_NUM_FAILED_LOGINS;
                    } else {
                        msgType = OnpeMessagesTypes.TYPE_ERROR;
                        msgPopup = ConstantsSeleccion.MSG_ERROR_PASSWORD_INCORRECTO;
                        //++ConstantsSeleccion.NUM_FAILED_LOGINS;
                    }
                }

            } else {
                //This user is blocked by 5 intents
                msgType = OnpeMessagesTypes.TYPE_ERROR;
                msgPopup = ConstantsSeleccion.MSG_ERROR_NUM_FAILED_LOGINS;
            }

        } else {
            msgPopup = ConstantsSeleccion.MSG_ERROR_USUARIO_INCORRECTO;
            msgType = OnpeMessagesTypes.TYPE_ERROR;
        }
       
        //Responses the DTO object
        response.setMessage(msgPopup);
        response.setObject(usuarioObject);
        response.setTypeMessage(msgType);
        return response;
    }
}
