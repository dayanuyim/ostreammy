package com.dayanuyim.ostreammy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.codec.binary.Hex;


public class Utils {
	/**
	 * 
	 * @param arr
	 * @param idx
	 * @return null or the element of array with index @idx
	 */
	public static Object arrayElement(Object[] arr, int idx){
		if(arr != null && arr.length > idx)
			return arr[idx];
		return null;
	}

	
	public static String[] uniqueSplit(String s, String sp)
	{
		if(s == null) return null;

		HashSet<String> strs = new HashSet<>(); //to unique
		for(String name: StringUtils.split(s, sp)){
			if(StringUtils.isNoneBlank(name)){
				strs.add(StringUtils.trim(name));
			}
		}
		return strs.toArray(new String[0]);
		
	}
	
    public static byte[] is2byte(InputStream is) throws IOException {
    	byte[] buf = new byte[8192];
    	int count;
    	ByteArrayOutputStream output = new ByteArrayOutputStream();
    	while ((count = is.read(buf)) != -1) {
    		output.write(buf, 0, count);
    	}
    	return output.toByteArray();
    }

    public static String byte2hex(byte[] b) {
		return Hex.encodeHexString(b).toUpperCase();
    }
	
	/************ Hash ******************/
    /*
    public static String getHash(String s, String algo) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		byte[] bytes = s.getBytes("UTF-8");
		return getHash(bytes, algo);
    }

    public static String getHash(InputStream is, String algo) throws NoSuchAlgorithmException, IOException{
    	byte[] bytes = is2byte(is);
    	return getHash(bytes, algo);
    }

	public static String getHash(byte[] bytes, String algo) throws NoSuchAlgorithmException
	{
		MessageDigest md = MessageDigest.getInstance(algo);
		byte[] digest = md.digest(bytes);
		return byte2hex(digest);
	}

	public static String getMD5(byte[] bytes) throws NoSuchAlgorithmException { return getHash(bytes, "MD5"); }
	public static String getSHA1(byte[] bytes) throws NoSuchAlgorithmException { return getHash(bytes, "SHA-1"); }
	public static String getSHA256(byte[] bytes) throws NoSuchAlgorithmException { return getHash(bytes, "SHA-256"); }
	*/
    
    public static String getHash(File file, String algo) throws FileNotFoundException, IOException, NoSuchAlgorithmException{
        
        try(InputStream fis = new FileInputStream(file)){
        	MessageDigest digest = MessageDigest.getInstance(algo);
			byte[] buffer = new byte[8192];
			for(int n; (n = fis.read(buffer)) != -1;){
				digest.update(buffer, 0, n);
			}
        	return byte2hex(digest.digest());
        }
    }

    public static String getMD5(File file) throws FileNotFoundException, IOException, NoSuchAlgorithmException{ return getHash(file, "MD5"); }
    public static String getSHA1(File file) throws FileNotFoundException, IOException, NoSuchAlgorithmException{ return getHash(file, "SHA-1"); }
    public static String getSHA256(File file) throws FileNotFoundException, IOException, NoSuchAlgorithmException{ return getHash(file, "SHA-256"); }
}
