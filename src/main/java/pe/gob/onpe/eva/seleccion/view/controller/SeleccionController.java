package pe.gob.onpe.eva.seleccion.view.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.sun.webkit.dom.KeyboardEventImpl;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import pe.gob.onpe.animations.view.OnpeAnimations;
import static pe.gob.onpe.eva.common.view.AppController.wEva;
import pe.gob.onpe.eva.model.mapper.CountGrado;
import pe.gob.onpe.eva.model.mapper.ODPESel;
import pe.gob.onpe.eva.seleccion.constant.ConstantsSeleccion;
import static pe.gob.onpe.eva.seleccion.view.controller.LocalAppController.timer1;
import static pe.gob.onpe.eva.seleccion.view.controller.LocalAppController.timer2;
import static pe.gob.onpe.eva.seleccion.view.controller.LocalAppController.timer3;

/**
 * FXML Controller class
 *
 * @author aquispec
 */
public class SeleccionController extends LocalAppController implements Initializable {

    @FXML
    private Pane apVerde1, apVerde2, apLoadCargaSel, apVerdeAsignacion, apVerdeSeleccion;
    @FXML
    private Pane apSeleccionAleatoria, apAsignacionPesos, imgCh1, imgCh2, imgCh3, imgCh4, imgCh5, imgCh6, imgCh7, imgCh8, imgCh9, imgCh10, imgCh11;
    @FXML
    private Label lblCargaAsignacion, lblCargaSeleccion, lblElectores, lblMesas, lblN1;
    @FXML
    private CheckBox chN1, chN2, chN3, chP, chAleatorio1, chAleatorio2, chAleatorio3, chAleatorio4, chAleatorio5, chAleatorio6, chAleatorio7;

    private static int timeCarga, timeAsignacion;
    private boolean boolCompletAsignacion, boolCompletSeleccion_1, boolCompletSeleccion_2, boolCompletSeleccion_3, boolCargaSeleccion, bollCargaAsignacion, boolConsulta, boolEnd, boolCheck = false;
    private int iProcSeleccion;
    private boolean boolEndAsig = false;
    private int timeCargaAsignacion, timeCargaSeleccion;
    private int countODPEs, optionSave;
    private ArrayList<String> lstInicioFin = new ArrayList<>();
    private String DateEndSeleccion = "";
    Task task;
    TimerTask timerTask3;
    int intTo = 0;
    int totalMesas90 = 0;
    private Properties systemProperties = new Properties(System.getProperties());
    private String tempPath = systemProperties.getProperty("java.io.tmpdir");
    List<ODPESel> lst = null;
    CountGrado countElectoresMesas = null;
    private Locale locale = new Locale("PE", "PE");
    Thread threadAsig, threadSel1, threadSel2, threadSel3;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        timeAsignacion = 11;
        threadAsig = null;
        threadSel1 = null;
        threadSel2 = null;
        threadSel3 = null;
        countElectoresMesas = oSeleccionService.getSeleccionService().fetchCantidadElectoresMesas();
        totalMesas90 = (int) (countElectoresMesas.getTotalMesas() * 0.9);
        timeCarga = 2000;
        timeCargaAsignacion = 5000;
        timeCargaSeleccion = 9000;
        apVerdeAsignacion.setStyle("-fx-background-color: #00B050;");
        apVerdeAsignacion.setVisible(false);
        apVerdeSeleccion.setStyle("-fx-background-color: #00B050;");
        apVerdeSeleccion.setVisible(false);
        apVerde1.setStyle("-fx-background-color: #00B050;");
        apVerde2.setStyle("-fx-background-color: #00B050;");
        imgCh1.setVisible(false);
        imgCh2.setVisible(false);
        imgCh3.setVisible(false);
        imgCh4.setVisible(false);
        imgCh5.setVisible(false);
        imgCh6.setVisible(false);
        imgCh7.setVisible(false);
        imgCh8.setVisible(false);
        imgCh9.setVisible(false);
        imgCh10.setVisible(false);
        imgCh11.setVisible(false);
        countODPEs = oSeleccionService.getSeleccionService().fetchCountODPEs();
        intTo = (int) (countODPEs * 0.9);

        TimerTask timerTask = new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        progressAsignacion();
                    }
                });
            }
        };
        timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 1000, timeCarga);

        lblCargaAsignacion.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                switch (newValue) {
                    case "1 %":
                        chN1.setDisable(false);
                        chN2.setDisable(false);
                        chN3.setDisable(false);
                        chP.setDisable(false);
                        lblN1.setDisable(false);
                        chAleatorio1.setDisable(false);
                        chAleatorio2.setDisable(false);
                        chAleatorio3.setDisable(false);
                        chAleatorio4.setDisable(false);
                        chAleatorio5.setDisable(false);
                        chAleatorio6.setDisable(false);
                        chAleatorio7.setDisable(false);
                        break;
                }
            }
        });

        lblCargaSeleccion.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                switch (newValue) {
                    case "99 %":
                        apVerdeSeleccion.setVisible(true);
                        break;
                    case "100 %":
                        apVerdeSeleccion.setVisible(true);
                        imgCh1.setVisible(true);
                        imgCh2.setVisible(true);
                        imgCh3.setVisible(true);
                        imgCh4.setVisible(true);
                        imgCh5.setVisible(true);
                        imgCh6.setVisible(true);
                        imgCh7.setVisible(true);
                        imgCh8.setVisible(true);
                        imgCh9.setVisible(true);
                        imgCh10.setVisible(true);
                        imgCh11.setVisible(true);
                        chN1.setTextFill(Paint.valueOf("#000000"));
                        chN2.setTextFill(Paint.valueOf("#000000"));
                        chN3.setTextFill(Paint.valueOf("#000000"));
                        chP.setTextFill(Paint.valueOf("#000000"));
                        lblN1.setTextFill(Paint.valueOf("#000000"));
                        chAleatorio1.setTextFill(Paint.valueOf("#000000"));
                        chAleatorio2.setTextFill(Paint.valueOf("#000000"));
                        chAleatorio3.setTextFill(Paint.valueOf("#000000"));
                        chAleatorio4.setTextFill(Paint.valueOf("#000000"));
                        chAleatorio5.setTextFill(Paint.valueOf("#000000"));
                        chAleatorio6.setTextFill(Paint.valueOf("#000000"));
                        chAleatorio7.setTextFill(Paint.valueOf("#000000"));
                        break;
                }
            }
        });
    }

    private void nextCheck(CheckBox chSelect, CheckBox chLoad) {
        chSelect.setTextFill(Paint.valueOf("#000000"));
        chLoad.setDisable(false);
        chLoad.setTextFill(Paint.valueOf("#FF5F3B"));
    }

    private void procesoAsignacion() {

        ConstantsSeleccion.STR_FECHA_HORA_INICIO = oSeleccionService.getSeleccionService().fetchFechaHora();

        lstInicioFin = oSeleccionService.getSeleccionService().ProcesoSeleccion25();
        if (lstInicioFin.size() > 0) {
            ConstantsSeleccion.STR_FECHA_HORA_INICIO = lstInicioFin.get(0);
            boolCompletAsignacion = true;
        }
    }

    private void iniBarraAsignacion() {

        moveY(apVerde1, 338 - (90 * 228 / 100), 1000 * 60 * timeAsignacion, null);
        animationHeight(apVerde1, 90 * 240 / 100.0, 1000 * 60 * timeAsignacion);
        counterNumber(lblCargaAsignacion, Integer.parseInt(lblCargaAsignacion.getText().substring(0, lblCargaAsignacion.getText().length() - 2)), 90, 1000 * 60 * timeAsignacion, " %", null);
        counterNumber(lblElectores, Integer.parseInt(lblElectores.getText()), countElectoresMesas.getTotalElectores() * 90 / 100, 1000 * 60 * timeAsignacion, null);
    }

    public void progressAsignacion() {
        if (!bollCargaAsignacion) {
            blokTop();

            bollCargaAsignacion = true;
            Task tAsig = new Task() {
                @Override
                protected Object call() throws Exception {
                    procesoAsignacion();
                    return null;
                }
            };

            Thread thread1 = new Thread(tAsig);
            thread1.start();

            Task tBarraAsig = new Task() {
                @Override
                protected Object call() throws Exception {
                    iniBarraAsignacion();
                    return null;
                }
            };
            threadAsig = new Thread(tBarraAsig);
            threadAsig.start();

            boolEndAsig = false;

            TimerTask timerTask2 = new TimerTask() {
                public void run() {
                    Platform.runLater(new Runnable() {
                        public void run() {
                            try {
                                if (boolEndAsig) {
                                    apVerdeAsignacion.setVisible(true);
                                    timer2.cancel();
                                    timer2.purge();
                                    procesoSeleccion_1();
                                }
                                if (boolCompletAsignacion) {
                                    threadAsig.interrupt();
                                    //if (electores1 >= countElectoresMesas.getTotalElectores()) {                                        
                                    moveY(apVerde1, 110, 3000, null);
                                    animationHeight(apVerde1, 240.0, 3000);
                                    counterNumber(lblCargaAsignacion, Integer.parseInt(lblCargaAsignacion.getText().substring(0, lblCargaAsignacion.getText().length() - 2)), 100, 3500, " %", null);
                                    counterNumber(lblElectores, Integer.parseInt(lblElectores.getText()), countElectoresMesas.getTotalElectores(), 3000, null);
                                    boolEndAsig = true;

                                }
                            } catch (Exception ex) {
                                Logger.getLogger(SeleccionController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });
                }
            };

            timer2 = new Timer();
            timer2.scheduleAtFixedRate(timerTask2, 1000, 3000);

            timer.cancel();
            timer.purge();
        }
    }

    private void iniBarraSeleccion(int porcent, int time) {
        moveY(apVerde2, 290 - (porcent * 230 / 100), time * 60000, null);
        animationHeight(apVerde2, porcent * 235 / 100.0, time * 60000);
        counterNumber(lblCargaSeleccion, Integer.parseInt(lblCargaSeleccion.getText().substring(0, lblCargaSeleccion.getText().length() - 2)), porcent, time * 60000, " %", null);
        counterNumber(lblMesas, Integer.parseInt(lblMesas.getText()), countElectoresMesas.getTotalMesas() * porcent / 100, time * 60000, null);
    }

    public void procesoSeleccion_1() {

        TimerTask timerTask2 = new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        try {
                            if (!boolCompletSeleccion_1 && iProcSeleccion == 0) {
                                task = new Task() {
                                    @Override
                                    protected Object call() throws Exception {
                                        procesoSeleccion25_1();
                                        return null;
                                    }
                                };
                                Thread thread1 = new Thread(task);
                                thread1.start();
                            }

                            if (boolCompletSeleccion_3 && iProcSeleccion == 3) {
                                moveY(apVerde2, 290 - (100 * 230 / 100), 3700, null);
                                animationHeight(apVerde2, 100 * 235 / 100.0, 3700);
                                counterNumber(lblCargaSeleccion, Integer.parseInt(lblCargaSeleccion.getText().substring(0, lblCargaSeleccion.getText().length() - 2)), 100, 3500, " %", null);
                                counterNumber(lblMesas, Integer.parseInt(lblMesas.getText()), countElectoresMesas.getTotalMesas(), 3000, null);
                                timer3.cancel();
                                timer3.purge();
                                TerminarProgressSeleccion();
                            }
                        } catch (Exception ex) {
                            Logger.getLogger(SeleccionController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
            }
        };
        timer3 = new Timer();
        timer3.scheduleAtFixedRate(timerTask2, 4000, 3000);
    }

    public void procesoSeleccion_2() {

        TimerTask timerTask2 = new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        try {
                            if (boolCompletSeleccion_1 && iProcSeleccion == 1) {
                                task = new Task() {
                                    @Override
                                    protected Object call() throws Exception {
                                        procesoSeleccion25_2();
                                        return null;
                                    }
                                };
                                Thread thread1 = new Thread(task);
                                thread1.start();
                            }

                            if (boolCompletSeleccion_2 && iProcSeleccion == 2) {
                                moveY(apVerde2, 290 - (66 * 230 / 100), 3000, null);
                                animationHeight(apVerde2, 66 * 235 / 100.0, 3000);
                                counterNumber(lblCargaSeleccion, Integer.parseInt(lblCargaSeleccion.getText().substring(0, lblCargaSeleccion.getText().length() - 2)), 66, 3000, " %", null);
                                counterNumber(lblMesas, Integer.parseInt(lblMesas.getText()), countElectoresMesas.getTotalMesas(), 3000, null);
                                task = new Task() {
                                    @Override
                                    protected Object call() throws Exception {
                                        procesoSeleccion_3();
                                        return null;
                                    }
                                };
                                Thread thread1 = new Thread(task);
                                thread1.start();
                                timer3.cancel();
                                timer3.purge();
                            }

                        } catch (Exception ex) {
                            Logger.getLogger(SeleccionController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
            }
        };
        timer3 = new Timer();
        timer3.scheduleAtFixedRate(timerTask2, 4000, 3000);
    }
    
    public void procesoSeleccion_3() {

        TimerTask timerTask2 = new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        try {
                            if (boolCompletSeleccion_2 && iProcSeleccion == 2) {
                                task = new Task() {
                                    @Override
                                    protected Object call() throws Exception {
                                        procesoSeleccion25_3();
                                        return null;
                                    }
                                };
                                Thread thread1 = new Thread(task);
                                thread1.start();
                            }

                            if (boolCompletSeleccion_3 && iProcSeleccion == 3) {
                                moveY(apVerde2, 290 - (100 * 230 / 100), 3800, null);
                                animationHeight(apVerde2, 100 * 235 / 100.0, 3800);
                                counterNumber(lblCargaSeleccion, Integer.parseInt(lblCargaSeleccion.getText().substring(0, lblCargaSeleccion.getText().length() - 2)), 100, 3500, " %", null);
                                counterNumber(lblMesas, Integer.parseInt(lblMesas.getText()), countElectoresMesas.getTotalMesas(), 3000, null);
                                timer3.cancel();
                                timer3.purge();
                                TerminarProgressSeleccion();
                            }
                        } catch (Exception ex) {
                            Logger.getLogger(SeleccionController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
            }
        };
        timer3 = new Timer();
        timer3.scheduleAtFixedRate(timerTask2, 4000, 3000);
    }
    
    private void procesoSeleccion25_2() {
        Task tBarraSeleccion2 = new Task() {
            @Override
            protected Object call() throws Exception {
                iniBarraSeleccion(63, 8);
                return null;
            }
        };
        Thread threadSel2 = new Thread(tBarraSeleccion2);
        threadSel2.start();

        iProcSeleccion = 2;
        oSeleccionService.getSeleccionService().seleccion25_2();
        boolCompletSeleccion_2 = true;
    }

    private void procesoSeleccion25_3() {
        Task tBarraSeleccion3 = new Task() {
            @Override
            protected Object call() throws Exception {
                //iniBarraSeleccion(96, 5);
                return null;
            }
        };
        Thread threadSel3 = new Thread(tBarraSeleccion3);
        threadSel3.start();

        iProcSeleccion = 3;
        oSeleccionService.getSeleccionService().seleccion25_3();
        boolCompletSeleccion_3 = true;
        System.out.println("FINALIZANDO SELECCION 25");
    }

    private void procesoSeleccion25_1() {

        iProcSeleccion = 1;
        Task tBarraSeleccion1 = new Task() {
            @Override
            protected Object call() throws Exception {
                iniBarraSeleccion(96, 13);
                return null;
            }
        };
        Thread threadSel1 = new Thread(tBarraSeleccion1);
        threadSel1.start();

        oSeleccionService.getSeleccionService().seleccion25_1();;
        boolCompletSeleccion_1 = true;


        iProcSeleccion = 2;
        oSeleccionService.getSeleccionService().seleccion25_2();
        boolCompletSeleccion_2 = true;

        iProcSeleccion = 3;
        oSeleccionService.getSeleccionService().seleccion25_3();
        boolCompletSeleccion_3 = true;
        System.out.println("FINALIZANDO SELECCION 25");
        threadSel1.interrupt();
    }

    public void ProgressSeleccion() {
        if (!boolCargaSeleccion) {
            moveY(apVerde2, 85, timeCargaSeleccion, null);
            animationHeight(apVerde2, 207.0, timeCargaSeleccion);
            boolCargaSeleccion = true;
            counterNumber(lblCargaSeleccion, 0, 90, timeCargaSeleccion, " %", null);
            counterNumber(lblMesas, 0, totalMesas90, timeCargaSeleccion, "", null);
        }
    }

    private void TerminarProgressSeleccion() {
        if (boolCompletSeleccion_3) {
            if (!boolEnd) {

                boolEnd = true;

                TimerTask timerTask2 = new TimerTask() {
                    public void run() {
                        Platform.runLater(new Runnable() {
                            public void run() {
                                try {
                                    timer2.cancel();
                                    timer2.purge();
                                    DateEndSeleccion = oSeleccionService.getSeleccionService().fetchFechaHora();
                                    ConstantsSeleccion.STR_FECHA_HORA_FIN = DateEndSeleccion;
                                    LoadResumen();
                                } catch (Exception ex) {
                                    Logger.getLogger(SeleccionController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        });
                    }
                };

                timer2 = new Timer();
                timer2.scheduleAtFixedRate(timerTask2, 6000, 10000);

                timer1.cancel();
                timer1.purge();
                timer3.cancel();
                timer3.purge();
            }
        }
    }

    public void LoadResumen() throws IOException, Exception {
        timer2.cancel();
        timer2.purge();
        AnchorPane pane = loadFxml(ConstantsSeleccion.W_SEL_FIN_SELECCION);
        final OnpeAnimations onpeAnimations = OnpeAnimations.getInstance();
        onpeAnimations.setApMain(wEva.apMain);
        onpeAnimations.openPopup(pane, 704);
    }

}
