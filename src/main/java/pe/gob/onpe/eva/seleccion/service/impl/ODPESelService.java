/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.eva.seleccion.service.impl;

import java.util.ArrayList;
import pe.gob.onpe.eva.dao.FactoryDao;
import pe.gob.onpe.eva.model.mapper.ODPESel;
import pe.gob.onpe.eva.model.mapper.Reporte;
import pe.gob.onpe.eva.seleccion.service.iface.IODPESelService;

/**
 *
 * @author aquispec
 */
public class ODPESelService implements IODPESelService {

    FactoryDao oFactor = new FactoryDao();

    @Override
    public ArrayList<ODPESel> fetchAll(Reporte report) {
        ArrayList<ODPESel> oLista = new ArrayList<>();
        
        try {
            // open conection
            oFactor.getConexionDao().conexionOpen();
            // listar los datos
            oLista = oFactor.getODPESelDao().fetchAll(report.getAmbito(), report.getUbigeo());
        } catch (Exception e) {
            throw e;
        } finally {
            // close conection
            oFactor.getConexionDao().conexionClose();
        }

        return oLista;
    }

}
