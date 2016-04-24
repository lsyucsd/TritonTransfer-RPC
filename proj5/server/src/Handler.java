package edu.ucsd.cse124;

import org.apache.thrift.TException;

import edu.ucsd.cse124.LargeBlockException;
import edu.ucsd.cse124.NoSuchBlockException;
import edu.ucsd.cse124.InvalidHashException;

import java.util.*;
import java.security.*;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class Handler implements TritonTransfer.Iface {
	protected Map<String, List<String>> map1;
	protected Map<String, ByteBuffer> map2;

	public Handler(){
		this.map1 = new HashMap<String, List<String>>();
		this.map2 = new HashMap<String, ByteBuffer>();
	}

	@Override
    public void ping() {
    }

    public List<String> uploadFile(String filename, List<String> hashlist) throws TException{
    	List<String> result = new ArrayList<String>();
    	if(map1.containsKey(filename) == false){
    		map1.put(filename, hashlist);
    	}
    	for(String hash : hashlist){
    		if(map2.containsKey(hash) == false){
    			result.add(hash);
    		}
    	}
    	return result;
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

        if(map2.containsKey(hashValue) == false){
        	map2.put(hashValue, blockValue);
        }

        return "OK";
    }

    public List<String> downloadFile(String filename) throws TException{
    	List<String> result = new ArrayList<String>();
    	result = map1.get(filename);
    	return result;
    }

    public ReTy downloadBlock(String hashValue) throws NoSuchBlockException, TException{
        //System.out.println("downloadBlock");
        //System.out.println(hashValue);
    	ReTy result = new ReTy();
    	if(map2.containsKey(hashValue) == false) throw new NoSuchBlockException();
    	result.content = map2.get(hashValue);
    	result.status = "OK";
    	return result;
    }
}

