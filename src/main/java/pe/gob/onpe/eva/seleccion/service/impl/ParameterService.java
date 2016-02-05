/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.eva.seleccion.service.impl;

import java.util.ArrayList;
import pe.gob.onpe.eva.dao.FactoryDao;
import pe.gob.onpe.eva.model.Parametro;
import pe.gob.onpe.eva.seleccion.service.iface.IParametroService;

/**
 *
 * @author aquispec
 */
public class ParameterService implements IParametroService{
    
    FactoryDao oFactor = new FactoryDao();

    @Override
    public ArrayList<Parametro> fetchAll(Parametro parametro) {
        ArrayList<Parametro> oLista = new ArrayList<>();
        
        try {
            // open conection
            oFactor.getConexionDao().conexionOpen();
            // listar los datos
            oLista = oFactor.getParametroDao().fetchAll(parametro);
        } catch (Exception e) {
            throw e;
        } finally {
            // close conection
            oFactor.getConexionDao().conexionClose();
        }

        return oLista;
    }

    @Override
    public void update(Parametro oParametro) {
        try {
            // open conection
            oFactor.getConexionDao().conexionOpen();
            // execute command
            oFactor.getParametroDao().update(oParametro);
        } catch (Exception e) {
            throw e;
        } finally {
            // close conection
            oFactor.getConexionDao().conexionClose();
        }
    }
    
}
