
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Random;
import java.util.Date;

// TODO: import GlobalConstants

public class file_Creator {
	
	static void usage() {
		System.out.println("Usage:");
		System.out.println("FileCreator ROOT_DIRECTORY NUMBER_OF_FILES");
	}
	
	public static File createFile(File rootDir) throws IOException {
		Random rand = new Random(Calendar.getInstance().getTimeInMillis());
		System.err.println("File Creator Called");
		File file = null;
		do {
			String fileNameStr = "";
			for (int i = 0; i < global_Variables.FileNameSize; i++) {
				fileNameStr += rand.nextInt(10);
			}
			file = new File(rootDir.getCanonicalPath() + File.separator + fileNameStr);
		} while (file.exists());
		
		OutputStream os = new FileOutputStream(file);
		for (int i = 0; i < global_Variables.FileSize; i++) {
			os.write(rand.nextInt(256) + Byte.MIN_VALUE);
		}
		os.close();
		return file;
	}
	
	public static void createMetaData(File file) throws IOException {
		// id,bit,id,bit,Time
		
		FileWriter metaDataFile = new FileWriter(file.getCanonicalPath() + global_Variables.MetaDataFileSuffix);
		Date date = new Date();
		long dateTime = date.getTime()/1000;
		
		// Replica counts
		BufferedWriter os = new BufferedWriter(metaDataFile);
		for (int i = 0; i < global_Variables.NumberOfReplicas; i++)
		{	
			os.write(i+1 + "," + 1 + ",");			// TODO change ( Assumption : 0 itself , other repplica 1 , 2
		}
		os.write("" + dateTime);
		os.close();
	}
	
	public static void main(String[] args) {
		if (args.length != 2) {
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
		
		int numberOfFiles = -1;
		try {
			numberOfFiles = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			System.err.println("Invalid number of files");
			System.exit(-1);
		}
		if (numberOfFiles <= 0) {
			System.err.println("Invalid number of files");
			System.exit(-1);
		}
		
		for (int i = 0; i < numberOfFiles; i++) {
			File file;
			try {
				file = createFile(rootDir);
				createMetaData(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
