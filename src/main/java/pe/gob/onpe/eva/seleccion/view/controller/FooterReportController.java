/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.eva.seleccion.view.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.fill.JRFileVirtualizer;
import net.sf.jasperreports.engine.fill.JRSwapFileVirtualizer;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSwapFile;
import net.sf.jasperreports.engine.util.xml.JRXPathExecuterUtils;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;
import pe.gob.onpe.animations.OnpeMessagesTypes;
import pe.gob.onpe.animations.view.OnpeAnimations;
import static pe.gob.onpe.eva.common.view.AppController.wEva;
import pe.gob.onpe.eva.dao.FactoryDao;
import pe.gob.onpe.eva.dao.common.util.DBUtil;
import pe.gob.onpe.eva.dao.common.util.ParameterDirection;
import pe.gob.onpe.eva.dao.common.util.ParameterOracle;
import pe.gob.onpe.eva.dao.impl.ConexionDao;
import pe.gob.onpe.eva.dao.impl.ParametroDao;
import pe.gob.onpe.eva.seleccion.constant.ConstantsSeleccion;
import static pe.gob.onpe.eva.seleccion.view.controller.LocalAppController.timer;
import pe.gob.onpe.util.io.OnpeIO;

/**
 * FXML Controller class
 *
 * @author aquispec
 */
public class FooterReportController extends LocalAppController implements Initializable {

    @FXML
    private Label lblPrint, lblExport;
    @FXML
    private Button btnPrint, btnExport;
    private Boolean success = true;
    private Properties systemProperties = new Properties(System.getProperties());
    private String tempPath = systemProperties.getProperty("java.io.tmpdir");
    private File guarda;

    @FXML
    private Button btnBuscar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        btnPrint.setVisible(false);
        btnExport.setVisible(false);
        lblPrint.setVisible(false);
        lblExport.setVisible(false);
        //btnBuscar = (Button) wEva.apMain.lookupAll("#btnSearch");
    }

    @FXML
    private void handlePrint(ActionEvent event) {
        success = false;
        btnBuscar = (Button) wEva.apMain.lookup("#btnSearch");
        Task task = new Task() {

            @Override
            protected Object call() throws Exception {

                File tempFile = new File(tempPath + "\\" + ConstantsSeleccion.N__SELECTED_REPORT.getDescripcion() + ".pdf");
                //JasperPrint jasperPrint = oSeleccionService.getSeleccionService().generarJasperPrint(ConstantsSeleccion.N__SELECTED_REPORT, ConstantsSeleccion.lstReport);

                JasperPrint jasperPrint = null;
                if (ConstantsSeleccion.N__SELECTED_REPORT.getCodigo().equals(ConstantsSeleccion.N_RPT_LISTA_PADRON_X_PESOS)
                        || ConstantsSeleccion.N__SELECTED_REPORT.getCodigo().equals(ConstantsSeleccion.N_RPT_LISTA_PADRON_ELECTORAL)
                        || ConstantsSeleccion.N__SELECTED_REPORT.getCodigo().equals(ConstantsSeleccion.N_RPT_LISTA_25_SELECCIONADOS)
                        || ConstantsSeleccion.N__SELECTED_REPORT.getCodigo().equals(ConstantsSeleccion.N_RPT_LISTA_CIUDADANOS_EXCLUIDOS)) {
                    jasperPrint = generateJasperPrint();
                } else {
                    jasperPrint = oSeleccionService.getSeleccionService().generarJasperPrint(ConstantsSeleccion.N__SELECTED_REPORT, ConstantsSeleccion.lstReport);
                }

                JRExporter exporter = new JRPdfExporter();

                if (!ConstantsSeleccion.N__SELECTED_REPORT.getCodigo().equals(ConstantsSeleccion.N_RPT_LISTA_PADRON_ELECTORAL)
                        || !ConstantsSeleccion.N__SELECTED_REPORT.getCodigo().equals(ConstantsSeleccion.N_RPT_LISTA_PADRON_X_PESOS)
                        || !ConstantsSeleccion.N__SELECTED_REPORT.getCodigo().equals(ConstantsSeleccion.N_RPT_LISTA_25_SELECCIONADOS)) {
                    for (int i = 0; i < ConstantsSeleccion.CONFIGURACION.getCopias(); i++) {
                        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                        exporter.setParameter(JRExporterParameter.OUTPUT_FILE, tempFile);
                        exporter.exportReport();
                        OnpeIO onpeIo = OnpeIO.getInstance();
                        onpeIo.seamlessPdfPrint(tempFile);
                    }
                } else {
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_FILE, tempFile);
                    exporter.exportReport();
                    OnpeIO onpeIo = OnpeIO.getInstance();
                    onpeIo.seamlessPdfPrint(tempFile);
                }

                success = true;
                return null;
            }
        };
        btnBuscar.setDisable(true);
        new Thread(task).start();
        fadeIn(lblPrint, 2000, null);
        runTimer(lblPrint);
    }

    @FXML
    private void handleExport(ActionEvent event) {

        try {

            //File tempFile = new File(tempPath);
            //JRFileVirtualizer virtualizador = new JRFileVirtualizer(5, tempPath);
            success = false;
            btnBuscar = (Button) wEva.apMain.lookup("#btnSearch");
            Task task = new Task() {

                @Override
                protected Object call() throws Exception {
                    JasperPrint jasperPrint = null;
                    if (ConstantsSeleccion.N__SELECTED_REPORT.getCodigo().equals(ConstantsSeleccion.N_RPT_LISTA_PADRON_X_PESOS)
                            || ConstantsSeleccion.N__SELECTED_REPORT.getCodigo().equals(ConstantsSeleccion.N_RPT_LISTA_PADRON_ELECTORAL)
                            || ConstantsSeleccion.N__SELECTED_REPORT.getCodigo().equals(ConstantsSeleccion.N_RPT_LISTA_25_SELECCIONADOS)
                            || ConstantsSeleccion.N__SELECTED_REPORT.getCodigo().equals(ConstantsSeleccion.N_RPT_LISTA_CIUDADANOS_EXCLUIDOS)) {
                        jasperPrint = generateJasperPrint();
                    } else {
                        jasperPrint = oSeleccionService.getSeleccionService().generarJasperPrint(ConstantsSeleccion.N__SELECTED_REPORT, ConstantsSeleccion.lstReport);
                    }

//VALE                    JasperPrint jasperPrint = oSeleccionService.getSeleccionService().generarJasperPrint(ConstantsSeleccion.N__SELECTED_REPORT, ConstantsSeleccion.lstReport);
                    //JasperPrint jasperPrint = oSeleccionService.getSeleccionService().generarJasperPrintResult(ConstantsSeleccion.N__SELECTED_REPORT, ConstantsSeleccion.cacheRowSet);
                    JasperExportManager.exportReportToPdfFile(jasperPrint, ConstantsSeleccion.CONFIGURACION.getPathPDF() + File.separator + ConstantsSeleccion.N__SELECTED_REPORT.getDescripcion() + ".pdf");

//                JRExporter exporter = new JRPdfExporter();
//
//                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
//                exporter.setParameter(JRExporterParameter.OUTPUT_FILE, guarda);
//                exporter.exportReport();
                    success = true;
                    return null;
                }
            };

            if (Files.isDirectory(Paths.get(ConstantsSeleccion.CONFIGURACION.getPathPDF()))) {
                guarda = new File(ConstantsSeleccion.CONFIGURACION.getPathPDF() + File.separator + ConstantsSeleccion.N__SELECTED_REPORT.getDescripcion() + ".pdf"); //fileChooser.showSaveDialog(wEva.mainStage);

                if (guarda.exists()) {
                    if (guarda.delete()) {
                        btnBuscar.setDisable(true);
                        new Thread(task).start();
                        fadeIn(lblExport, 2000, null);
                        runTimer(lblExport);
                    } else {
                        final OnpeAnimations onpeAnim = OnpeAnimations.getInstance();
                        onpeAnim.setApMain(wEva.apMain);
                        try {
                            onpeAnim.openPopup(getMessage("MSJ_PATH_FILE_OPEN"), OnpeMessagesTypes.TYPE_ERROR);
                        } catch (Exception ex) {
                            Logger.getLogger(FooterReportController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else {
                    btnBuscar.setDisable(true);
                    new Thread(task).start();
                    fadeIn(lblExport, 2000, null);
                    runTimer(lblExport);
                }

            } else {
                try {
                    final OnpeAnimations onpeAnim = OnpeAnimations.getInstance();
                    onpeAnim.setApMain(wEva.apMain);
                    onpeAnim.openPopup(getMessage("MSJ_PATH_NOT_EXIST"), OnpeMessagesTypes.TYPE_ERROR);
                } catch (Exception ex) {
                    Logger.getLogger(FooterReportController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(FooterReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private JasperPrint generateJasperPrint() throws JRException, IOException {

        JasperPrint jasperP = null;
        System.out.println("123");
        FactoryDao oFactor = new FactoryDao();
        OracleCallableStatement cmd = null;

        OracleResultSet resultSet = null;

        JasperReport reporte = null;
        try {
            // name of procedure

            Locale locale = new Locale("pe", "PE");
            HashMap<String, Object> mapper = new HashMap();
            mapper.put(JRParameter.REPORT_LOCALE, locale);

            String absolutePath = FooterReportController.class.getClassLoader().getResource(ConstantsSeleccion.PATH_LOGO_ONPE).toString();

            oFactor.getConexionDao().conexionOpen();

            if (ConstantsSeleccion.N__SELECTED_REPORT.getMesa() == null || ConstantsSeleccion.N__SELECTED_REPORT.getMesa().equals("- TODOS -")) {
                ConstantsSeleccion.N__SELECTED_REPORT.setMesa("0");
            }
            if (ConstantsSeleccion.N__SELECTED_REPORT.getUbigeo() == null) {
                ConstantsSeleccion.N__SELECTED_REPORT.setUbigeo("0");
            }

            if (ConstantsSeleccion.N__SELECTED_REPORT.getCodigo().equals(ConstantsSeleccion.N_RPT_LISTA_PADRON_X_PESOS)) {
                String sp = "{call PKG_SELECCION.SP_REPORTE_PESOS_C_UBIGEO(?,?,?,?,?,?,?)}";
                // list of parameters

                List<ParameterOracle> oListParam = new ArrayList<>();
                oListParam.add(new ParameterOracle("MESA_REC", ConstantsSeleccion.N__SELECTED_REPORT.getMesa(), OracleTypes.CHAR, ParameterDirection.Input));
                oListParam.add(new ParameterOracle("COD_UBIGEO", ConstantsSeleccion.N__SELECTED_REPORT.getUbigeo(), OracleTypes.CHAR, ParameterDirection.Input));
                oListParam.add(new ParameterOracle("PARAM_GRAD", ConstantsSeleccion.N__SELECTED_REPORT.getGrado(), OracleTypes.NUMBER, ParameterDirection.Input));
                oListParam.add(new ParameterOracle("PARAM_DISC", ConstantsSeleccion.N__SELECTED_REPORT.getDiscapacidad(), OracleTypes.NUMBER, ParameterDirection.Input));
                oListParam.add(new ParameterOracle("PARAM_EDAD_DESDE", ConstantsSeleccion.N__SELECTED_REPORT.getEdadDesde(), OracleTypes.NUMBER, ParameterDirection.Input));
                oListParam.add(new ParameterOracle("PARAM_EDAD_HASTA", ConstantsSeleccion.N__SELECTED_REPORT.getEdadHasta(), OracleTypes.NUMBER, ParameterDirection.Input));

                oListParam.add(new ParameterOracle("PO_CURSOR_RESULTADO", null, OracleTypes.CURSOR, ParameterDirection.Output));

                DBUtil dbUtil = new DBUtil();
                dbUtil.runSearch(oListParam, ConexionDao.getOracleConnection(), cmd, sp);
                resultSet = dbUtil.getOutputParameter("PO_CURSOR_RESULTADO").getParameterResultSet();

                InputStream inputStreamListaPadronXPesos = getClass().getClassLoader().getResource(ConstantsSeleccion.PATH_REPORT_JRXML + ConstantsSeleccion.JASPER_NAME_RPT_LISTA_PADRON_X_PESOS).openStream();
                reporte = (JasperReport) JRLoader.loadObject(inputStreamListaPadronXPesos);

            } else if (ConstantsSeleccion.N__SELECTED_REPORT.getCodigo().equals(ConstantsSeleccion.N_RPT_LISTA_PADRON_ELECTORAL)) {
                String sp = "{call PKG_SELECCION.SP_REPORTE_PADRON_ELECTORAL(?,?,?,?,?,?,?)}";
                // list of parameters

                List<ParameterOracle> oListParam = new ArrayList<>();
                oListParam.add(new ParameterOracle("MESA_REC", ConstantsSeleccion.N__SELECTED_REPORT.getMesa(), OracleTypes.CHAR, ParameterDirection.Input));
                oListParam.add(new ParameterOracle("COD_UBIGEO", ConstantsSeleccion.N__SELECTED_REPORT.getUbigeo(), OracleTypes.CHAR, ParameterDirection.Input));
                oListParam.add(new ParameterOracle("PARAM_GRAD", ConstantsSeleccion.N__SELECTED_REPORT.getGrado(), OracleTypes.NUMBER, ParameterDirection.Input));
                oListParam.add(new ParameterOracle("PARAM_DISC", ConstantsSeleccion.N__SELECTED_REPORT.getDiscapacidad(), OracleTypes.NUMBER, ParameterDirection.Input));
                oListParam.add(new ParameterOracle("PARAM_EDAD_DESDE", ConstantsSeleccion.N__SELECTED_REPORT.getEdadDesde(), OracleTypes.NUMBER, ParameterDirection.Input));
                oListParam.add(new ParameterOracle("PARAM_EDAD_HASTA", ConstantsSeleccion.N__SELECTED_REPORT.getEdadHasta(), OracleTypes.NUMBER, ParameterDirection.Input));

                oListParam.add(new ParameterOracle("PO_CURSOR_RESULTADO", null, OracleTypes.CURSOR, ParameterDirection.Output));

                DBUtil dbUtil = new DBUtil();
                dbUtil.runSearch(oListParam, ConexionDao.getOracleConnection(), cmd, sp);
                resultSet = dbUtil.getOutputParameter("PO_CURSOR_RESULTADO").getParameterResultSet();

                InputStream inputStreamListaPadronElectoral = getClass().getClassLoader().getResource(ConstantsSeleccion.PATH_REPORT_JRXML + ConstantsSeleccion.JASPER_NAME_RPT_LISTA_PADRON_ELECTORAL).openStream();
                reporte = (JasperReport) JRLoader.loadObject(inputStreamListaPadronElectoral);

            } else if (ConstantsSeleccion.N__SELECTED_REPORT.getCodigo().equals(ConstantsSeleccion.N_RPT_LISTA_25_SELECCIONADOS)) {
                String sp = "{call PKG_SELECCION.SP_REPORTE_25_CANDIDATOS(?,?,?)}";
                // list of parameters
                List<ParameterOracle> oListParam = new ArrayList<>();

                oListParam.add(new ParameterOracle("MESA_REC", ConstantsSeleccion.N__SELECTED_REPORT.getMesa(), OracleTypes.CHAR, ParameterDirection.Input));
                oListParam.add(new ParameterOracle("COD_UBIGEO", ConstantsSeleccion.N__SELECTED_REPORT.getUbigeo(), OracleTypes.CHAR, ParameterDirection.Input));
                oListParam.add(new ParameterOracle("PO_CURSOR_RESULTADO", null, OracleTypes.CURSOR, ParameterDirection.Output));

                DBUtil dbUtil = new DBUtil();
                dbUtil.runSearch(oListParam, ConexionDao.getOracleConnection(), cmd, sp);
                resultSet = dbUtil.getOutputParameter("PO_CURSOR_RESULTADO").getParameterResultSet();

                InputStream inputStreamLista25Seleccionados = getClass().getClassLoader().getResource(ConstantsSeleccion.PATH_REPORT_JRXML + ConstantsSeleccion.JASPER_NAME_RPT_LISTA_25_SELECCIONADOS).openStream();
                reporte = (JasperReport) JRLoader.loadObject(inputStreamLista25Seleccionados);

            } else if (ConstantsSeleccion.N__SELECTED_REPORT.getCodigo().equals(ConstantsSeleccion.N_RPT_LISTA_CIUDADANOS_EXCLUIDOS)) {

                String sp = "{call PKG_SELECCION.SP_REPORTE_CIUDADANOS_EXC(?,?,?)}";

                List<ParameterOracle> oListParam = new ArrayList<>();

                oListParam.add(new ParameterOracle("MESA_REC", ConstantsSeleccion.N__SELECTED_REPORT.getMesa(), OracleTypes.CHAR, ParameterDirection.Input));
                oListParam.add(new ParameterOracle("COD_UBIGEO", ConstantsSeleccion.N__SELECTED_REPORT.getUbigeo(), OracleTypes.CHAR, ParameterDirection.Input));
                oListParam.add(new ParameterOracle("PO_CURSOR_RESULTADO", null, OracleTypes.CURSOR, ParameterDirection.Output));

                DBUtil dbUtil = new DBUtil();
                dbUtil.runSearch(oListParam, ConexionDao.getOracleConnection(), cmd, sp);
                resultSet = dbUtil.getOutputParameter("PO_CURSOR_RESULTADO").getParameterResultSet();

                InputStream inputStreamReporteCiudadanosExcluidos = getClass().getClassLoader().getResource(ConstantsSeleccion.PATH_REPORT_JRXML + ConstantsSeleccion.JASPER_NAME_RPT_LISTA_CIUDADANOS_EXCLUIDOS).openStream();
                reporte = (JasperReport) JRLoader.loadObject(inputStreamReporteCiudadanosExcluidos);

            }
            mapper.put("codReporte", ConstantsSeleccion.N__SELECTED_REPORT.getCodigo());
            mapper.put("Distrito_CP", ConstantsSeleccion.CONFIGURACION.getEtiquetaDist());
            mapper.put("Dpto", ConstantsSeleccion.CONFIGURACION.getEtiquetaDpto());
            mapper.put("Prov", ConstantsSeleccion.CONFIGURACION.getEtiquetaProv());
            mapper.put("PATH_LOGO", absolutePath);
            mapper.put("titulo", ConstantsSeleccion.N__SELECTED_REPORT.getDescripcion());
            mapper.put("ELECCION", ConstantsSeleccion.CONFIGURACION.getNameEleccion());

            JRSwapFile archivoSwap = new JRSwapFile(tempPath, 4096, 25);
            JRSwapFileVirtualizer virtualizador = new JRSwapFileVirtualizer(25, archivoSwap, true);  //2
            mapper.put(JRParameter.REPORT_VIRTUALIZER, virtualizador);

            JasperReportsContext jrc = DefaultJasperReportsContext.getInstance();

            jrc.setProperty(
                    JRXPathExecuterUtils.PROPERTY_XPATH_EXECUTER_FACTORY,
                    "net.sf.jasperreports.engine.util.xml.JaxenXPathExecuterFactory");

            JasperFillManager jfm = JasperFillManager.getInstance(jrc);

            try {
                jasperP = jfm.fillReport(reporte, mapper, new JRResultSetDataSource(resultSet));
            } catch (JRException ex) {
                Logger.getLogger(FooterReportController.class.getName()).log(Level.SEVERE, null, ex);
            }

            resultSet.close();
            System.out.println("ACABO");
            oFactor.getConexionDao().conexionClose();

        } catch (SQLException e) {
            Logger.getLogger(ParametroDao.class.getName()).log(Level.SEVERE, null, e);
        }
        System.out.println("123");
        return jasperP;
    }

    public void runTimer(final Label lblHide) {
        btnExport.setDisable(true);
        btnPrint.setDisable(true);
        btnBuscar.setDisable(true);
        timer = new Timer();
        ComboBox cbListReport = (ComboBox) wEva.apMain.lookup("#cbListReport");

        cbListReport.setDisable(true);

        TimerTask timerTask = new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        try {
                            verifyProccess(lblHide);
                        } catch (Exception e) {
                            timer.cancel();
                            timer.purge();
                        }
                    }
                });
            }
        };

        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    private void verifyProccess(Label lblHide) {

        if (success) {
            success = false;
            fadeOut(lblHide, 2000, null);

            btnExport.setDisable(false);
            btnPrint.setDisable(false);
            ComboBox cbListReport = (ComboBox) wEva.apMain.lookup("#cbListReport");
            btnBuscar.setDisable(false);
            cbListReport.setDisable(false);
            timer.cancel();
            timer.purge();
        }
    }

}
