package com.dayanuyim.ostreammy.entity;

import java.io.File;
import java.time.LocalDate;
import java.util.Objects;

import javax.imageio.stream.ImageInputStream;

public class Album {
	private String id;
	private String name;
	private Person artist;
	private String tags;
	private LocalDate publishDate;
	private Corporation publisher;
	private AudioTrack[] tracks;
	private File[] booklets;
	private File[] othres;
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
	public Corporation getPublisher() {
		return publisher;
	}
	public void setPublisher(Corporation publisher) {
		this.publisher = publisher;
	}
	public AudioTrack[] getTracks() {
		return tracks;
	}
	public void setTracks(AudioTrack[] tracks) {
		for(AudioTrack t: tracks)
			t.setAlbum(this);
		this.tracks = tracks;
	}
	public File[] getBooklets() {
		return booklets;
	}
	public void setBooklets(File[] booklets) {
		this.booklets = booklets;
	}
	public File[] getOthres() {
		return othres;
	}
	public void setOthres(File[] othres) {
		this.othres = othres;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	//============================================
	
	/*
	public int getDiskNumber(){
		return disks == null? 0: disks.length;
	}
	*/
	

}
