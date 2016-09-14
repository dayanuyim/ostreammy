package com.dayanuyim.ostreammy.entity;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.ArrayUtils;

public class Album {
	private String id;
	
	@NotNull
	@Size(min=1)
	private String name;

	private Person artist;
	private String tags;
	private LocalDate publishDate;
	private String publisher;
	private AudioTrack[] tracks;
	private File[] booklets;
	private File[] others;
	private String comment;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Person getArtist() {
		return artist;
	}
	public void setArtist(Person artist) {
		this.artist = artist;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public LocalDate getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(LocalDate publishDate) {
		this.publishDate = publishDate;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public AudioTrack[] getTracks() {
		return tracks;
	}
	public void setTracks(AudioTrack[] tracks) {
		this.tracks = tracks;
	}
	public File[] getBooklets() {
		return booklets;
	}
	public void setBooklets(File[] booklets) {
		this.booklets = booklets;
	}
	public File[] getOthers() {
		return others;
	}
	public void setOthers(File[] others) {
		this.others = others;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	//============================================
	
	public List<AudioTrack[]> getDisks()
	{
		if(ArrayUtils.getLength(tracks) == 0)
			return null;

		ArrayList<AudioTrack[]> disks = new ArrayList<>();

		//group by disk no.
		Arrays.sort(tracks, (AudioTrack t1, AudioTrack t2) -> t1.compareTo(t2));

		int begin = 0;
		for(int i = 0; i < tracks.length; ++i){
			if(tracks[i].getDiskNo() != tracks[begin].getDiskNo()){
				disks.add(ArrayUtils.subarray(tracks, begin, i));
				begin = i;
			}
		}
		
		if(begin < tracks.length)
			disks.add(ArrayUtils.subarray(tracks, begin, tracks.length));
		
		return disks;
		
	}

	public File getCover(){
		if(ArrayUtils.getLength(booklets) > 0)
			return booklets[0];
		return null;
	}
}
