
import java.io.File;

public class sync_Server {
	static void usage() {
		System.out.println("Usage:");
		System.out.println("Server ROOT_DIRECTORY FILE_SERVER_PORT META_DATA_SERVER_PORT");
	}
	
	public static void main(String[] args) {
		System.out.println("Total args Passed: " + args.length);

		if (args.length != 3) {
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
		
		int fileServerPort = -1;
		try {
			fileServerPort = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			System.err.println("Invalid file server port.");
			System.exit(-1);
		}
		
		int metaDataServerPort = -1;
		try {
		 metaDataServerPort = Integer.parseInt(args[2]);
		} catch (NumberFormatException e) {
			System.err.println("Invalid meta data server port.");
			System.exit(-1);
		}
		
		file_Server fileServer = new file_Server(fileServerPort, rootDir);
		fileServer.start();
		
		meta_data_Server metaDataServer = new meta_data_Server(metaDataServerPort, rootDir);
		metaDataServer.start();
		
		try {
			// This will wait forever (until server is killed) since we don't implement a mechanism for shutting down the servers (gracefully).
			fileServer.join();
			metaDataServer.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
