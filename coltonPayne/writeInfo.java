package coltonPayne;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

//Saves the info I need to file on desktop
//Code partly stolen from this youtube video: https://www.youtube.com/watch?v=Wrx-_oPiI90



public class writeInfo {

	private static final String FILENAME = System.getProperty("user.home") + "/Desktop" + "/SIMULATION_LOG.txt";

	public static void saveToFile(String text) {

		try  {
			
			File f = new File(FILENAME);
			FileWriter fw = new FileWriter(f, true);
			PrintWriter pw = new PrintWriter(fw);
			
			pw.println(text);
			pw.close();
			

		} catch (IOException e) {

			System.out.println("Error in saveToFile");

		}

	}

}