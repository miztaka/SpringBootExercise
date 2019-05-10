package jp.honestyworks.demo.lightning_talk.service;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import jp.honestyworks.demo.lightning_talk.TestHelper;
import jp.honestyworks.demo.lightning_talk.entity.Talk;
import jp.honestyworks.demo.lightning_talk.entity.User;
import jp.honestyworks.demo.lightning_talk.model.TalkStatus;
import jp.honestyworks.demo.lightning_talk.repository.TalkRepository;
import jp.honestyworks.demo.lightning_talk.repository.UserRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@ComponentScan(basePackages="jp.honestyworks.demo.lightning_talk")
public class TalksServiceTest {
	
	private final static Log log = LogFactory.getLog(TalksServiceTest.class);
	
	@Autowired
	private TalksService talksService;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TalkRepository talkRepository;
	
	@Test
	public void testGetSessionDate_oddMonth() throws Exception {
		
		// given odd month
		Date submitted  = DateUtils.parseDate("2019-05-13", "yyyy-MM-dd");
		// when
		Date sessionDate = talksService.getSessionDate(submitted);
		// then
		assertEquals(sessionDate.getTime(), DateUtils.parseDate("2019-06-04", "yyyy-MM-dd").getTime());

	}

	@Test
	public void testGetSessionDate_evenMonthBeforeSession() throws Exception {
		
		// given even month before session
		Date submitted  = DateUtils.parseDate("2019-06-03", "yyyy-MM-dd");
		// when
		Date sessionDate = talksService.getSessionDate(submitted);
		// then
		assertEquals(sessionDate.getTime(), DateUtils.parseDate("2019-06-04", "yyyy-MM-dd").getTime());

	}

	@Test
	public void testGetSessionDate_evenMonthAfterSession() throws Exception {
		
		// given even month before session
		Date submitted  = DateUtils.parseDate("2019-06-04", "yyyy-MM-dd");
		// when
		Date sessionDate = talksService.getSessionDate(submitted);
		// then
		assertEquals(sessionDate.getTime(), DateUtils.parseDate("2019-08-06", "yyyy-MM-dd").getTime());

	}
	
	@Test
	public void testGetTalksOfNextSession_success() throws Exception {
		
		// given 2 submitted 1 draft
		User u = TestHelper.userEntity();
		entityManager.persist(u);
		
		Talk t = TestHelper.talkEntity();
		t.setTopic("1111");
		t.setUser(u);
		entityManager.persist(t);
		
		t = TestHelper.talkEntity();
		t.setUser(u);
		t.setTopic("2222");
		entityManager.persist(t);
		
		t.setSubmittedAt(DateUtils.addDays(new Date(), -1)); // this will be the first.
		entityManager.persist(t);

		t = TestHelper.talkEntity();
		t.setUser(u);
		t.setTopic("3333");
		t.setStatus(TalkStatus.draft); // this will not be selected.
		entityManager.persist(t);

		t = TestHelper.talkEntity();
		t.setUser(u);
		t.setTopic("4444");
		t.setSessionDate(DateUtils.addDays(new Date(), -2)); // this will not be selected.
		entityManager.persist(t);
		
		// when
		List<Talk> talks = talksService.getTalksOfNextSession();
		
		// then
		assertEquals(talks.size(), 2);
		assertEquals(talks.get(0).getStatus(), TalkStatus.submitted);
		assertEquals(talks.get(0).getTopic(), "2222");
	}
	
	@Test
	public void testCreateTalk_newUser() {

		// given
		Talk t = TestHelper.talkEntity();
		
		// when
		talksService.createTalk("miztaka@gmail.com", null, t);
		
		// then
		assertEquals(1L, userRepository.count());
		assertEquals(1L, talkRepository.count());
		assertEquals(
			talksService.getSessionDate(new Date()),
			talkRepository.findAll().get(0).getSessionDate());
	}
	
	@Test
	public void testCreateTalk_existUser() {
		
		// given
		String email = "miztaka@gmail.com";
		User u = TestHelper.userEntity();
		u.setEmail(email);
		entityManager.persist(u);
		
		Talk t = TestHelper.talkEntity();
		
		// when
		talksService.createTalk(email, null, t);
		
		// then
		assertEquals(1L, userRepository.count());
		assertEquals(1L, talkRepository.count());
		assertEquals(
			talksService.getSessionDate(new Date()),
			talkRepository.findAll().get(0).getSessionDate());
	}
	
}
