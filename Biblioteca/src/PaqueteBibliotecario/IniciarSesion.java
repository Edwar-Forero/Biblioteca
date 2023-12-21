package PaqueteBibliotecario;

import PaqueteVentanaPrin.BibliotecaGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;


public class IniciarSesion extends JFrame {

    JLabel lblTitulo = new JLabel("BIBLIOTECA U");
    ImageIcon Imagen = new ImageIcon("logo.png");
    JLabel lblImagen = new JLabel();
    public JTextField campoNombre;
    public JPasswordField campoContrasena;
    private JPanel panel;
    public JButton botonConfirmar;

    public IniciarSesion() {
        setTitle("Inicio de sesion: Biblioteca");
        setSize(340, 460);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Componentes();
        CamposTexto();
        ColocarTexto();
        ColocarBoton();
        Accion();
    }

    public void Componentes() {
        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);
        getContentPane().add(panel);

    }

    public void CamposTexto() {
        campoNombre = new JTextField();
        campoNombre.setBounds(20, 235, 170, 20);
        panel.add(campoNombre);

        campoContrasena = new JPasswordField();
        campoContrasena.setBounds(20, 295, 170, 20);
        panel.add(campoContrasena);
    }

    public void ColocarTexto() {
        JLabel ingresaNombre = new JLabel();
        ingresaNombre.setText("Nombre:");
        ingresaNombre.setBounds(20, 220, 65, 10);
        panel.add(ingresaNombre);

        lblTitulo.setBounds(120, 10, 90, 25);
        panel.add(lblTitulo);

        lblImagen.setBounds(100, 50, 140, 160);
        lblImagen.setIcon(new ImageIcon(Imagen.getImage().getScaledInstance(140, 160, Image.SCALE_SMOOTH)));
        panel.add(lblImagen);

        JLabel ingresaContrasena = new JLabel();
        ingresaContrasena.setText("Contraseña:");
        ingresaContrasena.setBounds(20, 280, 80, 10);
        panel.add(ingresaContrasena);
    }

    public void ColocarBoton() {
        botonConfirmar = new JButton();
        botonConfirmar.setText("Iniciar Sesión");
        botonConfirmar.setBounds(100, 350, 120, 25);
        panel.add(botonConfirmar);
    }

    public void Accion() {
        ActionListener confirmarUsuario = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = campoNombre.getText();
                String contrasena = campoContrasena.getText();
                boolean datosValidos = false;
                // Traigo la información de los archivos .bin y los guardo en un Hashmap
                try {
                    FileInputStream fileInJefe = new FileInputStream("jefe.bin");
                    ObjectInputStream objectInJefe = new ObjectInputStream(fileInJefe);
                    HashMap<String, String> cuentaJefe = (HashMap<String, String>) objectInJefe.readObject();
                    objectInJefe.close();
                    fileInJefe.close();

                    FileInputStream fileInNormal = new FileInputStream("normal.bin");
                    ObjectInputStream objectInNormal = new ObjectInputStream(fileInNormal);
                    HashMap<String, String> cuentaNormal = (HashMap<String, String>) objectInNormal.readObject();
                    objectInNormal.close();
                    fileInNormal.close();

                    // comprueba si la cuenta ingresada está en alguno de los archivos
                    if (cuentaJefe.containsKey(nombre) && cuentaJefe.get(nombre).equals(contrasena)) {
                        BibliotecaGUI.menuitemregistro.setEnabled(true);
                        datosValidos = true;
                    }
                    if (cuentaNormal.containsKey(nombre) && cuentaNormal.get(nombre).equals(contrasena)) {
                        BibliotecaGUI.menuitemregistro.setEnabled(false);
                        datosValidos = true;
                    }

                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }

                if (datosValidos) {
                    new BibliotecaGUI();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor, verifique los datos");
                }
            }

        };
        //Agrego la acción al botón
        botonConfirmar.addActionListener(confirmarUsuario);
    }

    public static void main(String[] args) {

        IniciarSesion iniciarSesion = new IniciarSesion();
    }
}