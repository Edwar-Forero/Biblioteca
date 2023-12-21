package PaqueteVentanaPrin;
import PaqueteBibliotecario.IniciarSesion;
import PaqueteBibliotecario.Registro;
import PaqueteLibro.Libro;
import PaquetePrestamo.Devolucion;
import PaquetePrestamo.Prestamo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;

public  class BibliotecaGUI extends JFrame{
    public static String nomLibro; // nombre usuario temporal para el prestamo
    public static String genElegido; // genero temporal elegido para el prestamo
    static String[] columnNames = {"Nombre", "Libro","Categoria", "Fecha"};
    static DefaultTableModel model = new DefaultTableModel(columnNames,0);
    static JTable table = new JTable(model);
    private Prestamo prestamo;
    public static JComboBox cbLibro = new JComboBox();
    public static JComboBox cbCategoria = new JComboBox();
    public static JMenuItem menuitemregistro = new JMenuItem("Crear Cuenta");
    private Libro libro = new Libro();
    JScrollPane scrollPane1 = new JScrollPane(cbCategoria);
    JScrollPane scrollPane2 = new JScrollPane(cbLibro);
    private static final String DATA_FILE = "datosPrestamos.bin";
    public static HashMap<String, HashMap<String,Integer>> morosos = new HashMap<>();
    private JPanel panel;
    public  BibliotecaGUI(){
        setTitle("Biblioteca");
        setSize(700,600);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        panel = new JPanel();
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setLayout(null);
        getContentPane().add(panel);
        ComponentesVentana();
    }

    public void ComponentesVentana(){
        ColocarItem();
        ColocarLabel();
        ColocarButton();
        ColocarCombo();
        cargarDatosTabla();
    }
    public void ColocarItem(){
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu estadoDeLosLibros = new JMenu("Estado de los libros");
        menuBar.add(estadoDeLosLibros);
        JMenuItem estadoLibros = new JMenuItem();
        estadoLibros.setText("Consultar");
        estadoDeLosLibros.add(estadoLibros);

        JMenu clieMora = new JMenu("Clientes en mora");
        JMenuItem menuitemClieMo;
        menuitemClieMo = new JMenuItem("Consultar Clientes");
        clieMora.add(menuitemClieMo);

        JMenuItem menuItemBorrarCLi;
        menuItemBorrarCLi = new JMenuItem("Eliminar Clientes");
        clieMora.add(menuItemBorrarCLi);
        menuBar.add(clieMora);

        JMenu menuRegistro = new JMenu("Registro");
        menuRegistro.add(menuitemregistro);
        menuBar.add(menuRegistro);

        JMenu menuSalir = new JMenu("Salir");
        JMenuItem menuitemnew;
        menuitemnew = new JMenuItem("Inicar Sesión Con Otra Cuenta");
        menuSalir.add(menuitemnew);
        JMenuItem menuitemsalir;
        menuitemsalir = new JMenuItem("Salir");
        menuSalir.add(menuitemsalir);
        menuBar.add(menuSalir);

        menuitemregistro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Registro();
                dispose();

            }
        });

        menuitemnew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new IniciarSesion();
                setVisible(false);
            }
        });

        menuitemsalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        menuitemClieMo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Devolucion.imprimirDatosMorosos();
            }
        });

        menuItemBorrarCLi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Pedir al usuario que ingrese el nombre del cliente a eliminar de la lista de mororosos
                String nombreEliminar = JOptionPane.showInputDialog("Ingrese el nombre del cliente a eliminar:");
                Devolucion.eliminarMoroso(nombreEliminar);
            }
        });

        estadoLibros.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextArea textArea = new JTextArea(String.valueOf(Libro.estadoLibros()));
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(400, 300));
                JOptionPane.showMessageDialog(null, scrollPane);

            }
        });
    }

    public void ColocarLabel(){
        JLabel texto1 = new JLabel();
        texto1.setText("CATEGORIAS:");
        texto1.setBounds(20,20,110,20);
        panel.add(texto1);

        JLabel texto2 = new JLabel();
        texto2.setText("LIBROS:");
        texto2.setBounds(300,20, 90,20);
        panel.add(texto2);
    }

    public void ColocarButton(){
        //boton para realizar un préstamo
        JButton prestamoLibro = new JButton();
        prestamoLibro.setBackground(Color.green);
        prestamoLibro.setText("Prestar libro");
        prestamoLibro.setBounds(270,195,140,25);
        panel.add(prestamoLibro);

        prestamoLibro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prestamo = new Prestamo();
                prestamo.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            }
        });

        //Boton para devolución del libro
        JButton devolver = new JButton();
        devolver.setBackground(Color.green);
        devolver.setText("Devolver libro");
        devolver.setBounds(510, 195, 140, 25);
        panel.add(devolver);

        devolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    String nombre = table.getValueAt(selectedRow, 0).toString();
                    String libro = table.getValueAt(selectedRow, 1).toString();
                    String categoria = table.getValueAt(selectedRow,2).toString();
                    String fechaPrestamoStr = table.getValueAt(selectedRow, 3).toString();

                    // Obtener la fecha de préstamo en formato LocalDate
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate fechaPrestamo = LocalDate.parse(fechaPrestamoStr, formatter);
                    LocalDate fechaActual = LocalDate.now();
                    // Calcular los días transcurridos
                    long diasTranscurridos = ChronoUnit.DAYS.between(fechaPrestamo, fechaActual);

                    int deuda = (int) ((diasTranscurridos-7) * 1000);
                    if (diasTranscurridos > 7) {
                        JOptionPane.showMessageDialog(null, "Este usuario tiene una multa de:  $" + deuda);
                        // Guardar los datos en el HashMap morosos
                        if (morosos.containsKey(nombre)) {
                            HashMap<String, Integer> librosMorosos = morosos.get(nombre);
                            librosMorosos.put(libro, deuda);
                        } else {
                            HashMap<String, Integer> librosMorosos = new HashMap<>();
                            librosMorosos.put(libro, deuda);
                            morosos.put(nombre, librosMorosos);
                        }
                        Devolucion.guardarDatosMorosos();
                    }
                    //***** Eliminar el préstamo del archivo .bin
                    Devolucion.eliminarPrestamo(nombre, libro, categoria, fechaPrestamoStr);
                    // Eliminar la fila elegida
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    model.removeRow(selectedRow);

                } else {
                    JOptionPane.showMessageDialog(null, "Por favor, selecciona una fila de la tabla");
                }
            }
        });

        JButton crearCat = new JButton("Agregar Cat/Lib");
        crearCat.setBackground(Color.green);
        crearCat.setBounds(30,195,140,25);
        panel.add(crearCat);

        crearCat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                libro.setVisible(true);
            }
        });
    }



    public void ColocarCombo(){
        HashMap<String, HashMap<String, String>> categoriasLibro = libro.getCategorias();
        cbCategoria.setBounds(110,20,150,20);
        panel.add(scrollPane1);
        panel.add(cbCategoria);

        // Cargar las categorías en el JComboBox cbCategoria
        for (String categoria : categoriasLibro.keySet()) {
            cbCategoria.removeItem(categoria);
            cbCategoria.addItem(categoria);
        }

        cbCategoria.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HashMap<String, HashMap<String, String>> categoriasLibro = libro.getCategorias();
                // Obtener la categoría seleccionada
                String categoriaSeleccionada = (String) cbCategoria.getSelectedItem();

                // Obtener el HashMap de libros correspondiente a la categoría seleccionada
                HashMap<String, String> librosCategoria = categoriasLibro.get(categoriaSeleccionada);

                // Limpiar el JComboBox cbLibro
                cbLibro.removeAllItems();

                // Cargar los libros de la categoría seleccionada en el JComboBox cbLibro
                for (Map.Entry<String, String> entry : librosCategoria.entrySet()) {
                    String libro = entry.getKey();
                    String estado = entry.getValue();
                    if (estado.equals("Disponible")) {
                        cbLibro.addItem(libro);
                    }else {
                        cbLibro.removeItem(libro);
                    }
                }
            }
        });

        cbLibro.setBounds(375,20,150,20);
        JScrollPane scrollPaneL = new JScrollPane(cbLibro);
        scrollPaneL.setPreferredSize(new Dimension(200, 100));
        panel.add(scrollPane2);
        panel.add(cbLibro);

        cbLibro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nomLibro = String.valueOf(cbLibro.getSelectedItem());
                genElegido = String.valueOf(cbCategoria.getSelectedItem());
            }
        });
    }
    public static void actualizarTabla(String nombre, String libro, String categoria, String fecha)
    {
        // Obtener el modelo de la tabla
        DefaultTableModel  model = (DefaultTableModel) table.getModel();
        // Crear un array con los datos de la nueva fila
        String[] rowData = {nombre, libro,categoria,fecha};
        // Agregar la nueva fila al modelo
        model.addRow(rowData);

    }
    public static void guardarDatosTabla(){
        try {
            FileOutputStream fileOut = new FileOutputStream(DATA_FILE);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(model.getDataVector());
            objectOut.close();
            fileOut.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void cargarDatosTabla(){
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 280, 660, 250);
        panel.add(scrollPane);
        try {
            FileInputStream fileIn = new FileInputStream(DATA_FILE);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            Vector<Vector<Object>> dataVector = (Vector<Vector<Object>>) objectIn.readObject();
            objectIn.close();
            fileIn.close();
            model.setDataVector(dataVector, new Vector<Object>(Arrays.asList(columnNames)));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
