/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.eva.seleccion.view.controller;

import crypto.util.hash.Hash;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import pe.gob.onpe.animations.OnpeMessagesTypes;
import pe.gob.onpe.animations.view.OnpeAnimations;
import pe.gob.onpe.eva.common.dto.LoginDTO;
import pe.gob.onpe.eva.common.view.AppController;
import pe.gob.onpe.eva.model.Parametro;
import pe.gob.onpe.eva.model.Usuario;
import pe.gob.onpe.eva.seleccion.constant.ConstantsSeleccion;
import pe.gob.onpe.eva.seleccion.service.FactoryService;

/**
 * FXML Controller class
 *
 * @author aquispec
 */
public class LoginController extends LocalAppController implements Initializable {

    @FXML
    private TextField txtUsuario;
    @FXML
    private AnchorPane ppAnchoPane;
    @FXML
    private Label lblEleccion;
    @FXML
    private PasswordField txtPassword;
    public boolean boolPopup;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {  
        getConfiguracion();
        boolPopup = false;
        txtUsuario.textProperty().addListener(onlySomeCharacters("[A-Za-z0-9]", txtUsuario, 10));
        txtPassword.textProperty().addListener(onlySomeCharacters("[A-Za-z0-9]", txtPassword, 8));    
        lblEleccion.setText(ConstantsSeleccion.CONFIGURACION.getAliasEleccion());
    }

    @FXML
    public void UserEnterPressed(ActionEvent event) throws Exception {
        if (!boolPopup) {
            boolPopup = true;
            Login();
        }
    }

    @FXML
    public void PasswordEnterPressed(ActionEvent event) throws Exception {

        if (!boolPopup) {
            boolPopup = true;
            Login();
        }
    }

    @FXML
    public void validateUsuario(ActionEvent event) throws IOException, Exception {
        if (!boolPopup) {
            boolPopup = true;
            Login();
        }
    }

    @FXML
    public void handleExit(ActionEvent event) throws IOException, Exception {
        wEva.mainStage.close();
    }

    @FXML
    public void Login() throws IOException, Exception {

        OnpeAnimations modal = null;
        //final OnpeAnimations animations = null;
        //final OnpeAnimations messages = null;
        Usuario usuario = new Usuario();
        usuario.setUsuario(txtUsuario.getText().toUpperCase());
        Hash hash = Hash.getInstance();
        String claveHash = hash.generateHashHex(txtPassword.getText());
        usuario.setClave(claveHash);

        if (!usuario.getUsuario().isEmpty()) {

            if (!usuario.getClave().isEmpty()) {

                FactoryService oService = new FactoryService();

                LoginDTO userDTO = oService.getUsuarioService().validateLogin(usuario);

                if (userDTO.getObject() != null) {

                    if (userDTO.getMessage() == null) {

                        Usuario u = (Usuario) userDTO.getObject();
                        ConstantsSeleccion.USUARIO_SESSION.setPerfilPk(u.getPerfilPk());
                        ConstantsSeleccion.USUARIO_SESSION.setUsuario(u.getUsuario().toUpperCase());
                        ConstantsSeleccion.USUARIO_SESSION.setClave(u.getClave());
                        ConstantsSeleccion.USUARIO_SESSION.setEstado(u.getEstado());
                        ConstantsSeleccion.USUARIO_SESSION.setEmpleadoPk(u.getEmpleadoPk());
                        ConstantsSeleccion.USUARIO_SESSION.setUsuarioPk(u.getUsuarioPk());
                        ConstantsSeleccion.USUARIO_SESSION.setActualizarDato((u.getActualizarDato()));

                        getConfiguracion();
                        openWindows(ConstantsSeleccion.W_PRINCIPAL_SELECCION25);
                        OnpeAnimations onpeAnim = OnpeAnimations.getInstance();
                        onpeAnim.setApMain(wEva.apMain);
                        
                    } else {
                        final OnpeAnimations messages = OnpeAnimations.getInstance();
                        messages.setApMain(AppController.wEva.apMain);
                        messages.openPopup(userDTO.getMessage(), userDTO.getTypeMessage());
                        messages.openPopup(userDTO.getMessage(), userDTO.getTypeMessage(), new EventHandler() {

                            @Override
                            public void handle(Event event) {
                                boolPopup = false;
                                messages.closePopups();
                            }
                        });
                    }
                } else {
                    final OnpeAnimations messages = OnpeAnimations.getInstance();
                    messages.setApMain(AppController.wEva.apMain);
                    messages.openPopup(userDTO.getMessage(), userDTO.getTypeMessage(), new EventHandler() {

                        @Override
                        public void handle(Event event) {
                            boolPopup = false;
                            messages.closePopups();
                        }
                    });
                }

            } else {
                final OnpeAnimations messages = OnpeAnimations.getInstance();
                messages.setApMain(AppController.wEva.apMain);
                messages.openPopup(ConstantsSeleccion.MSG_ERROR_PASSWORD_VACIO, OnpeMessagesTypes.TYPE_INFORMATION, new EventHandler() {

                    @Override
                    public void handle(Event event) {
                        boolPopup = false;
                        messages.closePopups();
                    }
                });
            }
        } else {

            if (!usuario.getClave().isEmpty()) {

                final OnpeAnimations messages = OnpeAnimations.getInstance();
                messages.setApMain(AppController.wEva.apMain);
                messages.openPopup(ConstantsSeleccion.MSG_ERROR_USUARIO_VACIO, OnpeMessagesTypes.TYPE_INFORMATION, new EventHandler() {

                    @Override
                    public void handle(Event event) {
                        boolPopup = false;
                        messages.closePopups();
                    }
                });

            } else {

                final OnpeAnimations messages = OnpeAnimations.getInstance();
                messages.setApMain(AppController.wEva.apMain);
                messages.openPopup(ConstantsSeleccion.MSG_ERROR_USUARIO_PASSWORD_VACIO, OnpeMessagesTypes.TYPE_INFORMATION, new EventHandler() {

                    @Override
                    public void handle(Event event) {
                        boolPopup = false;
                        messages.closePopups();
                    }
                });

            }
        }
    }    
}
