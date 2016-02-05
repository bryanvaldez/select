/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.onpe.eva.seleccion.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.fill.JRGzipVirtualizer;
import net.sf.jasperreports.engine.fill.JRSwapFileVirtualizer;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSwapFile;
import net.sf.jasperreports.engine.util.xml.JRXPathExecuterUtils;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.rowset.OracleCachedRowSet;
import pe.gob.onpe.eva.dao.FactoryDao;
import pe.gob.onpe.eva.model.Mesa;
import pe.gob.onpe.eva.model.Version;
import pe.gob.onpe.eva.model.mapper.CountGrado;
import pe.gob.onpe.eva.model.mapper.Exclusion;
import pe.gob.onpe.eva.model.mapper.MesaDesc;
import pe.gob.onpe.eva.model.mapper.ODPESel;
import pe.gob.onpe.eva.model.mapper.ParametroDesc;
import pe.gob.onpe.eva.model.mapper.PesoPadron;
import pe.gob.onpe.eva.model.mapper.Reporte;
import pe.gob.onpe.eva.model.mapper.UbigeoDesc;
import pe.gob.onpe.eva.seleccion.constant.ConstantsSeleccion;
import pe.gob.onpe.eva.seleccion.service.iface.ISeleccionService;

/**
 *
 * @author aquispec
 */
public class SeleccionService implements ISeleccionService {

    private Properties systemProperties = new Properties(System.getProperties());
    private String tempPath = systemProperties.getProperty("java.io.tmpdir");
    FactoryDao oFactor = new FactoryDao();

    @Override
    public ArrayList<Integer> validatePuestaCero() {
        
        ArrayList<Integer> result = new ArrayList<>();
        try {
            // open conection
            oFactor.getConexionDao().conexionOpen();
            // listar los datos
            result = oFactor.getSeleccionDao().validatePuestaCero();
        } catch (Exception e) {
            throw e;
        } finally {
            // close conection
            oFactor.getConexionDao().conexionClose();
        }
        return result;
    }

    @Override
    public String puestaCero() {
        String result = null;
        try {
            // open conection
            oFactor.getConexionDao().conexionOpen();
            // listar los datos
            result = oFactor.getSeleccionDao().puestaCero();
        } catch (Exception e) {
            throw e;
        } finally {
            // close conection
            oFactor.getConexionDao().conexionClose();
        }
        return result;
    }

    @Override
    public ArrayList<String> ProcesoSeleccion25() {
        ArrayList<String> oLista = new ArrayList<>();
        try {
            // open conection
            oFactor.getConexionDao().conexionOpen();
            // listar los datos
            oLista = oFactor.getSeleccionDao().procesoSeleccion25();
        } catch (Exception e) {
            throw e;
        } finally {
            // close conection
            oFactor.getConexionDao().conexionClose();
        }
        return oLista;
    }

    @Override
    public ArrayList<Reporte> fetchListReports() {
        ArrayList<Reporte> oLista = new ArrayList<>();

        try {
            // open conection
            oFactor.getConexionDao().conexionOpen();
            // listar los datos
            oLista = oFactor.getSeleccionDao().fetchListReports();
        } catch (Exception e) {
            throw e;
        } finally {
            // close conection
            oFactor.getConexionDao().conexionClose();
        }

        return oLista;
    }

    @Override
    public ArrayList<ODPESel> fetchListAmbito() {
        ArrayList<ODPESel> oLista = new ArrayList<>();

        try {
            // open conection
            oFactor.getConexionDao().conexionOpen();
            // listar los datos                                    
            oLista = oFactor.getODPESelDao().fetchAll();

        } catch (Exception e) {
            throw e;
        } finally {
            // close conection
            oFactor.getConexionDao().conexionClose();
        }

        return oLista;
    }

    @Override
    public ArrayList<UbigeoDesc> fetchListDpto(Integer nAmbito) {
        ArrayList<UbigeoDesc> oLista = new ArrayList<>();

        try {
            // open conection
            oFactor.getConexionDao().conexionOpen();
            // listar los datos                                    
            oLista = oFactor.getUbigeoDao().fetchAllList("000000", nAmbito);

        } catch (Exception e) {
            throw e;
        } finally {
            // close conection
            oFactor.getConexionDao().conexionClose();
        }

        return oLista;
    }

    @Override
    public ArrayList<UbigeoDesc> fetchListProv(String cDpto, Integer nAmbito) {
        ArrayList<UbigeoDesc> oLista = new ArrayList<>();

        try {
            // open conection
            oFactor.getConexionDao().conexionOpen();
            // listar los datos                        
            oLista = oFactor.getUbigeoDao().fetchAllList(cDpto, nAmbito);

        } catch (Exception e) {
            throw e;
        } finally {
            // close conection
            oFactor.getConexionDao().conexionClose();
        }

        return oLista;
    }

    @Override
    public ArrayList<UbigeoDesc> fetchListDist(String cProv, Integer nAmbito) {
        ArrayList<UbigeoDesc> oLista = new ArrayList<>();

        try {
            // open conection
            oFactor.getConexionDao().conexionOpen();
            // listar los datos                        
            oLista = oFactor.getUbigeoDao().fetchAllList(cProv, nAmbito);

        } catch (Exception e) {
            throw e;
        } finally {
            // close conection
            oFactor.getConexionDao().conexionClose();
        }

        return oLista;
    }

    @Override
    public ArrayList<MesaDesc> fetchListMesas(String cUbigeo) {
        ArrayList<MesaDesc> oLista = new ArrayList<>();

        try {
            // open conection
            oFactor.getConexionDao().conexionOpen();
            Mesa oMesa = new Mesa();
            oMesa.setUbigeoPk(cUbigeo);
            // listar los datos                       
            ArrayList<Mesa> oListaMesa = oFactor.getMesaDao().fetchAll(oMesa);

            for (Mesa m : oListaMesa) {
                MesaDesc oMesaDesc = new MesaDesc();
                oMesaDesc.setMesa(m.getMesaPk());

                oLista.add(oMesaDesc);
            }

        } catch (Exception e) {
            throw e;
        } finally {
            // close conection
            oFactor.getConexionDao().conexionClose();
        }

        return oLista;
    }

    @Override
    public ArrayList<ParametroDesc> fetchListTipGrad() {
        ArrayList<ParametroDesc> oLista = new ArrayList<>();

        try {
            // open conection
            oFactor.getConexionDao().conexionOpen();
            // listar los datos            
            oLista = oFactor.getParametroDescDao().fetchAllTipGradInt();

        } catch (Exception e) {
            throw e;
        } finally {
            // close conection
            oFactor.getConexionDao().conexionClose();
        }

        return oLista;
    }

    @Override
    public ArrayList<ParametroDesc> fetchListGrad(int nTipoGrado) {
        ArrayList<ParametroDesc> oLista = new ArrayList<>();

        try {
            // open conection
            oFactor.getConexionDao().conexionOpen();
            // listar los datos            
            oLista = oFactor.getParametroDescDao().fetchAllGradInt(nTipoGrado);

        } catch (Exception e) {
            throw e;
        } finally {
            // close conection
            oFactor.getConexionDao().conexionClose();
        }

        return oLista;
    }

    @Override
    public ArrayList<ParametroDesc> fetchListMotivo() {
        ArrayList<ParametroDesc> oLista = new ArrayList<>();

        try {
            // open conection
            oFactor.getConexionDao().conexionOpen();
            // listar los datos                        
            oLista = oFactor.getParametroDescDao().fetchAllMotivo();

        } catch (Exception e) {
            throw e;
        } finally {
            // close conection
            oFactor.getConexionDao().conexionClose();
        }

        return oLista;
    }

    @Override
    public List generarLstReporte(Reporte report) {
        ArrayList<PesoPadron> lstPesoPadron = new ArrayList<>();
        ArrayList<CountGrado> lstGrado = new ArrayList<>();
        ArrayList<Exclusion> lstExclusion = new ArrayList<>();
        ArrayList<ODPESel> lstOdpe = new ArrayList<>();

        try {
            oFactor.getConexionDao().conexionOpen();
            if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_LISTA_PADRON_ELECTORAL)) {
                lstPesoPadron = oFactor.getPesoPadronDao().fetchAllPadron(report.getMesa(), report.getUbigeo(), report.getGrado(), report.getDiscapacidad(), report.getEdadDesde(), report.getEdadHasta());
            } else if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_LISTA_PADRON_X_PESOS)) {
                lstPesoPadron = oFactor.getPesoPadronDao().fetchAll(report.getMesa(), report.getUbigeo(), report.getGrado(), report.getDiscapacidad(), report.getEdadDesde(), report.getEdadHasta());
            } else if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_LISTA_25_SELECCIONADOS)) {
                lstPesoPadron = oFactor.getPesoPadronDao().fetchReport25(report.getMesa(), report.getUbigeo());
            } else if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_LISTA_CIUDADANOS_EXCLUIDOS)) {
                lstPesoPadron = oFactor.getPesoPadronDao().fetchReportCiudadanosExcluidos(report.getMesa(), report.getUbigeo());
            } else if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_LISTA_CIUDADANOS_MULTI_EXCLUIDOS)) {
                lstPesoPadron = oFactor.getPesoPadronDao().fetchReportCiudadanosMultiExcluidos();
            } else if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_LISTA_CIUDADANOS_POR_MOTIVO_EXCLUSION)) {
                lstPesoPadron = oFactor.getPesoPadronDao().fetchReporteCiudadanosPorMotivo(report.getMotivo());
            } else if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_FRECUENCIA_MM_POR_CIUDADANO)) {
                lstPesoPadron = oFactor.getPesoPadronDao().fetchReportFrecuenciaMMPorCiudadano(report.getDocumento());
            } else if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_PESOS_X_RANGO_EDAD_N1)) {
                lstGrado = oFactor.getCountGradoDao().fetchReportPesosEdadN1();
            } else if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_GRADO_INSTUCCION_PADRON_ELECTORAL)) {
                lstGrado = oFactor.getCountGradoDao().fetchReportGradoPadron(report.getUbigeo());
            } else if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_PESOS_X_GRADO_INSTRUCCION_N1)) {
                lstGrado = oFactor.getCountGradoDao().fetchReportPesosGradoN1();
            } else if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_PESOS_X_DISCAPACIDAD_N1)) {
                lstGrado = oFactor.getCountGradoDao().fetchReportPesosDiscapacidadN1();
            } else if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_PESOS_X_RANGO_EDAD_N3)) {
                lstGrado = oFactor.getCountGradoDao().fetchReportPesosEdadN3();
            } else if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_GRADO_INSTRUCCION_25_SELECCIONADOS)) {
                lstGrado = oFactor.getCountGradoDao().fetchReportGradoPadron25(report.getUbigeo());
            } else if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_PESOS_X_FRECUENCIA_MM)) {
                lstGrado = oFactor.getCountGradoDao().fetchReportFrecuenciaMMN2();
            } else if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_MOTIVOS_EXCLUSION)) {
                lstExclusion = oFactor.getExclusionDao().fetchReportMotivosExclusion(report.getMotivo());
            } else if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_MESAS_ELECTORES_POR_UBIGEO)) {
                lstOdpe = oFactor.getODPESelDao().fetchAll(report.getAmbito(), report.getUbigeo());
            }
        } catch (Exception e) {
            throw e;
        } finally {
            oFactor.getConexionDao().conexionClose();
        }

        if (lstPesoPadron.size() > 0) {
            return lstPesoPadron;
        } else if (lstGrado.size() > 0) {
            return lstGrado;
        } else if (lstExclusion.size() > 0) {
            return lstExclusion;
        } else if (lstOdpe.size() > 0) {
            return lstOdpe;
        } else {
            return null;
        }
    }

    @Override
    public OracleCachedRowSet generarLstReporteResult(Reporte report) {

        OracleCachedRowSet result = null;
        try {
            oFactor.getConexionDao().conexionOpen();
            if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_LISTA_PADRON_ELECTORAL)) {
                //lstPesoPadron = oFactor.getPesoPadronDao().fetchAllPadron(report.getMesa(), report.getUbigeo(), report.getGrado(), report.getDiscapacidad(), report.getEdadDesde(), report.getEdadHasta());
            } else if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_LISTA_PADRON_X_PESOS)) {
                result = oFactor.getPesoPadronDao().fetchAllResult(report.getMesa(), report.getUbigeo(), report.getGrado(), report.getDiscapacidad(), report.getEdadDesde(), report.getEdadHasta());
            } else if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_LISTA_25_SELECCIONADOS)) {
                //lstPesoPadron = oFactor.getPesoPadronDao().fetchReport25(report.getMesa(), report.getUbigeo());
            } else if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_LISTA_CIUDADANOS_EXCLUIDOS)) {
                //lstPesoPadron = oFactor.getPesoPadronDao().fetchReportCiudadanosExcluidos(report.getMesa(), report.getUbigeo());
            } else if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_LISTA_CIUDADANOS_MULTI_EXCLUIDOS)) {
                //lstPesoPadron = oFactor.getPesoPadronDao().fetchReportCiudadanosMultiExcluidos();
            } else if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_LISTA_CIUDADANOS_POR_MOTIVO_EXCLUSION)) {
                //lstPesoPadron = oFactor.getPesoPadronDao().fetchReporteCiudadanosPorMotivo(report.getMotivo());
            } else if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_FRECUENCIA_MM_POR_CIUDADANO)) {
                //lstPesoPadron = oFactor.getPesoPadronDao().fetchReportFrecuenciaMMPorCiudadano(report.getDocumento());
            } else if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_PESOS_X_RANGO_EDAD_N1)) {
                //lstGrado = oFactor.getCountGradoDao().fetchReportPesosEdadN1();
            } else if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_GRADO_INSTUCCION_PADRON_ELECTORAL)) {
                //lstGrado = oFactor.getCountGradoDao().fetchReportGradoPadron(report.getUbigeo());
            } else if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_PESOS_X_GRADO_INSTRUCCION_N1)) {
                //lstGrado = oFactor.getCountGradoDao().fetchReportPesosGradoN1();
            } else if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_PESOS_X_DISCAPACIDAD_N1)) {
                //lstGrado = oFactor.getCountGradoDao().fetchReportPesosDiscapacidadN1();
            } else if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_PESOS_X_RANGO_EDAD_N3)) {
                //lstGrado = oFactor.getCountGradoDao().fetchReportPesosEdadN3();
            } else if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_GRADO_INSTRUCCION_25_SELECCIONADOS)) {
                //lstGrado = oFactor.getCountGradoDao().fetchReportGradoPadron25(report.getUbigeo());
            } else if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_PESOS_X_FRECUENCIA_MM)) {
                //lstGrado = oFactor.getCountGradoDao().fetchReportFrecuenciaMMN2();
            } else if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_MOTIVOS_EXCLUSION)) {
                //lstExclusion = oFactor.getExclusionDao().fetchReportMotivosExclusion(report.getMotivo());
            } else if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_MESAS_ELECTORES_POR_UBIGEO)) {
                //lstOdpe = oFactor.getODPESelDao().fetchAll(report.getAmbito(), report.getUbigeo());
            }
        } catch (Exception e) {
            throw e;
        } finally {
            oFactor.getConexionDao().conexionClose();
        }

        if (result != null) {
            return result;
        } else {
            return null;
        }
    }

    @Override
    public JasperPrint generarJasperPrint(Reporte report, List lstReport) {

        Locale locale = new Locale("pe", "PE");
        HashMap<String, Object> mapper = new HashMap();
        mapper.put(JRParameter.REPORT_LOCALE, locale);

        JasperReport reporte = null;
        JasperPrint jasperPrint = null;

        String absolutePath = SeleccionService.class.getClassLoader().getResource(ConstantsSeleccion.PATH_LOGO_ONPE).toString();

        if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_LISTA_PADRON_ELECTORAL)) {
            try {
                InputStream inputStreamListaPadronElectoral = getClass().getClassLoader().getResource(ConstantsSeleccion.PATH_REPORT_JRXML + ConstantsSeleccion.JASPER_NAME_RPT_LISTA_PADRON_ELECTORAL).openStream();
                reporte = (JasperReport) JRLoader.loadObject(inputStreamListaPadronElectoral);
                mapper.put("codReporte", report.getCodigo());
                mapper.put("Distrito_CP", ConstantsSeleccion.CONFIGURACION.getEtiquetaDist());
                mapper.put("Dpto", ConstantsSeleccion.CONFIGURACION.getEtiquetaDpto());
                mapper.put("Prov", ConstantsSeleccion.CONFIGURACION.getEtiquetaProv());
            } catch (IOException | JRException ex) {
                Logger.getLogger(SeleccionService.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_LISTA_PADRON_X_PESOS)) {
            try {
                InputStream inputStreamListaPadronXPesos = getClass().getClassLoader().getResource(ConstantsSeleccion.PATH_REPORT_JRXML + ConstantsSeleccion.JASPER_NAME_RPT_LISTA_PADRON_X_PESOS).openStream();
                reporte = (JasperReport) JRLoader.loadObject(inputStreamListaPadronXPesos);
                mapper.put("codReporte", report.getCodigo());
                mapper.put("Distrito_CP", ConstantsSeleccion.CONFIGURACION.getEtiquetaDist());
                mapper.put("Dpto", ConstantsSeleccion.CONFIGURACION.getEtiquetaDpto());
                mapper.put("Prov", ConstantsSeleccion.CONFIGURACION.getEtiquetaProv());
            } catch (IOException | JRException e) {
                Logger.getLogger(SeleccionService.class.getName()).log(Level.SEVERE, null, e);
            }
        } else if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_LISTA_25_SELECCIONADOS)) {
            try {
                InputStream inputStreamLista25Seleccionados = getClass().getClassLoader().getResource(ConstantsSeleccion.PATH_REPORT_JRXML + ConstantsSeleccion.JASPER_NAME_RPT_LISTA_25_SELECCIONADOS).openStream();
                reporte = (JasperReport) JRLoader.loadObject(inputStreamLista25Seleccionados);
                mapper.put("codReporte", report.getCodigo());
                mapper.put("Distrito_CP", ConstantsSeleccion.CONFIGURACION.getEtiquetaDist());
                mapper.put("Dpto", ConstantsSeleccion.CONFIGURACION.getEtiquetaDpto());
                mapper.put("Prov", ConstantsSeleccion.CONFIGURACION.getEtiquetaProv());
            } catch (IOException | JRException e) {
                Logger.getLogger(SeleccionService.class.getName()).log(Level.SEVERE, null, e);
            }
        } else if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_PESOS_X_RANGO_EDAD_N1)) {
            try {
                InputStream inputStreamReporteRangoEdadN1 = getClass().getClassLoader().getResource(ConstantsSeleccion.PATH_REPORT_JRXML + ConstantsSeleccion.JASPER_NAME_RPT_PESOS_X_RANGO_EDAD_N1).openStream();
                reporte = (JasperReport) JRLoader.loadObject(inputStreamReporteRangoEdadN1);
                ArrayList<CountGrado> lstGrado = (ArrayList<CountGrado>) lstReport;
                for (CountGrado grado : lstGrado) {
                    grado.setRangoEdad(grado.getRangoEdad().replace("mas", "mÃ¡s"));

                }
                ArrayList<CountGrado> lst = (ArrayList<CountGrado>) lstReport;
                for (CountGrado oGrado : lst) {
                    oGrado.setRangoEdad(oGrado.getRangoEdad().replace("mas", "mÃ¡s"));
                }
                mapper.put("codReporte", report.getCodigo());
            } catch (IOException | JRException e) {
                Logger.getLogger(SeleccionService.class.getName()).log(Level.SEVERE, null, e);
            }
        } else if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_GRADO_INSTUCCION_PADRON_ELECTORAL)) {
            try {
                InputStream inputStreamGradoInstruccion = getClass().getClassLoader().getResource(ConstantsSeleccion.PATH_REPORT_JRXML + ConstantsSeleccion.JASPER_NAME_RPT_GRADO_INSTRUCCION_PADRON_ELECTORAL).openStream();
                reporte = (JasperReport) JRLoader.loadObject(inputStreamGradoInstruccion);
                mapper.put("codReporte", report.getCodigo());
                mapper.put("Distrito_CP", ConstantsSeleccion.CONFIGURACION.getEtiquetaDist());
                mapper.put("Dpto", ConstantsSeleccion.CONFIGURACION.getEtiquetaDpto());
                mapper.put("Prov", ConstantsSeleccion.CONFIGURACION.getEtiquetaProv());
                int total = 0;
                ArrayList<CountGrado> lstGrado = (ArrayList<CountGrado>) lstReport;
                for (CountGrado padron : lstGrado) {
                    total += padron.getTotal();
                }
                mapper.put("totalElectores", total);
            } catch (IOException | JRException e) {
                Logger.getLogger(SeleccionService.class.getName()).log(Level.SEVERE, null, e);
            }
        } else if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_PESOS_X_GRADO_INSTRUCCION_N1)) {
            try {
                InputStream inputStreamReporteGradoInstruccionN1 = getClass().getClassLoader().getResource(ConstantsSeleccion.PATH_REPORT_JRXML + ConstantsSeleccion.JASPER_NAME_RPT_PESOS_X_GRADO_INSTRUCCION_N1).openStream();
                reporte = (JasperReport) JRLoader.loadObject(inputStreamReporteGradoInstruccionN1);
                mapper.put("codReporte", report.getCodigo());
            } catch (IOException | JRException e) {
                Logger.getLogger(SeleccionService.class.getName()).log(Level.SEVERE, null, e);
            }
        } else if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_PESOS_X_DISCAPACIDAD_N1)) {
            try {
                InputStream inputStreamReporteDiscapacidadN1 = getClass().getClassLoader().getResource(ConstantsSeleccion.PATH_REPORT_JRXML + ConstantsSeleccion.JASPER_NAME_RPT_PESOS_X_DISCAPACIDAD_N1).openStream();
                reporte = (JasperReport) JRLoader.loadObject(inputStreamReporteDiscapacidadN1);
                mapper.put("codReporte", report.getCodigo());
            } catch (IOException | JRException e) {
                Logger.getLogger(SeleccionService.class.getName()).log(Level.SEVERE, null, e);
            }
        } else if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_PESOS_X_RANGO_EDAD_N3)) {
            try {
                InputStream inputStreamReporteRangoEdadN3 = getClass().getClassLoader().getResource(ConstantsSeleccion.PATH_REPORT_JRXML + ConstantsSeleccion.JASPER_NAME_RPT_PESOS_X_RANGO_EDAD_N3).openStream();
                reporte = (JasperReport) JRLoader.loadObject(inputStreamReporteRangoEdadN3);
                mapper.put("codReporte", report.getCodigo());
            } catch (IOException | JRException e) {
                Logger.getLogger(SeleccionService.class.getName()).log(Level.SEVERE, null, e);
            }
        } else if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_GRADO_INSTRUCCION_25_SELECCIONADOS)) {
            try {
                InputStream inputStreamGradoInstruccion25 = getClass().getClassLoader().getResource(ConstantsSeleccion.PATH_REPORT_JRXML + ConstantsSeleccion.JASPER_NAME_RPT_GRADO_INSTRUCCION_25_SELECCIONADOS).openStream();
                reporte = (JasperReport) JRLoader.loadObject(inputStreamGradoInstruccion25);
                mapper.put("codReporte", report.getCodigo());
                mapper.put("Distrito_CP", ConstantsSeleccion.CONFIGURACION.getEtiquetaDist());
                mapper.put("Dpto", ConstantsSeleccion.CONFIGURACION.getEtiquetaDpto());
                mapper.put("Prov", ConstantsSeleccion.CONFIGURACION.getEtiquetaProv());
            } catch (IOException | JRException e) {
                Logger.getLogger(SeleccionService.class.getName()).log(Level.SEVERE, null, e);
            }
        } else if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_PESOS_X_FRECUENCIA_MM)) {
            try {
                InputStream inputStreamReporteFrecuenciaMM = getClass().getClassLoader().getResource(ConstantsSeleccion.PATH_REPORT_JRXML + ConstantsSeleccion.JASPER_NAME_RPT_PESOS_X_FRECUENCIA_MM).openStream();
                reporte = (JasperReport) JRLoader.loadObject(inputStreamReporteFrecuenciaMM);
                mapper.put("codReporte", report.getCodigo());
            } catch (IOException | JRException e) {
                Logger.getLogger(SeleccionService.class.getName()).log(Level.SEVERE, null, e);
            }
        } else if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_MOTIVOS_EXCLUSION)) {
            try {
                InputStream inputStreamReporteMotivosExclusion = getClass().getClassLoader().getResource(ConstantsSeleccion.PATH_REPORT_JRXML + ConstantsSeleccion.JASPER_NAME_RPT_MOTIVOS_EXCLUSION).openStream();
                reporte = (JasperReport) JRLoader.loadObject(inputStreamReporteMotivosExclusion);
                mapper.put("codReporte", report.getCodigo());
                int porExcluir = 0;
                int exclusiones = 0;
                ArrayList<Exclusion> lstExclusion = (ArrayList<Exclusion>) lstReport;
                for (Exclusion exclusion : lstExclusion) {
                    porExcluir += exclusion.getPorExcluir();
                    exclusiones += exclusion.getExclusiones();
                }
                mapper.put("totalPorExcluir", porExcluir);
                mapper.put("totalExclusiones", exclusiones);
            } catch (IOException | JRException e) {
                Logger.getLogger(SeleccionService.class.getName()).log(Level.SEVERE, null, e);
            }
        } else if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_LISTA_CIUDADANOS_EXCLUIDOS)) {
            try {
                InputStream inputStreamReporteCiudadanosExcluidos = getClass().getClassLoader().getResource(ConstantsSeleccion.PATH_REPORT_JRXML + ConstantsSeleccion.JASPER_NAME_RPT_LISTA_CIUDADANOS_EXCLUIDOS).openStream();
                reporte = (JasperReport) JRLoader.loadObject(inputStreamReporteCiudadanosExcluidos);
                mapper.put("codReporte", report.getCodigo());
                mapper.put("Distrito_CP", ConstantsSeleccion.CONFIGURACION.getEtiquetaDist());
                mapper.put("Dpto", ConstantsSeleccion.CONFIGURACION.getEtiquetaDpto());
                mapper.put("Prov", ConstantsSeleccion.CONFIGURACION.getEtiquetaProv());
            } catch (IOException | JRException e) {
                Logger.getLogger(SeleccionService.class.getName()).log(Level.SEVERE, null, e);
            }
        } else if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_LISTA_CIUDADANOS_MULTI_EXCLUIDOS)) {
            try {
                InputStream inputStreamReporteCiudadanosMultiExcluidos = getClass().getClassLoader().getResource(ConstantsSeleccion.PATH_REPORT_JRXML + ConstantsSeleccion.JASPER_NAME_RPT_LISTA_CIUDADANOS_MULTI_EXCLUIDOS).openStream();
                reporte = (JasperReport) JRLoader.loadObject(inputStreamReporteCiudadanosMultiExcluidos);
                mapper.put("codReporte", report.getCodigo());
                int totalReg = 0;
                int cantidad = 2;
                int count = 0;
                String resumen = "";
                String resumenTotales = "";
                resumen += "<table>";
                resumenTotales += "<table>";
                ArrayList<PesoPadron> lstPesoPadron = (ArrayList<PesoPadron>) lstReport;

                while (totalReg < lstPesoPadron.size()) {
                    count = 0;
                    for (PesoPadron peso : lstPesoPadron) {
                        if (cantidad == peso.getVecesExcluido()) {
                            count++;
                        }
                    }
                    if (count > 0) {
                        resumen += "<tr><td> Total de ciudadanos con " + cantidad + " Motivos de Exclusión </td></tr>";
                        resumenTotales += "<tr><td>" + count + "</td></tr>";
                    }
                    totalReg += count;
                    cantidad++;
                }
                resumen += "<tr><td> Total </td></tr>";
                resumen += "</table>";
                resumenTotales += "<tr><td>" + lstPesoPadron.size() + "</td></tr>";
                resumenTotales += "</table>";
                mapper.put("resumen", resumen);
                mapper.put("resumenTotales", resumenTotales);
                mapper.put("Distrito_CP", ConstantsSeleccion.CONFIGURACION.getEtiquetaDptoProvDist());
            } catch (IOException | JRException e) {
                Logger.getLogger(SeleccionService.class.getName()).log(Level.SEVERE, null, e);
            }
        } else if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_LISTA_CIUDADANOS_POR_MOTIVO_EXCLUSION)) {
            try {
                InputStream inputStreamReporteCiudadanosPorMotivo = getClass().getClassLoader().getResource(ConstantsSeleccion.PATH_REPORT_JRXML + ConstantsSeleccion.JASPER_NAME_RPT_LISTA_CIUDADANOS_POR_MOTIVO_EXCLUSION).openStream();
                reporte = (JasperReport) JRLoader.loadObject(inputStreamReporteCiudadanosPorMotivo);
                mapper.put("codReporte", report.getCodigo());
                mapper.put("Distrito_CP", ConstantsSeleccion.CONFIGURACION.getEtiquetaDptoProvDist());
            } catch (IOException | JRException e) {
                Logger.getLogger(SeleccionService.class.getName()).log(Level.SEVERE, null, e);
            }
        } else if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_FRECUENCIA_MM_POR_CIUDADANO)) {
            try {
                InputStream inputStreamReporteFrecuenciaPorCiudadano = getClass().getClassLoader().getResource(ConstantsSeleccion.PATH_REPORT_JRXML + ConstantsSeleccion.JASPER_NAME_RPT_LISTA_FRECUENCIA_MM_POR_CIUDADANO).openStream();
                reporte = (JasperReport) JRLoader.loadObject(inputStreamReporteFrecuenciaPorCiudadano);
                ArrayList<PesoPadron> lstPesoPadron = (ArrayList<PesoPadron>) lstReport;

                String st = lstPesoPadron.get(0).getMotivoExclusion().substring(0, 1);
                if (!"1".equals(st)) {
                    for (int i = 1; i <= lstPesoPadron.size(); i++) {
                        lstPesoPadron.get(i - 1).setMotivoExclusion(i + ") " + lstPesoPadron.get(i - 1).getMotivoExclusion());
                        lstPesoPadron.get(i - 1).setVecesExcluido(lstPesoPadron.size());
                    }
                }

                mapper.put("codReporte", report.getCodigo());
                mapper.put("Distrito_CP", ConstantsSeleccion.CONFIGURACION.getEtiquetaDptoProvDist());
            } catch (IOException | JRException e) {
                Logger.getLogger(SeleccionService.class.getName()).log(Level.SEVERE, null, e);
            }
        } else if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_MESAS_ELECTORES_POR_UBIGEO)) {
            try {
                InputStream inputStreamReporteMesasElectoresUbigeo = getClass().getClassLoader().getResource(ConstantsSeleccion.PATH_REPORT_JRXML + ConstantsSeleccion.JASPER_NAME_RPT_CONFIRMACION_SELECCION).openStream();
                reporte = (JasperReport) JRLoader.loadObject(inputStreamReporteMesasElectoresUbigeo);
                mapper.put("codReporte", report.getCodigo());
                ArrayList<ODPESel> lstODPE = new ArrayList<>();
                lstODPE = (ArrayList<ODPESel>) lstReport;
                int totalMesas = 0;
                int totalHabiles = 0;
                for (ODPESel odpe : lstODPE) {
                    totalMesas += odpe.getCantidadMesas();
                    totalHabiles += odpe.getHabiles();
                }
                mapper.put("totalMesas", totalMesas);
                mapper.put("totalElectores", totalHabiles);
                mapper.put("Distrito_CP", ConstantsSeleccion.CONFIGURACION.getEtiquetaDist());
                mapper.put("Dpto", ConstantsSeleccion.CONFIGURACION.getEtiquetaDpto());
                mapper.put("Prov", ConstantsSeleccion.CONFIGURACION.getEtiquetaProv());
                mapper.put("ODPE_ORC", ConstantsSeleccion.CONFIGURACION.getEtiquetaOdpe());

            } catch (IOException | JRException e) {
                Logger.getLogger(SeleccionService.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        mapper.put("PATH_LOGO", absolutePath);
        mapper.put("titulo", report.getDescripcion());
        mapper.put("ELECCION", ConstantsSeleccion.CONFIGURACION.getNameEleccion());


            //JRGzipVirtualizer virtualizador = new JRGzipVirtualizer(10);
            //mapper.put(JRParameter.REPORT_VIRTUALIZER, virtualizador);

            JRSwapFile archivoSwap = new JRSwapFile(tempPath, 4096, 25);
            JRSwapFileVirtualizer virtualizador = new JRSwapFileVirtualizer(25, archivoSwap, true);  //2
            mapper.put(JRParameter.REPORT_VIRTUALIZER, virtualizador);

        //String pathVirtualizer = systemProperties.getProperty("java.io.tmpdir");
        //JRFileVirtualizer virtualizador = new JRFileVirtualizer(5, pathVirtualizer);  // 11
        //JRGzipVirtualizer virtualizador = new JRGzipVirtualizer(10);
        JasperReportsContext jrc = DefaultJasperReportsContext.getInstance();

        jrc.setProperty(
                JRXPathExecuterUtils.PROPERTY_XPATH_EXECUTER_FACTORY,
                "net.sf.jasperreports.engine.util.xml.JaxenXPathExecuterFactory");

        JasperFillManager jfm = JasperFillManager.getInstance(jrc);

        try {
            
            //////////////////////////
            
            //////////////////////////
            
            jasperPrint = jfm.fillReport(reporte, mapper, new JRBeanCollectionDataSource(lstReport));
        } catch (JRException ex) {
            Logger.getLogger(SeleccionService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jasperPrint;
    }

    @Override
    public JasperPrint generarJasperPrintResult(Reporte report, OracleCachedRowSet lstReport) {

        Locale locale = new Locale("pe", "PE");
        HashMap<String, Object> mapper = new HashMap();
        mapper.put(JRParameter.REPORT_LOCALE, locale);

        JasperReport reporte = null;
        JasperPrint jasperPrint = null;

        String absolutePath = SeleccionService.class.getClassLoader().getResource(ConstantsSeleccion.PATH_LOGO_ONPE).toString();

//        if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_LISTA_PADRON_ELECTORAL)) {
//            try {
//                InputStream inputStreamListaPadronElectoral = getClass().getClassLoader().getResource(ConstantsSeleccion.PATH_REPORT_JRXML + ConstantsSeleccion.JASPER_NAME_RPT_LISTA_PADRON_ELECTORAL).openStream();
//                reporte = (JasperReport) JRLoader.loadObject(inputStreamListaPadronElectoral);
//                mapper.put("codReporte", report.getCodigo());
//                mapper.put("Distrito_CP", ConstantsSeleccion.CONFIGURACION.getEtiquetaDist());
//                mapper.put("Dpto", ConstantsSeleccion.CONFIGURACION.getEtiquetaDpto());
//                mapper.put("Prov", ConstantsSeleccion.CONFIGURACION.getEtiquetaProv());
//            } catch (IOException | JRException ex) {
//                Logger.getLogger(SeleccionService.class.getName()).log(Level.SEVERE, null, ex);
//            }
        //} else 
        if (report.getCodigo().equals(ConstantsSeleccion.N_RPT_LISTA_PADRON_X_PESOS)) {
            try {
                InputStream inputStreamListaPadronXPesos = getClass().getClassLoader().getResource(ConstantsSeleccion.PATH_REPORT_JRXML + ConstantsSeleccion.JASPER_NAME_RPT_LISTA_PADRON_X_PESOS).openStream();
                reporte = (JasperReport) JRLoader.loadObject(inputStreamListaPadronXPesos);

                mapper.put("codReporte", report.getCodigo());
                mapper.put("Distrito_CP", ConstantsSeleccion.CONFIGURACION.getEtiquetaDist());
                mapper.put("Dpto", ConstantsSeleccion.CONFIGURACION.getEtiquetaDpto());
                mapper.put("Prov", ConstantsSeleccion.CONFIGURACION.getEtiquetaProv());
            } catch (IOException | JRException e) {
                Logger.getLogger(SeleccionService.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        mapper.put("PATH_LOGO", absolutePath);
        mapper.put("titulo", report.getDescripcion());
        mapper.put("ELECCION", ConstantsSeleccion.CONFIGURACION.getNameEleccion());

        String pathVirtualizer = systemProperties.getProperty("java.io.tmpdir");
        //JRFileVirtualizer virtualizador = new JRFileVirtualizer(5, pathVirtualizer);
        //mapper.put(JRParameter.REPORT_VIRTUALIZER, virtualizador);

        JasperReportsContext jrc = DefaultJasperReportsContext.getInstance();

        jrc.setProperty(
                JRXPathExecuterUtils.PROPERTY_XPATH_EXECUTER_FACTORY,
                "net.sf.jasperreports.engine.util.xml.JaxenXPathExecuterFactory");

        JasperFillManager jfm = JasperFillManager.getInstance(jrc);

        try {
            //jasperPrint = JasperFillManager.fillReport(reporte, mapper, new JRResultSetDataSource(lstReport));
            jasperPrint = jfm.fillReport(reporte, mapper, new JRResultSetDataSource(lstReport));
        } catch (JRException ex) {
            Logger.getLogger(SeleccionService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jasperPrint;
    }

    @Override
    public String fetchFechaHora() {
        String result = null;
        try {
            // open conection
            oFactor.getConexionDao().conexionOpen();
            // listar los datos
            result = oFactor.getSeleccionDao().fetchFechaHora();
        } catch (Exception e) {
            throw e;
        } finally {
            // close conection
            oFactor.getConexionDao().conexionClose();
        }
        return result;
    }

    @Override
    public Integer fetchCountODPEs() {
        Integer result = null;
        try {
            // open conection
            oFactor.getConexionDao().conexionOpen();
            // listar los datos
            result = oFactor.getSeleccionDao().fetchCountODPEs();
        } catch (Exception e) {
            throw e;
        } finally {
            // close conection
            oFactor.getConexionDao().conexionClose();
        }
        return result;
    }

    @Override
    public ArrayList<CountGrado> ConfirmationPuestaCero() {
        ArrayList<CountGrado> oLista = new ArrayList<>();

        try {
            // open conection
            oFactor.getConexionDao().conexionOpen();
            // listar los datos
            oLista = oFactor.getCountGradoDao().confirmationPuestaCero();
        } catch (Exception e) {
            throw e;
        } finally {
            // close conection
            oFactor.getConexionDao().conexionClose();
        }

        return oLista;
    }

    @Override
    public ArrayList<Version> fethVersionEleccionActiva() {
        ArrayList<Version> oLista = new ArrayList<>();

        try {
            // open conection
            oFactor.getConexionDao().conexionOpen();
            // listar los datos
            oLista = oFactor.getVersionDao().fetchAllEleccionActiva();
        } catch (Exception e) {
            throw e;
        } finally {
            // close conection
            oFactor.getConexionDao().conexionClose();
        }

        return oLista;
    }

    @Override
    public String fetchNombreEleccion() {
        String result = null;
        try {
            // open conection
            oFactor.getConexionDao().conexionOpen();
            // listar los datos
            result = oFactor.getEleccionDao().fetchNombreEleccion();
        } catch (Exception e) {
            throw e;
        } finally {
            // close conection
            oFactor.getConexionDao().conexionClose();
        }
        return result;
    }

    @Override
    public String fetchAliasEleccion() {
        String result = null;
        try {
            // open conection
            oFactor.getConexionDao().conexionOpen();
            // listar los datos
            result = oFactor.getEleccionDao().fetchAliasEleccion();
        } catch (Exception e) {
            throw e;
        } finally {
            // close conection
            oFactor.getConexionDao().conexionClose();
        }
        return result;
    }

    @Override
    public CountGrado fetchCantidadElectoresMesas() {
        CountGrado result = null;
        try {
            // open conection
            oFactor.getConexionDao().conexionOpen();
            // listar los datos
            result = oFactor.getCountGradoDao().fetchCantidadElectoresMesas();
        } catch (Exception e) {
            throw e;
        } finally {
            // close conection
            oFactor.getConexionDao().conexionClose();
        }
        return result;
    }

    @Override
    public int getElectoresAsignados() {
        int resultado = 0;
        try {
            oFactor.getConexionDao().conexionOpen();
            resultado = oFactor.getPadronDao().getElectoresAsignados();
        } catch (Exception e) {
            throw e;
        } finally {
            oFactor.getConexionDao().conexionClose();
        }

        return resultado;
    }

    @Override
    public void seleccion25_1() {

        try {
            // open conection
            oFactor.getConexionDao().conexionOpen();
            // listar los datos
            oFactor.getSeleccionDao().seleccion25_1();
        } catch (Exception e) {
            throw e;
        } finally {
            oFactor.getConexionDao().conexionClose();
        }
    }

    @Override
    public void seleccion25_2() {

        try {
            // open conection
            oFactor.getConexionDao().conexionOpen();
            // listar los datos
            oFactor.getSeleccionDao().seleccion25_2();
        } catch (Exception e) {
            throw e;
        } finally {
            oFactor.getConexionDao().conexionClose();
        }
    }

    @Override
    public void seleccion25_3() {

        try {
            // open conection
            oFactor.getConexionDao().conexionOpen();
            // listar los datos
            oFactor.getSeleccionDao().seleccion25_3();
        } catch (Exception e) {
            throw e;
        } finally {
            oFactor.getConexionDao().conexionClose();
        }
    }

    @Override
    public int getMesasSeleccion() {
        int resultado = 0;
        try {
            oFactor.getConexionDao().conexionOpen();
            resultado = oFactor.getCabPadronDesignacionDao().getMesasSeleccion();
        } catch (Exception e) {
            throw e;
        } finally {
            oFactor.getConexionDao().conexionClose();
        }

        return resultado;
    }

}
