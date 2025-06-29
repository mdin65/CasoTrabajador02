package Controlador;

import Modelo.Trabajador;
import Persistencia.ArchivoTrabajadorDAO;
import Persistencia.DataTrabajador;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class TrabajadorControllerTest {
    private TrabajadorController controller;

    @BeforeEach
    public void setUp() {
        DataTrabajador dataHandler = new ArchivoTrabajadorDAO();
        controller = new TrabajadorController(dataHandler);
        controller.setModoPruebas(true); // Activamos modo pruebas
        controller.getTrabajadores().clear();
    }

    @Test
    public void testAgregarTrabajadorValido() {
        // Simulamos entrada de usuario
        String input = "Juan\nPerez\n12345678-9\nFonasa\nHabitat\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner sc = new Scanner(in);

        controller.agregarTrabajador(sc);

        assertEquals(1, controller.getTrabajadores().size());
        assertTrue(controller.getTrabajadores().containsKey("12.345.678-9"));
    }

    @Test
    public void testAgregarTrabajadorConRUTInvalido() {
        // Primero un RUT inválido, luego uno válido
        String input = "Maria\nGonzalez\n1234\n12345678-k\nColmena\nModelo\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner sc = new Scanner(in);

        controller.agregarTrabajador(sc);

        assertEquals(1, controller.getTrabajadores().size());
        assertTrue(controller.getTrabajadores().containsKey("12.345.678-K"));
    }

    @Test
    public void testEditarTrabajadorExistente() {
        // Agregamos un trabajador primero
        Trabajador t = new Trabajador("Carlos", "Lopez", "12.345.678-9", "Fonasa", "Habitat");
        controller.getTrabajadores().put("12.345.678-9", t);

        // Simulamos editar el nombre (opción 1)
        String input = "12.345.678-9\n1\nPedro\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner sc = new Scanner(in);

        controller.editarTrabajador(sc);

        assertEquals("Pedro", controller.getTrabajadores().get("12.345.678-9").getNombre());
    }

    @Test
    public void testEliminarTrabajadorConConfirmacion() {
        // Agregamos un trabajador primero
        Trabajador t = new Trabajador("Ana", "Silva", "11.222.333-4", "Banmedica", "Capital");
        controller.getTrabajadores().put("11.222.333-4", t);

        // Simulamos confirmación positiva (S)
        String input = "11.222.333-4\nS\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner sc = new Scanner(in);

        controller.eliminarTrabajador(sc);

        assertEquals(0, controller.getTrabajadores().size());
    }

    @Test
    public void testEliminarTrabajadorConCancelacion() {
        // Agregamos un trabajador primero
        Trabajador t = new Trabajador("Ana", "Silva", "11.222.333-4", "Banmedica", "Capital");
        controller.getTrabajadores().put("11.222.333-4", t);

        // Simulamos confirmación negativa (N)
        String input = "11.222.333-4\nN\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner sc = new Scanner(in);

        controller.eliminarTrabajador(sc);

        assertEquals(1, controller.getTrabajadores().size());
    }

}