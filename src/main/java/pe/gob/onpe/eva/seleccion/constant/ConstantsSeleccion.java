package pe.gob.onpe.eva.seleccion.constant;

import java.util.List;
import pe.gob.onpe.eva.model.Parametro;
import pe.gob.onpe.eva.model.Usuario;
import pe.gob.onpe.eva.model.mapper.Configuracion;
import pe.gob.onpe.eva.model.mapper.Reporte;

/**
 *
 * @author jlimachi
 */
public class ConstantsSeleccion {

    public static int W_LOGIN = 0;
    public static int W_PRINCIPAL_SELECCION25 = 1;
    public static int W_SEL_PUESTA0 = 2;
    public static int W_SEL_PUESTA0_CONFIRMACIÓN = 3;
    public static int W_SEL_PROCESO_SELECCION = 4;
    public static int W_SEL_REPORTES = 5;
    public static int W_SEL_FIN_SELECCION = 6;
    public static int W_SEL_MODAL_AUTH = 7;
    public static int W_SEL_MODAL_VERSION = 8;
    public static int W_SEL_MODAL_RESPALDO = 9;
    public static int W_PASSWORD_INCORRECTO = 10;
    public static int W_CONFIGURACION = 11;

    public static int WINDOW_SELECTED = 0;

    public static String PATH_IMG_SELECCION = "";

    public static Usuario USUARIO_SESSION = new Usuario();
    public static final int N_LOGIN_NEW = 1;

    public static final int N_LOGIN_AVAILABLE = 2;
    public static final int N_LOGIN_UNAVAILABLE = 3;

    public static int N_LOGIN_BLOCKED = 4;
    public static int ESTADO_PERFIL_ACTIVO = 2;
    public static int NUM_CHARACTERS_DNI = 8;

    public static String MSG_ERROR_USUARIO_INCORRECTO = "El usuario no existe.";
    public static String MSG_ERROR_PASSWORD_INCORRECTO = "La clave es incorrecta.";
    public static String MSG_ERROR_VAL_PERFIL = "Este usuario no tiene el perfil para el acceso al sistema.";
    public static String MSG_USUARIO_SESSION_ACTIVE = "Ya ha iniciado sesión desde otro equipo.";
    public static String MSG_ERROR_PASSWORD_VACIO = "Debe ingresar una clave.";
    public static String MSG_ERROR_USUARIO_VACIO = "Debe ingresar un usuario.";
    public static String MSG_ERROR_USUARIO_PASSWORD_VACIO = "Usuario y clave requeridos.";
    public static String MSG_EXCEPTION_001 = "Ocurrio un error inesperado: ";
    public static String MSG_ERROR_NUM_FAILED_LOGINS = "Ha superado los 5 intentos de acceso al sistema. Su usuario ha sido bloqueado.";
    public static String MSG_ERROR_PERFIL_INACTIVO = "Este usuario está inactivo.";
    public static String MSG_CLOSE_SESSION = "¿Está seguro que desea salir?";
    public static String MSG_REALIZE_PUESTA_CERO = "Ya se ha realizado una Selección de 25, debe ejecutar una Puesta a Cero primero.";
    public static String MSG_DEBE_REALIZAR_PUESTA_CERO = "El proceso de Selección de 25 ha sido interrumpido, debe ejecutar una Puesta a Cero primero.";
    public static String MSG_PRIMERO_PUESTA_CERO = "Debe ejecutar una Puesta a Cero primero.";
    public static String MSG_ATENCION_PUESTA_CERO = "Esta acción vuelve a cero todos los procesos ejecutados hasta el momento. ¿Está seguro que desea continuar?";
    public static String MSG_SUCCESS_EXPORT = "Se ha exportado la base de datos con éxito.";
    public static String MSG_ERROR_EXPORT = "Ha ocurrido un error, verifique que el sistema cumpla los requisitos de exportación.";
    public static String MSG_INFORMATION_DPTO_MESA = "Debe seleccionar un departamento o ingresar una mesa.";
    public static String MSG_ATENCION_RESPALDO = "¿Está seguro que desea generar un respaldo?";
    public static String MSG_ERROR_PATH_RESPALDO_ESPACIOS = "El nombre de la ruta especificada no debe contener espacios en blanco";//Las carpetas o ruta seleccionada, no deben contener espacios.";//"La ruta especificada, no debe contener espacios.";

    public static int LOGIN_FAILED = 5;
    public static int NUM_FAILED_LOGINS;

    public static Reporte N__SELECTED_REPORT;

    public static int N_OPTION_PRINT = 1;
    public static int N_OPTION_SAVEAS = 2;

    public static String NAME_DUMP = "EVA_EG.DMP";

    public static String N_NAME_RPT_CONFIRMACION_PUESTA_CERO = "LISTA DE CONFIRMACIÓN DE PUESTA A CERO DEL PROCESO DE SELECCIÓN DE LOS 25 CANDIDATOS AL CARGO DE MIEMBROS DE MESA";
    public static String N_NAME_RPT_VERSION = "VERSIÓN DEL SISTEMA";

    public static String N_RPT_LISTA_PADRON_ELECTORAL = "RPT010303";
    public static String N_RPT_LISTA_PADRON_X_PESOS = "RPT010307";
    public static String N_RPT_LISTA_25_SELECCIONADOS = "RPT010305";
    public static String N_RPT_GRADO_INSTUCCION_PADRON_ELECTORAL = "RPT010301";
    public static String N_RPT_GRADO_INSTRUCCION_25_SELECCIONADOS = "RPT010302";
    public static String N_RPT_PESOS_X_RANGO_EDAD_N1 = "RPT010312";
    public static String N_RPT_PESOS_X_GRADO_INSTRUCCION_N1 = "RPT010308";
    public static String N_RPT_PESOS_X_DISCAPACIDAD_N1 = "RPT010310";
    public static String N_RPT_PESOS_X_RANGO_EDAD_N3 = "RPT010309";
    public static String N_RPT_PESOS_X_FRECUENCIA_MM = "RPT010311";
    public static String N_RPT_MOTIVOS_EXCLUSION = "RPT010306";
    public static String N_RPT_LISTA_CIUDADANOS_EXCLUIDOS = "RPT010304";
    public static String N_RPT_LISTA_CIUDADANOS_MULTI_EXCLUIDOS = "RPT010313";
    public static String N_RPT_LISTA_CIUDADANOS_POR_MOTIVO_EXCLUSION = "RPT010314";
    public static String N_RPT_FRECUENCIA_MM_POR_CIUDADANO = "RPT010318";
    public static String N_RPT_MESAS_ELECTORES_POR_UBIGEO = "RPT010316";

    public static String JASPER_NAME_RPT_LISTA_PADRON_ELECTORAL = "Reporte_Padron_Electoral.jasper";
    public static String JASPER_NAME_RPT_LISTA_PADRON_X_PESOS = "Reporte_Padron_Con_Pesos.jasper";
    public static String JASPER_NAME_RPT_LISTA_25_SELECCIONADOS = "Reporte_25_Seleccionados.jasper";
    public static String JASPER_NAME_RPT_GRADO_INSTRUCCION_PADRON_ELECTORAL = "Reporte_gradosInstruccion.jasper";
    public static String JASPER_NAME_RPT_GRADO_INSTRUCCION_25_SELECCIONADOS = "Reporte_gradosInstruccion_25.jasper";
    public static String JASPER_NAME_RPT_PESOS_X_RANGO_EDAD_N1 = "Reporte_pesos_rango_edad_n1.jasper";
    public static String JASPER_NAME_RPT_PESOS_X_GRADO_INSTRUCCION_N1 = "Reporte_pesos_grado_instruccion_n1.jasper";
    public static String JASPER_NAME_RPT_PESOS_X_DISCAPACIDAD_N1 = "Reporte_pesos_discapacidad_n1.jasper";
    public static String JASPER_NAME_RPT_PESOS_X_RANGO_EDAD_N3 = "Reporte_pesos_rango_edad_n3.jasper";
    public static String JASPER_NAME_RPT_PESOS_X_FRECUENCIA_MM = "Reporte_pesos_frecuencia_mm_n2.jasper";
    public static String JASPER_NAME_RPT_MOTIVOS_EXCLUSION = "Reporte_Motivos_Exclusion.jasper";
    public static String JASPER_NAME_RPT_LISTA_CIUDADANOS_EXCLUIDOS = "Reporte_Ciudadanos_Excluidos.jasper";
    public static String JASPER_NAME_RPT_LISTA_CIUDADANOS_MULTI_EXCLUIDOS = "Reporte_Ciudadanos_MultiExclusion.jasper";
    public static String JASPER_NAME_RPT_LISTA_CIUDADANOS_MULTI_EXCLUIDOS_SUBREPORT = "resumenCiudadanosExclusion.jasper";
    public static String JASPER_NAME_RPT_LISTA_CIUDADANOS_POR_MOTIVO_EXCLUSION = "Reporte_Ciudadanos_Por_Motivo_Exclusion.jasper";
    public static String JASPER_NAME_RPT_LISTA_FRECUENCIA_MM_POR_CIUDADANO = "frecuencia_mm_por_ciudadano.jasper";
    public static String JASPER_NAME_RPT_CONFIRMACION_PUESTA_CERO = "puestaCero.jasper";
    public static String JASPER_NAME_RPT_CONFIRMACION_SELECCION = "ConfirmacionSeleccion.jasper";
    public static String JASPER_NAME_RPT_VERSION = "Reporte_Version.jasper";

    public static int N_LIMIT_REPORT_VIEW = 50;
    
    public static String CB_REPORT_OPTION_SELECT = "- SELECCIONE -";
    public static String CB_REPORT_OPTION_ALL = "- TODOS -";
    public static String STR_FECHA_HORA_INICIO = "";
    public static String STR_FECHA_HORA_FIN = "";

    public static String HEADER_TITLE_PUESTA_CERO = "PUESTA A CERO";
    public static String HEADER_TITLE_SELECCION25 = "SELECCIÓN DE 25";
    public static String HEADER_TITLE_REPORTES = "REPORTES";

    public static String STYLE_PUESTA_CERO = "title_module_puesta0";
    public static String STYLE_SELECCION25 = "title_module_seleccion";
    public static String STYLE_REPORTES = "title_module_reportes";

    public static String PATH_LOGO_ONPE = "pe/gob/onpe/eva/seleccion/view/image/logo_onpe.jpg";
    public static String PATH_REPORT_JRXML = "pe/gob/onpe/eva/seleccion/view/jrxml/";

    public static String STR_VERSION_CODIGO = "";

    public static Boolean SAVE_EXPORT = false;

    public static String APP_NAME = "Eva_Seleccion.exe";

    public static List lstReport = null;

    public static boolean PRINCIPAL_FXML = false;
    
    public static final String COD_ETIQ_ODPE = "0";
    public static final String COD_ETIQ_DPTO = "1";
    public static final String COD_ETIQ_PROV = "2";
    public static final String COD_ETIQ_DIST = "3";
    public static final String COD_ETIQ_DPTO_PROV_DIST = "4";

    public static final int TIPO_PARAMETRO_FECHA = 20;
    public static final int TIPO_PARAMETRO_PATHS = 29;
    public static final int TIPO_PARAMETRO_ETIQUETAS = 28;
    public static final int TIPO_PARAMETRO_COPIAS = 30;

    public static final String COD_PATH_PDF = "PATHPDF";
    public static final String COD_PATH_DB = "PATHDB";
    
    public static final String COD_COPIAS = "COPIA";

    public static final String COD_FECHA_SELECCION = "SELEC";
    public static final String COD_FECHA_SORTEO = "SORTEO";
    public static final String COD_FECHA_TACHAS = "TACHAS";
    public static final String COD_FECHA_EXCUSAS_JUSTIFICACIONES = "SOLIC";

    public static Parametro PARAM_FECHA_SELECCION = new Parametro();
    public static Parametro PARAM_FECHA_SORTEO = new Parametro();
    public static Parametro PARAM_FECHA_TACHAS = new Parametro();
    public static Parametro PARAM_FECHA_EXCUSAS = new Parametro();

    public static Configuracion CONFIGURACION = new Configuracion();
    
    public static String N_SELECT_W = "";
    public static final String N_PUESTACERO = "PUESTA CERO";
    public static final String N_SELECCION25 = "SELECCIÓN 25";
    public static final String N_REPORTES = "REPORTES";
    public static final String N_RESPALDO = "RESPALDO";

}
