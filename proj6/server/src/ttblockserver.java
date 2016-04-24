package edu.ucsd.cse124;

import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadPoolServer;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;

import java.util.*;
import java.net.*;

public class ttblockserver {
	public static int port = 9090;
	public static void StartsimpleServer(TritonTransfer.Processor<blockhandler> processor) {
		try {
			TServerTransport serverTransport = new TServerSocket(port);
			//System.out.println("port" + port);
	   		//TServer server = new TSimpleServer(
	     		//new Args(serverTransport).processor(processor));

	   // Use this for a multithreaded server
	   TServer server = new TThreadPoolServer(new
	   TThreadPoolServer.Args(serverTransport).processor(processor));

	   		System.out.println("Starting the simple server...");
	   		server.serve();
	  	} catch (Exception e) {
	   		e.printStackTrace();
	  	}
 	}
 
 	public static void main(String[] args) {
 		//System.out.println(args[0]);
 		port = Integer.parseInt(args[0]);
 		String mdserverip = args[1];
 		int mdserverport = Integer.parseInt(args[2]);
 		try {
      		TTransport transport;
     
      		transport = new TSocket(mdserverip, mdserverport);
      		transport.open();

      		TProtocol protocol = new  TBinaryProtocol(transport);
      		TritonTransfer.Client client = new TritonTransfer.Client(protocol);
      		String blockserverip = "";
      		try{
      			blockserverip = InetAddress.getLocalHost().getHostName();
      		}catch(UnknownHostException e){
      			e.printStackTrace();
      		}

      		//System.out.println(blockserverip);
      		client.sendAddr(blockserverip, port);

      		transport.close();
    	} catch (TException x) {
      		x.printStackTrace();
    	} 
    	blockhandler handler = new blockhandler();
    	StartsimpleServer(new TritonTransfer.Processor<blockhandler>(handler));
 	}
}