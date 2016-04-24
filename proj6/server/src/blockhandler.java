package edu.ucsd.cse124;

import org.apache.thrift.TException;

import edu.ucsd.cse124.LargeBlockException;
import edu.ucsd.cse124.NoSuchBlockException;
import edu.ucsd.cse124.InvalidHashException;

import java.util.*;
import java.security.*;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class blockhandler implements TritonTransfer.Iface {
	protected Map<String, ByteBuffer> map;

	public blockhandler(){
		this.map = new HashMap<String, ByteBuffer>();
	}

	@Override
    public void ping() {
    }

    public String uploadBlock(String hashValue, ByteBuffer blockValue) throws LargeBlockException, InvalidHashException, TException{
        //System.out.println("uploadBlock");
        String tmp = "";
        byte[] blockarray = blockValue.array();
    	if(blockarray.length > 16000) throw new LargeBlockException();
        //System.out.println("block: " + blockValue);

    	try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(blockarray);
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < hashedBytes.length; i++) {
                stringBuffer.append(Integer.toString((hashedBytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            tmp = stringBuffer.toString();
        } catch(NoSuchAlgorithmException e) {
            // catch the exception
        }
        //System.out.println("tmp:" + tmp);
        //System.out.println(hashValue);
        if(tmp.compareTo(hashValue) != 0) throw new InvalidHashException();

        if(map.containsKey(hashValue) == false){
        	map.put(hashValue, blockValue);
        }

        return "OK";
    }

    public ReTy downloadBlock(String hashValue) throws NoSuchBlockException, TException{
        //System.out.println("downloadBlock");
        //System.out.println(hashValue);
    	ReTy result = new ReTy();
    	if(map.containsKey(hashValue) == false) throw new NoSuchBlockException();
    	result.content = map.get(hashValue);
    	result.status = "OK";
    	return result;
    }

    public List<hashaddr> uploadFile(String filename, List<String> hashlist) throws TException{
        return null;
    }

    public List<hashaddr> downloadFile(String filename) throws TException{
        return null;
    }

    public void sendAddr(String IPAddress, int port) throws TException{
        return;
    }
}
