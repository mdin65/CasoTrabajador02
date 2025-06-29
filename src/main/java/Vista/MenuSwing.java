package Vista;

import Controlador.TrabajadorController;
import GUIs.FormularioTrabajador;
import Modelo.Trabajador;
import Persistencia.DataTrabajador;

import javax.swing.*;
import java.awt.*;

public class MenuSwing extends JFrame implements MenuUI {
    private final TrabajadorController controller;

    public MenuSwing(DataTrabajador dataHandler) {
        this.controller = new TrabajadorController(dataHandler);
        setTitle("Gestión de Trabajadores");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JButton btnMostrar = new JButton("Ver trabajadores");
        JButton btnAgregar = new JButton("Agregar trabajador");
        JButton btnEditar = new JButton("Editar trabajador");
        JButton btnEliminar = new JButton("Eliminar trabajador");

        btnMostrar.addActionListener(_ -> mostrarTrabajadores());
        btnAgregar.addActionListener(_ -> new FormularioTrabajador(this, controller, null));
        btnEditar.addActionListener(_ -> editarTrabajador());
        btnEliminar.addActionListener(_ -> eliminarTrabajador());

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.add(btnMostrar);
        panel.add(btnAgregar);
        panel.add(btnEditar);
        panel.add(btnEliminar);

        add(panel, BorderLayout.CENTER);
    }

    private void mostrarTrabajadores() {
        StringBuilder info = new StringBuilder();
        for (Trabajador t : controller.getTrabajadores().values()) {
            info.append("Nombre: ").append(t.getNombre()).append("\n");
            info.append("Apellido: ").append(t.getApellido()).append("\n");
            info.append("RUT: ").append(t.getRut()).append("\n");
            info.append("Isapre: ").append(t.getIsapre()).append("\n");
            info.append("AFP: ").append(t.getAfp()).append("\n");
            info.append("--------------\n");
        }
        JOptionPane.showMessageDialog(this, info.toString(), "Lista de trabajadores", JOptionPane.INFORMATION_MESSAGE);
    }

    private void editarTrabajador() {
        String rut = JOptionPane.showInputDialog(this, "Ingrese RUT a editar:");
        if (rut != null && controller.getTrabajadores().containsKey(rut)) {
            new FormularioTrabajador(this, controller, controller.getTrabajadores().get(rut));
        } else {
            JOptionPane.showMessageDialog(this, "Trabajador no encontrado.");
        }
    }

    private void eliminarTrabajador() {
        String rut = JOptionPane.showInputDialog(this, "Ingrese RUT a eliminar:");
        if (rut != null && controller.getTrabajadores().containsKey(rut)) {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea eliminar?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                controller.eliminarPorRut(rut);
                JOptionPane.showMessageDialog(this, "Trabajador eliminado.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Trabajador no encontrado.");
        }
    }

    @Override
    public void iniciar() {
        setVisible(true);
    }
}
