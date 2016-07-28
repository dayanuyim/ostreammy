package com.dayanuyim.ostreammy.entity;

public class CompactDisk {
	private AudioTrack[] tracks;
	
	public int getTrackNumber(){
		return tracks == null? 0: tracks.length;
	}

}
