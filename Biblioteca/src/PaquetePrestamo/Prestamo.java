package PaquetePrestamo;

import PaqueteLibro.Libro;
import PaqueteVentanaPrin.BibliotecaGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;


public class Prestamo extends JFrame {
    JPanel panel;
    public JTextField fechatxt;
    public JTextField nombrePrestamista;
    public static String fechaIngresada;
    public static String nombreCliente;
    public Prestamo(){
        setTitle("Préstamos");
        setSize(450,170);
        setLocationRelativeTo(null);
        Componentes();
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }
    public void Componentes(){
        panel = new JPanel();
        panel.setLayout(null);
        setBackground(Color.lightGray);
        getContentPane().add(panel);
        ColocarTexto();
        CamposTexto();
        ColocarBoton();
    }

    public void ColocarTexto(){
        JLabel texto1 = new JLabel();
        texto1.setText("Fecha de préstamo:");
        texto1.setBounds(15,20, 120,20);
        panel.add(texto1);

        JLabel texto2 = new JLabel();
        texto2.setText("Nombre del cliente:");
        texto2.setBounds(10, 60, 120,20);
        panel.add(texto2);
    }

    public void CamposTexto(){
        fechatxt = new JTextField();
        fechatxt.setBounds(135, 20, 100, 20);
        panel.add(fechatxt);

        nombrePrestamista = new JTextField();
        nombrePrestamista.setBounds(135, 60, 120,20);
        panel.add(nombrePrestamista);
    }

    public void ColocarBoton(){
        JButton prestarLibro = new JButton();
        prestarLibro.setBounds(270, 90, 150, 20);
        prestarLibro.setText("Hacer prestamo");
        panel.add(prestarLibro);

        prestarLibro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fechaIngresada = fechatxt.getText();
                nombreCliente = nombrePrestamista.getText();

                // Definir el formato de fecha esperado
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                try {
                    LocalDate fechaPrestamo = LocalDate.parse(fechaIngresada, formatter);
                    String nombre = nombreCliente;
                    String libro = BibliotecaGUI.nomLibro;
                    String categoria = BibliotecaGUI.genElegido;
                    String fecha = fechaIngresada;
                    BibliotecaGUI.actualizarTabla(nombre,libro,categoria, fecha);

                    // Cambiar el estado del libro a "No disponible"
                    if (Libro.getCategorias().containsKey(categoria)) {
                        HashMap<String, String> librosCategoria = Libro.getCategorias().get(categoria);
                        if (librosCategoria.containsKey(libro)) {
                            librosCategoria.put(libro, "No disponible");
                            System.out.println("El libro '" + libro + "' ha cambiado a estado 'No disponible'.");
                        }
                    }
                    // Guardar los cambios en el archivo categorias.bin
                    Libro.guardarLibros();
                    BibliotecaGUI.cbLibro.removeItem(BibliotecaGUI.cbLibro.getSelectedItem());
                    BibliotecaGUI.guardarDatosTabla();
                    dispose();

                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(null,"Error de formato de fecha: " + ex.getMessage());
                }
            }
        });
    }
}