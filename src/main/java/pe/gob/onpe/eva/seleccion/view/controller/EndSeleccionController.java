/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.eva.seleccion.view.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import pe.gob.onpe.animations.view.OnpeAnimations;
import static pe.gob.onpe.eva.common.view.AppController.wEva;
import pe.gob.onpe.eva.seleccion.constant.ConstantsSeleccion;

/**
 * FXML Controller class
 *
 * @author aquispec
 */
public class EndSeleccionController extends LocalAppController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Label lblFechaInicio,lblFechaFin;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        if (ConstantsSeleccion.STR_FECHA_HORA_INICIO != null && ConstantsSeleccion.STR_FECHA_HORA_FIN != null) {
            lblFechaInicio.setText(ConstantsSeleccion.STR_FECHA_HORA_INICIO);
            lblFechaFin.setText(ConstantsSeleccion.STR_FECHA_HORA_FIN);
        }        
    }    
    
    @FXML
    private void Cerrar(ActionEvent event) throws IOException{
        openWindows(ConstantsSeleccion.W_LOGIN);
    }
}
