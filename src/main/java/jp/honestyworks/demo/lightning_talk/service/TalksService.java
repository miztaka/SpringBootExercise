package jp.honestyworks.demo.lightning_talk.service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.honestyworks.demo.lightning_talk.entity.Talk;
import jp.honestyworks.demo.lightning_talk.entity.User;
import jp.honestyworks.demo.lightning_talk.model.TalkStatus;
import jp.honestyworks.demo.lightning_talk.repository.TalkRepository;
import jp.honestyworks.demo.lightning_talk.repository.UserRepository;
import jp.honestyworks.demo.lightning_talk.util.ExtDateUtil;

@Service
public class TalksService {
	
	@Autowired
	private TalkRepository talkRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	/**
	 * Get session date based on submitted timestamp.
	 * 
	 * @param submittedAt submitted timestamp
	 * @return session date for this submission
	 */
	public Date getSessionDate(Date submittedAt) {
		
		LocalDateTime ldt = ExtDateUtil.toLocalDateTime(submittedAt);
		if (ldt.getMonthValue() % 2 == 1) {
			// submitted in odd month
			LocalDateTime nextSession = ldt
				.plusMonths(1L)
				.with(TemporalAdjusters.firstInMonth(DayOfWeek.TUESDAY));
			return ExtDateUtil.toTruncatedDate(nextSession);
		}
		LocalDateTime nextSession = ldt.with(TemporalAdjusters.firstInMonth(DayOfWeek.TUESDAY));
		if (ldt.isBefore(nextSession)) {
			// submitted in even month before session day
			return ExtDateUtil.toTruncatedDate(nextSession);
		}
		// submitted in even month after session day
		nextSession = ldt.plusMonths(2L).with(TemporalAdjusters.firstInMonth(DayOfWeek.TUESDAY));
		return ExtDateUtil.toTruncatedDate(nextSession);
	}

	/**
	 * Get all topics for next session.
	 * 
	 * @return
	 */
	public List<Talk> getTalksOfNextSession() {
		
		Date today = DateUtils.truncate(new Date(), Calendar.DATE);
		return talkRepository.findByStatusAndSessionDateGreaterThanOrderBySubmittedAtAsc(TalkStatus.submitted, today);
	}
	
	/**
	 * Persist new submission related to userEmail.
	 * If user not exists, create it.
	 * 
	 * @param userEmail
	 * @param subject Sub from Id Token
	 * @param talk
	 */
	public void createTalk(String userEmail, String subject, Talk talk) {
		
		Optional<User> user = userRepository.findOptionalByEmail(userEmail);
		if (user.isPresent()) {
			talk.setUser(user.get());
		} else {
			User newUser = new User();
			newUser.setEmail(userEmail);
			newUser.setSub(subject != null ? subject : "local|" + UUID.randomUUID().toString());
			userRepository.save(newUser);
			talk.setUser(newUser);
		}
		
		// Persist new submission
		createTalk(talk);
		
		return;
	}
	
	/**
	 * Persist new submission
	 * (User is set.)
	 * 
	 * @param talk
	 */
	public void createTalk(Talk talk) {
		
		talk.setStatus(TalkStatus.submitted);
		talk.setSessionDate(getSessionDate(new Date()));
		talkRepository.save(talk);
	}

}
