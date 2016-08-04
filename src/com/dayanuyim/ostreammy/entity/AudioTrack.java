package com.dayanuyim.ostreammy.entity;

import java.io.File;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature.BasicBuilder;

public class AudioTrack {
	private Album album;
	private String title;
	private int no;
	private int diskNo;
	private int totalNo;
	private long length; //sec
	private Person[] artists;
	private Person originalArtist;
	private Person composer;
	private String genre;
	private String url;
	private String comment;
	private String sha1;
	private String encoder;
	private int bitrate;
	private int sampleRate;
	private boolean vbr;
	private File location;
	public Album getAlbum() {
		return album;
	}
	public void setAlbum(Album album) {
		this.album = album;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public int getDiskNo() {
		return diskNo;
	}
	public void setDiskNo(int diskNo) {
		this.diskNo = diskNo;
	}
	public int getTotalNo() {
		return totalNo;
	}
	public void setTotalNo(int totalNo) {
		this.totalNo = totalNo;
	}
	public long getLength() {
		return length;
	}
	public void setLength(long length) {
		this.length = length;
	}
	public Person[] getArtists() {
		return artists;
	}
	public void setArtists(Person[] artists) {
		this.artists = artists;
	}
	public Person getOriginalArtist() {
		return originalArtist;
	}
	public void setOriginalArtist(Person originalArtist) {
		this.originalArtist = originalArtist;
	}
	public Person getComposer() {
		return composer;
	}
	public void setComposer(Person composer) {
		this.composer = composer;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getSha1() {
		return sha1;
	}
	public void setSha1(String sha1) {
		this.sha1 = sha1;
	}
	public String getEncoder() {
		return encoder;
	}
	public void setEncoder(String encoder) {
		this.encoder = encoder;
	}
	public int getBitrate() {
		return bitrate;
	}
	public void setBitrate(int bitrate) {
		this.bitrate = bitrate;
	}
	public boolean isVbr() {
		return vbr;
	}
	public void setVbr(boolean vbr) {
		this.vbr = vbr;
	}
	public File getLocation() {
		return location;
	}
	public void setLocation(File location) {
		this.location = location;
	}
	public int getSampleRate() {
		return sampleRate;
	}
	public void setSampleRate(int sampleRate) {
		this.sampleRate = sampleRate;
	}
	
	//===============================================
}
