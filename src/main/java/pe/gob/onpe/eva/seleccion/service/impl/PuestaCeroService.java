/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.eva.seleccion.service.impl;

import pe.gob.onpe.eva.dao.FactoryDao;
import pe.gob.onpe.eva.model.PuestaCero;
import pe.gob.onpe.eva.seleccion.service.iface.IPuestaCeroService;

/**
 *
 * @author aquispec
 */
public class PuestaCeroService implements IPuestaCeroService {

    FactoryDao oFactor = new FactoryDao();

    @Override
    public void registerPuestaCero(PuestaCero oPuestaCero) {
        try {
            // open conection
            oFactor.getConexionDao().conexionOpen();
            // execute command
            oFactor.getPuestaCeroDao().registerPuestaCero(oPuestaCero);
        } catch (Exception e) {
            throw e;
        } finally {
            // close conection
            oFactor.getConexionDao().conexionClose();
        }
    }
}
