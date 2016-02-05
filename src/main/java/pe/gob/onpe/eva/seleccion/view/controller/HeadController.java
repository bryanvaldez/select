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
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import pe.gob.onpe.animations.OnpeMessagesTypes;
import pe.gob.onpe.animations.view.OnpeAnimations;
import pe.gob.onpe.eva.common.EvaConstant;
import static pe.gob.onpe.eva.common.view.AppController.wEva;
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
public class HeadController extends LocalAppController implements Initializable {

    /**
     * Initializes the controller class.
     */
//    @FXML
//    protected AnchorPane apCerrarSesion;
    @FXML
    private Label lblNameEleccion, lblNameUser;
    @FXML
    private Pane paneOptionsAdmin, paneOptions;
    @FXML
    private Button btnAdministrador, btnOption;
    private Boolean boolVisibleOptions = false;
    private Boolean boolVisibleOptionsAdmin = false;

    private Boolean threadFinish = false, exportResult = false, runExport = true;
    public boolean boolPopup, boolRespaldo;
    /*
     @FXML
     protected AnchorPane apBloker;
    
     @FXML
     protected Button btnAdministrador;
     */

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*apCerrarSesion.setVisible(false);

         String stylePuestaCero = ConstantsSeleccion.STYLE_PUESTA_CERO;
         String styleSeleccion = ConstantsSeleccion.STYLE_SELECCION25;
         String styleReportes = ConstantsSeleccion.STYLE_REPORTES;

         lblModule.getStyleClass().removeAll(stylePuestaCero, styleSeleccion, styleReportes);
         lblNameEleccion.setText(ConstantsSeleccion.CONFIGURACION.getAliasEleccion());
         btnAdministrador.setText(ConstantsSeleccion.USUARIO_SESSION.getUsuario());
        
         if (ConstantsSeleccion.WINDOW_SELECTED == ConstantsSeleccion.W_SEL_PUESTA0) {
         lblModule.setText(ConstantsSeleccion.HEADER_TITLE_PUESTA_CERO);
         lblModule.getStyleClass().add(stylePuestaCero);
         } else if (ConstantsSeleccion.WINDOW_SELECTED == ConstantsSeleccion.W_SEL_PROCESO_SELECCION) {
         lblModule.setText(ConstantsSeleccion.HEADER_TITLE_SELECCION25);
         lblModule.getStyleClass().add(styleSeleccion);
         } else if (ConstantsSeleccion.WINDOW_SELECTED == ConstantsSeleccion.W_SEL_REPORTES) {
         lblModule.setText(ConstantsSeleccion.HEADER_TITLE_REPORTES);
         lblModule.getStyleClass().add(styleReportes);
         }
         */
        lblNameUser.setText(ConstantsSeleccion.USUARIO_SESSION.getUsuario());
        lblNameEleccion.setText(ConstantsSeleccion.CONFIGURACION.getAliasEleccion());
        btnAdministrador.setText(ConstantsSeleccion.USUARIO_SESSION.getUsuario());
        paneOptionsAdmin.setLayoutY(-100);
        paneOptions.setLayoutY(-150);
        boolPopup = false;
        boolRespaldo = false;
        switch (ConstantsSeleccion.N_SELECT_W) {
            case ConstantsSeleccion.N_PUESTACERO:
                btnOption.setText(ConstantsSeleccion.N_PUESTACERO);
                break;
            case ConstantsSeleccion.N_SELECCION25:
                btnOption.setText(ConstantsSeleccion.N_SELECCION25);
                break;
            case ConstantsSeleccion.N_REPORTES:
                btnOption.setText(ConstantsSeleccion.N_REPORTES);
                break;
        }
    }

    @FXML
    private void btnAdministradorEntered(MouseEvent event) {
        boolVisibleOptionsAdmin = true;
        paneOptionsAdmin.setLayoutY(44);
        btnAdministrador.getStyleClass().remove("btnAdminHead");
        btnAdministrador.getStyleClass().add("btnAdminHeadSel");
    }

    @FXML
    private void btnAdministradorExited(MouseEvent event) {
        boolVisibleOptionsAdmin = false;
        paneOptionsAdmin.setLayoutY(-100);
        btnAdministrador.getStyleClass().add("btnAdminHead");
        btnAdministrador.getStyleClass().remove("btnAdminHeadSel");
    }

    @FXML
    private void paneAdminEntered(MouseEvent event) {
        boolVisibleOptionsAdmin = true;
        paneOptionsAdmin.setLayoutY(44);
        btnAdministrador.getStyleClass().remove("btnAdminHead");
        btnAdministrador.getStyleClass().add("btnAdminHeadSel");
    }

    @FXML
    private void paneAdminExited(MouseEvent event) {
        boolVisibleOptionsAdmin = false;
        paneOptionsAdmin.setLayoutY(-100);
        btnAdministrador.getStyleClass().add("btnAdminHead");
        btnAdministrador.getStyleClass().remove("btnAdminHeadSel");
    }

    @FXML
    private void btnOptionEntered(MouseEvent event) {
        boolVisibleOptions = true;
        paneOptions.setLayoutY(48);
        btnOption.getStyleClass().remove("btnAdminHead");
        btnOption.getStyleClass().add("btnAdminHeadSel");
    }

    @FXML
    private void btnOptionExited(MouseEvent event) {
        boolVisibleOptions = false;
        paneOptions.setLayoutY(-150);
        btnOption.getStyleClass().add("btnAdminHead");
        btnOption.getStyleClass().remove("btnAdminHeadSel");
    }

    @FXML
    private void paneOptionEntered(MouseEvent event) {
        boolVisibleOptions = true;
        paneOptions.setLayoutY(48);
        btnOption.getStyleClass().remove("btnAdminHead");
        btnOption.getStyleClass().add("btnAdminHeadSel");
    }

    @FXML
    private void paneOptionExited(MouseEvent event) {
        boolVisibleOptions = false;
        paneOptions.setLayoutY(-150);
        btnOption.getStyleClass().add("btnAdminHead");
        btnOption.getStyleClass().remove("btnAdminHeadSel");
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        openWindows(ConstantsSeleccion.W_LOGIN);
    }

    @FXML
    protected void handleConfig(ActionEvent event) throws IOException, Exception {

        AnchorPane pane = loadFxml(ConstantsSeleccion.W_CONFIGURACION);
        final OnpeAnimations onpeAnimations = OnpeAnimations.getInstance();
        onpeAnimations.setApMain(wEva.apMain);
        onpeAnimations.openPopup(pane, 704);

    }

    @FXML
    public void handlePCero(ActionEvent event) throws Exception {
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
    public void handleSeleccion(ActionEvent event) throws IOException, Exception {
        try {
            ArrayList<Integer> result = oSeleccionService.getSeleccionService().validatePuestaCero();

            if (result.get(0) == 0 && result.get(1) == 0) {
                ConstantsSeleccion.N_SELECT_W = ConstantsSeleccion.N_SELECCION25;
                ConstantsSeleccion.WINDOW_SELECTED = ConstantsSeleccion.W_SEL_PROCESO_SELECCION;
                openWindows(ConstantsSeleccion.W_SEL_PROCESO_SELECCION);
            } else if (result.get(0) == 0 && result.get(1) != 0) {
                OnpeAnimations onpeAnim = OnpeAnimations.getInstance();
                onpeAnim.openPopup(ConstantsSeleccion.MSG_DEBE_REALIZAR_PUESTA_CERO, OnpeMessagesTypes.TYPE_ERROR);
            }
            else {
                OnpeAnimations onpeAnim = OnpeAnimations.getInstance();
                onpeAnim.openPopup(ConstantsSeleccion.MSG_REALIZE_PUESTA_CERO, OnpeMessagesTypes.TYPE_ERROR);
            }
        } catch (Exception e) {
            Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @FXML
    public void handleReportes(ActionEvent event) throws IOException {
        ConstantsSeleccion.N_SELECT_W = ConstantsSeleccion.N_REPORTES;
        ConstantsSeleccion.WINDOW_SELECTED = ConstantsSeleccion.W_SEL_REPORTES;
        openWindows(ConstantsSeleccion.W_SEL_REPORTES);
    }

    @FXML
    public void handleRespaldo(ActionEvent event) throws Exception {

        final OnpeAnimations onpeAnim = OnpeAnimations.getInstance();
        onpeAnim.setApMain(wEva.apMain);

        onpeAnim.openPopup(ConstantsSeleccion.MSG_ATENCION_RESPALDO, OnpeMessagesTypes.TYPE_CONFIRMATION, OnpeMessagesTypes.OPCION_YES_NO, new javafx.event.EventHandler() {

            @Override
            public void handle(Event event) {
                if (!boolRespaldo) {
                    boolRespaldo = true;
                    try {
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
                                String usu = sConex.substring(sConex.indexOf(":thin:") + 6, sConex.indexOf("/"));
                                String pass = sConex.substring(sConex.indexOf("/") + 1, sConex.indexOf("@"));

                                final String command = "exp " + usu + "/" + pass + "@SEL_25 owner=" + usu + " file=" + oDump.getAbsolutePath() + " log=" + oLog.getAbsolutePath();

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
                            boolRespaldo = false;
                            onpeAnim.setApMain(wEva.apMain);
                            onpeAnim.openPopup(getMessage("MSJ_PATH_NOT_EXIST"), OnpeMessagesTypes.TYPE_ERROR);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (Exception ex) {
                        Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        }, null);

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
            boolRespaldo = false;
            threadFinish = false;
            runExport = true;

            oSeleccionService.getUsuarioService().changeStatusBackup(ConstantsSeleccion.USUARIO_SESSION, ConstantsSeleccion.N_LOGIN_UNAVAILABLE);
            timer.cancel();
            timer.purge();
        }
    }

}
