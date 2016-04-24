package edu.ucsd.cse124;

import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;

import java.util.*;

public class ttserver {
	public static int port = 9090;
	public static void StartsimpleServer(TritonTransfer.Processor<Handler> processor) {
		try {
			TServerTransport serverTransport = new TServerSocket(port);
			//System.out.println("port" + port);
	   		TServer server = new TSimpleServer(
	     		new Args(serverTransport).processor(processor));

	   // Use this for a multithreaded server
	   // TServer server = new TThreadPoolServer(new
	   // TThreadPoolServer.Args(serverTransport).processor(processor));

	   		System.out.println("Starting the simple server...");
	   		server.serve();
	  	} catch (Exception e) {
	   		e.printStackTrace();
	  	}
 	}
 
 	public static void main(String[] args) {
 		//System.out.println(args[0]);
 		port = Integer.parseInt(args[0]);
    	Handler handler = new Handler();
    	StartsimpleServer(new TritonTransfer.Processor<Handler>(handler));
 	}
}