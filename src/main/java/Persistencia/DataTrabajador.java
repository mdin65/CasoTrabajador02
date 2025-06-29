package Persistencia;

import Modelo.Trabajador;
import java.util.Map;

public interface DataTrabajador {
    Map<String, Trabajador> cargarTrabajadores();
    void guardarTrabajadores(Map<String, Trabajador> trabajadores);
}
