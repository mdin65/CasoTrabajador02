package Controlador;

import Modelo.Trabajador;
import Util.CargaDatos;
import Util.ValidadorRUT;

import java.util.Map;
import java.util.Scanner;

public class TrabajadorController {
    private Map<String, Trabajador> trabajadores;
    private boolean modoPruebas = false; // Flag para utilizar caso prueba, asi no elimina el documento

    // Constructor
    public TrabajadorController(Map<String, Trabajador> trabajadores) {
        this.trabajadores = trabajadores;
    }

    public void mostrarTodos() {
        System.out.println("\n--- Lista de Trabajadores ---");
        for (Trabajador t : trabajadores.values()) {
            System.out.println("Nombre: " + t.getNombre());
            System.out.println("Apellido: " + t.getApellido());
            System.out.println("RUT: " + t.getRut());
            System.out.println("Isapre: " + t.getIsapre());
            System.out.println("AFP: " + t.getAfp());
            System.out.println("----------------------");
        }
    }

    public void agregarTrabajador(Scanner sc) {
        System.out.println("\n--- Agregar Nuevo Trabajador ---");

        String nombre = obtenerDatoValido(sc, "Nombre");
        String apellido = obtenerDatoValido(sc, "Apellido");

        String rut;
        do {
            System.out.print("RUT (formato 12345678-9): ");
            rut = sc.nextLine();

            if (!ValidadorRUT.validarFormatoRUT(rut)) {
                System.out.println("RUT inválido. Por favor ingrese un RUT válido.");
                continue;
            }

            rut = ValidadorRUT.formatearRUT(rut);

            if (trabajadores.containsKey(rut)) {
                System.out.println("Este RUT ya está registrado.");
                continue;
            }

            break;
        } while (true);

        String isapre = obtenerDatoValido(sc, "Isapre");
        String afp = obtenerDatoValido(sc, "AFP");

        Trabajador nuevo = new Trabajador(nombre, apellido, rut, isapre, afp);
        trabajadores.put(rut, nuevo);
        CargaDatos.guardarTrabajadores(trabajadores);

        System.out.println("Trabajador agregado exitosamente!");
    }

    private String obtenerDatoValido(Scanner sc, String campo) {
        String dato;
        do {
            System.out.print(campo + ": ");
            dato = sc.nextLine().trim();

            if (dato.isEmpty()) {
                System.out.println("Error: El " + campo.toLowerCase() + " no puede estar vacío");
                continue;
            }

            return dato;
        } while (true);
    }


    public void editarTrabajador(Scanner sc) {
        String rut = obtenerRUTParaEdicion(sc);
        if (rut == null) return;

        Trabajador trabajador = trabajadores.get(rut);
        int opcion = mostrarMenuEdicionYObtenerOpcion(sc);

        if (opcion == -1) return;

        ejecutarEdicion(opcion, trabajador, sc);
        guardarCambios();
    }

    private String obtenerRUTParaEdicion(Scanner sc) {
        System.out.print("\nIngrese RUT del trabajador a editar: ");
        String rut = sc.nextLine();

        if (!trabajadores.containsKey(rut)) {
            System.out.println("Trabajador no encontrado.");
            return null;
        }
        return rut;
    }

    private int mostrarMenuEdicionYObtenerOpcion(Scanner sc) {
        System.out.println("\nSeleccione el atributo a modificar:");
        System.out.println("1. Nombre");
        System.out.println("2. Apellido");
        System.out.println("3. Isapre");
        System.out.println("4. AFP");
        System.out.print("Opción: ");

        try {
            return Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Opción no válida");
            return -1;
        }
    }

    private void ejecutarEdicion(int opcion, Trabajador trabajador, Scanner sc) {
        switch(opcion) {
            case 1:
                actualizarNombre(trabajador, sc);
                break;
            case 2:
                actualizarApellido(trabajador, sc);
                break;
            case 3:
                actualizarIsapre(trabajador, sc);
                break;
            case 4:
                actualizarAFP(trabajador, sc);
                break;
            default:
                System.out.println("Opción no válida");
        }
    }

    private void actualizarNombre(Trabajador trabajador, Scanner sc) {
        System.out.print("Nuevo nombre: ");
        trabajador.setNombre(sc.nextLine());
    }

    private void actualizarApellido(Trabajador trabajador, Scanner sc) {
        System.out.print("Nuevo apellido: ");
        trabajador.setApellido(sc.nextLine());
    }

    private void actualizarIsapre(Trabajador trabajador, Scanner sc) {
        System.out.print("Nueva Isapre: ");
        trabajador.setIsapre(sc.nextLine());
    }

    private void actualizarAFP(Trabajador trabajador, Scanner sc) {
        System.out.print("Nueva AFP: ");
        trabajador.setAfp(sc.nextLine());
    }

    private void guardarCambios() {
        CargaDatos.guardarTrabajadores(trabajadores);
        System.out.println("Cambios guardados exitosamente!");
    }
    public void eliminarTrabajador(Scanner sc) {
        String rut = obtenerRUTParaEliminar(sc);
        if (rut == null) return;

        if (!confirmarEliminacion(sc, trabajadores.get(rut))) {
            System.out.println("Eliminación cancelada");
            return;
        }

        ejecutarEliminacion(rut);

        if (!modoPruebas) {
            CargaDatos.guardarTrabajadores(trabajadores);
        }
    }

    private String obtenerRUTParaEliminar(Scanner sc) {
        System.out.print("\nIngrese RUT del trabajador a eliminar: ");
        String rut = sc.nextLine();

        if (!ValidadorRUT.validarFormatoRUT(rut)) {
            System.out.println("RUT inválido");
            return null;
        }

        rut = ValidadorRUT.formatearRUT(rut);

        if (!trabajadores.containsKey(rut)) {
            System.out.println("Trabajador no encontrado");
            return null;
        }

        return rut;
    }

    private boolean confirmarEliminacion(Scanner sc, Trabajador trabajador) {
        System.out.println("\nDatos del trabajador a eliminar:");
        System.out.println("Nombre: " + trabajador.getNombre());
        System.out.println("Apellido: " + trabajador.getApellido());
        System.out.println("RUT: " + trabajador.getRut());
        System.out.println("Isapre: " + trabajador.getIsapre());
        System.out.println("AFP: " + trabajador.getAfp());

        System.out.print("\n¿Está seguro que desea eliminar? (S/N): ");
        String confirmacion = sc.nextLine().toUpperCase();

        return confirmacion.equals("S") || confirmacion.equals("SI");
    }

    private void ejecutarEliminacion(String rut) {
        trabajadores.remove(rut);
        System.out.println("Trabajador eliminado: " + rut);
    }

    public void setModoPruebas(boolean activar) {
        this.modoPruebas = activar;
    }

}