/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.eva.seleccion.view.controller;

import crypto.util.hash.Hash;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
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
import pe.gob.onpe.animations.view.OnpeAnimations;
import pe.gob.onpe.eva.common.EvaConstant;
import pe.gob.onpe.eva.model.Version;
import pe.gob.onpe.eva.seleccion.constant.ConstantsSeleccion;
import pe.gob.onpe.util.io.OnpeIO;
import pe.gob.onpe.util.object.OnpeCalendar;

/**
 * FXML Controller class
 *
 * @author aquispec
 */
public class ModalVersionController extends LocalAppController implements Initializable {

    @FXML
    private Label lblVersion, lblCodigoVersion, lblFecha, lblFechaActualizacion;
    ArrayList<Version> lstVersion;
    /**
     * Initializes the controller class.
     */
    private Properties systemProperties = new Properties(System.getProperties());
    private String tempPath = systemProperties.getProperty("java.io.tmpdir");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        lstVersion = oSeleccionService.getSeleccionService().fethVersionEleccionActiva();
        lstVersion.get(0).setVersionSorteo(lstVersion.get(0).getVersionSorteo().replace("-", "/"));
        lstVersion.get(0).setVversionSorteo(lstVersion.get(0).getVversionSorteo().replace("-", "/"));
        OnpeCalendar onpeCal = OnpeCalendar.getInstance();
        Hash oHash = Hash.getInstance();
        File oFile = new File(EvaConstant.PATH_APP + "\\" + ConstantsSeleccion.APP_NAME);
        String hash = "";

        if (oFile.exists()) {
            try {
                hash = oHash.generateHash(oFile, true);
                Integer size = hash.length();

                if (size > 27) {
                    hash = hash.substring(0, 27);
                }

            } catch (Exception ex) {
                Logger.getLogger(ModalVersionController.class.getName()).log(Level.SEVERE, null, ex);
            }

            for (Version version : lstVersion) {
                //onpeCal.setTime(version.getFechaSeleccion());
                lblVersion.setText(version.getVersionSeleccion());
                ConstantsSeleccion.STR_VERSION_CODIGO = version.getVersionSeleccion();
                lblCodigoVersion.setText(hash);
                //onpeCal.setTime(version.getFechaSeleccion());
                lblFecha.setText(version.getVersionSorteo());
                //onpeCal.setTime(version.getFechaDb());
                lblFechaActualizacion.setText(version.getVversionSorteo());
            }
        }

    }

    @FXML
    private void Aceptar(ActionEvent event) throws IOException {
        //openWindows(ConstantsSeleccion.W_PRINCIPAL_SELECCION25);
        OnpeAnimations onpeAnim = OnpeAnimations.getInstance();
        onpeAnim.closePopups();
        
    }

    @FXML
    private void Imprimir(ActionEvent event) throws IOException, JRException, PrinterException {
        String absolutePath = PuestaCeroConfirmationController.class.getClassLoader().getResource(ConstantsSeleccion.PATH_LOGO_ONPE).toString();

        HashMap<String, Object> parameters = new HashMap();
        Locale locale = new Locale("pe", "PE");
        parameters.put(JRParameter.REPORT_LOCALE, locale);
        parameters.put("PATH_LOGO", absolutePath);
        parameters.put("titulo", ConstantsSeleccion.N_NAME_RPT_VERSION);
        parameters.put("ELECCION", ConstantsSeleccion.CONFIGURACION.getNameEleccion());
        String tempFile = tempPath + "\\" + ConstantsSeleccion.N_NAME_RPT_VERSION + ".pdf";
        ImprimirPDF(tempFile, parameters);
    }

    private void ImprimirPDF(String mPath, HashMap<String, Object> parameters) throws JRException, PrinterException, IOException {

        InputStream inputStreamResumen = getClass().getClassLoader().getResource(ConstantsSeleccion.PATH_REPORT_JRXML + ConstantsSeleccion.JASPER_NAME_RPT_VERSION).openStream();
        JasperReport reporte = (JasperReport) JRLoader.loadObject(inputStreamResumen);
        JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, parameters, new JRBeanCollectionDataSource(lstVersion));        
        JRExporter exporter = new JRPdfExporter();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_FILE, new File(mPath));
        exporter.exportReport();

        File mFile = new File(mPath);//
        OnpeIO onpeIo = OnpeIO.getInstance();
        onpeIo.seamlessPdfPrint(mFile);
    }
}
