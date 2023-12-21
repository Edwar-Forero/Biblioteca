package PaqueteLibro;

import PaqueteVentanaPrin.BibliotecaGUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.HashMap;
import javax.swing.*;

public class Libro extends JFrame implements Serializable {

    // HashMap que se usarán para guardar los valores de las categorías
    private static HashMap<String, HashMap<String, String>> categorias;
    private JComboBox<String> cbCatego;
    private JTextField txtLibro;
    private JTextField txtNewCat;
    JScrollPane scrollPane = new JScrollPane(cbCatego);
    public Libro() {
        setSize(450,300);
        setLocationRelativeTo(null);
        setVisible(false);
        ComponentesVentana();
        //Inicializamos las categorias predeterminadas
        categorias = new HashMap<>();
        categorias.put("Novelas Clásicas", crearHashMapNovelasClasicas());
        categorias.put("Terror", crearHashMapTerror());
        categorias.put("Ingeniería", crearHashMapIngenieria());
        setTitle("New Categoria/libro");
        cargarLibros();
        guardarLibros();

    }
    public void ComponentesVentana(){
        JPanel panel = new JPanel();
        panel.setLayout(null);
        setBackground(Color.lightGray);
        getContentPane().add(panel);

        JLabel lblcat = new JLabel("Categoria");
        lblcat.setBounds(5,5,100,20);
        panel.add(lblcat);

        JLabel lblLibro = new JLabel("Libro");
        lblLibro.setBounds(290,5,100,20);
        panel.add(lblLibro);

        JLabel lblNewCat = new JLabel("Categoria");
        lblNewCat.setBounds(5,180,100,20);
        panel.add(lblNewCat);

        cbCatego = new JComboBox<>();
        cbCatego.setBounds(80,5,140,20);
        panel.add(scrollPane);
        panel.add(cbCatego);

        txtLibro = new JTextField();
        txtLibro.setBounds(325,5,100,20);
        panel.add(txtLibro);

        txtNewCat = new JTextField();
        txtNewCat.setBounds(90,180,120,20);
        panel.add(txtNewCat);

        JButton btbNewLibro = new JButton("Agregar Libro");
        btbNewLibro.setBounds(290,50,140,25);
        panel.add(btbNewLibro);

        // Permite guardar un nuevo libro en la categoria elejida
        btbNewLibro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String libro = txtLibro.getText();
                String categoria = (String) cbCatego.getSelectedItem();
                agregarLibro(categoria, libro);
                guardarLibros();
                imprimirCategorias();
                txtLibro.setText("");
            }
        });

        JButton btbNewCat = new JButton("Agregar Categoria");
        btbNewCat.setBounds(50,220,140,25);
        panel.add(btbNewCat);
        // Permite guardar una nueva categoria en el archivo
        btbNewCat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nuevaCategoria = txtNewCat.getText();
                agregarCategoria(nuevaCategoria);
                guardarLibros();
                txtNewCat.setText("");
            }
        });

        JButton btbEliminarCat = new JButton("Eliminar Categoria");
        btbEliminarCat.setBounds(210,220,145,25);
        panel.add(btbEliminarCat);

        //permite eliminar una categoria del archivo
        btbEliminarCat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String categoria = txtNewCat.getText();
                eliminarCategoria(categoria);
                imprimirCategorias();
                txtNewCat.setText("");
            }
        });

        JButton btnEliminarLib = new JButton("Eliminar libro");
        btnEliminarLib.setBounds(290,90,145,25);
        panel.add(btnEliminarLib);

        //permite eliminar un libro de la categoria elejida
        btnEliminarLib.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String libro = txtLibro.getText();
                String categoria = (String) cbCatego.getSelectedItem();
                eliminarLibro(categoria, libro);
                imprimirCategorias();
                txtLibro.setText("");
            }
        });

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                new BibliotecaGUI();
                dispose();
            }
        });
    }

    // Métodos para crear los HashMap de las categorías predefinidas
    private HashMap<String, String> crearHashMapNovelasClasicas() {
        HashMap<String, String> novelasClasicasH = new HashMap<>();
        novelasClasicasH.put("Orgullo y Prejuicio", "Disponible");
        novelasClasicasH.put("Romeo y Julieta", "Disponible");
        novelasClasicasH.put("Los Miserables", "Disponible");
        novelasClasicasH.put("Drácula", "Disponible");
        novelasClasicasH.put("Cumbres Borrascosas", "Disponible");
        return novelasClasicasH;
    }
    private HashMap<String, String> crearHashMapTerror() {
        HashMap<String, String> terrorH = new HashMap<>();
        terrorH.put("La casa infernal", "Disponible");
        terrorH.put("Los Cuadernos Lovecraft", "Disponible");
        terrorH.put("Wody", "Disponible");
        terrorH.put("Temores Crecientes", "Disponible");
        terrorH.put("Seria Crave", "Disponible");
        return terrorH;
    }
    private HashMap<String, String> crearHashMapIngenieria() {
        HashMap<String, String> ingenieriaH = new HashMap<>();
        ingenieriaH.put("Teorema del Loro", "Disponible");
        ingenieriaH.put("Planilandia", "Disponible");
        ingenieriaH.put("La Mano que Piensa", "Disponible");
        ingenieriaH.put("Continuous Delivery", "Disponible");
        ingenieriaH.put("Soft Skills", "Disponible");
        return ingenieriaH;
    }
    // Método para eliminar libro de una la categoria elegida
    public void eliminarLibro(String categoria, String libro) {
        if (categorias.containsKey(categoria)) {
            HashMap<String, String> librosCategoria = categorias.get(categoria);
            if (librosCategoria.containsKey(libro)) {
                librosCategoria.remove(libro);
                guardarLibros(); // Guardar los cambios después de eliminar el libro
                System.out.println("El libro '" + libro + "' ha sido eliminado de la categoría '" + categoria + "'.");
            } else {
                JOptionPane.showMessageDialog(null,"El libro '" + libro + "' no existe en la categoría '" + categoria + "'.");
            }
        }else{
            JOptionPane.showMessageDialog(null,"Seleccione una categorias");
        }
    }

    // Método para eliminar una categoría y todos sus libros
    public void eliminarCategoria(String categoria) {
        if (categorias.containsKey(categoria)) {
            categorias.remove(categoria);
            cbCatego.removeItem(categoria); // Remover la categoría del JComboBox
            BibliotecaGUI.cbCategoria.removeItem(categoria);
            guardarLibros(); // Guardar los cambios después de eliminar la categoría
            System.out.println("La categoría '" + categoria + "' y todos sus libros han sido eliminados.");
        } else {
            System.out.println("La categoría '" + categoria + "' no existe.");
        }
    }

    // Métodos para cambiar el valor del HashMap en caso de que se devuelva el libro
    public static void agregarLibro(String categoria, String nom) {
        if (categorias.containsKey(categoria)) {
            HashMap<String, String> categoriaH = categorias.get(categoria);
            if (!categoriaH.containsKey(nom)) {
                categoriaH.put(nom, "Disponible"); // Establecer el estado inicial como "Disponible"
            }else {
                System.out.println("El libro '" + nom + "' ya existe en la categoría '" + categoria + "'.");

            }
        }
    }

    public void agregarCategoria(String categoria) {
        if (!categorias.containsKey(categoria)) {
            categorias.put(categoria, new HashMap<>());
            cbCatego.addItem(categoria); // Agregar la nueva categoría al JComboBox
        }
        else{
            JOptionPane.showMessageDialog(null,"Esta categoria ya existe");
        }
    }

    // Método para guardar los libros en un archivo .bin
    public static void guardarLibros() {
        try {
            String nombreArchivo = "categorias.bin";
            FileOutputStream fileOut = new FileOutputStream(nombreArchivo);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(categorias);
            objectOut.close();
            fileOut.close();
            System.out.println("Las categorías se han guardado correctamente en " + nombreArchivo);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // Método para cargar los libros desde un archivo .bin
    public void cargarLibros() {
        try {
            String nombreArchivo = "categorias.bin";
            FileInputStream fileIn = new FileInputStream(nombreArchivo);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            categorias = (HashMap<String, HashMap<String, String>>) objectIn.readObject();
            objectIn.close();
            fileIn.close();
            System.out.println("Las categorías se han cargado correctamente desde " + nombreArchivo);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        cbCatego.removeAllItems(); // Limpiar el JComboBox
        for (String categoria : categorias.keySet()) {
            cbCatego.addItem(categoria); // Agregar todas las categorías al JComboBox nuevamente
        }
    }
    //se utiliza para devolverle las categorias a los combobox
    public static HashMap<String, HashMap<String, String>> getCategorias() {
        return categorias;
    }

    //imprime las respectivas categorias con sus libros y el estado
    public void imprimirCategorias() {
        for (String categoria : categorias.keySet()) {
            System.out.println("Categoría: " + categoria);
            HashMap<String, String> librosCategoria = categorias.get(categoria);
            for (String libro : librosCategoria.keySet()) {
                String estado = librosCategoria.get(libro);
                System.out.println("- Libro: " + libro + ", Estado: " + estado);
            }
            System.out.println();
        }
    }

    public static StringBuilder estadoLibros() {
        StringBuilder mensaje = new StringBuilder();
        for (String categoria : categorias.keySet()) {
            mensaje.append("Categoría: ").append(categoria).append("\n");
            HashMap<String, String> librosCategoria = categorias.get(categoria);
            for (String libro : librosCategoria.keySet()) {
                String estado = librosCategoria.get(libro);
                mensaje.append("- Libro: ").append(libro).append(", Estado: ").append(estado).append("\n");
            }
            mensaje.append("\n");
        }
        return mensaje;
    }
}
