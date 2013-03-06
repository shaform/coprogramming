package net.coprg.coprg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

class ProcessStreamHandler extends Thread {

	InputStream inputStream;
	StringBuilder outputBuffer = new StringBuilder();

	ProcessStreamHandler(InputStream in) {
		inputStream = in;
	}

	public void run() {
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				outputBuffer.append(line + "\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public StringBuilder getOutputBuffer() {
		return outputBuffer;
	}
}

public class Executor {

}

