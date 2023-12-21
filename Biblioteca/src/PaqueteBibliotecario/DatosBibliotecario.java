package PaqueteBibliotecario;

import javax.swing.*;
import java.io.*;
import java.util.HashMap;

public class DatosBibliotecario implements Serializable {
    String nuevoNombre;
    String nuevaContraseña;
    // HashMap que guarda las cuentas de los bibliotecarios jefes, su información se guarda en jefe.bin
    public static HashMap<String, String> agregarJefe = new HashMap<>();

    // HashMap que guarda las cuentas de los bibliotecarios normales, su información se guarda en normal.bin
    public static HashMap<String, String> agregarNormal = new HashMap<>();
    public DatosBibliotecario(){}


    //Creo un método de tipo Hashmap para guardar el jefe primario
    public HashMap<String, String> BibliotecarioMaster(){
        this.agregarJefe.put("Jorge", "WF");
        return agregarJefe;
    }


    //Guarda y carga los datos de los bibliotecarios jefes
    public HashMap<String, String> nuevaSesion(){
        try {
            FileInputStream fileIn = new FileInputStream("jefe.bin");
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            agregarJefe = (HashMap<String, String>) objectIn.readObject();
            objectIn.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        agregarJefe.put(this.nuevoNombre, this.nuevaContraseña);
        try {
            FileOutputStream fileOut = new FileOutputStream("jefe.bin");
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(agregarJefe);
            objectOut.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return agregarJefe;
    }

    //Guarda la cuenta de un nuevo bibliotecario normal en el Hashmap
    public HashMap<String, String> BibliotecarioNormal(){
        agregarNormal.put(this.nuevoNombre, this.nuevaContraseña);
        return agregarNormal;
    }

    //Guarda y carga los datos de los bibliotecarios normales
    public HashMap<String, String> nuevaSesionNormal(){
        try {
            FileInputStream fileIn = new FileInputStream("normal.bin");
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            agregarNormal = (HashMap<String, String>) objectIn.readObject();
            objectIn.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        agregarNormal.put(this.nuevoNombre, this.nuevaContraseña);
        try {
            FileOutputStream fileOut = new FileOutputStream("normal.bin");
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(agregarNormal);
            objectOut.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return agregarNormal;
    }

    // Metodo para eliminar la cuenta de un bibliotecario
    void eliminarCuenta(String archivo, String nombre, String contraseña) {
        try {
            FileInputStream fileIn = new FileInputStream(archivo);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            HashMap<String, String> cuentas = (HashMap<String, String>) objectIn.readObject();
            objectIn.close();
            fileIn.close();

            if (cuentas.containsKey(nombre) && cuentas.get(nombre).equals(contraseña)) {
                cuentas.remove(nombre);
                FileOutputStream fileOut = new FileOutputStream(archivo);
                ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
                objectOut.writeObject(cuentas);
                objectOut.close();
                fileOut.close();
                JOptionPane.showMessageDialog(null, "Cuenta eliminada con éxito.");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró la cuenta o la contraseña es incorrecta.");
            }
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}