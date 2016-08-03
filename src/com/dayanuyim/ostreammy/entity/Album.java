package com.dayanuyim.ostreammy.entity;

import java.io.File;
import java.time.LocalDate;
import java.util.Objects;

import javax.imageio.stream.ImageInputStream;

public class Album {
	private String id;
	private String name;
	private Person artist;
	private String genre;
	private LocalDate publishDate;
	private Corporation publisher;
	private AudioTrack[] tracks;
	private File[] booklets;
	private File[] othres;
	private String comment;
	
	/*
	public int getDiskNumber(){
		return disks == null? 0: disks.length;
	}
	*/
}
