package com.dayanuyim.ostreammy.entity;

import java.io.File;

public class AudioTrack {
	private Album album;
	private String title;
	private String no;
	private int length; //sec
	private Person[] artists;
	private Person originalArtist;
	private Person composer;
	private String genre;
	private String url;
	private String comment;
	private String sha1;
	private String encoder;
	private int bitrate;
	private boolean vbr;
	private File location;
}
