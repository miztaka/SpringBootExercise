package jp.honestyworks.demo.lightning_talk.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.honestyworks.demo.lightning_talk.entity.Talk;
import jp.honestyworks.demo.lightning_talk.model.TalkStatus;

@Repository
public interface TalkRepository extends JpaRepository<Talk, Long> {

	List<Talk> findByStatusAndSessionDateGreaterThanOrderBySubmittedAtAsc(
		TalkStatus submitted, Date today);

}
