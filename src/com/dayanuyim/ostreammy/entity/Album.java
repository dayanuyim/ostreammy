package com.dayanuyim.ostreammy.entity;

import java.time.LocalDate;
import java.util.Objects;

import javax.imageio.stream.ImageInputStream;

public class Album {
	private String name;
	private LocalDate publishDate;
	private CompactDisk[] disks;
	private Person artist;
	private String[] booklet_paths;
	
	public int getDiskNumber(){
		return disks == null? 0: disks.length;
	}

}
