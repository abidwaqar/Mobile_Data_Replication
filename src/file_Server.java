
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class file_Server extends Thread {
	private class RequestHandler extends Thread {
		public final static int BufferSize = 1024;
		
		private Socket socket;
		
		public RequestHandler(Socket socket) {
			this.socket = socket;
		}
		
		private void resetModificationBit(File file, String clientID) {
			// TODO: Clear the modification bit of this file.
			try 
			{
				BufferedReader file_br = new BufferedReader(new FileReader(file));
				String[] metaDataArr = file_br.readLine().split(",");
				for (int i = 0; i<metaDataArr.length-1; i = i+2)
				{
					if (metaDataArr[i] == clientID)
					{
						metaDataArr[i+1] = "0";
					}
				}
//				date and time remains same
//				metaDataArr[metaDataArr.length-1] = Long.toString(new Date().getTime()/1000);
				
				String metaData = new String();
				BufferedWriter file_bw = new BufferedWriter(new FileWriter(file));
				for(int i = 0; i< metaDataArr.length;++i)
				{
					metaData += metaDataArr[i];
				}
				file_bw.write(metaData);
				
				file_br.close();
				file_bw.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			
			System.out.println("In File server");
			
			byte[] buffer = new byte[BufferSize];
			
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
				//get Client ID 
				String clientID = reader.readLine();
				System.out.println("FS: ClientID: " + clientID);
				
				//get total files to send
				int filesNum = reader.read();
				System.out.println("Files To Be Synced:" + filesNum);
				
				for (int i = 0; i< filesNum; ++i)
				{
					String fileName = reader.readLine();
					System.out.println("File name: " + fileName);
					File file = new File(rootDir.getCanonicalFile() + File.separator + fileName);
//					System.out.println(file.getAbsolutePath());
					OutputStream os = new BufferedOutputStream(socket.getOutputStream());
					InputStream fis = new BufferedInputStream(new FileInputStream(file));
					
					while (fis.read(buffer) != -1) {
						os.write(buffer);
						System.out.println("FS Sending data");
					}
					os.write(buffer);
					resetModificationBit(file, clientID);
					
					fis.close();
					os.close();
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private int port;
	private File rootDir;
	
	public file_Server(int port, File rootDir) {
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
