/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.eva.seleccion.view.controller;

import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import pe.gob.onpe.animations.OnpeMessagesTypes;
import pe.gob.onpe.animations.view.OnpeAnimations;

import static pe.gob.onpe.eva.common.view.AppController.wEva;
import pe.gob.onpe.eva.model.mapper.CountGrado;
import pe.gob.onpe.eva.seleccion.constant.ConstantsSeleccion;
import pe.gob.onpe.util.io.OnpeIO;

/**
 * FXML Controller class
 *
 * @author aquispec
 */
public class PuestaCeroConfirmationController extends LocalAppController implements Initializable {

    private Properties systemProperties = new Properties(System.getProperties());
    private String tempPath = systemProperties.getProperty("java.io.tmpdir");
    private ArrayList<CountGrado> lstGrado = new ArrayList<>();
    private int optionSave;
    @FXML
    private Label lblMesas, lblElecHabil, lblExcl, lblCandidatos;
    @FXML
    private Label lblPrint, lblExport;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        optionSave = 0;
        lstGrado = oSeleccionService.getSeleccionService().ConfirmationPuestaCero();

        lblPrint.setVisible(false);
        lblExport.setVisible(false);

        Integer totalMesas = lstGrado.get(0).getTotal();
        Integer elecHabil = lstGrado.get(1).getTotal();
        Integer elecExcl = lstGrado.get(2).getTotal();

        DecimalFormat nFormat = new DecimalFormat("#,###");

        lblMesas.setText(nFormat.format(totalMesas).replace(".", ","));
        lblElecHabil.setText(nFormat.format(elecHabil).replace(".", ","));
        lblExcl.setText(nFormat.format(elecExcl).replace(".", ","));
        lblCandidatos.setText("0");
    }

    @FXML
    private void handlePrint(ActionEvent event) throws IOException, JRException, PrinterException {
        String tempFile = tempPath + "\\" + ConstantsSeleccion.N_NAME_RPT_CONFIRMACION_PUESTA_CERO + ".pdf";
        String absolutePath = PuestaCeroConfirmationController.class.getClassLoader().getResource(ConstantsSeleccion.PATH_LOGO_ONPE).toString();
        optionSave = ConstantsSeleccion.N_OPTION_PRINT;
        Locale locale = new Locale("pe", "PE");
        HashMap<String, Object> parameters = new HashMap();
        parameters.put(JRParameter.REPORT_LOCALE, locale);
        parameters.put("PATH_LOGO", absolutePath);
        parameters.put("ELECCION", ConstantsSeleccion.CONFIGURACION.getNameEleccion());
        parameters.put("titulo", ConstantsSeleccion.N_NAME_RPT_CONFIRMACION_PUESTA_CERO);

        for (int i = 0; i < ConstantsSeleccion.CONFIGURACION.getCopias(); i++) {
            ImprimirPDF(tempFile, parameters);
        }

        fadeIn(lblPrint, 2000, new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                fadeOut(lblPrint, 2000, null);
            }
        });

    }

    @FXML
    private void handleExport(ActionEvent event) throws IOException, JRException, PrinterException, URISyntaxException, Exception {
        optionSave = ConstantsSeleccion.N_OPTION_SAVEAS;
        String absolutePath = PuestaCeroConfirmationController.class.getClassLoader().getResource(ConstantsSeleccion.PATH_LOGO_ONPE).toString();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(ConstantsSeleccion.N_NAME_RPT_CONFIRMACION_PUESTA_CERO);
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("pdf files (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);
        File guarda = new File(ConstantsSeleccion.CONFIGURACION.getPathPDF() + File.separator + ConstantsSeleccion.N_NAME_RPT_CONFIRMACION_PUESTA_CERO + ".pdf"); //fileChooser.showSaveDialog(wEva.mainStage); 

        if (Files.isDirectory(Paths.get(ConstantsSeleccion.CONFIGURACION.getPathPDF()))) {
            guarda = new File(ConstantsSeleccion.CONFIGURACION.getPathPDF() + File.separator + ConstantsSeleccion.N_NAME_RPT_CONFIRMACION_PUESTA_CERO + ".pdf"); //fileChooser.showSaveDialog(wEva.mainStage);

            if (guarda.exists()) {
                if (guarda.delete()) {                    
                    Locale locale = new Locale("pe", "PE");
                    HashMap<String, Object> parameters = new HashMap();
                    parameters.put(JRParameter.REPORT_LOCALE, locale);
                    parameters.put("PATH_LOGO", absolutePath);
                    parameters.put("ELECCION", ConstantsSeleccion.CONFIGURACION.getNameEleccion());
                    parameters.put("titulo", ConstantsSeleccion.N_NAME_RPT_CONFIRMACION_PUESTA_CERO);
                    ImprimirPDF(guarda.getPath(), parameters);

                    fadeIn(lblExport, 2000, new EventHandler<ActionEvent>() {

                        @Override
                        public void handle(ActionEvent event) {
                            fadeOut(lblExport, 2000, null);
                        }
                    });
                } else {
                    final OnpeAnimations onpeAnim = OnpeAnimations.getInstance();
                    onpeAnim.setApMain(wEva.apMain);
                    onpeAnim.openPopup(getMessage("MSJ_PATH_FILE_OPEN"), OnpeMessagesTypes.TYPE_ERROR);
                }
            } else {
                Locale locale = new Locale("pe", "PE");
                HashMap<String, Object> parameters = new HashMap();
                parameters.put(JRParameter.REPORT_LOCALE, locale);
                parameters.put("PATH_LOGO", absolutePath);
                parameters.put("ELECCION", ConstantsSeleccion.CONFIGURACION.getNameEleccion());
                parameters.put("titulo", ConstantsSeleccion.N_NAME_RPT_CONFIRMACION_PUESTA_CERO);
                ImprimirPDF(guarda.getPath(), parameters);

                fadeIn(lblExport, 2000, new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        fadeOut(lblExport, 2000, null);
                    }
                });
            }

        } else {
            final OnpeAnimations onpeAnim = OnpeAnimations.getInstance();
            onpeAnim.setApMain(wEva.apMain);
            onpeAnim.openPopup(getMessage("MSJ_PATH_NOT_EXIST"), OnpeMessagesTypes.TYPE_ERROR);
        }
    }

    private void ImprimirPDF(String mPath, HashMap<String, Object> parameters) throws JRException, PrinterException, IOException {
        LocalAppController.inputStreamPuestaCero = PuestaCeroConfirmationController.class.getClassLoader().getResource(ConstantsSeleccion.PATH_REPORT_JRXML + ConstantsSeleccion.JASPER_NAME_RPT_CONFIRMACION_PUESTA_CERO).openStream();  //getClass().getResourceAsStream("jrxml/puestaCero.jasper");
        JasperReport reporte = (JasperReport) JRLoader.loadObject(LocalAppController.inputStreamPuestaCero);//("../jrxml/puestaCero.jasper");
        for (CountGrado cGrado : lstGrado) {
            cGrado.setDescripcion(cGrado.getDescripcion().replace("HABILES", "HÁBILES"));
        }
        JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, parameters, new JRBeanCollectionDataSource(lstGrado));
        JRExporter exporter = new JRPdfExporter();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_FILE, new File(mPath));
        exporter.exportReport();

        if (optionSave == ConstantsSeleccion.N_OPTION_PRINT) {
            File oFile = new File(mPath);
            OnpeIO onpeIo = OnpeIO.getInstance();
            onpeIo.seamlessPdfPrint(oFile);
        }
    }
}
