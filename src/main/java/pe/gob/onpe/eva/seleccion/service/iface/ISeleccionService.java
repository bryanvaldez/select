/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.eva.seleccion.service.iface;

import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JasperPrint;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.rowset.OracleCachedRowSet;
import pe.gob.onpe.eva.model.Version;
import pe.gob.onpe.eva.model.mapper.CountGrado;
import pe.gob.onpe.eva.model.mapper.MesaDesc;
import pe.gob.onpe.eva.model.mapper.ODPESel;
import pe.gob.onpe.eva.model.mapper.ParametroDesc;
import pe.gob.onpe.eva.model.mapper.Reporte;
import pe.gob.onpe.eva.model.mapper.UbigeoDesc;

/**
 *
 * @author aquispec
 */
public interface ISeleccionService {
    
    ArrayList<Integer> validatePuestaCero();
    String puestaCero();
    
    ArrayList<String> ProcesoSeleccion25();
    
    ArrayList<Reporte> fetchListReports();
    ArrayList<ODPESel> fetchListAmbito();
    ArrayList<UbigeoDesc> fetchListDpto(Integer nAmbito);
    ArrayList<UbigeoDesc> fetchListProv(String cDpto, Integer nAmbito);
    ArrayList<UbigeoDesc> fetchListDist(String cDist, Integer nAmbito);
    ArrayList<MesaDesc> fetchListMesas(String cUbigeo);
    ArrayList<ParametroDesc> fetchListTipGrad();
    ArrayList<ParametroDesc> fetchListGrad(int nTipoGrado);
    ArrayList<ParametroDesc> fetchListMotivo();
    
    JasperPrint generarJasperPrint(Reporte reporte, List lstReport);
    JasperPrint generarJasperPrintResult(Reporte report, OracleCachedRowSet lstReport);
    List generarLstReporte(Reporte reporte);       
    OracleCachedRowSet generarLstReporteResult(Reporte report);
    
    String fetchFechaHora();
    Integer fetchCountODPEs();    
    ArrayList<CountGrado> ConfirmationPuestaCero();
    
    ArrayList<Version> fethVersionEleccionActiva();
    String fetchNombreEleccion();
    
    String fetchAliasEleccion();
    
    public CountGrado fetchCantidadElectoresMesas();
    
    public int getElectoresAsignados();
    
    public void seleccion25_1();
    
    public void seleccion25_2();
    
    public void seleccion25_3();
    
    public int getMesasSeleccion();
}
