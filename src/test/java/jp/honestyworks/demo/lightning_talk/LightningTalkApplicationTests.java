package jp.honestyworks.demo.lightning_talk;

import java.util.Date;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import jp.honestyworks.demo.lightning_talk.dto.TalkDto;
import jp.honestyworks.demo.lightning_talk.entity.Talk;
import jp.honestyworks.demo.lightning_talk.entity.User;
import jp.honestyworks.demo.lightning_talk.model.TalkStatus;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LightningTalkApplicationTests {
	
	@Autowired
	private ModelMapper modelMapper;

	/**
	 * Test model mapper configuration
	 */
	@Test
	public void modelMapperTest() {
		
		// given
		Talk t = TestHelper.talkEntity();
		t.setId(1000L);
		
		// when
		TalkDto dto = modelMapper.map(t, TalkDto.class);
		
		// then
		assertEquals(dto.getBrowser(), t.getBrowser());
		assertEquals(dto.getDescription(), t.getDescription());
		assertNull(dto.getEmail());
		assertEquals(dto.getHostname(), t.getHostname());
		assertEquals(dto.getId(), t.getId());
		assertEquals(dto.getOs(), t.getOs());
		assertEquals(dto.getSessionDate(), t.getSessionDate());
		assertEquals(dto.getSubmittedAt(), t.getSubmittedAt());
		assertEquals(dto.getTopic(), t.getTopic());
	}

}
