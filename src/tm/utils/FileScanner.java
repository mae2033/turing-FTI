package tm.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileScanner {

	Scanner scan = new Scanner(System.in);
	Scanner fileScan;

	public FileScanner() {
	}

	public FileScanner(File f) throws FileNotFoundException {
		setFileScan(new Scanner(f));
		getFileScan().useDelimiter("\n");
	}

	public Scanner getFileScan() {
		return fileScan;
	}

	public void setFileScan(Scanner fileScan) {
		this.fileScan = fileScan;
	}

	public void setFileScan(File file) throws FileNotFoundException {
		this.fileScan = new Scanner(file);
		getFileScan().useDelimiter("\n");
	}
}