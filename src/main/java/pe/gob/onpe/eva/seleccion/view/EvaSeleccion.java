/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.eva.seleccion.view;

import crypto.util.encription.Crypto;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Properties;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import pe.gob.onpe.animations.view.OnpeAnimations;
import pe.gob.onpe.eva.common.EvaConstant;
import pe.gob.onpe.eva.common.view.AppController;
import pe.gob.onpe.eva.common.view.Screen;
import pe.gob.onpe.eva.seleccion.constant.ConstantsSeleccion;
import pe.gob.onpe.eva.seleccion.service.FactoryService;
import pe.gob.onpe.eva.seleccion.view.controller.LocalAppController;

/**
 *
 * @author lguerra
 */
public class EvaSeleccion extends Application {

    private Properties systemProperties = new Properties(System.getProperties());
    private Stage mainStage;
    public static Date FechaInicio;

    @Override
    public void start(Stage stage) throws Exception {
        this.mainStage = stage;
        LoadMainWindow();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private void LoadMainWindow() throws Exception {
        EvaConstant.PATH_APP = systemProperties.getProperty("user.dir");

        AppController.wEva = new WindowEvaSeleccion();
        AppController.wEva.pathFxml = getClass().getResource("fxml/").toString();
        ConstantsSeleccion.PATH_IMG_SELECCION = getClass().getResource("image/").toString();
        AppController.wEva.mainStage = this.mainStage;

        Screen screenLogin = AppController.wEva.windows[ConstantsSeleccion.W_LOGIN];
        FXMLLoader loaderLogin = new FXMLLoader(new URL(AppController.wEva.pathFxml + screenLogin.getFxmlWindow()));
        AppController.wEva.apMain = (AnchorPane) loaderLogin.load();
        Scene scene = new Scene(AppController.wEva.apMain);
        AppController.wEva.mainStage.setScene(scene);

        AppController.wEva.mainStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                System.out.println("1");
                if (LocalAppController.timer != null) {
                    LocalAppController.timer.cancel();
                    LocalAppController.timer.purge();
                }
                if (LocalAppController.timer1 != null) {
                    LocalAppController.timer1.cancel();
                    LocalAppController.timer1.purge();
                }
                if (LocalAppController.timer2 != null) {
                    LocalAppController.timer2.cancel();
                    LocalAppController.timer2.purge();
                }
                if (LocalAppController.timer3 != null) {
                    LocalAppController.timer3.cancel();
                    LocalAppController.timer3.purge();
                }
                System.out.println("close sesion");
                if (ConstantsSeleccion.USUARIO_SESSION != null) {
                    FactoryService oSeleccionService = new FactoryService();
                    oSeleccionService.getUsuarioService().logout(ConstantsSeleccion.USUARIO_SESSION);
                }
                killProccess();
            }
        });

        AppController.wEva.mainStage.setFullScreenExitHint("");
        AppController.wEva.mainStage.setFullScreen(EvaConstant.MODE_PROD);
        AppController.wEva.mainStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        AppController.wEva.mainStage.getScene().addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.SECONDARY) {
                    event.consume();
                }
            }
        });

        OnpeAnimations onpeAnim = OnpeAnimations.getInstance();
        onpeAnim.setApMain(AppController.wEva.apMain);

        AppController.wEva.mainStage.show();
    }

    private void killProccess() {
        String osName = System.getProperty("os.name");
        String cmd = "";
        if (osName.toUpperCase().contains("WIN")) {
            cmd += "taskkill /f /im javaw.exe";
            //cmd += "tskill java";            
        } 
//        else {
//            cmd += "taskkill /f /im javaw.exe";
//            //cmd += "killall java";
//            //cmd += "killall javaw";            
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
    
}
