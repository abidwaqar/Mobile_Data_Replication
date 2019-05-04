import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

// TODO: import GlobalConstants

public class file_Shuffler {
	// Change every 10th file
	public final static float ChangeProbability = 0.1f;
	
	private static void usage() {
		System.out.println("Usage:");
		System.out.println("FileShuffler ROOT_DIRECTORY");
	}

	private static void changeFile(File file) throws IOException {
		Random rand = new Random();
		
		file.delete();
		OutputStream os = new FileOutputStream(file);
		for (int i = 0; i < global_Variables.FileSize; i++) {
			os.write(rand.nextInt(256) + Byte.MIN_VALUE);
		}
		os.close();
	}
	
	private static void changeMetaData(File file) throws IOException {
		// TODO: File has been changed. --> Change meta-data of this file.
		file_Creator.createMetaData(file);
	}
	
	public static void main(String[] args) {
		if (args.length != 1) {
			usage();
			System.exit(-1);
		}

		String rootDirStr = args[0];
		File rootDir = new File(rootDirStr);
		if (!rootDir.isDirectory()) {
			System.err.println("Root directory '" + rootDir + "' is no directorty.");
			System.exit(-1);
		}
		if (!rootDir.exists()) {
			System.err.println("Root directory '" + rootDir + "' does not exist.");
			System.exit(-1);
		}
		
		Random rand = new Random();
		File[] files = rootDir.listFiles();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (file.getPath().endsWith(global_Variables.MetaDataFileSuffix)) {
				continue;
			}
			
			double r = rand.nextDouble();
			if (r <= 0.5)
			{
				try {
					changeFile(file);
					changeMetaData(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
