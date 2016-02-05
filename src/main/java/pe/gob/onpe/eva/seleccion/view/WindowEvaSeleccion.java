/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.eva.seleccion.view;

import pe.gob.onpe.eva.common.view.Screen;
import pe.gob.onpe.eva.common.view.WindowMain;

/**
 *
 * @author aquispec
 */
public class WindowEvaSeleccion extends WindowMain {

    public WindowEvaSeleccion() {
        this.pathImage = "/pe/gob/onpe/eva/users/view/image/";
        this.windows = new Screen[]{
            new Screen("Login1.fxml", "LOGIN"),
            new Screen("Principal.fxml", "SELECCION 25"),
            new Screen("PuestaCero.fxml", "PUESTA A CERO"),
            new Screen("PuestaCeroConfirmacion.fxml", "CONFIRMACIÓN PUESTA A CERO"),
            new Screen("Seleccion.fxml", "PROCESO DE SELECCIÓN"),
            new Screen("Reportes.fxml", "REPORTES"),
            new Screen("EndSeleccion.fxml", "FIN DE SELECCION"),
            new Screen("ModalAutorizacion.fxml", "AUTORIZACIÓN"),
            new Screen("ModalVersion.fxml", "VERSION"),
            new Screen("GenerateDump.fxml", "RESPALDO"),
            new Screen("ModalAutPassword.fxml", "PASSWORD INCORRECTO"),
            new Screen("Configuracion.fxml", "CONFIGURACIÓN")
        };
    }
}
