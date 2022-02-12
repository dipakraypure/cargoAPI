package com.cargo.mail;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Mail {

	private String from;
	private String to;
	private String subject;
	private String content;
	
	@Override
	public String toString() {
		return "Mail [from=" + from + ", to=" + to + ", subject=" + subject + ", content=" + content + "]";
	}
	
	
}
