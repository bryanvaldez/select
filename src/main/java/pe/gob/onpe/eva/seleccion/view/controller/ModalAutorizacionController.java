/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.eva.seleccion.view.controller;

import crypto.util.hash.Hash;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import pe.gob.onpe.animations.OnpeMessagesTypes;
import pe.gob.onpe.animations.view.OnpeAnimations;

import pe.gob.onpe.eva.common.view.AppController;
import pe.gob.onpe.eva.seleccion.constant.ConstantsSeleccion;

/**
 * FXML Controller class
 *
 * @author aquispec
 */
public class ModalAutorizacionController extends LocalAppController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private AnchorPane apModal, apAut;
    @FXML
    private Button btnAceptar, btnSalir;
    @FXML
    private PasswordField txtClave;
    @FXML
    private Label lblError;

    private EventHandler eventMessage;
    private String mClave;
    public boolean boolPopup;
    OnpeAnimations messages;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        boolPopup = false;
        lblError.setVisible(false);
        txtClave.textProperty().addListener(onlySomeCharacters("[A-Za-z0-9]", txtClave, 8));
    }

    @FXML
    private void Cancelar(ActionEvent event) {
        OnpeAnimations onpeAnim = OnpeAnimations.getInstance();
        onpeAnim.closePopups();
    }

    @FXML
    private void clavePressEnter(ActionEvent event) throws IOException, Exception {

        VerificarClave();

    }

    @FXML
    private void Aceptar(ActionEvent event) throws IOException, Exception {

        VerificarClave();
    }

    private void VerificarClave() throws IOException, Exception {

        int i = 0;
        for (Node nodo : wEva.apMain.lookupAll(".popup_background")) {
            i++;
        }
        if (i == 2) {
            if (!txtClave.getText().equals("")) {
                Hash hash = Hash.getInstance();
                String claveHash = hash.generateHashHex(txtClave.getText());

                if (claveHash.equals(ConstantsSeleccion.USUARIO_SESSION.getClave())) {
                    ConstantsSeleccion.WINDOW_SELECTED = ConstantsSeleccion.W_SEL_PUESTA0;
                    openWindows(ConstantsSeleccion.W_SEL_PUESTA0);

                } else {
                    final OnpeAnimations messages = OnpeAnimations.getInstance();
                    messages.setApMain(AppController.wEva.apMain);
                    messages.openPopup(ConstantsSeleccion.MSG_ERROR_PASSWORD_INCORRECTO, OnpeMessagesTypes.TYPE_ERROR);
                    txtClave.setText("");
                }
            } else {
                final OnpeAnimations messages = OnpeAnimations.getInstance();
                messages.setApMain(AppController.wEva.apMain);
                messages.openPopup(ConstantsSeleccion.MSG_ERROR_PASSWORD_VACIO, OnpeMessagesTypes.TYPE_ERROR);
            }
        }

    }

}
