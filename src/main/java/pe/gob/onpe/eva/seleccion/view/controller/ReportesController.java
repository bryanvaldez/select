/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.eva.seleccion.view.controller;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import pe.gob.onpe.animations.OnpeMessagesTypes;
import pe.gob.onpe.animations.view.OnpeAnimations;
import pe.gob.onpe.eva.common.view.AppController;

import pe.gob.onpe.eva.model.mapper.CountGrado;
import pe.gob.onpe.eva.model.mapper.Exclusion;
import pe.gob.onpe.eva.model.mapper.ODPESel;
import pe.gob.onpe.eva.model.mapper.ParametroDesc;
import pe.gob.onpe.eva.model.mapper.PesoPadron;
import pe.gob.onpe.eva.model.mapper.Reporte;
import pe.gob.onpe.eva.model.mapper.UbigeoDesc;
import pe.gob.onpe.eva.seleccion.constant.ConstantsSeleccion;

/**
 * FXML Controller class
 *
 * @author aquispec
 */
public class ReportesController extends LocalAppController implements Initializable {

    @FXML
    private ComboBox<Reporte> cbListReport;

    @FXML

    private Label lblDpto, lblProv, lblDist, lblTipoGrado, lblGrado, lblRangoEdad, lblDesde, lblHasta, lblDiscapacidad, lblMesa, lblMotivo, lblVacio, lblDocumento, lblTitleIn, lblAmbito, lblMensajeVistaPrevia, lblODPE, colDistrito, colProvincia, colDepartamento;

    @FXML
    private TextField txtDesde, txtHasta, txtDocumento, txtMesa;

    @FXML
    private ComboBox<UbigeoDesc> cbListDpto, cbListProv, cbListDist;
    @FXML
    private ComboBox<ParametroDesc> cbTipoGrado, cbGrado, cbListMotivo;

    @FXML
    private ComboBox<ODPESel> cbListAmbito;

    @FXML
    private ComboBox<String> cbDiscapacidad;

    @FXML
    private GridPane gridReportePadron;

    @FXML
    private ScrollPane scrollPadron;

    @FXML
    private Pane pane1, pane2, pane3, pane4, pane5, pane6, pane7, pane8, pane9, pane10, pane11, pane12, pane13, pane14, paneReporte;

    @FXML
    private AnchorPane apBusqueda, apReporte, apFooter;
    @FXML
    private ProgressIndicator piLoading;

    OnpeAnimations animations = OnpeAnimations.getInstance();

    boolean bDiscapacidad = false;
    private Boolean searchComplete = false, consultComplete = true;
    //private List oList = null;
    private Integer speedAnim = 500;
    private Button btnPrint, btnExport;

    @FXML
    private Button btnSearch;

    /**
     * Initializes the controller class.
     */
    private List<Reporte> lstReportes;
    private List<UbigeoDesc> lstDpto, lstProv, lstDist;
    private List<ParametroDesc> lstTipoGrado, lstGrado, lstMotivo;
    private List<ODPESel> lstAmbito;
    int nAmbito = 0;

    private void cleanPanesAll() {
        pane1.setVisible(false);
        pane2.setVisible(false);
        pane3.setVisible(false);
        pane4.setVisible(false);
        pane5.setVisible(false);
        pane6.setVisible(false);
        pane7.setVisible(false);
        pane8.setVisible(false);
        pane9.setVisible(false);
        pane10.setVisible(false);
        pane11.setVisible(false);
        pane12.setVisible(false);
        pane13.setVisible(false);
        pane14.setVisible(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblAmbito.setText(ConstantsSeleccion.CONFIGURACION.getEtiquetaOdpe() + ":");
        lblODPE.setText(ConstantsSeleccion.CONFIGURACION.getEtiquetaOdpe() + ":");
        lblDpto.setText(ConstantsSeleccion.CONFIGURACION.getEtiquetaDpto() + ":");
        lblProv.setText(ConstantsSeleccion.CONFIGURACION.getEtiquetaProv() + ":");
        piLoading.setVisible(false);
        lblVacio.setVisible(false);

        lblDist.setText(ConstantsSeleccion.CONFIGURACION.getEtiquetaDist() + ":");
        colDistrito.setText(ConstantsSeleccion.CONFIGURACION.getEtiquetaDist());
        colDepartamento.setText(ConstantsSeleccion.CONFIGURACION.getEtiquetaDpto());
        colProvincia.setText(ConstantsSeleccion.CONFIGURACION.getEtiquetaProv());

        txtDesde.setText("");
        txtHasta.setText("");
        txtDesde.textProperty().addListener(onlySomeCharacters("[0-9]", txtDesde, 3));
        txtHasta.textProperty().addListener(onlySomeCharacters("[0-9]", txtHasta, 3));
        txtDocumento.setText("");
        txtDocumento.textProperty().addListener(onlySomeCharacters("[A-Z0-9]", txtDocumento, 8));
        txtMesa.setText("");
        txtMesa.textProperty().addListener(onlySomeCharacters("[0-9]", txtMesa, 6));
        hideControls(false);
        lblDocumento.setVisible(false);
        lblAmbito.setVisible(false);
        lblMotivo.setVisible(false);
        cbListMotivo.setVisible(false);
        cleanPanesAll();
        apBusqueda.setVisible(false);
        apReporte.setVisible(false);

        ConstantsSeleccion.N__SELECTED_REPORT = new Reporte();
        lstReportes = oSeleccionService.getSeleccionService().fetchListReports();

        btnPrint = (Button) apFooter.lookup("#btnPrint");
        btnExport = (Button) apFooter.lookup("#btnExport");

        fadeOut(btnPrint, speedAnim, null);
        fadeOut(btnExport, speedAnim, null);

        ObservableList<Reporte> data = FXCollections.observableArrayList();

        for (Reporte mParam : lstReportes) {
            data.add(mParam);
        }
        cbListReport.setItems(data);

        cbListReport.valueProperty().addListener(new ChangeListener<Reporte>() {
            @Override
            public void changed(ObservableValue<? extends Reporte> observable, Reporte oldValue, Reporte newValue) {

                txtDesde.setText("");
                txtHasta.setText("");
                txtDocumento.setText("");
                lblDocumento.setVisible(false);
                lblAmbito.setVisible(false);
                txtMesa.setText("");
                ConstantsSeleccion.lstReport = null;
                ConstantsSeleccion.N__SELECTED_REPORT = newValue;
                ConstantsSeleccion.N__SELECTED_REPORT.setGrado(0);
                //changeFilter(newValue.getCodigo());
                cleanControlsBox();
                cleanPanesAll();

                cbGrado.getSelectionModel().selectFirst();
                cbListMotivo.getSelectionModel().selectFirst();
                piLoading.setLayoutY(400.0);
                
                cleanComboBoxAll(cbListDpto);
                if (!ConstantsSeleccion.N__SELECTED_REPORT.getCodigo().equals(ConstantsSeleccion.N_RPT_MESAS_ELECTORES_POR_UBIGEO)) {
                    lstDpto = oSeleccionService.getSeleccionService().fetchListDpto(nAmbito);
                    if (!ConstantsSeleccion.N__SELECTED_REPORT.getCodigo().equals(ConstantsSeleccion.N_RPT_LISTA_PADRON_ELECTORAL)
                            && !ConstantsSeleccion.N__SELECTED_REPORT.getCodigo().equals(ConstantsSeleccion.N_RPT_LISTA_PADRON_X_PESOS)) {
                        //&& !ConstantsSeleccion.N__SELECTED_REPORT.getCodigo().equals(ConstantsSeleccion.N_RPT_LISTA_25_SELECCIONADOS)) {
                        cbListDpto.getItems().add(new UbigeoDesc("0", ConstantsSeleccion.CB_REPORT_OPTION_ALL));
                    } else {
                        cbListDpto.getItems().add(new UbigeoDesc("0", ConstantsSeleccion.CB_REPORT_OPTION_SELECT));
                    }

                    for (UbigeoDesc mParam : lstDpto) {
                        cbListDpto.getItems().add(mParam);
                    }
                } else {
                    cbListDpto.getItems().add(new UbigeoDesc("0", ConstantsSeleccion.CB_REPORT_OPTION_ALL));
                }
                cbListDpto.getSelectionModel().selectFirst();

                try {
                    if (ConstantsSeleccion.N__SELECTED_REPORT.getCodigo().equals(ConstantsSeleccion.N_RPT_PESOS_X_RANGO_EDAD_N1) //REPORTE DE PESOS POR RANGO DE EDAD (N1)
                            || ConstantsSeleccion.N__SELECTED_REPORT.getCodigo().equals(ConstantsSeleccion.N_RPT_PESOS_X_GRADO_INSTRUCCION_N1) //REPORTE DE PESOS POR GRADO DE INSTRUCCIÓN (N1)
                            || ConstantsSeleccion.N__SELECTED_REPORT.getCodigo().equals(ConstantsSeleccion.N_RPT_PESOS_X_DISCAPACIDAD_N1) //REPORTE DE PESOS POR DISCAPACIDAD (N1)
                            || ConstantsSeleccion.N__SELECTED_REPORT.getCodigo().equals(ConstantsSeleccion.N_RPT_PESOS_X_RANGO_EDAD_N3) //REPORTE DE PESOS POR RANGO DE EDAD (N3)                                 
                            || ConstantsSeleccion.N__SELECTED_REPORT.getCodigo().equals(ConstantsSeleccion.N_RPT_LISTA_CIUDADANOS_MULTI_EXCLUIDOS)
                            || ConstantsSeleccion.N__SELECTED_REPORT.getCodigo().equals(ConstantsSeleccion.N_RPT_PESOS_X_FRECUENCIA_MM)) {
                        ActionEvent event = new ActionEvent();
                        piLoading.setLayoutY(250.0);
                        reportLoading();
                        Buscar(event);
                    } else {
                        btnPrint = (Button) apFooter.lookup("#btnPrint");
                        btnExport = (Button) apFooter.lookup("#btnExport");

                        fadeOut(btnPrint, speedAnim, null);
                        fadeOut(btnExport, speedAnim, null);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(ReportesController.class.getName()).log(Level.SEVERE, null, ex);
                }

                selectedViewPane(newValue.getCodigo());
            }
        });

        //MOTIVOS DE EXCLUSION
        lstMotivo = oSeleccionService.getSeleccionService().fetchListMotivo();

        cbListMotivo.getItems().add(new ParametroDesc(0, ConstantsSeleccion.CB_REPORT_OPTION_ALL, 0));
        for (ParametroDesc mParam : lstMotivo) {
            cbListMotivo.getItems().add(mParam);
        }

        cbListMotivo.valueProperty().addListener(new ChangeListener<ParametroDesc>() {
            @Override
            public void changed(ObservableValue<? extends ParametroDesc> observable, ParametroDesc oldValue, ParametroDesc newValue) {
                if (newValue.getIdTypeParameter() != 0) {
                    ConstantsSeleccion.N__SELECTED_REPORT.setMotivo(newValue.getIdTypeParameter());
                } else {
                    ConstantsSeleccion.N__SELECTED_REPORT.setMotivo(0);
                }
            }
        });

        //TIPO GRADO - GRADO
        lstTipoGrado = oSeleccionService.getSeleccionService().fetchListTipGrad();
        cbTipoGrado.getItems().add(new ParametroDesc(0, ConstantsSeleccion.CB_REPORT_OPTION_ALL, 0));
        for (ParametroDesc mParam : lstTipoGrado) {
            cbTipoGrado.getItems().add(mParam);
        }

        cbTipoGrado.valueProperty().addListener(new ChangeListener<ParametroDesc>() {
            @Override
            public void changed(ObservableValue<? extends ParametroDesc> observable, ParametroDesc oldValue, ParametroDesc newValue) {

                if (newValue.getIdTypeParameter() != 0) {
                    ConstantsSeleccion.N__SELECTED_REPORT.setGrado(newValue.getIdTypeParameter());
                    lstGrado = oSeleccionService.getSeleccionService().fetchListGrad(newValue.getIdTypeParameter());

                    cleanComboBoxAll(cbGrado);
                    cbGrado.getItems().add(new ParametroDesc(0, ConstantsSeleccion.CB_REPORT_OPTION_ALL, 0));
                    if (lstGrado != null) {
                        for (ParametroDesc mParam : lstGrado) {
                            cbGrado.getItems().add(mParam);
                        }
                    }
                    cbGrado.getSelectionModel().selectFirst();
                } else {
                    ConstantsSeleccion.N__SELECTED_REPORT.setGrado(cbTipoGrado.getSelectionModel().selectedItemProperty().get().getIdTypeParameter());
                    cleanComboBoxAll(cbGrado);
                }
            }
        });

        cbGrado.valueProperty().addListener(new ChangeListener<ParametroDesc>() {
            @Override
            public void changed(ObservableValue<? extends ParametroDesc> observable, ParametroDesc oldValue, ParametroDesc newValue) {
                if (newValue.getIdTypeParameter() != 0) {
                    ConstantsSeleccion.N__SELECTED_REPORT.setGrado(newValue.getIdTypeParameter());
                }
            }
        });

        //AMBITO - RESUMEN
        lstAmbito = oSeleccionService.getSeleccionService().fetchListAmbito();
        cbListAmbito.getItems().add(new ODPESel(0, ConstantsSeleccion.CB_REPORT_OPTION_ALL));
        for (ODPESel mParam : lstAmbito) {
            cbListAmbito.getItems().add(mParam);
        }

        cbListAmbito.valueProperty().addListener(new ChangeListener<ODPESel>() {
            @Override
            public void changed(ObservableValue<? extends ODPESel> observable, ODPESel oldValue, ODPESel newValue) {

                txtMesa.setText("");
                ConstantsSeleccion.N__SELECTED_REPORT.setMesa("");
                if (newValue.getOdpePk() != 0) {
                    ConstantsSeleccion.N__SELECTED_REPORT.setAmbito(newValue.getOdpePk());

                    lstDpto = oSeleccionService.getSeleccionService().fetchListDpto(newValue.getOdpePk());

                    cleanComboBoxAll(cbListDpto);
                    cleanComboBoxAll(cbListProv);
                    cleanComboBoxAll(cbListDist);

                    cbListDpto.getItems().add(new UbigeoDesc("0", ConstantsSeleccion.CB_REPORT_OPTION_ALL));
                    if (lstDpto != null) {
                        for (UbigeoDesc mParam : lstDpto) {
                            cbListDpto.getItems().add(mParam);
                        }
                    }

                    cbListDpto.getSelectionModel().selectFirst();
                    cbListProv.getSelectionModel().selectFirst();
                    cbListDist.getSelectionModel().selectFirst();
                } else {
                    ConstantsSeleccion.N__SELECTED_REPORT.setAmbito(0);
                    ConstantsSeleccion.N__SELECTED_REPORT.setUbigeo("0");
                    cleanComboBoxAll(cbListDpto);
                    cleanComboBoxAll(cbListProv);
                    cleanComboBoxAll(cbListDist);
                }
            }
        });

        //DEPARTAMENTO - PROVINCIA - DISTRITO - MESA                  
        lstDpto = oSeleccionService.getSeleccionService().fetchListDpto(nAmbito);
        cbListDpto.getItems().add(new UbigeoDesc("0", ConstantsSeleccion.CB_REPORT_OPTION_ALL));
        for (UbigeoDesc mParam : lstDpto) {
            cbListDpto.getItems().add(mParam);
        }

        cbListDpto.valueProperty().addListener(new ChangeListener<UbigeoDesc>() {
            @Override
            public void changed(ObservableValue<? extends UbigeoDesc> observable, UbigeoDesc oldValue, UbigeoDesc newValue) {

                txtMesa.setText("");
                ConstantsSeleccion.N__SELECTED_REPORT.setMesa("");
                if (!"0".equals(newValue.getUbigeoPk())) {
                    ConstantsSeleccion.N__SELECTED_REPORT.setUbigeo(newValue.getUbigeoPk());
                    lstProv = oSeleccionService.getSeleccionService().fetchListProv(newValue.getUbigeoPk(), nAmbito);

                    cleanComboBoxAll(cbListProv);
                    cleanComboBoxAll(cbListDist);

                    cbListProv.getItems().add(new UbigeoDesc("0", ConstantsSeleccion.CB_REPORT_OPTION_ALL));
                    if (lstProv != null) {
                        for (UbigeoDesc mParam : lstProv) {
                            cbListProv.getItems().add(mParam);
                        }
                    }

                    cbListProv.getSelectionModel().selectFirst();
                    cbListDist.getSelectionModel().selectFirst();
                } else {
                    ConstantsSeleccion.N__SELECTED_REPORT.setUbigeo("0");
                    cleanComboBoxAll(cbListProv);
                    cleanComboBoxAll(cbListDist);
                }
            }
        });

        cbListProv.valueProperty().addListener(new ChangeListener<UbigeoDesc>() {
            @Override
            public void changed(ObservableValue<? extends UbigeoDesc> observable, UbigeoDesc oldValue, UbigeoDesc newValue) {

                txtMesa.setText("");
                ConstantsSeleccion.N__SELECTED_REPORT.setMesa("");
                if (!"0".equals(newValue.getUbigeoPk())) {
                    ConstantsSeleccion.N__SELECTED_REPORT.setUbigeo(newValue.getUbigeoPk());
                    lstDist = oSeleccionService.getSeleccionService().fetchListDist(newValue.getUbigeoPk(), nAmbito);

                    cleanComboBoxAll(cbListDist);

                    cbListDist.getItems().add(new UbigeoDesc("0", ConstantsSeleccion.CB_REPORT_OPTION_ALL));
                    if (lstDist != null) {
                        for (UbigeoDesc mParam : lstDist) {
                            cbListDist.getItems().add(mParam);
                        }
                    }

                    cbListDist.getSelectionModel().selectFirst();
                } else {
                    ConstantsSeleccion.N__SELECTED_REPORT.setUbigeo((cbListDpto.getSelectionModel().selectedItemProperty().get().getUbigeoPk().substring(0, 2)).concat("0000"));
                    cleanComboBoxAll(cbListDist);
                }

            }
        });

        cbListDist.valueProperty().addListener(new ChangeListener<UbigeoDesc>() {
            @Override
            public void changed(ObservableValue<? extends UbigeoDesc> observable, UbigeoDesc oldValue, UbigeoDesc newValue) {
                txtMesa.setText("");
                ConstantsSeleccion.N__SELECTED_REPORT.setMesa("");
                if (!"0".equals(newValue.getUbigeoPk())) {
                    ConstantsSeleccion.N__SELECTED_REPORT.setUbigeo(newValue.getUbigeoPk());
                } else {
                    ConstantsSeleccion.N__SELECTED_REPORT.setUbigeo((cbListDpto.getSelectionModel().selectedItemProperty().get().getUbigeoPk().substring(0, 2)
                            + cbListProv.getSelectionModel().selectedItemProperty().get().getUbigeoPk().substring(2, 4)).concat("00"));
                }

            }
        });

        //DISCAPACIDAD
        cbDiscapacidad.getItems().add(ConstantsSeleccion.CB_REPORT_OPTION_ALL);
        cbDiscapacidad.getItems().add("SI");
        cbDiscapacidad.getItems().add("NO");
        cbDiscapacidad.getSelectionModel().selectFirst();

        ConstantsSeleccion.N__SELECTED_REPORT.setDiscapacidad(-1);
        bDiscapacidad = false;
        cbDiscapacidad.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.equals(ConstantsSeleccion.CB_REPORT_OPTION_ALL)) {
                    ConstantsSeleccion.N__SELECTED_REPORT.setDiscapacidad(-1);
                    bDiscapacidad = false;
                } else {
                    if (newValue.equals("SI")) {
                        ConstantsSeleccion.N__SELECTED_REPORT.setDiscapacidad(1);
                        bDiscapacidad = true;
                    } else {
                        if (newValue.equals("NO")) {
                            ConstantsSeleccion.N__SELECTED_REPORT.setDiscapacidad(0);
                            bDiscapacidad = true;
                        }
                    }
                }
            }
        });
    }

    @FXML
    private void cleanComboBoxAll(ComboBox cbo) {
        cbo.getItems().clear();
    }

    @FXML
    private void selectedViewPane(String oReporte) {

        hideControls(false);
        lblMotivo.setVisible(false);
        cbListMotivo.setVisible(false);

        if (oReporte.equals(ConstantsSeleccion.N_RPT_LISTA_PADRON_ELECTORAL) //LISTA DE PADRÓN ELECTORAL
                || oReporte.equals(ConstantsSeleccion.N_RPT_LISTA_PADRON_X_PESOS)) { //LISTA DE PADRÓN ELECTORAL CON PESOS
            hideControls(true);
            apBusqueda.setVisible(true);
            apReporte.setLayoutY(295.0); // 136
            txtDocumento.setVisible(false);
            cbListAmbito.setVisible(false);
            //lblMensajeVistaPrevia.setVisible(true);

        } else if (oReporte.equals(ConstantsSeleccion.N_RPT_LISTA_25_SELECCIONADOS) //LISTA DE 25 CANDIDATOS SELECCIONADOS
                || oReporte.equals(ConstantsSeleccion.N_RPT_LISTA_CIUDADANOS_EXCLUIDOS)) { //LISTA DE CIUDADANOS EXCLUIDOS
            lblDpto.setVisible(true);
            lblProv.setVisible(true);
            lblDist.setVisible(true);
            lblMesa.setVisible(true);

            cbListDpto.setVisible(true);
            cbListProv.setVisible(true);
            cbListDist.setVisible(true);
            txtMesa.setVisible(true);

            lblDpto.setLayoutY(26.0);
            lblProv.setLayoutY(26.0);
            lblDist.setLayoutY(26.0);
            lblAmbito.setLayoutY(55.0);

            cbListDpto.setLayoutY(23.0);
            cbListProv.setLayoutY(23.0);
            cbListDist.setLayoutY(23.0);
            cbListAmbito.setLayoutY(54.0);

            lblMesa.setLayoutX(30.0);
            lblMesa.setLayoutY(55.0);
            txtMesa.setLayoutX(146.0);
            txtMesa.setLayoutY(53.0);
            //lblMensajeVistaPrevia.setVisible(true);
            apBusqueda.setVisible(true);
            apReporte.setLayoutY(295.0);

        } else if (oReporte.equals(ConstantsSeleccion.N_RPT_GRADO_INSTUCCION_PADRON_ELECTORAL) //REPORTE POR GRADO DE INSTRUCCIÓN DEL PADRÓN
                || oReporte.equals(ConstantsSeleccion.N_RPT_GRADO_INSTRUCCION_25_SELECCIONADOS)) { //REPORTE POR GRADO DE INSTRUCCIÓN DE LA SELECCIÓN DE 25 CANDIDATOS A MIEMBROS DE MESA
            lblDpto.setVisible(true);
            lblProv.setVisible(true);
            lblDist.setVisible(true);
            cbListDpto.setVisible(true);
            cbListProv.setVisible(true);
            cbListDist.setVisible(true);

            lblDpto.setLayoutY(26.0);
            lblProv.setLayoutY(26.0);
            lblDist.setLayoutY(26.0);
            lblAmbito.setLayoutY(55.0);

            cbListDpto.setLayoutY(23.0);
            cbListProv.setLayoutY(23.0);
            cbListDist.setLayoutY(23.0);
            cbListAmbito.setLayoutY(54.0);

            //lblMensajeVistaPrevia.setVisible(false);
            apBusqueda.setVisible(true);
            apReporte.setLayoutY(295.0);

        } else if (oReporte.equals(ConstantsSeleccion.N_RPT_PESOS_X_RANGO_EDAD_N1) //REPORTE DE PESOS POR RANGO DE EDAD (N1)
                || oReporte.equals(ConstantsSeleccion.N_RPT_PESOS_X_GRADO_INSTRUCCION_N1) //REPORTE DE PESOS POR GRADO DE INSTRUCCIÓN (N1)
                || oReporte.equals(ConstantsSeleccion.N_RPT_PESOS_X_DISCAPACIDAD_N1) //REPORTE DE PESOS POR DISCAPACIDAD (N1)
                || oReporte.equals(ConstantsSeleccion.N_RPT_PESOS_X_RANGO_EDAD_N3) //REPORTE DE PESOS POR RANGO DE EDAD (N3)                                 
                || oReporte.equals(ConstantsSeleccion.N_RPT_LISTA_CIUDADANOS_MULTI_EXCLUIDOS)) { //LISTA DE CIUDADANOS CON MÁS DE UN MOTIVO DE EXCLUSIÓN
            hideControls(false);
            lblMotivo.setVisible(false);
            cbListMotivo.setVisible(false);

            //lblMensajeVistaPrevia.setVisible(false);
            apBusqueda.setVisible(false);
            apReporte.setLayoutY(136.0);

        } else if (oReporte.equals(ConstantsSeleccion.N_RPT_PESOS_X_FRECUENCIA_MM)) { //REPORTE DE PESOS POR NÚMERO DE VECES QUE HA EJERCIDO EL CARGO DE MIEMBRO DE MESA (N2)
            hideControls(false);
            lblMotivo.setVisible(false);
            cbListMotivo.setVisible(false);

            //lblMensajeVistaPrevia.setVisible(false);
            apBusqueda.setVisible(false);
            apReporte.setLayoutY(136);

        } else if (oReporte.equals(ConstantsSeleccion.N_RPT_MOTIVOS_EXCLUSION)) { //REPORTE DE MOTIVOS DE EXCLUSIÓN                  
            lblMotivo.setVisible(true);
            cbListMotivo.setVisible(true);

            //lblMensajeVistaPrevia.setVisible(false);
            apBusqueda.setVisible(true);
            apReporte.setLayoutY(295.0);
            txtDocumento.setVisible(false);

        } else if (oReporte.equals(ConstantsSeleccion.N_RPT_LISTA_CIUDADANOS_POR_MOTIVO_EXCLUSION)) {
            lblMotivo.setVisible(true);
            cbListMotivo.setVisible(true);

            //lblMensajeVistaPrevia.setVisible(true);
            apBusqueda.setVisible(true);
            apReporte.setLayoutY(295.0);
        } else if (oReporte.equals(ConstantsSeleccion.N_RPT_FRECUENCIA_MM_POR_CIUDADANO)) {
            hideControls(false);
            lblDocumento.setVisible(true);
            txtDocumento.setVisible(true);

            //lblMensajeVistaPrevia.setVisible(false);
            apBusqueda.setVisible(true);
            apReporte.setLayoutY(295.0);
        } else if (oReporte.equals(ConstantsSeleccion.N_RPT_MESAS_ELECTORES_POR_UBIGEO)) {
            lblDpto.setVisible(true);
            lblProv.setVisible(true);
            lblDist.setVisible(true);
            cbListDpto.setVisible(true);
            cbListProv.setVisible(true);
            cbListDist.setVisible(true);

            lblAmbito.setVisible(true);
            cbListAmbito.setVisible(true);

            lblDpto.setLayoutY(55.0);
            lblProv.setLayoutY(55.0);
            lblDist.setLayoutY(55.0);
            lblAmbito.setLayoutY(26.0);

            cbListDpto.setLayoutY(54.0);
            cbListProv.setLayoutY(54.0);
            cbListDist.setLayoutY(54.0);
            cbListAmbito.setLayoutY(23.0);

            //lblMensajeVistaPrevia.setVisible(true);
            apBusqueda.setVisible(true);
            apReporte.setLayoutY(295.0);
        }

    }

    private boolean validateEdades() {
        boolean result = false;
        if (txtDesde.getText().equals("")) {
            ConstantsSeleccion.N__SELECTED_REPORT.setEdadDesde(0);
        } else {
            ConstantsSeleccion.N__SELECTED_REPORT.setEdadDesde(Integer.valueOf(txtDesde.getText()));
        }
        if (txtHasta.getText().equals("")) {
            ConstantsSeleccion.N__SELECTED_REPORT.setEdadHasta(119);
        } else {
            ConstantsSeleccion.N__SELECTED_REPORT.setEdadHasta(Integer.valueOf(txtHasta.getText()));
        }
        if (ConstantsSeleccion.N__SELECTED_REPORT.getEdadHasta() < ConstantsSeleccion.N__SELECTED_REPORT.getEdadDesde()) {
            txtDesde.getStyleClass().add("txtRed");
            txtHasta.getStyleClass().add("txtRed");
            result = false;
        } else {
            txtDesde.getStyleClass().remove("txtRed");
            txtHasta.getStyleClass().remove("txtRed");
            result = true;
        }

        if (ConstantsSeleccion.N__SELECTED_REPORT.getEdadDesde() > 120 || ConstantsSeleccion.N__SELECTED_REPORT.getEdadHasta() > 120) {
            if (ConstantsSeleccion.N__SELECTED_REPORT.getEdadDesde() > 120) {
                txtDesde.getStyleClass().add("txtRed");
            } else {
                txtDesde.getStyleClass().remove("txtRed");
            }

            if (ConstantsSeleccion.N__SELECTED_REPORT.getEdadHasta() > 120) {
                txtHasta.getStyleClass().add("txtRed");
            } else {
                txtHasta.getStyleClass().remove("txtRed");
            }
            result = false;
        } else {
            result = true;
        }

        return result;
    }

    private boolean validateDocument() {
        boolean result = false;

        if (txtDocumento.getText().equals("")) {
            txtDocumento.getStyleClass().add("txtRed");
            result = false;
        } else if (txtDocumento.getText().length() < 8) {
            txtDocumento.getStyleClass().add("txtRed");
            result = false;
        } else {
            txtDocumento.getStyleClass().remove("txtRed");
            result = true;
        }

        return result;
    }

    @FXML
    private void Buscar(ActionEvent event) throws Exception {
        Boolean proccess = true;

        if (ConstantsSeleccion.N__SELECTED_REPORT != null) {

            if (ConstantsSeleccion.N__SELECTED_REPORT.getCodigo().equals(ConstantsSeleccion.N_RPT_LISTA_PADRON_ELECTORAL)
                    || ConstantsSeleccion.N__SELECTED_REPORT.getCodigo().equals(ConstantsSeleccion.N_RPT_LISTA_PADRON_X_PESOS)) {

                if (txtMesa.getText().equals("") && ConstantsSeleccion.N__SELECTED_REPORT.getUbigeo().equals("0")) {
                    final OnpeAnimations messages = OnpeAnimations.getInstance();
                    messages.setApMain(AppController.wEva.apMain);
                    messages.openPopup(ConstantsSeleccion.MSG_INFORMATION_DPTO_MESA, OnpeMessagesTypes.TYPE_INFORMATION);
                    return;

                }
            }

            if (ConstantsSeleccion.N_RPT_LISTA_PADRON_ELECTORAL.equals(ConstantsSeleccion.N__SELECTED_REPORT.getCodigo())
                    || ConstantsSeleccion.N_RPT_LISTA_PADRON_X_PESOS.equals(ConstantsSeleccion.N__SELECTED_REPORT.getCodigo())
                    || ConstantsSeleccion.N_RPT_LISTA_25_SELECCIONADOS.equals(ConstantsSeleccion.N__SELECTED_REPORT.getCodigo())
                    || ConstantsSeleccion.N_RPT_LISTA_CIUDADANOS_EXCLUIDOS.equals(ConstantsSeleccion.N__SELECTED_REPORT.getCodigo())
                    || ConstantsSeleccion.N_RPT_LISTA_CIUDADANOS_MULTI_EXCLUIDOS.equals(ConstantsSeleccion.N__SELECTED_REPORT.getCodigo())
                    || ConstantsSeleccion.N_RPT_LISTA_CIUDADANOS_POR_MOTIVO_EXCLUSION.equals(ConstantsSeleccion.N__SELECTED_REPORT.getCodigo())
                    || ConstantsSeleccion.N_RPT_FRECUENCIA_MM_POR_CIUDADANO.equals(ConstantsSeleccion.N__SELECTED_REPORT.getCodigo())) {

                proccess = validateEdades();
            }

            if (ConstantsSeleccion.N_RPT_FRECUENCIA_MM_POR_CIUDADANO.equals(ConstantsSeleccion.N__SELECTED_REPORT.getCodigo())) {

                proccess = validateDocument();
            }

            if (proccess) {
                btnPrint = (Button) apFooter.lookup("#btnPrint");
                btnExport = (Button) apFooter.lookup("#btnExport");

                reportLoading();

                if (!cbListMotivo.isVisible()) {
                    ConstantsSeleccion.N__SELECTED_REPORT.setMotivo(0);
                }

                if (!bDiscapacidad) {
                    ConstantsSeleccion.N__SELECTED_REPORT.setDiscapacidad(-1);
                }

                if (cbListMotivo.getSelectionModel().selectedItemProperty().get().getDescription().equals(ConstantsSeleccion.CB_REPORT_OPTION_ALL)) {
                    ConstantsSeleccion.N__SELECTED_REPORT.setMotivo(0);
                }

                if (cbListAmbito.getSelectionModel().selectedItemProperty().get().getOdpe().equals(ConstantsSeleccion.CB_REPORT_OPTION_ALL)) {
                    ConstantsSeleccion.N__SELECTED_REPORT.setAmbito(0);
                }

                if (!"".equals(txtDocumento.getText())) {
                    ConstantsSeleccion.N__SELECTED_REPORT.setDocumento(txtDocumento.getText());
                }

                if (!"".equals(txtMesa.getText())) {
                    ConstantsSeleccion.N__SELECTED_REPORT.setMesa(txtMesa.getText());
                } else {
                    ConstantsSeleccion.N__SELECTED_REPORT.setMesa("0");
                }

                Task tSearch;
                tSearch = new Task() {

                    @Override
                    protected Object call() throws Exception {
                        searchReport();
                        return null;
                    }
                };

                timer3 = new Timer();

                TimerTask timerTask = new TimerTask() {
                    public void run() {
                        Platform.runLater(new Runnable() {
                            public void run() {
                                try {
                                    verifyComplete();
                                } catch (Exception e) {
                                    timer3.cancel();
                                    timer3.purge();
                                }
                            }
                        });
                    }
                };

                timer3.scheduleAtFixedRate(timerTask, 0, 2000);

                new Thread(tSearch).start();
            }
        }
    }

    private void verifyComplete() {
        if (searchComplete) {
            makeReport();
            searchComplete = false;

            timer3.cancel();
            timer3.purge();
        }
    }

    private void makeReport() {
        if (ConstantsSeleccion.lstReport != null) {
            Integer sizeList = ConstantsSeleccion.lstReport.size();
            Integer limit = ConstantsSeleccion.N_LIMIT_REPORT_VIEW;
            lblMensajeVistaPrevia.setVisible(false);
            if (limit < sizeList) {
                lblMensajeVistaPrevia.setVisible(true);
            }
            if (sizeList < ConstantsSeleccion.N_LIMIT_REPORT_VIEW) {
                limit = sizeList;
            }

            Locale locale = new Locale("PE", "PE");
            NumberFormat numberFormat = NumberFormat.getInstance(locale);

            if (ConstantsSeleccion.N_RPT_LISTA_PADRON_ELECTORAL.equals(ConstantsSeleccion.N__SELECTED_REPORT.getCodigo())
                    || ConstantsSeleccion.N_RPT_LISTA_PADRON_X_PESOS.equals(ConstantsSeleccion.N__SELECTED_REPORT.getCodigo())
                    || ConstantsSeleccion.N_RPT_LISTA_25_SELECCIONADOS.equals(ConstantsSeleccion.N__SELECTED_REPORT.getCodigo())
                    || ConstantsSeleccion.N_RPT_LISTA_CIUDADANOS_EXCLUIDOS.equals(ConstantsSeleccion.N__SELECTED_REPORT.getCodigo())
                    || ConstantsSeleccion.N_RPT_LISTA_CIUDADANOS_MULTI_EXCLUIDOS.equals(ConstantsSeleccion.N__SELECTED_REPORT.getCodigo())
                    || ConstantsSeleccion.N_RPT_LISTA_CIUDADANOS_POR_MOTIVO_EXCLUSION.equals(ConstantsSeleccion.N__SELECTED_REPORT.getCodigo())
                    || ConstantsSeleccion.N_RPT_FRECUENCIA_MM_POR_CIUDADANO.equals(ConstantsSeleccion.N__SELECTED_REPORT.getCodigo())) {

                gridReportePadron.getRowConstraints().clear();
                gridReportePadron.getChildren().clear();

                ArrayList<PesoPadron> lstPadron = (ArrayList<PesoPadron>) ConstantsSeleccion.lstReport;

                if (lstPadron.size() > 0) {
                    if (ConstantsSeleccion.N_RPT_LISTA_PADRON_ELECTORAL.equals(ConstantsSeleccion.N__SELECTED_REPORT.getCodigo())) {

                        pane4.setVisible(true);

                        for (int i = 0; i < limit; i++) {
                            Label mesa = new Label(lstPadron.get(i).getMesa());
                            mesa.getStyleClass().add("lblGrid200");

                            Label dni = new Label(lstPadron.get(i).getDNI());
                            dni.getStyleClass().add("lblGrid200");

                            Label edad = new Label(String.valueOf(lstPadron.get(i).getEdad()));
                            edad.getStyleClass().add("lblGrid100");

                            Label dis = new Label(lstPadron.get(i).getDiscapacitado());
                            dis.getStyleClass().add("lblGrid100");

                            Label grado = new Label(lstPadron.get(i).getGradoInstruccion());
                            grado.getStyleClass().add("lblGrid200");

                            Label mm = new Label(lstPadron.get(i).getFrecuenciaMesa());
                            mm.getStyleClass().add("lblGrid100");

                            Label exc = new Label(lstPadron.get(i).getExcluido());
                            exc.getStyleClass().add("lblGrid100");

                            if (i % 2 == 0) {
                                mesa.getStyleClass().add("lblGridZebra");
                                dni.getStyleClass().add("lblGridZebra");
                                edad.getStyleClass().add("lblGridZebra");
                                dis.getStyleClass().add("lblGridZebra");
                                grado.getStyleClass().add("lblGridZebra");
                                mm.getStyleClass().add("lblGridZebra");
                                exc.getStyleClass().add("lblGridZebra");
                            }

                            mesa.getStyleClass().add("label200");
                            dni.getStyleClass().add("label200");
                            edad.getStyleClass().add("label100");
                            dis.getStyleClass().add("label100");
                            grado.getStyleClass().add("label290");
                            mm.getStyleClass().add("label200");
                            exc.getStyleClass().add("label200");

                            gridReportePadron.addRow(i + 1, mesa);
                            gridReportePadron.addRow(i + 1, dni);
                            gridReportePadron.addRow(i + 1, edad);
                            gridReportePadron.addRow(i + 1, dis);
                            gridReportePadron.addRow(i + 1, grado);
                            gridReportePadron.addRow(i + 1, mm);
                            gridReportePadron.addRow(i + 1, exc);
                        }

                    } else if (ConstantsSeleccion.N_RPT_LISTA_PADRON_X_PESOS.equals(ConstantsSeleccion.N__SELECTED_REPORT.getCodigo())) {

                        pane5.setVisible(true);
                        for (int i = 0; i < limit; i++) {
                            Label mesa = new Label(lstPadron.get(i).getMesa());
                            mesa.getStyleClass().add("lblGrid100");

                            Label exc = new Label(lstPadron.get(i).getExcluido());
                            exc.getStyleClass().add("lblGrid50");

                            Label bolo = new Label(lstPadron.get(i).getOrden());
                            bolo.getStyleClass().add("lblGrid50");

                            Label dni = new Label(lstPadron.get(i).getDNI());
                            dni.getStyleClass().add("lblGrid200");

                            Label grado = new Label(lstPadron.get(i).getGradoInstruccion());
                            grado.getStyleClass().add("lblGrid200");

                            Label edad = new Label(String.valueOf(lstPadron.get(i).getEdad()));
                            edad.getStyleClass().add("lblGrid50");

                            Label dis = new Label(lstPadron.get(i).getDiscapacitado());
                            dis.getStyleClass().add("lblGrid50");

                            Label mm = new Label(lstPadron.get(i).getFrecuenciaMesa());
                            mm.getStyleClass().add("lblGrid50");

                            Label n1 = new Label(String.valueOf(lstPadron.get(i).getPesoN1()));
                            n1.getStyleClass().add("lblGrid50");

                            Label n2 = new Label(String.valueOf(lstPadron.get(i).getPesoN2()));
                            n2.getStyleClass().add("lblGrid50");

                            Label n3 = new Label(String.valueOf(lstPadron.get(i).getPesoN3()));
                            n3.getStyleClass().add("lblGrid50");

                            Label n4 = new Label(String.valueOf(lstPadron.get(i).getPesoTotal()));
                            n4.getStyleClass().add("lblGrid100");

                            if (i % 2 == 0) {
                                mesa.getStyleClass().add("lblGridZebra");
                                exc.getStyleClass().add("lblGridZebra");
                                bolo.getStyleClass().add("lblGridZebra");
                                dni.getStyleClass().add("lblGridZebra");
                                grado.getStyleClass().add("lblGridZebra");
                                edad.getStyleClass().add("lblGridZebra");
                                dis.getStyleClass().add("lblGridZebra");
                                mm.getStyleClass().add("lblGridZebra");
                                n1.getStyleClass().add("lblGridZebra");
                                n2.getStyleClass().add("lblGridZebra");
                                n3.getStyleClass().add("lblGridZebra");
                                n4.getStyleClass().add("lblGridZebra");
                            }

                            mesa.getStyleClass().add("label100");
                            exc.getStyleClass().add("label50");
                            bolo.getStyleClass().add("label50");
                            dni.getStyleClass().add("label200");
                            grado.getStyleClass().add("label200");
                            edad.getStyleClass().add("label50");
                            dis.getStyleClass().add("label90");
                            mm.getStyleClass().add("label100");
                            n1.getStyleClass().add("label100");
                            n2.getStyleClass().add("label100");
                            n3.getStyleClass().add("label100");
                            n4.getStyleClass().add("label150");

                            gridReportePadron.addRow(i + 1, mesa);
                            gridReportePadron.addRow(i + 1, exc);
                            gridReportePadron.addRow(i + 1, bolo);
                            gridReportePadron.addRow(i + 1, dni);
                            gridReportePadron.addRow(i + 1, grado);
                            gridReportePadron.addRow(i + 1, edad);
                            gridReportePadron.addRow(i + 1, dis);
                            gridReportePadron.addRow(i + 1, mm);
                            gridReportePadron.addRow(i + 1, n1);
                            gridReportePadron.addRow(i + 1, n2);
                            gridReportePadron.addRow(i + 1, n3);
                            gridReportePadron.addRow(i + 1, n4);

                        }

                    } else if (ConstantsSeleccion.N_RPT_LISTA_25_SELECCIONADOS.equals(ConstantsSeleccion.N__SELECTED_REPORT.getCodigo())) {

                        pane1.setVisible(true);
                        for (int i = 0; i < limit; i++) {
                            Label mesa = new Label(lstPadron.get(i).getMesa());
                            mesa.getStyleClass().add("lblGrid200");

                            Label bolo = new Label(lstPadron.get(i).getOrden());
                            bolo.getStyleClass().add("lblGrid100");

                            Label dni = new Label(lstPadron.get(i).getDNI());
                            dni.getStyleClass().add("lblGrid200");

                            Label edad = new Label(String.valueOf(lstPadron.get(i).getEdad()));
                            edad.getStyleClass().add("lblGrid100");

                            Label grado = new Label(String.valueOf(lstPadron.get(i).getGradoInstruccion()));
                            grado.getStyleClass().add("lblGrid300");

                            Label mm = new Label(lstPadron.get(i).getFrecuenciaMesa());
                            mm.getStyleClass().add("lblGrid100");

                            if (i % 2 == 0) {
                                mesa.getStyleClass().add("lblGridZebra");
                                bolo.getStyleClass().add("lblGridZebra");
                                dni.getStyleClass().add("lblGridZebra");
                                grado.getStyleClass().add("lblGridZebra");
                                edad.getStyleClass().add("lblGridZebra");
                                mm.getStyleClass().add("lblGridZebra");
                            }

                            mesa.getStyleClass().add("label200");
                            bolo.getStyleClass().add("label200");
                            dni.getStyleClass().add("label200");
                            edad.getStyleClass().add("label200");
                            grado.getStyleClass().add("label250");
                            mm.getStyleClass().add("label240");

                            gridReportePadron.addRow(i + 1, mesa);
                            gridReportePadron.addRow(i + 1, bolo);
                            gridReportePadron.addRow(i + 1, dni);
                            gridReportePadron.addRow(i + 1, edad);
                            gridReportePadron.addRow(i + 1, grado);
                            gridReportePadron.addRow(i + 1, mm);

                        }

                    } else if (ConstantsSeleccion.N_RPT_LISTA_CIUDADANOS_EXCLUIDOS.equals(ConstantsSeleccion.N__SELECTED_REPORT.getCodigo())) {

                        pane2.setVisible(true);
                        for (int i = 0; i < limit; i++) {
                            Label mesa = new Label(lstPadron.get(i).getMesa());
                            mesa.getStyleClass().add("lblGrid500");

                            Label dni = new Label(lstPadron.get(i).getDNI());
                            dni.getStyleClass().add("lblGrid500");

                            Label mm = new Label(lstPadron.get(i).getFrecuenciaMesa());
                            mm.getStyleClass().add("lblGrid500");

                            Label motivo = new Label(lstPadron.get(i).getMotivoExclusion());
                            motivo.getStyleClass().add("lblGrid500");

                            if (i % 2 == 0) {
                                mesa.getStyleClass().add("lblGridZebra");
                                dni.getStyleClass().add("lblGridZebra");
                                mm.getStyleClass().add("lblGridZebra");
                                motivo.getStyleClass().add("lblGridZebra");
                            }

                            mesa.getStyleClass().add("label250");
                            dni.getStyleClass().add("label250");
                            mm.getStyleClass().add("label200");
                            motivo.getStyleClass().add("label590");

                            gridReportePadron.addRow(i + 1, mesa);
                            gridReportePadron.addRow(i + 1, dni);
                            gridReportePadron.addRow(i + 1, mm);
                            gridReportePadron.addRow(i + 1, motivo);

                        }

                    } else if (ConstantsSeleccion.N_RPT_LISTA_CIUDADANOS_MULTI_EXCLUIDOS.equals(ConstantsSeleccion.N__SELECTED_REPORT.getCodigo())) {

                        pane6.setVisible(true);
                        for (int i = 0; i < limit; i++) {
                            Label mesa = new Label(lstPadron.get(i).getMesa());
                            mesa.getStyleClass().add("lblGrid200");

                            Label dni = new Label(lstPadron.get(i).getDNI());
                            dni.getStyleClass().add("lblGrid200");

                            Label nombres = new Label(lstPadron.get(i).getaPaterno() + " " + lstPadron.get(i).getaMaterno() + ", " + lstPadron.get(i).getNombres());
                            nombres.getStyleClass().add("lblGrid500");

                            Label cant = new Label(String.valueOf(lstPadron.get(i).getVecesExcluido()));
                            cant.getStyleClass().add("lblGrid100");

                            if (i % 2 == 0) {
                                mesa.getStyleClass().add("lblGridZebra");
                                dni.getStyleClass().add("lblGridZebra");
                                nombres.getStyleClass().add("lblGridZebra");
                                cant.getStyleClass().add("lblGridZebra");
                            }

                            mesa.getStyleClass().add("label250");
                            dni.getStyleClass().add("label250");
                            nombres.getStyleClass().add("label600");
                            cant.getStyleClass().add("label200");

                            gridReportePadron.addRow(i + 1, mesa);
                            gridReportePadron.addRow(i + 1, dni);
                            gridReportePadron.addRow(i + 1, nombres);
                            gridReportePadron.addRow(i + 1, cant);

                        }

                    } else if (ConstantsSeleccion.N_RPT_LISTA_CIUDADANOS_POR_MOTIVO_EXCLUSION.equals(ConstantsSeleccion.N__SELECTED_REPORT.getCodigo())) {

                        pane3.setVisible(true);
                        for (int i = 0; i < limit; i++) {
                            Label mesa = new Label(lstPadron.get(i).getMesa());
                            mesa.getStyleClass().add("lblGrid200");

                            Label dni = new Label(lstPadron.get(i).getDNI());
                            dni.getStyleClass().add("lblGrid200");

                            Label nombres = new Label(lstPadron.get(i).getaPaterno() + " " + lstPadron.get(i).getaMaterno() + ", " + lstPadron.get(i).getNombres());
                            nombres.getStyleClass().add("lblGrid600");

                            if (i % 2 == 0) {
                                mesa.getStyleClass().add("lblGridZebra");
                                dni.getStyleClass().add("lblGridZebra");
                                nombres.getStyleClass().add("lblGridZebra");
                            }

                            mesa.getStyleClass().add("label250");
                            dni.getStyleClass().add("label250");
                            nombres.getStyleClass().add("label790");

                            gridReportePadron.addRow(i + 1, mesa);
                            gridReportePadron.addRow(i + 1, dni);
                            gridReportePadron.addRow(i + 1, nombres);
                        }
                    } else if (ConstantsSeleccion.N_RPT_FRECUENCIA_MM_POR_CIUDADANO.equals(ConstantsSeleccion.N__SELECTED_REPORT.getCodigo())) {

                        pane13.setVisible(true);
                        for (int i = 0; i < limit; i++) {
                            Label mesa = new Label(lstPadron.get(i).getMesa());
                            mesa.getStyleClass().add("lblGrid100");

                            Label dni = new Label(lstPadron.get(i).getDNI());
                            dni.getStyleClass().add("lblGrid200");

                            Label descrip = new Label(lstPadron.get(i).getMotivoExclusion());
                            descrip.getStyleClass().add("lblGrid700");

                            if (i % 2 == 0) {
                                mesa.getStyleClass().add("lblGridZebra");
                                dni.getStyleClass().add("lblGridZebra");
                                descrip.getStyleClass().add("lblGridZebra");
                            }

                            mesa.getStyleClass().add("label240");
                            dni.getStyleClass().add("label250");
                            descrip.getStyleClass().add("label800");

                            gridReportePadron.addRow(i + 1, mesa);
                            gridReportePadron.addRow(i + 1, dni);
                            gridReportePadron.addRow(i + 1, descrip);
                        }

                    }

                    reportLoaded(true);
                } else {
                    reportLoaded(false);
                }
            }

            /**/
            if (ConstantsSeleccion.N_RPT_PESOS_X_RANGO_EDAD_N1.equals(ConstantsSeleccion.N__SELECTED_REPORT.getCodigo())
                    || ConstantsSeleccion.N_RPT_GRADO_INSTUCCION_PADRON_ELECTORAL.equals(ConstantsSeleccion.N__SELECTED_REPORT.getCodigo())
                    || ConstantsSeleccion.N_RPT_PESOS_X_GRADO_INSTRUCCION_N1.equals(ConstantsSeleccion.N__SELECTED_REPORT.getCodigo())
                    || ConstantsSeleccion.N_RPT_PESOS_X_DISCAPACIDAD_N1.equals(ConstantsSeleccion.N__SELECTED_REPORT.getCodigo())
                    || ConstantsSeleccion.N_RPT_PESOS_X_RANGO_EDAD_N3.equals(ConstantsSeleccion.N__SELECTED_REPORT.getCodigo())
                    || ConstantsSeleccion.N_RPT_GRADO_INSTRUCCION_25_SELECCIONADOS.equals(ConstantsSeleccion.N__SELECTED_REPORT.getCodigo())
                    || ConstantsSeleccion.N_RPT_PESOS_X_FRECUENCIA_MM.equals(ConstantsSeleccion.N__SELECTED_REPORT.getCodigo())) {

                ArrayList<CountGrado> lstCountGrado = (ArrayList<CountGrado>) ConstantsSeleccion.lstReport;
                gridReportePadron.getRowConstraints().clear();
                gridReportePadron.getChildren().clear();

                if (lstCountGrado.size() > 0) {
                    if (ConstantsSeleccion.N_RPT_PESOS_X_RANGO_EDAD_N1.equals(ConstantsSeleccion.N__SELECTED_REPORT.getCodigo())) {

                        pane11.setVisible(true);
                        for (int i = 0; i < limit; i++) {
                            Label descripcion = new Label(lstCountGrado.get(i).getRangoEdad());
                            descripcion.getStyleClass().add("lblGrid800");

                            Label peso = new Label(String.valueOf(lstCountGrado.get(i).getPeso()));
                            peso.getStyleClass().add("lblGrid200");

                            if (i % 2 == 0) {
                                descripcion.getStyleClass().add("lblGridZebra");
                                peso.getStyleClass().add("lblGridZebra");
                            }

                            descripcion.getStyleClass().add("label890");
                            peso.getStyleClass().add("label400");

                            gridReportePadron.addRow(i + 1, descripcion);
                            gridReportePadron.addRow(i + 1, peso);

                        }

                    } else if (ConstantsSeleccion.N_RPT_GRADO_INSTUCCION_PADRON_ELECTORAL.equals(ConstantsSeleccion.N__SELECTED_REPORT.getCodigo())) {

                        pane12.setVisible(true);
                        for (int i = 0; i < limit; i++) {
                            Label descripcion = new Label(lstCountGrado.get(i).getGradoInstruccion());
                            descripcion.getStyleClass().add("lblGrid800");

                            Integer num = lstCountGrado.get(i).getTotal();

                            Label peso = new Label(String.valueOf(numberFormat.format(num)));
                            peso.getStyleClass().add("lblGrid200");

                            if (i % 2 == 0) {
                                descripcion.getStyleClass().add("lblGridZebra");
                                peso.getStyleClass().add("lblGridZebra");
                            }

                            descripcion.getStyleClass().add("label890");
                            peso.getStyleClass().add("label400");

                            gridReportePadron.addRow(i + 1, descripcion);
                            gridReportePadron.addRow(i + 1, peso);

                        }

                    } else if (ConstantsSeleccion.N_RPT_PESOS_X_GRADO_INSTRUCCION_N1.equals(ConstantsSeleccion.N__SELECTED_REPORT.getCodigo())) {

                        pane9.setVisible(true);
                        for (int i = 0; i < limit; i++) {
                            Label descripcion = new Label(lstCountGrado.get(i).getGradoInstruccion());
                            descripcion.getStyleClass().add("lblGrid800");

                            Label peso = new Label(String.valueOf(lstCountGrado.get(i).getPeso()));
                            peso.getStyleClass().add("lblGrid200");

                            if (i % 2 == 0) {
                                descripcion.getStyleClass().add("lblGridZebra");
                                peso.getStyleClass().add("lblGridZebra");
                            }

                            descripcion.getStyleClass().add("label890");
                            peso.getStyleClass().add("label400");

                            gridReportePadron.addRow(i + 1, descripcion);
                            gridReportePadron.addRow(i + 1, peso);
                        }

                    } else if (ConstantsSeleccion.N_RPT_PESOS_X_DISCAPACIDAD_N1.equals(ConstantsSeleccion.N__SELECTED_REPORT.getCodigo())) {

                        pane8.setVisible(true);
                        for (int i = 0; i < limit; i++) {
                            Label descripcion = new Label(String.valueOf(lstCountGrado.get(i).getDescripcion()));
                            descripcion.getStyleClass().add("lblGrid800");

                            Label peso = new Label(String.valueOf(lstCountGrado.get(i).getPeso()));
                            peso.getStyleClass().add("lblGrid200");

                            if (i % 2 == 0) {
                                descripcion.getStyleClass().add("lblGridZebra");
                                peso.getStyleClass().add("lblGridZebra");
                            }

                            descripcion.getStyleClass().add("label890");
                            peso.getStyleClass().add("label400");

                            gridReportePadron.addRow(i + 1, descripcion);
                            gridReportePadron.addRow(i + 1, peso);

                        }

                    } else if (ConstantsSeleccion.N_RPT_PESOS_X_RANGO_EDAD_N3.equals(ConstantsSeleccion.N__SELECTED_REPORT.getCodigo())) {

                        pane11.setVisible(true);
                        for (int i = 0; i < limit; i++) {
                            Label descripcion = new Label(lstCountGrado.get(i).getRangoEdad());
                            descripcion.getStyleClass().add("lblGrid800");

                            Label peso = new Label(String.valueOf(lstCountGrado.get(i).getPeso()));
                            peso.getStyleClass().add("lblGrid200");

                            if (i % 2 == 0) {
                                descripcion.getStyleClass().add("lblGridZebra");
                                peso.getStyleClass().add("lblGridZebra");
                            }

                            descripcion.getStyleClass().add("label890");
                            peso.getStyleClass().add("label400");

                            gridReportePadron.addRow(i + 1, descripcion);
                            gridReportePadron.addRow(i + 1, peso);

                        }

                    } else if (ConstantsSeleccion.N_RPT_GRADO_INSTRUCCION_25_SELECCIONADOS.equals(ConstantsSeleccion.N__SELECTED_REPORT.getCodigo())) {

                        pane12.setVisible(true);
                        for (int i = 0; i < limit; i++) {
                            Label descripcion = new Label(lstCountGrado.get(i).getGradoInstruccion());
                            descripcion.getStyleClass().add("lblGrid800");

                            Integer num = lstCountGrado.get(i).getTotal();

                            Label peso = new Label(String.valueOf(numberFormat.format(num)));
                            peso.getStyleClass().add("lblGrid200");

                            if (i % 2 == 0) {
                                descripcion.getStyleClass().add("lblGridZebra");
                                peso.getStyleClass().add("lblGridZebra");
                            }

                            descripcion.getStyleClass().add("label890");
                            peso.getStyleClass().add("label400");

                            gridReportePadron.addRow(i + 1, descripcion);
                            gridReportePadron.addRow(i + 1, peso);

                        }

                    } else if (ConstantsSeleccion.N_RPT_PESOS_X_FRECUENCIA_MM.equals(ConstantsSeleccion.N__SELECTED_REPORT.getCodigo())) {

                        pane10.setVisible(true);
                        for (int i = 0; i < limit; i++) {

                            Label descripcion = new Label(String.valueOf(lstCountGrado.get(i).getFrecuencia()));
                            descripcion.getStyleClass().add("lblGrid800");

                            Label peso = new Label(String.valueOf(lstCountGrado.get(i).getPeso()));
                            peso.getStyleClass().add("lblGrid200");

                            if (i % 2 == 0) {
                                descripcion.getStyleClass().add("lblGridZebra");
                                peso.getStyleClass().add("lblGridZebra");
                            }

                            descripcion.getStyleClass().add("label890");
                            peso.getStyleClass().add("label400");

                            gridReportePadron.addRow(i + 1, descripcion);
                            gridReportePadron.addRow(i + 1, peso);

                        }
                    }

                    reportLoaded(true);
                } else {
                    reportLoaded(false);
                }
            }

            /**/
            if (ConstantsSeleccion.N_RPT_MOTIVOS_EXCLUSION.equals(ConstantsSeleccion.N__SELECTED_REPORT.getCodigo())) {

                ArrayList<Exclusion> lstExcl = (ArrayList<Exclusion>) ConstantsSeleccion.lstReport;
//            apReporte.setVisible(true);
//            gridReportePadron.setVisible(true);
//            scrollPadron.setVisible(true);
                gridReportePadron.getRowConstraints().clear();
                gridReportePadron.getChildren().clear();

                if (lstExcl.size() > 0) {
                    if (ConstantsSeleccion.N_RPT_MOTIVOS_EXCLUSION.equals(ConstantsSeleccion.N__SELECTED_REPORT.getCodigo())) {
                        pane7.setVisible(true);

                        for (int i = 0; i < limit; i++) {
                            Label nro = new Label(String.valueOf(lstExcl.get(i).getNro()));
                            nro.getStyleClass().add("lblGrid100");

                            Label motivo = new Label(lstExcl.get(i).getMotivoExclusion().trim());
                            motivo.getStyleClass().add("lblGrid500");

                            Label doc = new Label(lstExcl.get(i).getDocumento());
                            doc.getStyleClass().add("lblGrid200");

                            Integer num1 = lstExcl.get(i).getPorExcluir();
                            Label xexc = new Label(String.valueOf(numberFormat.format(num1)));
                            //Label xexc = new Label(String.valueOf(lstExcl.get(i).getPorExcluir()));
                            xexc.getStyleClass().add("lblGrid100");

                            Integer num2 = lstExcl.get(i).getExclusiones();
                            Label exc = new Label(String.valueOf(numberFormat.format(num2)));
                            //Label exc = new Label(String.valueOf(lstExcl.get(i).getExclusiones()));
                            exc.getStyleClass().add("lblGrid100");

                            if (i % 2 == 0) {
                                nro.getStyleClass().add("lblGridZebra");
                                motivo.getStyleClass().add("lblGridZebra");
                                doc.getStyleClass().add("lblGridZebra");
                                xexc.getStyleClass().add("lblGridZebra");
                                exc.getStyleClass().add("lblGridZebra");
                            }

                            nro.getStyleClass().add("label100");
                            motivo.getStyleClass().add("label540");
                            doc.getStyleClass().add("label250");
                            xexc.getStyleClass().add("label200");
                            exc.getStyleClass().add("label200");

                            gridReportePadron.addRow(i + 1, nro);
                            gridReportePadron.addRow(i + 1, motivo);
                            gridReportePadron.addRow(i + 1, doc);
                            gridReportePadron.addRow(i + 1, xexc);
                            gridReportePadron.addRow(i + 1, exc);

                        }
                    }

                    reportLoaded(true);
                } else {
                    reportLoaded(false);
                }
            }

            /**/
            if (ConstantsSeleccion.N_RPT_MESAS_ELECTORES_POR_UBIGEO.equals(ConstantsSeleccion.N__SELECTED_REPORT.getCodigo())) {

                ArrayList<ODPESel> lstODPESel = (ArrayList<ODPESel>) ConstantsSeleccion.lstReport;
//            apReporte.setVisible(true);
//            gridReportePadron.setVisible(true);
//            scrollPadron.setVisible(true);
                gridReportePadron.getRowConstraints().clear();
                gridReportePadron.getChildren().clear();

                if (lstODPESel.size() > 0) {
                    if (ConstantsSeleccion.N_RPT_MESAS_ELECTORES_POR_UBIGEO.equals(ConstantsSeleccion.N__SELECTED_REPORT.getCodigo())) {
                        pane14.setVisible(true);

                        for (int i = 0; i < limit; i++) {
                            Label odpe = new Label(lstODPESel.get(i).getOdpe());
                            odpe.getStyleClass().add("lblGrid200");

                            Label dpto = new Label(lstODPESel.get(i).getDepartamento());
                            dpto.getStyleClass().add("lblGrid200");

                            Label prov = new Label(lstODPESel.get(i).getProvincia());
                            prov.getStyleClass().add("lblGrid200");

                            Label dist = new Label(lstODPESel.get(i).getDistrito());
                            dist.getStyleClass().add("lblGrid200");

                            Label mesa = new Label(String.valueOf(lstODPESel.get(i).getCantidadMesas()));
                            mesa.getStyleClass().add("lblGrid100");

                            Integer numElec = lstODPESel.get(i).getHabiles();

                            Label elec = new Label(String.valueOf(numberFormat.format(numElec)));
                            elec.getStyleClass().add("lblGrid100");

                            if (i % 2 == 0) {
                                odpe.getStyleClass().add("lblGridZebra");
                                dpto.getStyleClass().add("lblGridZebra");
                                prov.getStyleClass().add("lblGridZebra");
                                dist.getStyleClass().add("lblGridZebra");
                                mesa.getStyleClass().add("lblGridZebra");
                                elec.getStyleClass().add("lblGridZebra");
                            }

                            odpe.getStyleClass().add("label200");
                            dpto.getStyleClass().add("label200");
                            prov.getStyleClass().add("label240");
                            dist.getStyleClass().add("label250");
                            mesa.getStyleClass().add("label200");
                            elec.getStyleClass().add("label200");

                            gridReportePadron.addRow(i + 1, odpe);
                            gridReportePadron.addRow(i + 1, dpto);
                            gridReportePadron.addRow(i + 1, prov);
                            gridReportePadron.addRow(i + 1, dist);
                            gridReportePadron.addRow(i + 1, mesa);
                            gridReportePadron.addRow(i + 1, elec);

                        }
                    }

                    reportLoaded(true);
                } else {
                    reportLoaded(false);
                }
            }
        } else {
            reportLoaded(false);
        }
    }

    private void reportLoading() {
        btnSearch.setDisable(true);

        cbListReport.setDisable(true);
        fadeIn(piLoading, speedAnim, null);
        fadeOut(lblVacio, speedAnim, null);
        fadeOut(btnPrint, speedAnim, null);
        fadeOut(btnExport, speedAnim, null);
        fadeOut(lblVacio, speedAnim, null);
        fadeOut(btnSearch, speedAnim, null);
        fadeOut(apReporte, speedAnim, null);
    }

    private void reportLoaded(Boolean withData) {
        cbListReport.setDisable(false);
        fadeOut(piLoading, speedAnim, null);
        fadeIn(btnSearch, speedAnim, null);

        if (withData) {
            fadeIn(btnPrint, speedAnim, null);
            fadeIn(btnExport, speedAnim, null);
            fadeIn(apReporte, speedAnim, null);
            fadeIn(gridReportePadron, speedAnim, null);
            fadeIn(scrollPadron, speedAnim, null);
        } else {
            cleanPanesAll();
            fadeIn(lblVacio, speedAnim, null);
        }
        btnSearch.setDisable(false);

    }

    private void searchReport() {
        //ConstantsSeleccion.cacheRowSet = oSeleccionService.getSeleccionService().generarLstReporteResult(ConstantsSeleccion.N__SELECTED_REPORT);
        ConstantsSeleccion.lstReport = oSeleccionService.getSeleccionService().generarLstReporte(ConstantsSeleccion.N__SELECTED_REPORT);
        searchComplete = true;
    }

    private void cleanControlsBox() {

        cbListAmbito.getSelectionModel().selectFirst();
        cbListDpto.getSelectionModel().selectFirst();
        cbListProv.getItems().clear();
        cbListDist.getItems().clear();

        cbTipoGrado.getSelectionModel().selectFirst();
        cbGrado.getItems().clear();

        cbListMotivo.getSelectionModel().selectFirst();

        cbDiscapacidad.getSelectionModel().selectFirst();

        txtDesde.setText("");
        txtHasta.setText("");
        txtDocumento.setText("");
        txtMesa.setText("");

        pane1.setVisible(false);
        pane2.setVisible(false);
        pane3.setVisible(false);
        pane4.setVisible(false);
        pane5.setVisible(false);
        pane6.setVisible(false);
        gridReportePadron.setVisible(false);
        gridReportePadron.getChildren().clear();

        apBusqueda.setVisible(false);
        apReporte.setVisible(false);
        lblVacio.setVisible(false);
    }

    @FXML
    private void hideControls(boolean bVisible) {

        lblDpto.setVisible(bVisible);
        lblProv.setVisible(bVisible);
        lblDist.setVisible(bVisible);
        lblTipoGrado.setVisible(bVisible);
        lblGrado.setVisible(bVisible);
        lblRangoEdad.setVisible(bVisible);
        lblDesde.setVisible(bVisible);
        lblHasta.setVisible(bVisible);
        lblDiscapacidad.setVisible(bVisible);
        lblMesa.setVisible(bVisible);

        cbListDpto.setVisible(bVisible);
        cbListProv.setVisible(bVisible);
        cbListDist.setVisible(bVisible);
        cbListAmbito.setVisible(bVisible);
        txtMesa.setVisible(bVisible);
        cbTipoGrado.setVisible(bVisible);
        cbGrado.setVisible(bVisible);
        cbDiscapacidad.setVisible(bVisible);

        txtDesde.setVisible(bVisible);
        txtHasta.setVisible(bVisible);
        txtDocumento.setVisible(bVisible);

        lblMesa.setLayoutX(485.0);
        lblMesa.setLayoutY(86.0);
        txtMesa.setLayoutX(598.0);
        txtMesa.setLayoutY(84.0);

        lblDpto.setLayoutY(26.0);
        lblProv.setLayoutY(26.0);
        lblDist.setLayoutY(26.0);
        lblAmbito.setLayoutY(55.0);

        cbListDpto.setLayoutY(23.0);
        cbListProv.setLayoutY(23.0);
        cbListDist.setLayoutY(23.0);
        cbListAmbito.setLayoutY(54.0);

    }

}
