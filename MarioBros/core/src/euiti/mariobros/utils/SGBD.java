package euiti.mariobros.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

public class SGBD {
    private static SGBD miSGBD;

    private SGBD() {
        File f = new File("mario.db");
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static SGBD getMiSGBD() {
        if (miSGBD == null) {
            miSGBD = new SGBD();
        }
        return miSGBD;
    }

    public void insert(int puntuacion) throws IOException {
        // Leemos el archivo
        LinkedList<String[]> listPunt = readFile();

        LinkedList<String[]> listPunt1 = addInOrder(listPunt, puntuacion);

        // Escribimos el nuevo orden en el archivo
        FileWriter fname = new FileWriter("mario.db");
        PrintWriter printWriter = new PrintWriter(fname);
        for (int j = 0; j < listPunt1.size(); j++) {
            if (j < 10) {
                String[] tmp = new String[2];
                tmp = listPunt1.get(j);
                printWriter.println(tmp[0] + ";" + tmp[1]);
            }
        }
        printWriter.flush();
        printWriter.close();

    }

    public LinkedList<String[]> readFile() throws IOException {
        LinkedList<String[]> listPunt = new LinkedList<String[]>();
        FileReader fr = new FileReader("mario.db");
        BufferedReader br = new BufferedReader(fr);
        String line;

        while ((line = br.readLine()) != null) {
            String[] split = line.split(";");
            listPunt.add(split);
        }
        return listPunt;
    }

    private LinkedList<String[]> addInOrder(LinkedList<String[]> listPunt, int puntuacion) {
        int i = 0;
        boolean posicion = false;

        while (!posicion && i < listPunt.size()) {
            int act = Integer.parseInt(listPunt.get(i)[1]);
            if (act < puntuacion) {
                posicion = true;
            }
            i++;
        }

        if (!(i == 10 && !posicion)) {
            // COGER NOMBRE
            String[] tmp = new String[2];
            String name = JOptionPane.showInputDialog("What's your name?");
            tmp[0] = name;
            tmp[1] = puntuacion + "";
            if ((i == listPunt.size() && i == 0 && posicion) || (i == listPunt.size() && i == 0)) {
                listPunt.add(0, tmp);
            } else if (posicion) {
                listPunt.add(i - 1, tmp);
            } else {
                listPunt.add(i, tmp);
            }

        }


        return listPunt;
    }


    public class MyTextInputListener implements Input.TextInputListener {
        String text;
        boolean getIt = false;

        @Override
        public void input(String text) {
            this.text = text;
        }

        @Override
        public void canceled() {
        }

        public String getText() {
            getIt = true;
            return this.text;

        }
    }
}
