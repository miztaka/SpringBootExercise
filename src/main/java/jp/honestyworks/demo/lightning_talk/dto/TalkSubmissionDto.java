package jp.honestyworks.demo.lightning_talk.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import jp.honestyworks.demo.lightning_talk.validation.WordCount;

public class TalkSubmissionDto {
	
	@NotEmpty
	@Size(max = 80)
	private String topic;
	
	@NotEmpty
	@WordCount(max = 120)
	private String description;
	
	@NotEmpty
	@Size(max = 255)
	@Email
	private String email;
	
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

}
