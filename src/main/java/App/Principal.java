package App;

import Modelo.Trabajador;
import Util.CargaDatos;
import Vista.MenuCLI;

import java.util.Map;

public class Principal {
    public static void main(String[] args) {
        Map<String, Trabajador> trabajadores = CargaDatos.cargaTrabajadoresDesdeResources();
        new MenuCLI(trabajadores).iniciar();
    }
}