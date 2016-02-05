/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.eva.seleccion.view.controller;

import com.sun.org.apache.xml.internal.serializer.utils.Utils;
import java.io.File;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import pe.gob.onpe.animations.OnpeMessagesTypes;
import pe.gob.onpe.animations.view.OnpeAnimations;
import pe.gob.onpe.eva.common.view.AppController;
import static pe.gob.onpe.eva.common.view.AppController.wEva;
import pe.gob.onpe.eva.model.Parametro;
import pe.gob.onpe.eva.seleccion.constant.ConstantsSeleccion;
import pe.gob.onpe.util.object.OnpeCalendar;

/**
 * FXML Controller class
 *
 * @author aquispec
 */
public class ConfigController extends LocalAppController implements Initializable {

    @FXML
    private Label lblEtiquetas, lblCarpetas, lblFechas;
    @FXML
    private AnchorPane apEtiquetas, apCarpetas, apFechas;
    @FXML
    private TextField txtODPE, txtDptoProvDist, txtDpto, txtProv, txtDist, txtPagina, txtPDF, txtRespaldo;
    @FXML
    private TextArea textNombreProcesoP, textNombreProcesoR;
    @FXML
    private DatePicker dpTachasInicio, dpTachasFin, dpExcusasInicio, dpExcusasFin; //dpSeleccionInicio, dpSeleccionFin, dpSorteoInicio, dpSorteoFin, 
    @FXML
    private Button btnGuardar;
    private File guarda;
    OnpeAnimations messages;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        messages = OnpeAnimations.getInstance();
        messages.setApMain(LocalAppController.wEva.apMain);

        apEtiquetas.setLayoutX(34);
        apCarpetas.setLayoutX(2000);
        apFechas.setLayoutX(2000);

        lblEtiquetas.setStyle("-fx-text-fill: #067071;");
        lblCarpetas.setStyle("-fx-text-fill: #666666;");
        lblFechas.setStyle("-fx-text-fill: #666666;");

        txtPagina.textProperty().addListener(onlySomeCharacters("[1-9]", txtPagina, 1));
        txtODPE.textProperty().addListener(onlySomeCharacters("[a-zA-Z]", txtODPE, 12));
        txtDpto.textProperty().addListener(onlySomeCharacters("[a-zA-Z]", txtDpto, 12));
        txtProv.textProperty().addListener(onlySomeCharacters("[a-zA-Z]", txtProv, 12));
        txtDist.textProperty().addListener(onlySomeCharacters("[a-zA-Z. ]", txtDist, 12));
        txtDptoProvDist.textProperty().addListener(onlySomeCharacters("[a-zA-Z-/.\\ ]", txtDptoProvDist, 18));
        textNombreProcesoP.textProperty().addListener(onlySomeCharacters("[a-zA-Z0-9áÁéÉíÍóÓúÚñÑ-/.,\\ ]", textNombreProcesoP, 250));
        textNombreProcesoR.textProperty().addListener(onlySomeCharacters("[a-zA-Z0-9áÁéÉíÍóÓúÚñÑ-/.,\\ ]", textNombreProcesoR, 300));

        txtPDF.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.length() > 200) {
                    txtPDF.setText(oldValue);
                    try {
                        messages.openPopup(getMessage("MSJ_MAX_LENGT"), OnpeMessagesTypes.TYPE_INFORMATION);

                    } catch (Exception ex) {
                        Logger.getLogger(ConfigController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        txtRespaldo.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.length() > 200) {
                    txtRespaldo.setText(oldValue);
                    try {
                        messages.openPopup(getMessage("MSJ_MAX_LENGT"), OnpeMessagesTypes.TYPE_INFORMATION);

                    } catch (Exception ex) {
                        Logger.getLogger(ConfigController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        txtDist.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                txtDist.setText(newValue.replace("  ", " "));
                if (newValue.length() == 1 && newValue.equals(" ")) {
                    txtDist.setText(oldValue);
                }
            }
        });

        txtDptoProvDist.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                txtDptoProvDist.setText(newValue.replace("  ", " "));
                if (newValue.length() == 1 && newValue.equals(" ")) {
                    txtDptoProvDist.setText(oldValue);
                }
            }
        });

        textNombreProcesoP.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                textNombreProcesoP.setText(newValue.toUpperCase().replace("  ", " "));
                if (newValue.length() == 1 && newValue.equals(" ")) {
                    textNombreProcesoP.setText(oldValue);
                }
            }
        });

        textNombreProcesoR.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                textNombreProcesoR.setText(newValue.toUpperCase().replace("  ", " "));
                if (newValue.length() == 1 && newValue.equals(" ")) {
                    textNombreProcesoR.setText(oldValue);
                }
            }
        });

        RestaurarValores();

        lblEtiquetas.setOnMouseClicked(new EventHandler<Event>() {

            @Override
            public void handle(Event event) {
                apEtiquetas.setLayoutX(34);
                apCarpetas.setLayoutX(2000);
                apFechas.setLayoutX(2000);
                lblEtiquetas.setStyle("-fx-text-fill: #067071;");
                lblCarpetas.setStyle("-fx-text-fill: #666666;");
                lblFechas.setStyle("-fx-text-fill: #666666;");
            }
        });

        lblCarpetas.setOnMouseClicked(new EventHandler<Event>() {

            @Override
            public void handle(Event event) {
                apEtiquetas.setLayoutX(2000);
                apCarpetas.setLayoutX(34);
                apFechas.setLayoutX(2000);
                lblCarpetas.setStyle("-fx-text-fill: #067071;");
                lblEtiquetas.setStyle("-fx-text-fill: #666666;");
                lblFechas.setStyle("-fx-text-fill: #666666;");
            }
        });
//        lblFechas.setOnMouseClicked(new EventHandler<Event>() {
//
//            @Override
//            public void handle(Event event) {
//                apEtiquetas.setLayoutX(2000);
//                apCarpetas.setLayoutX(2000);
//                apFechas.setLayoutX(14);
//                lblFechas.setStyle("-fx-text-fill: #067071;");
//                lblEtiquetas.setStyle("-fx-text-fill: #666666;");
//                lblCarpetas.setStyle("-fx-text-fill: #666666;");
//            }
//        });
    }

    @FXML
    public void btnSearchPDF(ActionEvent event) {
        String descripcion = "";

        DirectoryChooser pathChooser = new DirectoryChooser();
        guarda = pathChooser.showDialog(wEva.mainStage);   //showSaveDialog(wEva.mainStage);

        if (guarda != null) {
            txtPDF.setText(guarda.getPath());
        }
    }

    @FXML
    public void btnSearchRespaldo(ActionEvent event) throws Exception {
        String descripcion = "";

        DirectoryChooser pathChooser = new DirectoryChooser();
        guarda = pathChooser.showDialog(wEva.mainStage);   //showSaveDialog(wEva.mainStage);

        if (guarda != null) {
            if (guarda.getPath().contains(" ")) {
                final OnpeAnimations messages = OnpeAnimations.getInstance();
                messages.setApMain(AppController.wEva.apMain);
                messages.openPopup(ConstantsSeleccion.MSG_ERROR_PATH_RESPALDO_ESPACIOS, OnpeMessagesTypes.TYPE_INFORMATION);
            } else {
                txtRespaldo.setText(guarda.getPath());
            }

        }
    }

    @FXML
    public void menor(ActionEvent event) {

        int num = Integer.parseInt(txtPagina.getText());
        if (num > 1) {
            txtPagina.setText(String.valueOf(num - 1));
        }
    }

    @FXML
    public void mayor(ActionEvent event) {
        int num = Integer.parseInt(txtPagina.getText());
        if (num < 10) {
            txtPagina.setText(String.valueOf(num + 1));
        }
    }

    @FXML
    public void RestaurarValues(ActionEvent event) {
        RestaurarValores();
    }

    @FXML
    public void Guardar(ActionEvent event) throws Exception {

        btnGuardar.setDisable(true);

        if (txtODPE.getText().equals("")) {
            messages.openPopup(getMessage("MSJ_ETIQUETA_ODPE_VACIA"), OnpeMessagesTypes.TYPE_ERROR);
            btnGuardar.setDisable(false);
            return;
        }

        if (txtDpto.getText().equals("")) {
            messages.openPopup(getMessage("MSJ_ETIQUETA_DEPARTAMENTO_VACIA"), OnpeMessagesTypes.TYPE_ERROR);
            btnGuardar.setDisable(false);
            return;
        }

        if (txtProv.getText().equals("")) {
            messages.openPopup(getMessage("MSJ_ETIQUETA_PROVINCIA_VACIA"), OnpeMessagesTypes.TYPE_ERROR);
            btnGuardar.setDisable(false);
            return;
        }

        if (txtDist.getText().equals("")) {
            messages.openPopup(getMessage("MSJ_ETIQUETA_DISTRITO_VACIA"), OnpeMessagesTypes.TYPE_ERROR);
            btnGuardar.setDisable(false);
            return;
        }

        if (txtDptoProvDist.getText().equals("")) {
            messages.openPopup(getMessage("MSJ_ETIQUETA_DPTOPROVDIST_VACIA"), OnpeMessagesTypes.TYPE_ERROR);
            btnGuardar.setDisable(false);
            return;
        }

        if (textNombreProcesoP.getText().equals("")) {
            messages.openPopup(getMessage("MSJ_ETIQUETA_ALIAS_ELECCION_VACIA"), OnpeMessagesTypes.TYPE_ERROR);
            btnGuardar.setDisable(false);
            return;
        }

        if (textNombreProcesoR.getText().equals("")) {
            messages.openPopup(getMessage("MSJ_ETIQUETA_NOMBRE_ELECCION_VACIA"), OnpeMessagesTypes.TYPE_ERROR);
            btnGuardar.setDisable(false);
            return;
        }
//        if (dpTachasInicio.getValue() != null && dpTachasFin.getValue() != null) {
//            if (dpTachasInicio.getValue().toEpochDay() > dpTachasFin.getValue().toEpochDay()) {
//                messages.openPopup(getMessage("MSJ_DATES_INVALID"), OnpeMessagesTypes.TYPE_ERROR);
//                btnGuardar.setDisable(false);
//                return;
//            }
//        }
//        if (dpExcusasInicio.getValue() != null && dpExcusasFin.getValue() != null) {
//            if (dpExcusasInicio.getValue().toEpochDay() > dpExcusasFin.getValue().toEpochDay()) {
//                messages.openPopup(getMessage("MSJ_DATES_INVALID"), OnpeMessagesTypes.TYPE_ERROR);
//                btnGuardar.setDisable(false);
//                return;
//            }
//        }
//
//        if (dpTachasFin.getValue() != null && dpExcusasInicio.getValue() != null) {
//            if (dpTachasFin.getValue().toEpochDay() > dpExcusasInicio.getValue().toEpochDay()) {
//                messages.openPopup(getMessage("MSJ_DATES_INVALID"), OnpeMessagesTypes.TYPE_ERROR);
//                btnGuardar.setDisable(false);
//                return;
//            }
//        }
        try {
            saveConfiguracion();
            //saveFechas();

            Set<Node> newApWindow = wEva.apMain.lookupAll(".msg_Eleccion");

            for (Node nod : newApWindow) {
                Label lbl = (Label) nod;
                lbl.setText(ConstantsSeleccion.CONFIGURACION.getAliasEleccion());
            }

            newApWindow = wEva.apMain.lookupAll(".etiq_ODPE");
            for (Node nod : newApWindow) {
                Label lbl = (Label) nod;
                lbl.setText(ConstantsSeleccion.CONFIGURACION.getEtiquetaOdpe());
            }

            newApWindow = wEva.apMain.lookupAll(".dgODPE");

            for (Node nod : newApWindow) {
                Label lbl = (Label) nod;
                lbl.setText(ConstantsSeleccion.CONFIGURACION.getEtiquetaOdpe());
            }

            newApWindow = wEva.apMain.lookupAll(".etiq_Dpto");
            for (Node nod : newApWindow) {
                Label lbl = (Label) nod;
                lbl.setText(ConstantsSeleccion.CONFIGURACION.getEtiquetaDpto());
            }

            newApWindow = wEva.apMain.lookupAll(".dgDpto");

            for (Node nod : newApWindow) {
                Label lbl = (Label) nod;
                lbl.setText(ConstantsSeleccion.CONFIGURACION.getEtiquetaDpto());
            }

            newApWindow = wEva.apMain.lookupAll(".etiq_Prov");
            for (Node nod : newApWindow) {
                Label lbl = (Label) nod;
                lbl.setText(ConstantsSeleccion.CONFIGURACION.getEtiquetaProv());
            }

            newApWindow = wEva.apMain.lookupAll(".dgProv");

            for (Node nod : newApWindow) {
                Label lbl = (Label) nod;
                lbl.setText(ConstantsSeleccion.CONFIGURACION.getEtiquetaProv());
            }

            newApWindow = wEva.apMain.lookupAll(".etiq_Dist");
            for (Node nod : newApWindow) {
                Label lbl = (Label) nod;
                lbl.setText(ConstantsSeleccion.CONFIGURACION.getEtiquetaDist());
            }

            newApWindow = wEva.apMain.lookupAll(".dgDist");

            for (Node nod : newApWindow) {
                Label lbl = (Label) nod;
                lbl.setText(ConstantsSeleccion.CONFIGURACION.getEtiquetaDist());
            }

            messages.closePopups();
            messages.openPopup(getMessage("MSJ_DATOS_GUARDADOS"), OnpeMessagesTypes.TYPE_SUCCESS);
            btnGuardar.setDisable(false);

        } catch (Exception ex) {
            getConfiguracion();
            Logger.getLogger(ConfigController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void Cancelar(ActionEvent event) {
        OnpeAnimations onpeAnim = OnpeAnimations.getInstance();
        onpeAnim.closePopups();
    }

    private void RestaurarValores() {

        textNombreProcesoR.setText(ConstantsSeleccion.CONFIGURACION.getNameEleccion());
        textNombreProcesoP.setText(ConstantsSeleccion.CONFIGURACION.getAliasEleccion());

        txtPagina.setText(String.valueOf(ConstantsSeleccion.CONFIGURACION.getCopias()));
        txtODPE.setText(ConstantsSeleccion.CONFIGURACION.getEtiquetaOdpe());
        txtDpto.setText(ConstantsSeleccion.CONFIGURACION.getEtiquetaDpto());
        txtProv.setText(ConstantsSeleccion.CONFIGURACION.getEtiquetaProv());
        txtDist.setText(ConstantsSeleccion.CONFIGURACION.getEtiquetaDist());
        txtDptoProvDist.setText(ConstantsSeleccion.CONFIGURACION.getEtiquetaDptoProvDist());

//        if (!ConstantsSeleccion.PARAM_FECHA_SELECCION.getCodigo().equals("")) {
//            dpSeleccionInicio.setValue(Instant.ofEpochMilli(ConstantsSeleccion.PARAM_FECHA_SELECCION.getFechaIni().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
//            dpSeleccionFin.setValue(Instant.ofEpochMilli(ConstantsSeleccion.PARAM_FECHA_SELECCION.getFechaFin().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
//        }
//        if (!ConstantsSeleccion.PARAM_FECHA_SORTEO.getCodigo().equals("")) {
//            dpSorteoInicio.setValue(Instant.ofEpochMilli(ConstantsSeleccion.PARAM_FECHA_SORTEO.getFechaIni().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
//            dpSorteoFin.setValue(Instant.ofEpochMilli(ConstantsSeleccion.PARAM_FECHA_SORTEO.getFechaFin().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
//        }
        if (!ConstantsSeleccion.PARAM_FECHA_TACHAS.getCodigo().equals("")) {
            dpTachasInicio.setValue(Instant.ofEpochMilli(ConstantsSeleccion.PARAM_FECHA_TACHAS.getFechaIni().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
            dpTachasFin.setValue(Instant.ofEpochMilli(ConstantsSeleccion.PARAM_FECHA_TACHAS.getFechaFin().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
        }
        if (!ConstantsSeleccion.PARAM_FECHA_EXCUSAS.getCodigo().equals("")) {
            dpExcusasInicio.setValue(Instant.ofEpochMilli(ConstantsSeleccion.PARAM_FECHA_EXCUSAS.getFechaIni().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
            dpExcusasFin.setValue(Instant.ofEpochMilli(ConstantsSeleccion.PARAM_FECHA_EXCUSAS.getFechaFin().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
        }

        txtPDF.setText(ConstantsSeleccion.CONFIGURACION.getPathPDF());
        ConstantsSeleccion.CONFIGURACION.getPathPDF();
        txtRespaldo.setText(ConstantsSeleccion.CONFIGURACION.getPathRespaldo());
        ConstantsSeleccion.CONFIGURACION.getPathRespaldo();
    }

    private void saveConfiguracion() {

        ConstantsSeleccion.CONFIGURACION.setEtiquetaOdpe(txtODPE.getText());
        ConstantsSeleccion.CONFIGURACION.setEtiquetaDpto(txtDpto.getText());
        ConstantsSeleccion.CONFIGURACION.setEtiquetaProv(txtProv.getText());
        ConstantsSeleccion.CONFIGURACION.setEtiquetaDist(txtDist.getText());
        ConstantsSeleccion.CONFIGURACION.setEtiquetaDptoProvDist(txtDptoProvDist.getText());

        ConstantsSeleccion.CONFIGURACION.setNameEleccion(textNombreProcesoR.getText());
        ConstantsSeleccion.CONFIGURACION.setAliasEleccion(textNombreProcesoP.getText());

        ConstantsSeleccion.CONFIGURACION.setPathPDF(txtPDF.getText());
        ConstantsSeleccion.CONFIGURACION.setPathRespaldo(txtRespaldo.getText());
        ConstantsSeleccion.CONFIGURACION.setCopias(Integer.parseInt(txtPagina.getText()));

        oSeleccionService.getConfiguracionService().update(ConstantsSeleccion.CONFIGURACION);

    }

    private void saveFechas() {
        try {
            OnpeCalendar calendar = OnpeCalendar.getInstance();
            String fechaInicio = "";
            String fechaFin = "";
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

            calendar.setLocalDate(dpTachasInicio.getValue());
            fechaInicio = calendar.toString("dd/MM/yyyy");
            calendar.setLocalDate(dpTachasFin.getValue());
            fechaFin = calendar.toString("dd/MM/yyyy");

            if (!fechaFin.equals(df.format(ConstantsSeleccion.PARAM_FECHA_TACHAS.getFechaFin())) || !fechaInicio.equals(df.format(ConstantsSeleccion.PARAM_FECHA_TACHAS.getFechaIni()))) {
                ConstantsSeleccion.PARAM_FECHA_TACHAS.setFechaIni(Date.from(dpTachasInicio.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
                ConstantsSeleccion.PARAM_FECHA_TACHAS.setFechaFin(Date.from(dpTachasFin.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
                oSeleccionService.getParametroService().update(ConstantsSeleccion.PARAM_FECHA_TACHAS);
            }

            calendar.setLocalDate(dpExcusasInicio.getValue());
            fechaInicio = calendar.toString("dd/MM/yyyy");
            calendar.setLocalDate(dpExcusasFin.getValue());
            fechaFin = calendar.toString("dd/MM/yyyy");

            if (!fechaFin.equals(df.format(ConstantsSeleccion.PARAM_FECHA_EXCUSAS.getFechaFin())) || !fechaInicio.equals(df.format(ConstantsSeleccion.PARAM_FECHA_EXCUSAS.getFechaIni()))) {
                ConstantsSeleccion.PARAM_FECHA_EXCUSAS.setFechaIni(Date.from(dpExcusasInicio.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
                ConstantsSeleccion.PARAM_FECHA_EXCUSAS.setFechaFin(Date.from(dpExcusasFin.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
                oSeleccionService.getParametroService().update(ConstantsSeleccion.PARAM_FECHA_EXCUSAS);
            }
        } catch (Exception ex) {
            Logger.getLogger(ConfigController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
