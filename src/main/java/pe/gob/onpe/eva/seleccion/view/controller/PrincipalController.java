/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.eva.seleccion.view.controller;

import crypto.util.encription.Crypto;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import pe.gob.onpe.animations.OnpeMessagesTypes;
import pe.gob.onpe.animations.view.OnpeAnimations;
import pe.gob.onpe.eva.common.EvaConstant;
import static pe.gob.onpe.eva.common.view.AppController.wEva;
import pe.gob.onpe.eva.model.Version;
import pe.gob.onpe.eva.security.EvaKeys;
import pe.gob.onpe.eva.seleccion.constant.ConstantsSeleccion;
import static pe.gob.onpe.eva.seleccion.view.controller.LocalAppController.timer;
import pe.gob.onpe.util.io.OnpeIO;
import pe.gob.onpe.util.object.OnpeCalendar;

/**
 * FXML Controller class
 *
 * @author aquispec
 */
public class PrincipalController extends LocalAppController implements Initializable {

    @FXML
    private AnchorPane apCerrarSesion;
    @FXML
    private Button lblVersion, btnAdministrador;
    @FXML

    public Label lblNombreEleccion;
    private Boolean threadFinish = false, exportResult = false, runExport = true;
    ArrayList<Version> lstVersion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        apCerrarSesion.setVisible(false);

        btnAdministrador.setText(ConstantsSeleccion.USUARIO_SESSION.getUsuario());
        ConstantsSeleccion.CONFIGURACION.getAliasEleccion();
        lblNombreEleccion.setText(ConstantsSeleccion.CONFIGURACION.getAliasEleccion());

        lstVersion = oSeleccionService.getSeleccionService().fethVersionEleccionActiva();
        ConstantsSeleccion.STR_VERSION_CODIGO = lstVersion.get(0).getVersionSeleccion();
        lblVersion.setText("Versión: " + ConstantsSeleccion.STR_VERSION_CODIGO);
    }

    @FXML
    private void btnAdministrarEntered(MouseEvent event) {
        apCerrarSesion.setVisible(true);
    }

    @FXML
    private void btnAdministrarExited(MouseEvent event) {
        apCerrarSesion.setVisible(false);
    }

    @FXML
    private void btnCerrarSesionEntered(MouseEvent event) {
        apCerrarSesion.setVisible(true);
    }

    @FXML
    private void btnCerrarSesionExited(MouseEvent event) {
        apCerrarSesion.setVisible(false);
    }

    @FXML
    private void PuestaCero(ActionEvent event) throws IOException, Exception {
        final OnpeAnimations onpeAnim = OnpeAnimations.getInstance();
        onpeAnim.setApMain(wEva.apMain);

        onpeAnim.openPopup(ConstantsSeleccion.MSG_ATENCION_PUESTA_CERO, OnpeMessagesTypes.TYPE_CONFIRMATION, OnpeMessagesTypes.OPCION_YES_NO, new javafx.event.EventHandler() {

            @Override
            public void handle(Event event) {
                try {
                    ConstantsSeleccion.N_SELECT_W = ConstantsSeleccion.N_PUESTACERO;
                    AnchorPane apAuth = loadFxml(ConstantsSeleccion.W_SEL_MODAL_AUTH);
                    onpeAnim.openPopup(apAuth);
                } catch (IOException ex) {
                    Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }, null);
    }

    @FXML
    private void Seleccion25(ActionEvent event) throws IOException, Exception {
        //Boolean result = oSeleccionService.getSeleccionService().validatePuestaCero();        
//        if (result) {
//            ConstantsSeleccion.N_SELECT_W = ConstantsSeleccion.N_SELECCION25;
//            ConstantsSeleccion.WINDOW_SELECTED = ConstantsSeleccion.W_SEL_PROCESO_SELECCION;
//            openWindows(ConstantsSeleccion.W_SEL_PROCESO_SELECCION);
//        } else {
//            OnpeAnimations onpeAnim = OnpeAnimations.getInstance();
//            onpeAnim.openPopup(ConstantsSeleccion.MSG_REALIZE_PUESTA_CERO, OnpeMessagesTypes.TYPE_ERROR);
//        }
    }

    @FXML
    private void btnReportes(ActionEvent event) throws IOException {
        ConstantsSeleccion.N_SELECT_W = ConstantsSeleccion.N_REPORTES;
        ConstantsSeleccion.WINDOW_SELECTED = ConstantsSeleccion.W_SEL_REPORTES;
        openWindows(ConstantsSeleccion.W_SEL_REPORTES);
    }

    @FXML
    private void handleRespaldo(ActionEvent event) throws IOException, Exception {
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat formatHour = new SimpleDateFormat("HH:mm");
        Date dateNow = new Date();
        String fecha = "_" + formatDate.format(dateNow) + "_" + formatHour.format(dateNow);
        fecha = fecha.replace(":", "");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(ConstantsSeleccion.NAME_DUMP.replace(".DMP", fecha + ".DMP"));

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("dmp files (*.dmp)", "*.DMP");
        fileChooser.getExtensionFilters().add(extFilter);

        if (Files.isDirectory(Paths.get(ConstantsSeleccion.CONFIGURACION.getPathRespaldo()))) {
            File oFile = new File(ConstantsSeleccion.CONFIGURACION.getPathRespaldo() + File.separator + ConstantsSeleccion.NAME_DUMP.replace(".DMP", fecha + ".DMP"));  //fileChooser.showSaveDialog(wEva.mainStage);

            if (oFile != null) {
                String logName = oFile.getName().toUpperCase();

                OnpeCalendar onpeCalendar = OnpeCalendar.getInstance();
                String date = "_" + onpeCalendar.toString("dd/MM/yyyy").replace("/", "_");

                String nameDump = oFile.getName().toUpperCase().replace(".DMP", "");
                logName = logName.replace(".DMP", "");

                File oLog = new File(oFile.getParent() + "\\" + logName + ".LOG");
                File oDump = new File(oFile.getParent() + "\\" + nameDump + ".DMP");

                OnpeIO onpeIO = OnpeIO.getInstance();
                String path = Paths.get(EvaConstant.PATH_APP).toString();
                File mFile = new File(path + "\\" + EvaConstant.CONEXION_FILE);
                String strLoad = onpeIO.readFileAsString(mFile);

                Crypto crypto = Crypto.getInstance(EvaKeys.ENCRYPT);
                String sConex = crypto.decrypt(strLoad);
                String usu = sConex.substring(sConex.indexOf(":thin:")+6, sConex.indexOf("/"));
                String pass = sConex.substring(sConex.indexOf("/")+1, sConex.indexOf("@"));
                
                
                final String command = "exp " + usu +"/"+ pass +"@SEL_25 owner="+usu+" file=" + oDump.getAbsolutePath() + " log=" + oLog.getAbsolutePath();               

                Task oTaks = new Task() {

                    @Override
                    protected Object call() throws Exception {
                        OnpeIO onpeIO = OnpeIO.getInstance();                        

                        oSeleccionService.getUsuarioService().changeStatusBackup(ConstantsSeleccion.USUARIO_SESSION, ConstantsSeleccion.N_LOGIN_AVAILABLE);

                        exportResult = onpeIO.execCommand(command);
                        threadFinish = true;

                        return null;
                    }
                };

                new Thread(oTaks).start();

                timer = new Timer();

                TimerTask timerTask = new TimerTask() {
                    public void run() {
                        Platform.runLater(new Runnable() {
                            public void run() {
                                try {
                                    validateExport();                                   
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

        } else {
            final OnpeAnimations onpeAnim = OnpeAnimations.getInstance();
            onpeAnim.setApMain(wEva.apMain);
            onpeAnim.openPopup(getMessage("MSJ_PATH_NOT_EXIST"), OnpeMessagesTypes.TYPE_ERROR);
        }

    }

    private void validateExport() throws IOException, Exception {
        if (runExport) {
            runExport = false;

            OnpeAnimations onpeAnim = OnpeAnimations.getInstance();
            AnchorPane apRespaldo = loadFxml(ConstantsSeleccion.W_SEL_MODAL_RESPALDO);
            onpeAnim.openPopup(apRespaldo);
        }

        if (threadFinish) {
            OnpeAnimations onpeAnim = OnpeAnimations.getInstance();
            onpeAnim.closePopups();

            if (exportResult) {
                onpeAnim.openPopup(ConstantsSeleccion.MSG_SUCCESS_EXPORT, OnpeMessagesTypes.TYPE_INFORMATION);
            } else {
                onpeAnim.openPopup(ConstantsSeleccion.MSG_ERROR_EXPORT, OnpeMessagesTypes.TYPE_ERROR);
            }

            threadFinish = false;
            runExport = true;

            oSeleccionService.getUsuarioService().changeStatusBackup(ConstantsSeleccion.USUARIO_SESSION, ConstantsSeleccion.N_LOGIN_UNAVAILABLE);
            timer.cancel();
            timer.purge();
        }
    }

    @FXML
    private void LoadVersion(ActionEvent event) throws IOException, Exception {
        OnpeAnimations onpeAnim = OnpeAnimations.getInstance();
        onpeAnim.setApMain(wEva.apMain);

        AnchorPane apVersion = loadFxml(ConstantsSeleccion.W_SEL_MODAL_VERSION);
        onpeAnim.openPopup(apVersion);
    }

    @FXML
    private void handleConfig(ActionEvent event) throws IOException, Exception {
        ConstantsSeleccion.PRINCIPAL_FXML = true;
        AnchorPane pane = loadFxml(ConstantsSeleccion.W_CONFIGURACION);
        final OnpeAnimations onpeAnimations = OnpeAnimations.getInstance();
        onpeAnimations.setApMain(wEva.apMain);
        onpeAnimations.openPopup(pane, 704);
    }
}
