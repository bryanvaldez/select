/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.eva.seleccion.view.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import pe.gob.onpe.eva.model.PuestaCero;
import pe.gob.onpe.eva.seleccion.constant.ConstantsSeleccion;
import static pe.gob.onpe.eva.seleccion.view.controller.LocalAppController.timer;

/**
 * FXML Controller class
 *
 * @author aquispec
 */
public class PuestaCeroController extends LocalAppController implements Initializable {

    @FXML
    private Label lblCarga, lblCarga1;
    @FXML
    private AnchorPane apCarga, apCargaBarra, apCargaLeft, apCargaMid, apCargaRight;

    private int posXapCarga;
    private Integer timeAnimation = 2000;
    private static int timeCarga;
    private boolean boolComplet, intCarga = false;
    Task task;
    TimerTask timerTask1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        apCargaLeft.setOpacity(0);
        apCargaMid.setOpacity(0);
        apCargaRight.setOpacity(0);

        posXapCarga = 33;
        boolComplet = false;
        timeCarga = 2000;
        // TODO
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        try {
                            progress();
                        } catch (Exception e) {
                            timer.cancel();
                            timer.purge();
                        }
                    }
                });
            }
        };

        timer.scheduleAtFixedRate(timerTask, 1000, timeCarga);

        task = new Task() {

            @Override
            protected Object call() throws Exception {
                puestaCero();
                return null;
            }
        };

    }

    private void puestaCero() {
        String result = null;        
        result = oSeleccionService.getSeleccionService().puestaCero();
        if (result.equals("")) {
            boolComplet = true;            
            PuestaCero oPuestaCero = new PuestaCero();
            oPuestaCero.setUsuario(ConstantsSeleccion.USUARIO_SESSION.getUsuarioPk());
            oPuestaCero.setUsuarioAdmin(ConstantsSeleccion.USUARIO_SESSION.getUsuarioPk());
            oSeleccionService.getPuestaCero().registerPuestaCero(oPuestaCero);
        }
    }

    private void progress() {
        if (!intCarga) {
            blokTop();
            new Thread(task).start();
            animationWidth(apCargaBarra, 900.0, timeAnimation, null);
            moveX(apCarga, 933, timeAnimation, null);
            counterNumber(lblCarga, 0, 90, timeAnimation, "%", null);

            intCarga = true;
        }

        if (boolComplet) {
            animationWidth(apCargaBarra, 956.0, timeAnimation, new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    fadeIn(apCargaLeft, 1000, null);
                    fadeIn(apCargaMid, 1000, null);
                    fadeIn(apCargaRight, 1000, null);

                    final Timer tNext = new Timer();

                    TimerTask ttNext = new TimerTask() {
                        public void run() {
                            Platform.runLater(new Runnable() {
                                public void run() {
                                    try {                                        
                                        openWindows(ConstantsSeleccion.W_SEL_PUESTA0_CONFIRMACIÓN);
                                        unblokTop();
                                        tNext.cancel();
                                        tNext.purge();
                                    } catch (IOException ex) {
                                        tNext.cancel();
                                        tNext.purge();
                                    }
                                }
                            });
                        }
                    };

                    tNext.scheduleAtFixedRate(ttNext, 2000, 10000);
                }
            });

            moveX(apCarga, 989, timeAnimation, null);
            counterNumber(lblCarga, 90, 100, timeAnimation, "%", null);

            timer.cancel();
            timer.purge();
        }
    }
}
