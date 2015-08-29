package com.csvsim;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import com.csvsim.generator.Generator;
import com.csvsim.wrapper.CsvGenerator;
import com.csvsim.wrapper.utils.Wrapper2Object;

import javassist.CannotCompileException;
import javassist.NotFoundException;

public class CsvSimulatorServerThread extends Thread {
	private Socket socket = null;
	private Object csvObject;
	private Generator generator;
	private Integer numRecords;
	private boolean listening;

	public CsvSimulatorServerThread(CsvGenerator csvGenerator, Integer numRecords, Socket socket) throws InstantiationException, IllegalAccessException, NotFoundException, CannotCompileException {
		this.csvObject = Wrapper2Object.createCsvObject(csvGenerator);
		this.generator = Wrapper2Object.getGenerator(csvGenerator, csvObject);
		this.numRecords = numRecords;
		this.socket = socket;
		this.listening = true;
	}

	public void stopListening() {
		System.out.println("deteniendome");
		listening = false;
	}

	@Override
	public void run() {

		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

			if(numRecords!=null){
				for (int i = 0; i < numRecords; i++) {
					generator.generate();
					out.println(csvObject);
				}
			}
			else{
				while (listening) {
					generator.generate();
					out.println(csvObject);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
