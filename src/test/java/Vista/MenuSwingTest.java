package Vista;

import Controlador.TrabajadorController;
import Modelo.Trabajador;
import Persistencia.ArchivoTrabajadorDAO;
import Persistencia.DataTrabajador;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import static org.junit.jupiter.api.Assertions.*;

public class MenuSwingTest {
    protected MenuSwing menuSwing;
    protected TrabajadorController controller;

    @BeforeEach
    void setUp() {
        DataTrabajador dataHandler = new ArchivoTrabajadorDAO();
        menuSwing = new MenuSwing(dataHandler);
        controller = new TrabajadorController(dataHandler);
        menuSwing.setController(controller); // Uso del método protegido
    }

    @Test
    void testConstructorInicializaController() {
        DataTrabajador dataHandler = new ArchivoTrabajadorDAO();

        MenuSwing menu = new MenuSwing(dataHandler);

        assertNotNull(menu.getContentPane().getComponents(), "Debe tener componentes inicializados");
        assertDoesNotThrow(() -> {
            Field field = MenuSwing.class.getDeclaredField("controller");
            field.setAccessible(true);
            assertNotNull(field.get(menu), "Controller no debe ser null");
        });
    }
    @Test
    void testSetControllerActualizaCorrectamente() {
        MenuSwing menu = new MenuSwing(new ArchivoTrabajadorDAO());
        TrabajadorController nuevoController = new TrabajadorController(new ArchivoTrabajadorDAO());

        menu.setController(nuevoController);

        assertDoesNotThrow(() -> {
            Field field = MenuSwing.class.getDeclaredField("controller");
            field.setAccessible(true);
            assertEquals(nuevoController, field.get(menu), "Controller no se actualizó correctamente");
        });
    }
    @Test
    void testBotonMostrarTrabajadores() {
        MenuSwing menu = new MenuSwing(new ArchivoTrabajadorDAO());
        TrabajadorController testController = new TrabajadorController(new ArchivoTrabajadorDAO());
        testController.getTrabajadores().put("12345678-9",
                new Trabajador("Juan", "Pérez", "12345678-9", "Fonasa", "Habitat"));
        menu.setController(testController);

        boolean ejecucionExitosa = false;
        for (Component comp : menu.getContentPane().getComponents()) {
            if (comp instanceof JPanel) {
                for (Component btn : ((JPanel)comp).getComponents()) {
                    if (btn instanceof JButton && ((JButton)btn).getText().equals("Ver trabajadores")) {
                        ((JButton)btn).doClick();
                        ejecucionExitosa = true;
                    }
                }
            }
        }

        assertTrue(ejecucionExitosa, "Debe encontrar y hacer click en el botón");
    }

}