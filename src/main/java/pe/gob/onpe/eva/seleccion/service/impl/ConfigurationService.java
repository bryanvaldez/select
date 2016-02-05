/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.eva.seleccion.service.impl;

import pe.gob.onpe.eva.dao.FactoryDao;
import pe.gob.onpe.eva.model.mapper.Configuracion;
import pe.gob.onpe.eva.seleccion.service.iface.IConfiguracionService;

/**
 *
 * @author aquispec
 */
public class ConfigurationService implements IConfiguracionService {

    FactoryDao oFactor = new FactoryDao();

    @Override
    public void update(Configuracion oConfiguracion) {
        try {
            // open conection
            oFactor.getConexionDao().conexionOpen();
            // execute command
            oFactor.getConfiguracionDao().update(oConfiguracion);
        } catch (Exception e) {
            throw e;
        } finally {
            // close conection
            oFactor.getConexionDao().conexionClose();
        }
    }

}
