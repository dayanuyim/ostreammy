package com.dayanuyim.ostreammy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
//import org.glassfish.jersey.server.mvc.Viewable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

import com.dayanuyim.ostreammy.annotation.Location;
import com.dayanuyim.ostreammy.entity.Album;
import com.dayanuyim.ostreammy.entity.AudioTrack;
import com.dayanuyim.ostreammy.entity.Corporation;
import com.dayanuyim.ostreammy.entity.Person;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import static com.dayanuyim.ostreammy.Utils.*;
import static org.apache.commons.lang3.StringUtils.*;

@RequestMapping(value=Repo.URI_PATH)
@Controller
public class Repo {
	public static final String URI_PATH = "/repo";

	public static final String PRELOAD_PATH = "/_";
	public static final String DEF_ALBUM_IMG_NAME = "default_album_image";
	public static final String ARRAY_STR_SP = ";";

	private static Logger logger = LoggerFactory.getLogger(Repo.class);

	
	@Autowired
	@Qualifier("repo") @Location
	private File repo;
	
	@Autowired
	@Qualifier("preload") @Location
	private File preload;
	
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFoundException() {
        return null;
    }
	
	@GetMapping
	public String info(Model model)
	{
		model.addAttribute("repo", repo);
		model.addAttribute("preload", preload);
		return "repo_info";
	}

	@GetMapping("/**")
	@ResponseBody
	public FileSystemResource download(HttpServletRequest req) throws UnsupportedEncodingException
	{
		String sub_uri = req.getServletPath() + defaultString(req.getPathInfo());
		sub_uri = sub_uri.substring(URI_PATH.length());
		return downloadFile(preload, sub_uri);
	}

	@GetMapping(Repo.PRELOAD_PATH + "/**")
	@ResponseBody
	public FileSystemResource downloadPreload(HttpServletRequest req) throws UnsupportedEncodingException
	{
		String sub_uri = req.getServletPath() + defaultString(req.getPathInfo());
		sub_uri = sub_uri.substring((URI_PATH + PRELOAD_PATH).length());
		return downloadFile(preload, sub_uri);
	}
	
	private FileSystemResource downloadFile(File dir, String sub_uri) throws UnsupportedEncodingException
	{
		String path = URLDecoder.decode(sub_uri, "UTF-8");
		logger.info("Download '{}' from {}.", path, dir.getAbsolutePath());

		//get server file
		File file = new File(dir, path);
		logger.debug("Download '{}' from server.", file.getAbsolutePath());
		if(!file.exists()){
			logger.error("The resource '{}' is not found", file.getAbsolutePath());
			throw new ResourceNotFoundException();
		}

		return new FileSystemResource(file);
	}
	
	@GetMapping("/preload")
	public String preloadTheFirst(Model model,
			HttpServletRequest req)
			throws UnsupportedTagException, InvalidDataException, IOException, NoSuchAlgorithmException
	{
		return preload(model, req, 1);
	}

	@GetMapping("/preload/{sn:[0-9]+}")
	public String preload(Model model,
			HttpServletRequest req,
			@PathVariable("sn") int sn)
			throws UnsupportedTagException, InvalidDataException, IOException, NoSuchAlgorithmException
	{
		logger.info("[preload] uri:     {}", req.getRequestURI());
		logger.info("[preload] context: {}", req.getContextPath());
		logger.info("[preload] servlet: {}", req.getServletPath());
		logger.info("[preload] info   : {}", req.getPathInfo());

		File file = getPreloadData(preload, sn);
		if(file == null)
			//return "No data";
			return "/index.html";

		if(file.isFile()){
			//return "Audio Track: " + file.getAbsolutePath();
			return "/index.html";
		}
		
		if(file.isDirectory()){
			Album album = buildAlbum(file);

			model.addAttribute("album", album);
			model.addAttribute("repo", preload);
			model.addAttribute("prefixPath", req.getContextPath() + URI_PATH + PRELOAD_PATH);
			//return "Album: " + file.getAbsolutePath();
			return "album";
		}

		//return "Unknown data: " + file.getAbsolutePath();
		return "/index.html";
	}
	
	public static String getSubstringBeforeInclude(String s, String spliter)
	{
		int sp_pos = s.indexOf(spliter);
		if(sp_pos < 0)
			throw new RuntimeException(s + " does not contain " + spliter);
		return s.substring(0, sp_pos + spliter.length());
	}

	public static String getSubstingAfterExclude(String s, String spliter)
	{
		int sp_pos = s.indexOf(spliter);
		if(sp_pos < 0)
			throw new RuntimeException(s + " does not contain " + spliter);
		return s.substring(sp_pos + spliter.length());
	}
	
	
	public static void sortByFilename(File[] files)
	{
		Arrays.sort(files, (File f1, File f2) -> f1.getName().compareTo(f2.getName()));
	}
	
	public static Album buildAlbum(File dir) throws UnsupportedTagException, InvalidDataException, IOException, NoSuchAlgorithmException
	{
		if(dir == null) throw new RuntimeException("root dir does not exist");
		if(!dir.isDirectory()) throw new RuntimeException("root dir is not dir");

		//collect sub files and dirs
		File[] files = dir.listFiles(File::isFile);
		File[] dirs = dir.listFiles(File::isDirectory);
		if(ArrayUtils.getLength(dirs) == 0 && ArrayUtils.getLength(files) == 0)
			throw new RuntimeException("No files within dir to build album");
		
		ArrayList<File[]> disks = new ArrayList<>();

		//all sub-files as a disk
		disks.add(files);

		//a sub-dirs as a disk
		sortByFilename(dirs);
		for(File d: dirs)
			disks.add(d.listFiles());
		
		ArrayList<AudioTrack> tracks = new ArrayList<>();
		HashSet<File> booklets = new HashSet<>();
		HashSet<File> others = new HashSet<>();

		int disk_no = 1;
		for(File[] disk: disks){
			sortByFilename(disk);
			List<AudioTrack> disk_tracks = buildDisk(disk_no, disk, booklets, others);
			if(disk_tracks.size() > 0){
				logger.info("get {} tracks for disk {}", disk_tracks.size(), disk_no);
				disk_no++;
				tracks.addAll(disk_tracks);
			}
		}
		
		//build album
		Album album = (tracks.size() > 0)? mp3BuildAlbum(tracks.get(0).getLocation()): new Album();
		if(StringUtils.isBlank(album.getName())) album.setName(dir.getName());

		if(album.getArtist() == null && tracks.size() > 0)
			album.setArtist((Person) arrayElement(tracks.get(0).getArtists(), 0));

		if(tracks.size() > 0){
			//tracks.sort((AudioTrack t1, AudioTrack t2) -> t1.compareTo(t2));
			album.setTracks(tracks.toArray(new AudioTrack[0]));
		}

		if(others.size() > 0) album.setOthers(others.toArray(new File[0]));

		if(booklets.size() > 0){
			//add embedded album image
			File embed_album_img = (File) arrayElement(album.getBooklets(), 0);
			if(embed_album_img != null)
				booklets.add(embed_album_img);
			album.setBooklets(booklets.toArray(new File[0]));
		}
		
		// debug print =============================
		logger.info("alubm '{}':", album.getName());

		if(album.getTracks() != null)
			for(AudioTrack t: album.getTracks())
				logger.info("{}.{}/{} - {}", t.getDiskNo(), t.getNo(), t.getTotalNo(), t.getTitle());

		if(album.getBooklets() != null)
			for(File booklet: album.getBooklets())
				logger.info("booklet: {}", booklet.getAbsolutePath());
		
		if(album.getOthers() != null)
			for(File other: album.getOthers())
				logger.info("other: {}", other.getAbsolutePath());
		
		return album;
	}
	
	public static List<AudioTrack> buildDisk(int disk_no, File[] files, Set<File> booklets, Set<File> others) throws UnsupportedTagException, InvalidDataException, IOException, NoSuchAlgorithmException
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
			String ext = FilenameUtils.getExtension(file.getName()).toLowerCase();
			switch(ext){
			case "mp3":
				AudioTrack track = mp3BuildTrack(file);
				if(track.getDiskNo() <= 0)
					track.setDiskNo(disk_no);

				tracks.add(track);
				break;

			case "gif":
			case "png":
			case "jpg":
				booklets.add(file);
				break;

			default:
				others.add(file);
			}
		}
		return tracks;
	}
	
	
	public static Album mp3BuildAlbum(File file) throws UnsupportedTagException, InvalidDataException, IOException, NoSuchAlgorithmException
	{
		if(!file.isFile())
			throw new RuntimeException("not a file");
		
		Mp3File mp3 = new Mp3File(file);

		//dig data
		String year = mp3.hasId3v2Tag()? mp3.getId3v2Tag().getYear():
						mp3.hasId3v1Tag()? mp3.getId3v1Tag().getYear(): null;
		
		String album_name = mp3.hasId3v2Tag()? mp3.getId3v2Tag().getAlbum():
						 	mp3.hasId3v1Tag()? mp3.getId3v1Tag().getAlbum(): null;

		String album_artist = mp3.hasId3v2Tag()? mp3.getId3v2Tag().getAlbumArtist(): null;
		String publisher = mp3.hasId3v2Tag()? mp3.getId3v2Tag().getPublisher(): null;
		byte[] album_img = mp3.hasId3v2Tag()? mp3.getId3v2Tag().getAlbumImage():  null;
		String album_img_mime = mp3.hasId3v2Tag()? mp3.getId3v2Tag().getAlbumImageMimeType():  null;

		//parsing data
		int year_num = NumberUtils.toInt(year, 0);
		Corporation[] publishers = genCorporations(publisher, ARRAY_STR_SP);
		Person[] artists = genPersons(album_artist, ARRAY_STR_SP);
		File album_img_file = getAlbumImageFile(file,album_img, album_img_mime);
		
		//build album
		Album album = new Album();
		album.setName(album_name);
		album.setArtist((Person)arrayElement(artists, 0));
		album.setPublisher((Corporation)arrayElement(publishers, 0));
		if(year_num > 0) album.setPublishDate(LocalDate.ofYearDay(year_num, 1));
		if(album_img_file != null) album.setBooklets(new File[]{album_img_file});
		
		return album;
	}

	public static AudioTrack mp3BuildTrack(File file) throws UnsupportedTagException, InvalidDataException, IOException, NoSuchAlgorithmException
	{
		if(!file.isFile())
			throw new RuntimeException("not a file");
		
		Mp3File mp3 = new Mp3File(file);

		String title = mp3.hasId3v2Tag()? mp3.getId3v2Tag().getTitle():
						mp3.hasId3v1Tag()? mp3.getId3v1Tag().getTitle(): null;

		String artist = mp3.hasId3v2Tag()? mp3.getId3v2Tag().getArtist():
						 mp3.hasId3v1Tag()? mp3.getId3v1Tag().getArtist(): null;

		String track_no = mp3.hasId3v2Tag()? mp3.getId3v2Tag().getTrack():
						 mp3.hasId3v1Tag()? mp3.getId3v1Tag().getTrack(): null;

		Integer genre = mp3.hasId3v2Tag()? mp3.getId3v2Tag().getGenre():
						mp3.hasId3v1Tag()? mp3.getId3v1Tag().getGenre(): null;

		String genre_desc = mp3.hasId3v2Tag()? mp3.getId3v2Tag().getGenreDescription():
						mp3.hasId3v1Tag()? mp3.getId3v1Tag().getGenreDescription(): null;
						
		String comment = mp3.hasId3v2Tag()? mp3.getId3v2Tag().getComment():
						mp3.hasId3v1Tag()? mp3.getId3v1Tag().getComment(): null;
						
		String orig_artist = mp3.hasId3v2Tag()? mp3.getId3v2Tag().getOriginalArtist(): null;
		String composer = mp3.hasId3v2Tag()? mp3.getId3v2Tag().getComposer(): null;
		String url = mp3.hasId3v2Tag()? mp3.getId3v2Tag().getUrl(): null;
		String encoder = mp3.hasId3v2Tag()? mp3.getId3v2Tag().getEncoder(): null;
		
		//parsing data ================================================
		int[] track_nos = parseTrackNo(track_no);
		Person[] artists = genPersons(artist, ARRAY_STR_SP);
		Person[] orig_artists = genPersons(orig_artist, ARRAY_STR_SP);
		Person[] composers = genPersons(composer, ARRAY_STR_SP);
		String[] tags = uniqueSplit(genre_desc, ARRAY_STR_SP);
		
		//fill up track
		AudioTrack track = new AudioTrack();
		track.setTitle(StringUtils.isBlank(title)? file.getName(): title);
		track.setDiskNo(track_nos[0]);
		track.setNo(track_nos[1]);
		track.setTotalNo(track_nos[2]);
		track.setLength(new Date(mp3.getLengthInMilliseconds()));
		track.setArtists(artists);
		track.setOriginalArtist((Person)arrayElement(orig_artists, 0));
		track.setComposer((Person)arrayElement(composers, 0));  //only the first person
		track.setTags(StringUtils.join(tags, ARRAY_STR_SP));
		track.setComment(comment);
		track.setUrl(url);
		track.setEncoder(encoder);
		track.setVbr(mp3.isVbr());
		track.setBitrate(mp3.getBitrate());
		track.setSampleRate(mp3.getSampleRate());
		track.setSha1(getSHA1(file));
		track.setLocation(file);
		
		return track;
	}

	private static File getAlbumImageFile(File file, byte[] album_img, String album_img_mime) throws IOException, FileNotFoundException {
		if(album_img == null)
			return null;

		String ext = mimeToFileExt(album_img_mime);
		if(ext == null){
			logger.warn("unregconized embedded album image mime type: " + album_img_mime);
			throw new RuntimeException("unregconized embedded album image mime type: " + album_img_mime);
		}
		
		File album_img_file = new File(file.getParentFile(), DEF_ALBUM_IMG_NAME + ext);
		if(!album_img_file.exists()){
			try(RandomAccessFile ra_file = new RandomAccessFile(album_img_file, "rw")){
				ra_file.write(album_img);
			}
		}
		return album_img_file;
	}
	
	private static String mimeToFileExt(String mime){
			return mime.equals("image/gif")? ".gif":
					mime.equals("image/png")? ".png":
					mime.equals("image/jpeg")? ".jpg": null;
	}
	
	
	// <disk_no>.<track_no>/<total_no>
	public static int[] parseTrackNo(String s)
	{
		if(s == null)
			return new int[]{0, 0, 0};

		int pos = s.indexOf('.');
		int disk_no = (pos < 0)? 0: NumberUtils.toInt(s.substring(0, pos), 0);

		int pos2 = s.lastIndexOf('/');
		int total_no = (pos2 < 0)? 0: NumberUtils.toInt(s.substring(pos2 + 1), 0);
		
		pos = (pos < 0)? 0: pos + 1;
		pos2 = (pos2 < 0)? s.length(): pos2;
		int track_no = NumberUtils.toInt(s.substring(pos, pos2), 0);

		return new int[]{disk_no, track_no, total_no};
	}

	public static Person[] genPersons(String s, String sp)
	{
		String[] names = uniqueSplit(s, sp);
		if(names == null) return null;

		Person[] persons = new Person[names.length];
		for(int i = 0; i < persons.length; ++i){
			persons[i] = new Person();
			persons[i].setName(names[i]);
		}
		return persons;
	}

	public static Corporation[] genCorporations(String s, String sp)
	{
		String[] names = uniqueSplit(s, sp);
		if(names == null) return null;

		Corporation[] corporations = new Corporation[names.length];
		for(int i = 0; i < corporations.length; ++i){
			corporations[i] = new Corporation();
			corporations[i].setName(names[i]);
		}
		return corporations;
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
