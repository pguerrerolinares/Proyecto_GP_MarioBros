package euiti.mariobros.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;

public class SGBD {
	private static SGBD miSGBD;
	private File f;

	private SGBD() {
		f = new File("/home/andrea/Documentos/GP/Proyecto_GP_MarioBros/MarioBros/core/assets/bd/mario.db");          
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
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
		FileWriter fname = new FileWriter("/home/andrea/Documentos/GP/Proyecto_GP_MarioBros/MarioBros/core/assets/bd/mario.db", true);
		PrintWriter printWriter = new PrintWriter(fname);
		//printWriter.println();

		printWriter.println(puntuacion + ";");
		printWriter.close();
		 
		LinkedList<Integer> punt = new LinkedList<>();
		FileReader fr = new FileReader("/home/andrea/Documentos/GP/Proyecto_GP_MarioBros/MarioBros/core/assets/bd/mario.db");
		BufferedReader br = new BufferedReader(fr);
		String line;
	
		while ((line = br.readLine()) !=null) {
			System.out.println(line);
			
		}

	}

	private void read() {

	}

}
