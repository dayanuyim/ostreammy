package com.dayanuyim.ostreammy.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;

import com.dayanuyim.ostreammy.ResourceNotFoundException;

import org.apache.commons.codec.binary.Hex;


public class Utils {
	
	private static Logger logger = LoggerFactory.getLogger(Utils.class);
	
	/**
	 * 
	 * @param arr
	 * @param idx
	 * @return null or the element of array with index @idx
	 */
	public static <T> T elementAt(T[] arr, int idx){
		if(arr != null && arr.length > idx)
			return arr[idx];
		return null;
	}

	public static <T> T elementLast(T[] arr){
		if(arr != null && arr.length > 0)
			return arr[arr.length - 1];
		return null;
	}

	public static <T> T elementFirst(T[] arr){
		return elementAt(arr, 0);
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
	
	public static String asciiToUTF8(String str) throws UnsupportedEncodingException
	{
		if(str == null) return null;
		return new String(str.getBytes("iso-8859-1"), "UTF-8");
	}
	
	public static String urlToUTF8(String url) throws UnsupportedEncodingException{
		
		if(url == null) return null;
		 return URLDecoder.decode(url, "UTF-8");
	}

	public static FileSystemResource download(String uri, UriFileMapper mapper) throws UnsupportedEncodingException
	{
		uri = urlToUTF8(uri);
		logger.debug("Download '{}' from server.", uri);

		File file = mapper.toFile(uri);
		logger.debug("Download '{}' from server.", file.getAbsolutePath());

		if(!file.exists()){
			logger.error("The resource '{}' is not found", file.getAbsolutePath());
			throw new ResourceNotFoundException();
		}

		return new FileSystemResource(file);
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
