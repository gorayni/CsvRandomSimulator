package com.csvsim;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.util.Iterator;

import javassist.CannotCompileException;
import javassist.NotFoundException;

import org.yaml.snakeyaml.Yaml;

import com.csvsim.generator.Generator;
import com.csvsim.wrapper.CsvGenerator;
import com.csvsim.wrapper.utils.CsvGenerator2Graph;
import com.csvsim.wrapper.utils.Wrapper2Object;
import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;

public class CsvSimulator {
	
	public static void simulate(CsvGenerator csvGenerator, Integer numRecords) throws InstantiationException, IllegalAccessException, NotFoundException, CannotCompileException {		
		Object csvObject = Wrapper2Object.createCsvObject(csvGenerator);
		Generator generator = Wrapper2Object.getGenerator(csvGenerator, csvObject);
		if(numRecords!=null){
			for (int i = 0; i < numRecords; i++) {
				generator.generate();
				System.out.println(csvObject);
			}
		}
		else{
			while (true) {
				generator.generate();
				System.out.println(csvObject);
			}
		}

	}

	@SuppressWarnings("resource")
	public static void simulate(CsvGenerator csvGenerator, Integer numRecords, Integer port) throws InstantiationException, IllegalAccessException, NotFoundException, CannotCompileException {
		try {
			ServerSocket serverSocket = new ServerSocket(port);

			System.out.println("Waiting for connections on port " + port);
			CsvSimulatorServerThread current = new CsvSimulatorServerThread(csvGenerator, numRecords, serverSocket.accept());
			current.start();
			System.out.println("Connected client...");

			while (true) {
				System.out.println("Waiting for connections on port " + port);
				CsvSimulatorServerThread newThread = new CsvSimulatorServerThread(csvGenerator, numRecords, serverSocket.accept());
				current.stopListening();
				current = newThread;
				current.start();
				System.out.println("Connected client...");
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		}
	}	

	public static JSAPResult parse(String[] args) throws JSAPException {

		JSAP jsap = new JSAP();

		FlaggedOption configFileOpt = new FlaggedOption("yamlFile", JSAP.STRING_PARSER, null, JSAP.REQUIRED, 'f', JSAP.NO_LONGFLAG, "YAML configuration file");
		FlaggedOption numRecordsOpt = new FlaggedOption("numRecords", JSAP.INTEGER_PARSER, "-1", JSAP.NOT_REQUIRED, 'n', JSAP.NO_LONGFLAG, "Number of records to simulate");
		FlaggedOption dotFileOpt = new FlaggedOption("dotFile", JSAP.STRING_PARSER, null, JSAP.NOT_REQUIRED, 'd', JSAP.NO_LONGFLAG, "DOT filename destiny");
		FlaggedOption portOpt = new FlaggedOption("port", JSAP.INTEGER_PARSER, "-1", JSAP.NOT_REQUIRED, 'p', JSAP.NO_LONGFLAG, "TCP server port");
		
		jsap.registerParameter(configFileOpt);
		jsap.registerParameter(numRecordsOpt);
		jsap.registerParameter(dotFileOpt);
		jsap.registerParameter(portOpt);

		JSAPResult configuration = jsap.parse(args);

		if (!configuration.success()) {

			@SuppressWarnings("rawtypes")
			Iterator errs = configuration.getErrorMessageIterator();
			while (errs.hasNext()) {
				System.err.println("Error: " + errs.next());
			}

			System.err.println();
			System.err.println("Usage: simulator [OPTIONS]");
			System.err.println("                " + jsap.getUsage());
			System.err.println();
			System.err.println(jsap.getHelp());
			System.exit(1);
		}

		return configuration;
	}
	
	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, NotFoundException, CannotCompileException,
			IOException, JSAPException {

		
		JSAPResult configuration = parse(args);

		String yamlConfigFile = configuration.getString("yamlFile");
		Integer numRecords = (configuration.getInt("numRecords") > 0 ? configuration.getInt("numRecords") : null);
		String dotFilename = configuration.getString("dotFile");
		Integer port = (configuration.getInt("port") > 0 ? configuration.getInt("port") : null);
		
		Yaml yaml = new Yaml();
		InputStream input = new FileInputStream(yamlConfigFile);
		CsvGenerator csvGenerator = (CsvGenerator) yaml.load(input);

		if (dotFilename != null) {
			CsvGenerator2Graph.toDot(new FileWriter(dotFilename), csvGenerator);
			System.exit(0);
		}
		
		if (port != null) {
			CsvSimulator.simulate(csvGenerator, numRecords, port);
		} else {
			CsvSimulator.simulate(csvGenerator, numRecords);
		}
 
	}

}
