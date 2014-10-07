package org.finra.datagenerator.tools;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;

public class CreateDataFile {
	static public int numberOfLines = 20000;
	static public int numberOfProperties = 100;
	static public String separator = "|";

	static public enum Values {
		A, B, C, D, E, F, G, H, I, J
	}
	
//	static public enum Values {
//		A, B, C, D, E, F, G, H, I, J, A2, B2, C2, D2, E2, F2, G2, H2, I2, J2, A3, B3, C3, D3, E3, F3, G3, H3, I3,
//		A4, B4, C4, D4, E4, F4, G4, H4, I4, J4, A5, B5, C5, D5, E5, F5, G5, H5, I5, J5, A6, B6, C6, D6, E6, F6, G6, H6, I6
//	}

	static public int valuesLength = Values.values().length;
	static Random rnd = new Random();

	public static void main(String[] args) {
		createDataFileDo();
	}

	public static void createDataFileDo() {
		PrintWriter writer;
		try {
			writer = new PrintWriter("src/main/resources/data.txt", "UTF-8");

			for (int i1 = 0; i1 < numberOfLines; i1++) {
				StringBuffer sb = new StringBuffer();
				for (int i2 = 0; i2 < numberOfProperties; i2++) {
					sb.append(Values.values()[rnd.nextInt(valuesLength)]);
					if (i2 < numberOfProperties + 1) {
						sb.append(separator);
					}
				}
				writer.println(sb.toString());
			}

			writer.close();
			System.out.println("File 'src/main/resources/data.txt' created.");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
