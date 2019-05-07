package euiti.mariobros.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
		// Leemos el archivo
		LinkedList<String> listPunt = readFile();

		System.out.println(listPunt);
		
		LinkedList<String> listPunt1 = addInOrder(listPunt, puntuacion);
		
		System.out.println(listPunt1);
		
		// Escribimos el nuevo orden en el archivo
		FileWriter fname = new FileWriter("/home/andrea/Documentos/GP/Proyecto_GP_MarioBros/MarioBros/core/assets/bd/mario.db");
		PrintWriter printWriter = new PrintWriter(fname);
		for (int j = 0; j < listPunt1.size(); j++) {
			if (j < 5) {
				printWriter.println(listPunt1.get(j));
			}
		}
		printWriter.flush();
		printWriter.close();

	}

	public LinkedList<String> readFile() throws IOException {
		LinkedList<String> listPunt = new LinkedList<>();
		FileReader fr = new FileReader("/home/andrea/Documentos/GP/Proyecto_GP_MarioBros/MarioBros/core/assets/bd/mario.db");
		BufferedReader br = new BufferedReader(fr);
		String line;

		while ((line = br.readLine()) !=null) {
			listPunt.add(line);
		}
		return listPunt;
	}
	
	private LinkedList<String> addInOrder(LinkedList<String> listPunt, int puntuacion){
		int i = 0;
		boolean posicion = false;
		
		while(!posicion && i<listPunt.size()) {
			int act = Integer.parseInt(listPunt.get(i));
			if(act < puntuacion) {
				posicion = true;
			}
			i++;
		}
		
		System.out.println(i);
		if( (i == listPunt.size() && i == 0 && posicion) || (i == listPunt.size() && i == 0)) {
			listPunt.add(0, String.valueOf(puntuacion));
			System.out.println("hola");
		}else {
			listPunt.add(i-1, String.valueOf(puntuacion));
			System.out.println("ei");
		}

	
		
		return listPunt;
	}

}
