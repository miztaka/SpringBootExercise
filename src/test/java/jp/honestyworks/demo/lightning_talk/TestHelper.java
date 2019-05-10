package jp.honestyworks.demo.lightning_talk;

import java.util.Date;

import jp.honestyworks.demo.lightning_talk.entity.Talk;
import jp.honestyworks.demo.lightning_talk.entity.User;
import jp.honestyworks.demo.lightning_talk.model.TalkStatus;

public class TestHelper {
	
	public static User userEntity() {
		
		User u = new User();
		u.setEmail("miztaka@honestyworks.jp");
		u.setSub("sub|xxxxx");
		
		return u;
	}
	
	public static Talk talkEntity() {
		
		Talk t = new Talk();
		t.setTopic("foo");
		t.setDescription("bar");
		t.setSessionDate(new Date(System.currentTimeMillis() + 86400*1000*100));
		t.setSubmittedAt(new Date());
		t.setStatus(TalkStatus.submitted);
		t.setUser(userEntity());
		t.setIpaddr("127.0.0.1");
		t.setOs("Ubuntu");
		t.setHostname("honestyworks.jp");
		t.setBrowser("Chrome");
		
		return t;
	}

}
