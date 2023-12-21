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
import java.util.Iterator;

public class Registro extends JFrame {
    JLabel lblTitulo = new JLabel("BIBLIOTECA U");
    ImageIcon imagen = new ImageIcon("logo.png");
    JLabel lblimagen = new JLabel();
    public JTextField campoNombre;
    public JPasswordField campoContrasena;
    private JPanel panel;
    private JRadioButton radioJefe;
    private JRadioButton radioNormal;
    JButton btnEliminarCuenta = new JButton("Eliminar Cuenta");
    DatosBibliotecario datosBibliotecario = new DatosBibliotecario();
    public Registro() {
        setTitle("Registro y eliminacion de bibliotecario: Biblioteca");
        setSize(340,460);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        Componentes();
        CamposTexto();
        ColocarTexto();
        ColocarBoton();

    }

    public void Componentes(){
        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);
        getContentPane().add(panel);
    }
    public void CamposTexto (){
        campoNombre = new JTextField();
        campoNombre.setBounds(20,235, 170,20);
        panel.add(campoNombre);

        campoContrasena = new JPasswordField();
        campoContrasena.setBounds(20,295,170,20);
        panel.add(campoContrasena);
    }
    public void ColocarTexto () {
        JLabel ingresaNombre = new JLabel();
        ingresaNombre.setText("Nombre:");
        ingresaNombre.setBounds(20, 220, 65, 10);
        panel.add(ingresaNombre);

        lblTitulo.setBounds(120,10,90,25);
        panel.add(lblTitulo);

        lblimagen.setBounds(100,50,140,160);
        lblimagen.setIcon(new ImageIcon(imagen.getImage().getScaledInstance(140,160, Image.SCALE_SMOOTH)));
        panel.add(lblimagen);

        JLabel ingresaContrasena = new JLabel();
        ingresaContrasena.setText("Contraseña:");
        ingresaContrasena.setBounds(20,280,80,10);
        panel.add(ingresaContrasena);

        radioJefe = new JRadioButton("Jefe");
        radioJefe.setBounds(20, 320, 70, 25);
        panel.add(radioJefe);

        radioNormal = new JRadioButton("Normal");
        radioNormal.setBounds(100, 320, 80, 25);
        panel.add(radioNormal);

        ButtonGroup grupoRadioButtons = new ButtonGroup();
        grupoRadioButtons.add(radioJefe);
        grupoRadioButtons.add(radioNormal);
    }
    public void ColocarBoton (){
        JButton botonRegistrar = new JButton();
        botonRegistrar.setText("Crear Cuenta");
        botonRegistrar.setBounds(20, 350, 120, 25);
        panel.add(botonRegistrar);

        botonRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = campoNombre.getText();
                String contraseña = campoContrasena.getText();
                if (campoNombre.getText().equals("") || campoContrasena.getText().equals(""))
                {
                    JOptionPane.showMessageDialog(null,"Digite información correcta en los campos");
                }
                else{
                    if(radioJefe.isSelected()){
                        // se envían los datos ingresados a la clase datosBibliotecario
                        datosBibliotecario.nuevoNombre = nombre;
                        datosBibliotecario.nuevaContraseña = contraseña;
                        datosBibliotecario.BibliotecarioMaster();
                        datosBibliotecario.nuevaSesion();
                    } else if (radioNormal.isSelected()) {
                        datosBibliotecario.nuevoNombre = nombre;
                        datosBibliotecario.nuevaContraseña = contraseña;
                        datosBibliotecario.BibliotecarioNormal();
                        datosBibliotecario.nuevaSesionNormal();
                    }
                    campoContrasena.setText("");
                    campoNombre.setText("");
                }
            }
        });
        //Inicio de sesión de un nuevo o existente bibliotecario
        JButton botonSesion = new JButton();
        botonSesion.setText("Iniciar Sesion");
        botonSesion.setBounds(180, 350 ,120, 25);
        panel.add(botonSesion);

        botonSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = campoNombre.getText();
                String contrasena = campoContrasena.getText();
                boolean datosValidos = false;

                try {
                    FileInputStream fileInJefe = new FileInputStream("jefe.bin");
                    ObjectInputStream objectInJefe = new ObjectInputStream(fileInJefe);
                    HashMap<String, String> cuentaJefe = (HashMap<String, String>) objectInJefe.readObject();
                    objectInJefe.close();
                    fileInJefe.close();

                    FileInputStream fileInNormal = new FileInputStream("normal.bin");
                    ObjectInputStream objectInNormal = new ObjectInputStream(fileInNormal);
                    HashMap<String,String> cuentaNormal = (HashMap<String, String>) objectInNormal.readObject();
                    objectInNormal.close();
                    fileInNormal.close();

                    if (cuentaJefe.containsKey(nombre) && cuentaJefe.get(nombre).equals(contrasena)) {
                        BibliotecaGUI.menuitemregistro.setEnabled(true);
                        datosValidos = true;

                    }
                    if(cuentaNormal.containsKey(nombre) && cuentaNormal.get(nombre).equals(contrasena)){
                        BibliotecaGUI.menuitemregistro.setEnabled(false);
                        datosValidos = true;
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }

                if (datosValidos) {
                    BibliotecaGUI a = new BibliotecaGUI();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor, verifique los datos");
                }
            }
        });

        btnEliminarCuenta.setBounds(100,390,130,25);
        panel.add(btnEliminarCuenta);

        btnEliminarCuenta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = campoNombre.getText();
                String contrasena = campoContrasena.getText();
                if(radioJefe.isSelected()){
                    datosBibliotecario.eliminarCuenta("jefe.bin", nombre, contrasena);
                }else if (radioNormal.isSelected()){
                    datosBibliotecario.eliminarCuenta("normal.bin", nombre, contrasena);

                }else{
                    JOptionPane.showMessageDialog(null,"Elija la la categoria en que se encuentra la cuenta");
                }
                campoNombre.setText("");
                campoContrasena.setText("");
            }
        });
    }
}