package Vista;

import Controlador.TrabajadorController;
import Modelo.Trabajador;

import java.util.Map;
import java.util.Scanner;

public class MenuCLI {
    private Map<String, Trabajador> trabajadores;
    private Scanner scanner;
    private TrabajadorController controller;

    public MenuCLI(Map<String, Trabajador> trabajadores) {
        this.trabajadores = trabajadores;
        this.scanner = new Scanner(System.in);
        this.controller = new TrabajadorController(trabajadores);
    }

    public void iniciar() {
        int opcion;
        do {
            mostrarMenuPrincipal();
            opcion = obtenerOpcionUsuario();
            ejecutarOpcion(opcion);
        } while (opcion != 5);

        scanner.close();
    }

    private void mostrarMenuPrincipal() {
        System.out.println("\n--- Menú Principal ---");
        System.out.println("1. Ver todos los trabajadores");
        System.out.println("2. Agregar nuevo trabajador");
        System.out.println("3. Editar trabajador");
        System.out.println("4. Eliminar trabajador");  // Nueva opción
        System.out.println("5. Salir");                // Cambiado de 4 a 5
        System.out.print("Seleccione: ");
    }

    private int obtenerOpcionUsuario() {
        while (!scanner.hasNextInt()) {
            System.out.println("Entrada inválida. Por favor ingrese un número.");
            scanner.next(); // Limpiar entrada inválida
            System.out.print("Seleccione: ");
        }
        int opcion = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer
        return opcion;
    }

    private void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                controller.mostrarTodos();
                break;
            case 2:
                controller.agregarTrabajador(scanner);
                break;
            case 3:
                controller.editarTrabajador(scanner);
                break;
            case 4:
                controller.eliminarTrabajador(scanner);
                break;
            case 5:
                System.out.println("Saliendo del sistema...");
                break;
            default:
                System.out.println("Opción inválida.");
        }
    }
}