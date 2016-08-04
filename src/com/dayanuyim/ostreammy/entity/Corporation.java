package com.dayanuyim.ostreammy.entity;

import java.time.LocalDate;

//integrate with Person?
public class Corporation {
	private String name;
	private String engName;
	private String chtName;
	private LocalDate birthday;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEngName() {
		return engName;
	}
	public void setEngName(String engName) {
		this.engName = engName;
	}
	public String getChtName() {
		return chtName;
	}
	public void setChtName(String chtName) {
		this.chtName = chtName;
	}
	public LocalDate getBirthday() {
		return birthday;
	}
	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}
}
