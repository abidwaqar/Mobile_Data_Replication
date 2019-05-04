import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class global_Variables {
	// The meta data of a file with name "FILENAME" is stored in the file "FILENAME.meta" (same directory as original file).
	public final static String MetaDataFileSuffix = ".meta";
	public static final int NumberOfReplicas = 2;
	public static int current_id = 0;
	
	public static final int FileNameSize = 8;
	public static final int FileSize = 1024;
	
	public static void resetModificationBit(File metafile, String clientID, String timeStamp) {
		// TODO: Clear the modification bit of this file.
		try 
		{
			BufferedReader file_br = new BufferedReader(new FileReader(metafile));
			String[] metaDataArr = file_br.readLine().split(",");
			for (int i = 0; i<metaDataArr.length-1; i = i+2)
			{
				if (metaDataArr[i].equals(clientID))
				{
					metaDataArr[i+1] = "0";
				}
			}
			
			String metaData = new String();
			BufferedWriter file_bw = new BufferedWriter(new FileWriter(metafile));
			for(int i = 0; i< metaDataArr.length; ++i)
			{
				metaData += metaDataArr[i];
				metaData += ",";
			}
			metaData += timeStamp;
			file_bw.write(metaData);
			System.out.println(metaData);
			
			file_br.close();
			file_bw.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void resetModificationBit(File metafile, String clientID) {
		// TODO: Clear the modification bit of this file.
		try 
		{
			BufferedReader file_br = new BufferedReader(new FileReader(metafile));
			String[] metaDataArr = file_br.readLine().split(",");
			for (int i = 0; i<metaDataArr.length-1; i = i+2)
			{
				if (metaDataArr[i].equals(clientID))
				{
					metaDataArr[i+1] = "0";
				}
			}
			
			String metaData = new String();
			BufferedWriter file_bw = new BufferedWriter(new FileWriter(metafile));
			for(int i = 0; i< metaDataArr.length; ++i)
			{
				metaData += metaDataArr[i];
				if (i != metaDataArr.length-1)
					metaData += ",";
			}
			file_bw.write(metaData);
			System.out.println(metaData);
			
			file_br.close();
			file_bw.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
