/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.eva.seleccion.view.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import pe.gob.onpe.animations.OnpeMessagesTypes;
import pe.gob.onpe.animations.view.OnpeAnimations;
import pe.gob.onpe.eva.common.view.AppController;
import pe.gob.onpe.eva.model.Parametro;
import pe.gob.onpe.eva.model.Usuario;
import pe.gob.onpe.eva.seleccion.constant.ConstantsSeleccion;
import pe.gob.onpe.eva.seleccion.service.FactoryService;

/**
 *
 * @author mpazo
 */
public class LocalAppController extends AppController {

    public static InputStream inputStreamPuestaCero = null;

    protected FactoryService oSeleccionService = new FactoryService();
    public static Timer timer = null;
    public static Timer timer1 = null;
    public static Timer timer2 = null;
    public static Timer timer3 = null;

    ResourceBundle rb;

    List<Parametro> lstParamEtiquetas;

    public LocalAppController() {
        lstParamEtiquetas = new ArrayList<>();
        rb = ResourceBundle.getBundle("pe.gob.onpe.eva.seleccion.message.message");
    }

    protected ChangeListener<String> onlySomeCharacters(String restriccion, final TextField texto) {
        final StringProperty someRestrict = new SimpleStringProperty();
        someRestrict.set(restriccion);

        ChangeListener<String> result = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if ((someRestrict.get() != null && !someRestrict.get().equals("") && !newValue.matches(someRestrict.get() + "*"))) {
                    texto.setText(oldValue);
                }
            }
        };
        return result;
    }

    protected ChangeListener<String> onlySomeCharacters(String restriccion, final TextField texto, final int length) {
        final StringProperty someRestrict = new SimpleStringProperty();
        someRestrict.set(restriccion);

        ChangeListener<String> result = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if ((someRestrict.get() != null && !someRestrict.get().equals("") && !newValue.matches(someRestrict.get() + "*")) || newValue.length() > length) {
                    texto.setText(oldValue);
                }
            }
        };
        return result;
    }

    protected ChangeListener<String> onlySomeCharacters(String restriccion, final TextArea texto, final int length) {
        final StringProperty someRestrict = new SimpleStringProperty();
        someRestrict.set(restriccion);

        ChangeListener<String> result = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if ((someRestrict.get() != null && !someRestrict.get().equals("") && !newValue.matches(someRestrict.get() + "*")) || newValue.length() > length) {
                    texto.setText(oldValue);
                }
            }
        };
        return result;
    }

    protected void blokTop() {
        AnchorPane apBlok = (AnchorPane) wEva.apMain.lookup(".top_bloker");
        apBlok.setMouseTransparent(false);
        apBlok.setVisible(true);
        fade(apBlok, 0.5, 500, null);
    }

    protected void unblokTop() {
        AnchorPane apBlok = (AnchorPane) wEva.apMain.lookup(".top_bloker");
        apBlok.setMouseTransparent(true);
        fadeOut(apBlok);
    }

    @FXML
    protected void handleLogout(ActionEvent event) throws IOException, Exception {
        OnpeAnimations onpeAnim = OnpeAnimations.getInstance();
        onpeAnim.openPopup(ConstantsSeleccion.MSG_CLOSE_SESSION, OnpeMessagesTypes.TYPE_CONFIRMATION, OnpeMessagesTypes.OPCION_YES_NO, new EventHandler() {

            @Override
            public void handle(Event event) {
                try {
                    oSeleccionService.getUsuarioService().logout(ConstantsSeleccion.USUARIO_SESSION);
                    ConstantsSeleccion.USUARIO_SESSION = new Usuario();
                    openWindows(ConstantsSeleccion.W_LOGIN);
                } catch (IOException ex) {
                    Logger.getLogger(HeadController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    protected String getMessage(String key) {
        return rb.getString(key);
    }

    protected void getConfiguracion() {
        ConstantsSeleccion.CONFIGURACION.setNameEleccion(oSeleccionService.getSeleccionService().fetchNombreEleccion().toUpperCase());
        ConstantsSeleccion.CONFIGURACION.setAliasEleccion(oSeleccionService.getSeleccionService().fetchAliasEleccion().toUpperCase());
        getEtiquetas();
        getFechas();
        getPaths();
        getNumeroCopias();
    }

    private void getNumeroCopias() {
        Parametro par = new Parametro();
        par.setTipoParametroPk(ConstantsSeleccion.TIPO_PARAMETRO_COPIAS);
        List<Parametro> lstParam = oSeleccionService.getParametroService().fetchAll(par);

        if (!lstParamEtiquetas.isEmpty()) {
            ConstantsSeleccion.CONFIGURACION.setCopias(Integer.parseInt(lstParam.get(0).getValor()));
        }
    }

    private void getEtiquetas() {
        Parametro par = new Parametro();
        par.setTipoParametroPk(ConstantsSeleccion.TIPO_PARAMETRO_ETIQUETAS);
        lstParamEtiquetas = oSeleccionService.getParametroService().fetchAll(par);

        for (Parametro oParam : lstParamEtiquetas) {
            switch (oParam.getCodigo()) {
                case ConstantsSeleccion.COD_ETIQ_ODPE:
                    //ConstantsSeleccion.STR_ETIQ_ODPE = oParam.getValor();
                    ConstantsSeleccion.CONFIGURACION.setEtiquetaOdpe(oParam.getValor());
                    break;
                case ConstantsSeleccion.COD_ETIQ_DPTO:
                    //ConstantsSeleccion.STR_ETIQ_DPTO = oParam.getValor();
                    ConstantsSeleccion.CONFIGURACION.setEtiquetaDpto(oParam.getValor());
                    break;
                case ConstantsSeleccion.COD_ETIQ_PROV:
                    //ConstantsSeleccion.STR_ETIQ_PROV = oParam.getValor();
                    ConstantsSeleccion.CONFIGURACION.setEtiquetaProv(oParam.getValor());
                    break;
                case ConstantsSeleccion.COD_ETIQ_DIST:
                    //ConstantsSeleccion.STR_ETIQ_DIST = oParam.getValor();
                    ConstantsSeleccion.CONFIGURACION.setEtiquetaDist(oParam.getValor());
                    break;
                case ConstantsSeleccion.COD_ETIQ_DPTO_PROV_DIST:
                    //ConstantsSeleccion.STR_ETIQ_DPTO_PROV_DIST = oParam.getValor();
                    ConstantsSeleccion.CONFIGURACION.setEtiquetaDptoProvDist(oParam.getValor());
                    break;
                default:
                    break;
            }
        }
    }

    private void getFechas() {
        Parametro paramFechas = new Parametro();
        paramFechas.setTipoParametroPk(ConstantsSeleccion.TIPO_PARAMETRO_FECHA);
        List<Parametro> lstParamFechas = oSeleccionService.getParametroService().fetchAll(paramFechas);

        for (Parametro lstParamFecha : lstParamFechas) {
            switch (lstParamFecha.getCodigo()) {
                case ConstantsSeleccion.COD_FECHA_SELECCION:
                    ConstantsSeleccion.PARAM_FECHA_SELECCION = lstParamFecha;
                    break;
                case ConstantsSeleccion.COD_FECHA_SORTEO:
                    ConstantsSeleccion.PARAM_FECHA_SORTEO = lstParamFecha;
                    break;
                case ConstantsSeleccion.COD_FECHA_TACHAS:
                    ConstantsSeleccion.PARAM_FECHA_TACHAS = lstParamFecha;
                    break;
                case ConstantsSeleccion.COD_FECHA_EXCUSAS_JUSTIFICACIONES:
                    ConstantsSeleccion.PARAM_FECHA_EXCUSAS = lstParamFecha;
                    break;
                default:
                    break;
            }
        }
    }

    private void getPaths() {
        Parametro paramPaths = new Parametro();
        paramPaths.setTipoParametroPk(ConstantsSeleccion.TIPO_PARAMETRO_PATHS);
        List<Parametro> lstParamPaths = oSeleccionService.getParametroService().fetchAll(paramPaths);

        for (Parametro lstParamPath : lstParamPaths) {
            switch (lstParamPath.getCodigo()) {
                case ConstantsSeleccion.COD_PATH_DB:
                    ConstantsSeleccion.CONFIGURACION.setPathRespaldo(lstParamPath.getDescripcion());
                    break;
                case ConstantsSeleccion.COD_PATH_PDF:
                    ConstantsSeleccion.CONFIGURACION.setPathPDF(lstParamPath.getDescripcion());
                    break;
                default:
                    break;
            }
        }
    }
}
