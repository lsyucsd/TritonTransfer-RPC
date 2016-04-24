package edu.ucsd.cse124;

import org.apache.thrift.TException;

import edu.ucsd.cse124.LargeBlockException;
import edu.ucsd.cse124.NoSuchBlockException;
import edu.ucsd.cse124.InvalidHashException;

import java.util.*;
import java.security.*;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class mdhandler implements TritonTransfer.Iface {
	protected Map<String, List<String>> map1;
	protected Map<String, hashaddr> map2;
    protected List<Address> servers;
    public int index = 0;

	public mdhandler(){
		this.map1 = new HashMap<String, List<String>>();
		this.map2 = new HashMap<String, hashaddr>();
        servers = new ArrayList<Address>();
	}

	@Override
    public void ping() {
    }

    public List<hashaddr> uploadFile(String filename, List<String> hashlist) throws TException{
    	List<hashaddr> result = new ArrayList<hashaddr>();
    	if(map1.containsKey(filename) == false){
    		map1.put(filename, hashlist);
    	}
        List<String> templist = new ArrayList<String>();
        for(String hash : hashlist){
            if(!templist.contains(hash)){
                templist.add(hash);
            }
        }
    	for(String hash : templist){
            if(!map2.containsKey(hash)){
        		hashaddr tmp = new hashaddr();
                tmp.hashValue = hash;
                tmp.IPAddress1 = servers.get(index).ipAddress;
                tmp.port1 = servers.get(index).port;
                //System.out.println("Port1: " + tmp.port1);
                index = (index+1)%3;
                tmp.IPAddress2 = servers.get(index).ipAddress;
                tmp.port2 = servers.get(index).port;
                index = (index+1)%3;
                map2.put(hash, tmp);
                result.add(tmp);
            }
    	}
    	return result;
    }

    public List<hashaddr> downloadFile(String filename) throws TException{
    	List<hashaddr> result = new ArrayList<hashaddr>();
    	List<String> ha = map1.get(filename);
        for(String h : ha){
            result.add(map2.get(h));
        }
    	return result;
    }

    public void sendAddr(String IPAddress, int port) throws TException{
        //System.out.println("RegIP: " + IPAddress);
        //System.out.println("RegPort: " + port);
        Address tmp = new Address(IPAddress, port);
        tmp.port = port;
        //System.out.println("tmp.port" + tmp.port);
        servers.add(tmp);
    }

    public String uploadBlock(String hashValue, ByteBuffer blockValue) throws LargeBlockException, InvalidHashException, TException{
        return null;
    }

    public ReTy downloadBlock(String hashValue) throws NoSuchBlockException, TException{
        return null;
    }

}

