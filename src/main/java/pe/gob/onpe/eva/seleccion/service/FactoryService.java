/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.eva.seleccion.service;

import pe.gob.onpe.eva.seleccion.service.iface.*;
import pe.gob.onpe.eva.seleccion.service.impl.*;

/**
 *
 * @author aquispec
 */
public class FactoryService {

    public IUsuarioService getUsuarioService() {
        return new UserService();
    }

    public ISeleccionService getSeleccionService() {
        return new SeleccionService();
    }

    public IODPESelService getODPESelService() {
        return new ODPESelService();
    }

    public IPuestaCeroService getPuestaCero() {
        return new PuestaCeroService();
    }

    public IParametroService getParametroService() {
        return new ParameterService();
    }

    public IConfiguracionService getConfiguracionService() {
        return new ConfigurationService();
    }
}
