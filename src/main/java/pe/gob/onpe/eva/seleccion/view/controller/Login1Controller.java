/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.eva.seleccion.view.controller;

import crypto.util.encription.Crypto;
import crypto.util.hash.Hash;
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
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import pe.gob.onpe.animations.OnpeMessagesTypes;
import pe.gob.onpe.animations.view.OnpeAnimations;
import pe.gob.onpe.eva.common.EvaConstant;
import pe.gob.onpe.eva.common.dto.LoginDTO;
import pe.gob.onpe.eva.common.view.AppController;
import static pe.gob.onpe.eva.common.view.AppController.wEva;
import pe.gob.onpe.eva.model.Usuario;
import pe.gob.onpe.eva.model.Version;
import pe.gob.onpe.eva.security.EvaKeys;
import pe.gob.onpe.eva.seleccion.constant.ConstantsSeleccion;
import pe.gob.onpe.eva.seleccion.service.FactoryService;
import static pe.gob.onpe.eva.seleccion.view.controller.LocalAppController.timer;
import pe.gob.onpe.util.io.OnpeIO;
import pe.gob.onpe.util.object.OnpeCalendar;

/**
 * FXML Controller class
 *
 * @author AQuispeC
 */
public class Login1Controller extends LocalAppController implements Initializable {

    @FXML
    private Pane paneLineaMedio, paneBlancoIzq, paneImgDerecha, paneDerechaPrincipal, paneOptionsAdmin;

    @FXML
    private ImageView imgLineGreen;

    @FXML
    private TextField txtUsuario;

    @FXML
    private Button lblVersion, btnAdministrador;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblEleccion, lblEleccion1;
    public boolean boolPopup, boolRespaldo;

    private final int timePreLoadLogin = 1000;
    private final int timeLoadLogin = 1000;
    private Boolean boolVisibleOptionsAdmin = false;
    ArrayList<Version> lstVersion;
    private Boolean threadFinish = false, exportResult = false, runExport = true;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

//        paneBlancoIzq.setLayoutX(-1096);
//        paneBlancoIzq.setLayoutY(768);
//        paneLineaMedio.setLayoutX(-966);
//        paneLineaMedio.setLayoutY(768);
//
//        TimerTask timerTask = new TimerTask() {
//            public void run() {
//                Platform.runLater(new Runnable() {
//                    public void run() {
//                        loadLogin();
//                    }
//                });
//            }
//        };
//        timer = new Timer();
//        timer.scheduleAtFixedRate(timerTask, timePreLoadLogin, 5000);
        getConfiguracion();
        lblEleccion.setText(ConstantsSeleccion.CONFIGURACION.getAliasEleccion());
        lblEleccion1.setText(ConstantsSeleccion.CONFIGURACION.getAliasEleccion());
        //btnAdministrador.setText(ConstantsSeleccion.USUARIO_SESSION.getUsuario());
        boolPopup = false;
        boolRespaldo = false;
        txtUsuario.textProperty().addListener(onlySomeCharacters("[A-Za-z0-9]", txtUsuario, 10));
        txtPassword.textProperty().addListener(onlySomeCharacters("[A-Za-z0-9]", txtPassword, 8));
        paneOptionsAdmin.setLayoutX(1500);

        if (ConstantsSeleccion.USUARIO_SESSION.getUsuario() != null) {
            RotateTransition rt = new RotateTransition(Duration.millis(1), imgLineGreen);
            rt.setByAngle(90);
            rt.setCycleCount(1);
            rt.setAutoReverse(false);
            rt.play();

            moveX(imgLineGreen, 120, 1, null);
            fadeOut(paneBlancoIzq, 1, null);
            fadeOut(paneImgDerecha, 1, null);

            fadeIn(paneDerechaPrincipal, 1, null);
            lstVersion = oSeleccionService.getSeleccionService().fethVersionEleccionActiva();
            ConstantsSeleccion.STR_VERSION_CODIGO = lstVersion.get(0).getVersionSeleccion();
            lblVersion.setText("Versión: " + ConstantsSeleccion.STR_VERSION_CODIGO);
            btnAdministrador.setText(ConstantsSeleccion.USUARIO_SESSION.getUsuario());

        } else {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    txtUsuario.requestFocus();
                }
            });
        }
    }

    private void loadLogin() {
        timer.cancel();
        timer.purge();
        moveY(paneBlancoIzq, 0, timeLoadLogin, null);
        moveX(paneBlancoIzq, 0, timeLoadLogin, null);
        moveY(paneLineaMedio, 0, timeLoadLogin, null);
        moveX(paneLineaMedio, 240, timeLoadLogin, null);
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
    public void validateUsuario(ActionEvent event) throws Exception {
        if (!boolPopup) {
            boolPopup = true;
            Login();
        }
    }

    private void transitionLogin() {
        RotateTransition rt = new RotateTransition(Duration.millis(timeLoadLogin), imgLineGreen);
        rt.setByAngle(90);
        rt.setCycleCount(1);
        rt.setAutoReverse(false);
        rt.play();

        moveX(imgLineGreen, 120, timeLoadLogin - 300, null);
        fadeOut(paneBlancoIzq, timeLoadLogin, null);
        fadeOut(paneDerechaPrincipal, 0, null);
        //fadeOut(paneImgDerecha, timeLoadLogin,null);
        //fadeIn(paneDerechaPrincipal, timeLoadLogin, null);
        fadeOut(paneImgDerecha, timeLoadLogin, new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                fadeIn(paneDerechaPrincipal, 1, null);
                lstVersion = oSeleccionService.getSeleccionService().fethVersionEleccionActiva();
                ConstantsSeleccion.STR_VERSION_CODIGO = lstVersion.get(0).getVersionSeleccion();
                lblVersion.setText("Versión: " + ConstantsSeleccion.STR_VERSION_CODIGO);
                btnAdministrador.setText(ConstantsSeleccion.USUARIO_SESSION.getUsuario());
            }
        });
    }

    @FXML
    private void btnAdminEntered(MouseEvent event) {
        boolVisibleOptionsAdmin = true;
        paneOptionsAdmin.setLayoutX(795);
        btnAdministrador.getStyleClass().remove("btn_Administrador");
        btnAdministrador.getStyleClass().add("btn_AdministradorSel");
    }

    @FXML
    private void btnAdminExited(MouseEvent event) {
        boolVisibleOptionsAdmin = false;
        paneOptionsAdmin.setLayoutX(1500);
        btnAdministrador.getStyleClass().add("btn_Administrador");
        btnAdministrador.getStyleClass().remove("btn_AdministradorSel");
    }

    @FXML
    private void paneAdminEntered(MouseEvent event) {
        boolVisibleOptionsAdmin = true;
        paneOptionsAdmin.setLayoutX(795);
        btnAdministrador.getStyleClass().remove("btn_Administrador");
        btnAdministrador.getStyleClass().add("btn_AdministradorSel");
    }

    @FXML
    private void paneAdminExited(MouseEvent event) {
        boolVisibleOptionsAdmin = false;
        paneOptionsAdmin.setLayoutX(1500);
        btnAdministrador.getStyleClass().add("btn_Administrador");
        btnAdministrador.getStyleClass().remove("btn_AdministradorSel");
    }

//    @FXML
//    public void handleAdmin(ActionEvent event) {
//        if (boolVisibleOptionsAdmin) {
//            boolVisibleOptionsAdmin = false;
//            paneOptionsAdmin.setLayoutX(1500);
//            btnAdministrador.getStyleClass().add("btn_Administrador");
//            btnAdministrador.getStyleClass().remove("btn_AdministradorSel");
//        } else {
//            boolVisibleOptionsAdmin = true;
//            paneOptionsAdmin.setLayoutX(795);
//            btnAdministrador.getStyleClass().remove("btn_Administrador");
//            btnAdministrador.getStyleClass().add("btn_AdministradorSel");
//        }
//    }
    @FXML
    public void handleExit(ActionEvent event) throws IOException, Exception {
        killProccess();
        wEva.mainStage.close();
    }

    private void killProccess() {
        String osName = System.getProperty("os.name");
        String cmd = "";
        if (osName.toUpperCase().contains("WIN")) {
            cmd += "taskkill /f /im javaw.exe";
        }
//        else {
//            cmd += "taskkill /f /im javaw.exe";           
//        }
        Process hijo;
        try {
            hijo = Runtime.getRuntime().exec(cmd);
            hijo.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

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

                        transitionLogin();

//                        getConfiguracion();
//                        openWindows(ConstantsSeleccion.W_PRINCIPAL_SELECCION25);
//                        OnpeAnimations onpeAnim = OnpeAnimations.getInstance();
//                        onpeAnim.setApMain(wEva.apMain);
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

    @FXML
    private void handlePuestaCero(ActionEvent event) throws IOException, Exception {
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
    private void handleSeleccion25(ActionEvent event) throws IOException, Exception {

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
    private void handleReportes(ActionEvent event) throws IOException {
        ConstantsSeleccion.N_SELECT_W = ConstantsSeleccion.N_REPORTES;
        ConstantsSeleccion.WINDOW_SELECTED = ConstantsSeleccion.W_SEL_REPORTES;
        openWindows(ConstantsSeleccion.W_SEL_REPORTES);
    }

    @FXML
    private void handleRespaldo(ActionEvent event) throws IOException, Exception {

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
