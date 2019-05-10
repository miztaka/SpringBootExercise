package jp.honestyworks.demo.lightning_talk.repository;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import jp.honestyworks.demo.lightning_talk.TestHelper;
import jp.honestyworks.demo.lightning_talk.entity.Talk;
import jp.honestyworks.demo.lightning_talk.entity.User;
import jp.honestyworks.demo.lightning_talk.model.TalkStatus;

@RunWith(SpringRunner.class)
@DataJpaTest
// @ActiveProfiles("test")
@AutoConfigureTestDatabase(replace=Replace.NONE)
public class TalkRepositoryTest {
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private TalkRepository talkRepository;

	@Test
	public void whenFindAll_thenReturnTwoRecords() {
		
		// given
		User u = TestHelper.userEntity();
		entityManager.persist(u);
		
		Talk t = TestHelper.talkEntity();
		t.setUser(u);
		entityManager.persist(t);

		t = TestHelper.talkEntity();
		t.setTopic("foo2");
		t.setDescription("bar2");
		t.setUser(u);
		entityManager.persist(t);
		
		// when
		List<Talk> talks = talkRepository.findAll();
		
		// then
		assertEquals(2, talks.size());
		assertEquals(TalkStatus.submitted, talks.get(0).getStatus());
	}

}
