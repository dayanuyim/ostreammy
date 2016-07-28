package com.dayanuyim.ostreammy;

import java.awt.List;
import java.io.File;
import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.hamcrest.core.Is;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Path("/repo")
public class Repo {
	
	public static final String repo_path = "/home/tsungtatsai/web/repo";
	public static final String preload_path = repo_path + "/_preload";

	private static Logger logger = LoggerFactory.getLogger(Repo.class);

	@GET
	public String hello(){
		return "The page for reposiotry management";
	}
	
	@GET
	@Path("/preload")
	public String preloadTheFirst(){
		return _preload(1);
	}

	@GET
	@Path("/preload/{sn}")
	public String preload(@PathParam("sn") int sn)
	{
		return _preload(sn);
	}
	
	private String _preload(int sn)
	{
		File file = getRepoData(sn);
		if(file == null)
			return "No data exists";

		if(file.isFile()){
			return "Audio Track: " + file.getAbsolutePath();
		}
		
		if(file.isDirectory()){
			return "Album: " + file.getAbsolutePath();
		}

		return "Unknown data: " + file.getAbsolutePath();
	}
	
	public static File getRepoData(int sn)
	{
		File repo = new File(preload_path);
		if(!repo.exists())
			throw new RuntimeException("The repo folder does not exist");
		if(!repo.isDirectory())
			throw new RuntimeException("The repo folder is not dir");

		for(File f: repo.listFiles()){
			if(f.isDirectory() && f.list().length == 0){
				logger.info("deleting empty dir '{}'", f.getAbsolutePath());
				f.delete();
				continue;
			}

			if((--sn) == 0)
				return f;
		}
		/*
		//get regular files first
		File[] files = getDataFiles(repo);
		if(sn <= files.length)
			return files[sn - 1];
		
		//try get dirs
		sn -= files.length;
		File[] dirs = getDataDirs(repo);
		if(sn <= dirs.length)
			return dirs[sn - 1];
		*/

		return null;
	}
	
	public static File[] getDataFiles(File root)
	{
		ArrayList<File> files = new ArrayList<>();
		for(File f: root.listFiles()){
			if(f.isFile())
				files.add(f);
		}
		return files.toArray(new File[0]);
	}
	
	//get all dirs which contains data(regular files)
	public static File[] getDataDirs(File root)
	{
		ArrayList<File> dirs = new ArrayList<>();

		_getDataDirs(dirs, root);

		if(dirs.size() > 0 && dirs.get(dirs.size()-1).equals(root))
			dirs.remove(dirs.size()-1);
		return dirs.toArray(new File[0]);
	}
		
	public static void _getDataDirs(ArrayList<File> dirs,File root)
	{
		File[] children = root.listFiles();
		if(children.length == 0){
			logger.warn("The forlder '{}' is empty, deleting it...", root.getAbsoluteFile());
			//to delete 
		}

		boolean has_files = false;
		for(File f: root.listFiles()){
			if(f.isDirectory())
				_getDataDirs(dirs, f);
			else if(f.isFile())
				has_files = true;
		}

		if(has_files)
			dirs.add(root);
	}

}
