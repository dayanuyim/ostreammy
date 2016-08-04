package com.dayanuyim.ostreammy;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.apache.commons.io.FileSystemUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.spi.ThreadContextStack;
import org.junit.Assume;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.w3c.dom.css.ElementCSSInlineStyle;

import com.dayanuyim.ostreammy.entity.Album;
import com.dayanuyim.ostreammy.entity.AudioTrack;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import javassist.expr.NewArray;

@Path("/repo")
@Controller
public class Repo {
	private static Logger logger = LoggerFactory.getLogger(Repo.class);
	
	@Autowired
	@Qualifier("repo")
	private File repo;

	private File preload;

	@PostConstruct
	private void init(){
		//repo = new File(repo_path);
		preload = new File(repo, "_preload");
	}
	
	@GET
	public String hello(){
		String msg = "";
		msg += "The page for reposiotry management:" + "<br>";
		msg += "[repo] " + repo.getAbsolutePath() + "<br>";
		msg += "[preload] " + preload.getAbsolutePath() + "<br>";
		return msg;
	}
	
	@GET
	@Path("/preload")
	public String preloadTheFirst() throws UnsupportedTagException, InvalidDataException, IOException{
		return _preload(1);
	}

	@GET
	@Path("/preload/{sn}")
	public String preload(@PathParam("sn") int sn) throws UnsupportedTagException, InvalidDataException, IOException
	{
		return _preload(sn);
	}
	
	private String _preload(int sn) throws UnsupportedTagException, InvalidDataException, IOException
	{
		File file = getPreloadData(preload, sn);
		if(file == null)
			return "No data exists";

		if(file.isFile()){
			return "Audio Track: " + file.getAbsolutePath();
		}
		
		if(file.isDirectory()){
			buildAlbum(file);
			return "Album: " + file.getAbsolutePath();
		}

		return "Unknown data: " + file.getAbsolutePath();
	}
	
	public static void sortByFilename(File[] files)
	{
		Arrays.sort(files, (File f1, File f2) -> f1.getName().compareTo(f2.getName()));
	}
	
	public static Album buildAlbum(File root) throws UnsupportedTagException, InvalidDataException, IOException
	{
		if(root == null) throw new RuntimeException("root dir does not exist");
		if(!root.isDirectory()) throw new RuntimeException("root dir is not dir");

		//collect sub files and dirs
		File[] files = root.listFiles(File::isFile);
		File[] dirs = root.listFiles(File::isDirectory);
		if(ArrayUtils.getLength(dirs) == 0 && ArrayUtils.getLength(files) == 0)
			throw new RuntimeException("No files within dir to build album");
		
		ArrayList<File[]> disks = new ArrayList<>();

		//all sub-files as a disk
		disks.add(files);

		//a sub-dirs as a disk
		sortByFilename(dirs);
		for(File dir: dirs)
			disks.add(dir.listFiles());
		
		Album album = new Album();
		ArrayList<File> others = new ArrayList<>();
		ArrayList<AudioTrack> tracks = new ArrayList<>();

		int disk_no = 1;
		for(File[] disk: disks){
			sortByFilename(disk);
			List<AudioTrack> disk_tracks = buildDisk(disk, disk_no, others);
			if(disk_tracks.size() > 0){
				logger.info("get {} tracks for disk {}", disk_tracks.size(), disk_no);
				disk_no++;
				tracks.addAll(disk_tracks);
			}
		}
		
		album.setTracks(tracks.toArray(new AudioTrack[0]));
		
		for(AudioTrack t: album.getTracks()){
			logger.info("{}.{}/{} - {}", t.getDiskNo(), t.getNo(), t.getTotalNo(), t.getTitle());
		}

		for(File other: others){
			logger.info("other: {}", other.getAbsolutePath());
		}
		
		return album;
	}
	
	public static List<AudioTrack> buildDisk(File[] files, int disk_no, List<File> others) throws UnsupportedTagException, InvalidDataException, IOException
	{
		ArrayList<AudioTrack> tracks = new ArrayList<>();
		for(File file: files)
		{
			if(file.isDirectory()){
				logger.warn("sub dir '{}' will be ignored", file.getAbsolutePath());
				continue;
			}

			//TODO check music type...
			// support MP3 now
			if(FilenameUtils.getExtension(file.getName()).toLowerCase().equals("mp3")){
				AudioTrack track = buildMp3Track(file);
				if(track.getDiskNo() <= 0)
					track.setDiskNo(disk_no);
				tracks.add(track);
			}
			else{
				//collect others
				if(others != null)
					others.add(file);
			}
		}
		return tracks;
	}
	
	public static AudioTrack buildMp3Track(File file) throws UnsupportedTagException, InvalidDataException, IOException
	{
		if(!file.isFile())
			throw new RuntimeException("not a file");
		
		AudioTrack track = new AudioTrack();
		
		Mp3File mp3 = new Mp3File(file);

		String title = mp3.hasId3v2Tag()? mp3.getId3v2Tag().getTitle():
						mp3.hasId3v1Tag()? mp3.getId3v1Tag().getTitle(): null;

		String artist = mp3.hasId3v2Tag()? mp3.getId3v2Tag().getArtist():
						 mp3.hasId3v1Tag()? mp3.getId3v1Tag().getArtist(): null;

		String track_no = mp3.hasId3v2Tag()? mp3.getId3v2Tag().getTrack():
						 mp3.hasId3v1Tag()? mp3.getId3v1Tag().getTrack(): null;

		String album_artist = mp3.hasId3v2Tag()? mp3.getId3v2Tag().getAlbumArtist(): null;

		String album_name = mp3.hasId3v2Tag()? mp3.getId3v2Tag().getAlbum():
						 	mp3.hasId3v1Tag()? mp3.getId3v1Tag().getAlbum(): null;

		Integer genre = mp3.hasId3v2Tag()? mp3.getId3v2Tag().getGenre():
						mp3.hasId3v1Tag()? mp3.getId3v1Tag().getGenre(): null;

		String genre_desc = mp3.hasId3v2Tag()? mp3.getId3v2Tag().getGenreDescription():
						mp3.hasId3v1Tag()? mp3.getId3v1Tag().getGenreDescription(): null;
						
		String year = mp3.hasId3v2Tag()? mp3.getId3v2Tag().getYear():
						mp3.hasId3v1Tag()? mp3.getId3v1Tag().getYear(): null;
		
		String comment = mp3.hasId3v2Tag()? mp3.getId3v2Tag().getComment():
						mp3.hasId3v1Tag()? mp3.getId3v1Tag().getComment(): null;
		
		int[] track_nos = parseTrackNo(track_no);
		track.setDiskNo(track_nos[0]);
		track.setNo(track_nos[1]);
		track.setTotalNo(track_nos[2]);
		
		track.setTitle(StringUtils.isBlank(title)? file.getName(): title);
		track.setLength(mp3.getLengthInSeconds());
		track.setBitrate(mp3.getBitrate());
		track.setSampleRate(mp3.getSampleRate());
		track.setComment(comment);
		track.setLocation(file);
		
		return track;
	}
	
	// <disk_no>.<track_no>/<total_no>
	public static int[] parseTrackNo(String s)
	{
		int pos = s.indexOf('.');
		int disk_no = (pos < 0)? 0: NumberUtils.toInt(s.substring(0, pos), 0);

		int pos2 = s.lastIndexOf('/');
		int total_no = (pos2 < 0)? 0: NumberUtils.toInt(s.substring(pos2 + 1), 0);
		
		pos = (pos < 0)? 0: pos + 1;
		pos2 = (pos2 < 0)? s.length(): pos2;
		int track_no = NumberUtils.toInt(s.substring(pos, pos2), 0);

		return new int[]{disk_no, track_no, total_no};
	}
	
	
	
	public static File getPreloadData(File dir,int sn)
	{
		if(!dir.exists())
			throw new RuntimeException("The repo folder does not exist");
		if(!dir.isDirectory())
			throw new RuntimeException("The repo folder is not dir");

		for(File f: dir.listFiles()){
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
