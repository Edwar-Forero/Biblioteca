package PaquetePrestamo;

import PaqueteLibro.Libro;
import PaqueteVentanaPrin.BibliotecaGUI;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Vector;

public class Devolucion implements Serializable {
    private static final String DATA_FILE = "datosPrestamos.bin";

    public Devolucion(){

    }
    public static void eliminarPrestamo(String nombre, String libro, String categoria, String fecha){
        try {
            FileInputStream fileIn = new FileInputStream(DATA_FILE);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            Vector<Vector<Object>> dataVector = (Vector<Vector<Object>>) objectIn.readObject();
            objectIn.close();
            fileIn.close();
            // Buscar el préstamo en el dataVector y eliminarlo
            for (int i = 0; i < dataVector.size(); i++) {
                Vector<Object> rowData = dataVector.get(i);
                if (rowData.get(0).equals(nombre) && rowData.get(1).equals(libro) && rowData.get(2).equals(categoria) && rowData.get(3).equals(fecha)) {
                    dataVector.remove(i);
                    break;
                }
            }
            // Guardar los datos actualizados en el archivo
            FileOutputStream fileOut = new FileOutputStream(DATA_FILE);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(dataVector);
            objectOut.close();
            fileOut.close();

            // Cambiar el estado del libro a "Disponible"
            if (Libro.getCategorias().containsKey(categoria)) {
                HashMap<String, String> librosCategoria = Libro.getCategorias().get(categoria);
                if (librosCategoria.containsKey(libro)) {
                    librosCategoria.put(libro, "Disponible");
                    System.out.println("El libro '" + libro + "' ha cambiado a estado 'Disponible'.");
                }
            }
            // Guardar los cambios en el archivo .bin
            Libro.guardarLibros();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void guardarDatosMorosos(){
        try {
            FileInputStream fileIn = new FileInputStream("clientesMorosos.bin");
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            HashMap<String, HashMap<String, Integer>> morososRecuperados = (HashMap<String, HashMap<String, Integer>>) objectIn.readObject();
            objectIn.close();
            fileIn.close();

            // Agregar los nuevos datos a los datos recuperados
            morososRecuperados.putAll(BibliotecaGUI.morosos);

            FileOutputStream fileOut = new FileOutputStream("clientesMorosos.bin");
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(morososRecuperados);
            objectOut.close();
            fileOut.close();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public static void imprimirDatosMorosos() {
        try {
            FileInputStream fileIn = new FileInputStream("clientesMorosos.bin");
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            HashMap<String, HashMap<String, Integer>> morososRecuperados = (HashMap<String, HashMap<String, Integer>>) objectIn.readObject();
            objectIn.close();
            fileIn.close();

            // Crear una JTextArea para mostrar los datos de los clientes morosos
            JTextArea textArea = new JTextArea();
            textArea.setEditable(false);

            // Recorrer los datos recuperados y agregarlos al JTextArea
            for (String nombre : morososRecuperados.keySet()) {
                textArea.append("Nombre: " + nombre + "\n");
                HashMap<String, Integer> librosMorosos = morososRecuperados.get(nombre);
                for (String libro : librosMorosos.keySet()) {
                    int deuda = librosMorosos.get(libro);
                    textArea.append("Libro: " + libro + ", Deuda: $" + deuda + "\n");
                }
                textArea.append("\n");
            }

            // Crear un JScrollPane para mostrar el JTextArea dentro de una ventana emergente
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));

            // Mostrar la ventana emergente con los datos de los clientes morosos
            JOptionPane.showMessageDialog(null, scrollPane, "Clientes Morosos", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void eliminarMoroso(String nombreMoroso) {
        try {
            FileInputStream fileIn = new FileInputStream("clientesMorosos.bin");
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            HashMap<String, HashMap<String, Integer>> morososRecuperados = (HashMap<String, HashMap<String, Integer>>) objectIn.readObject();
            objectIn.close();
            fileIn.close();

            // Eliminar el usuario del HashMap
            morososRecuperados.remove(nombreMoroso);

            FileOutputStream fileOut = new FileOutputStream("clientesMorosos.bin");
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(morososRecuperados);
            objectOut.close();
            fileOut.close();

            System.out.println("El usuario " + nombreMoroso + " ha sido eliminado correctamente.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

