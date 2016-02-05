/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.eva.seleccion.service.iface;

import java.util.ArrayList;
import pe.gob.onpe.eva.model.Parametro;

/**
 *
 * @author aquispec
 */
public interface IParametroService {
    ArrayList<Parametro> fetchAll(Parametro parametro);
    
    public void update(Parametro oParametro);
}
