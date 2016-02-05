package pe.gob.onpe.eva.seleccion.service.iface;

import pe.gob.onpe.eva.common.dto.EmpleadoDTO;
import pe.gob.onpe.eva.common.dto.LoginDTO;
import pe.gob.onpe.eva.model.Empleado;
import pe.gob.onpe.eva.model.Usuario;

/**
 *
 * @author Jose Limachi
 */
public interface IUsuarioService {

    public void logout(Usuario usuario);

    public LoginDTO validateLogin(Usuario usuario);

    public void changeStatusBackup(Usuario usuario, Integer status);
}
