package Persistencia;

import Modelo.Trabajador;
import Util.CargaDatos;

import java.util.Map;

public class ArchivoTrabajadorDAO implements DataTrabajador {
    @Override
    public Map<String, Trabajador> cargarTrabajadores() {
        return CargaDatos.cargaTrabajadoresDesdeResources();
    }

    @Override
    public void guardarTrabajadores(Map<String, Trabajador> trabajadores) {
        CargaDatos.guardarTrabajadores(trabajadores);
    }
}