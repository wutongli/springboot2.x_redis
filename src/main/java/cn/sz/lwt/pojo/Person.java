package cn.sz.lwt.pojo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Person {
	private String name;
	private Integer age;
	//@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss",,timezone = "GMT+8")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8"
    )
	private Date   birthday;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
}
