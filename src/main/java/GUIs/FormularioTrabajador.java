package GUIs;

import Controlador.TrabajadorController;
import Modelo.Trabajador;
import Util.ValidadorRUT;

import javax.swing.*;
import java.awt.*;

public class FormularioTrabajador extends JDialog {
    private final JTextField nombreField = new JTextField(15);
    private final JTextField apellidoField = new JTextField(15);
    private final JTextField rutField = new JTextField(15);
    private final JTextField isapreField = new JTextField(15);
    private final JTextField afpField = new JTextField(15);

    public FormularioTrabajador(JFrame parent, TrabajadorController controller, Trabajador trabajador) {
        super(parent, true);
        setTitle(trabajador == null ? "Agregar trabajador" : "Editar trabajador");
        setSize(300, 250);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(6, 2));

        // Campos y etiquetas
        add(new JLabel("Nombre:"));
        add(nombreField);
        add(new JLabel("Apellido:"));
        add(apellidoField);
        add(new JLabel("RUT:"));
        add(rutField);
        add(new JLabel("Isapre:"));
        add(isapreField);
        add(new JLabel("AFP:"));
        add(afpField);

        JButton btnGuardar = getJButton(controller, trabajador);

        // Espacio vacío + botón guardar
        add(new JLabel());
        add(btnGuardar);


        if (trabajador != null) {
            nombreField.setText(trabajador.getNombre());
            apellidoField.setText(trabajador.getApellido());
            rutField.setText(trabajador.getRut());
            rutField.setEnabled(false); // RUT no editable
            isapreField.setText(trabajador.getIsapre());
            afpField.setText(trabajador.getAfp());
        }

        setVisible(true);
    }

    private JButton getJButton(TrabajadorController controller, Trabajador trabajador) {
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(_ -> {
            String nombre = nombreField.getText().trim();
            String apellido = apellidoField.getText().trim();
            String rut = rutField.getText().trim().toUpperCase();
            String isapre = isapreField.getText().trim();
            String afp = afpField.getText().trim();

            if (nombre.isEmpty() || apellido.isEmpty() || rut.isEmpty() || isapre.isEmpty() || afp.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!ValidadorRUT.validarFormatoRUT(rut)) {
                JOptionPane.showMessageDialog(this, "RUT inválido. Debe estar en formato 12345678-9.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            rut = ValidadorRUT.formatearRUT(rut);

            if (trabajador == null && controller.getTrabajadores().containsKey(rut)) {
                JOptionPane.showMessageDialog(this, "Este RUT ya está registrado.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Trabajador nuevo = new Trabajador(nombre, apellido, rut, isapre, afp);

            controller.getTrabajadores().put(rut, nuevo);
            controller.guardar();
            dispose();
        });
        return btnGuardar;
    }
}
