import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.FileSystems;

public class meta_data_Server extends Thread {
	private class RequestHandler extends Thread {
		public final static int BufferSize = 1024;
		
		private Socket socket;
		
		public RequestHandler(Socket socket) {
			this.socket = socket;
		}
				
		private void sendMetaData(PrintWriter printer, String clientID) {
			// TODO: Send meta data of local files to client using "modified bit" approach.
			//       That is, send meta data of all files that were modified since
			//       the client downloaded the file the last time or that are new (= have never
			//       been downloaded by this client so far)
			
			File[] files = rootDir.listFiles();
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				if (file.getPath().endsWith(global_Variables.MetaDataFileSuffix)) {
					try 
					{
						BufferedReader file_br = new BufferedReader(new FileReader(file));
						String[] metaDataArr = file_br.readLine().split(",");
						
						for (int j = 0; j<metaDataArr.length-1; j = j+2)
						{
							if (metaDataArr[j].equals(clientID) && metaDataArr[j+1].equals("1"))
							{
								//sending filename, timestamp
//								String[] a = file.getName().split(".");
//								System.out.println(file.getName());
								System.out.println(file.getName().replace(global_Variables.MetaDataFileSuffix, "") + "," + metaDataArr[metaDataArr.length-1]);
								printer.println(file.getName().replace(global_Variables.MetaDataFileSuffix, "") + "," + metaDataArr[metaDataArr.length-1]);
								printer.flush();
							}
						}
						
						file_br.close();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
			printer.println("-1");
			printer.flush();
		}
		
		@Override
		public void run() {
			System.out.println("In Metadata server");
			
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String clientID = reader.readLine();
				System.out.println("MDS: clientID:" + clientID);
				
				PrintWriter printer = new PrintWriter(socket.getOutputStream());
				sendMetaData(printer, clientID);
				
				reader.close();
				printer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
	}
	
	private int port;
	private File rootDir;
	
	public meta_data_Server(int port, File rootDir) {
		this.port = port;
		this.rootDir = rootDir;
	}
	
	@Override
	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			while (true) {
				Socket socket = serverSocket.accept();
				RequestHandler requestHandler = new RequestHandler(socket);
				requestHandler.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
