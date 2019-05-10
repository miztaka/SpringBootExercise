package jp.honestyworks.demo.lightning_talk.api;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doAnswer;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;


import static org.mockito.Mockito.any;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import jp.honestyworks.demo.lightning_talk.TestHelper;
import jp.honestyworks.demo.lightning_talk.dto.TalkDto;
import jp.honestyworks.demo.lightning_talk.dto.TalkSubmissionDto;
import jp.honestyworks.demo.lightning_talk.entity.Talk;
import jp.honestyworks.demo.lightning_talk.entity.User;
import jp.honestyworks.demo.lightning_talk.service.TalksService;

@RunWith(SpringRunner.class)
@WebMvcTest(TalksApiController.class)
@AutoConfigureMockMvc
@ActiveProfiles("SECURITY_MOCK")
public class TalksApiControllerTest {
	
	private final static Log log = LogFactory.getLog(TalksApiControllerTest.class);
	
	@Autowired
    private WebApplicationContext context;	
	
	@MockBean
	private TalksService talksService;
	
	@Autowired
	private TalksApiController controller;
	
	//@Autowired
    private MockMvc mockMvc;
    
    private MockHttpServletRequestBuilder postRequest;
	
	@Before
    public void setup() {
        mockMvc = MockMvcBuilders
          .webAppContextSetup(context)
          .apply(springSecurity())
          .build();
        postRequest = MockMvcRequestBuilders.post("/api/v1/talks")
      	  .header("Authorization", "Bearer xxxxxxxxxxxxxxxxxxxxxxxxxxxx");
    }
	
	@Test
	public void testCreateTalk_valid() throws Exception {

		// given mock service class
		doAnswer((Answer) invocation -> {
			String capturedEmail = (String)invocation.getArgument(0);
			Talk capturedTalk = (Talk)invocation.getArgument(2);
			User u = TestHelper.userEntity();
			u.setEmail(capturedEmail);
			u.setId(1000L);
			capturedTalk.setUser(u);
			capturedTalk.setId(10000L);
			return null;
		}).when(talksService).createTalk(any(String.class), any(String.class), any(Talk.class));
		
		TalkSubmissionDto s = new TalkSubmissionDto();
		s.setEmail("foo@bar.com");
		s.setTopic(StringUtils.repeat("a", 80));
		s.setDescription(StringUtils.repeat("word", " ", 120));
		ObjectMapper mapper = new ObjectMapper();
		String body = mapper.writeValueAsString(s);
		log.warn(body);
		
	    mockMvc.perform(postRequest
	      .content(body)
	      .contentType(MediaType.APPLICATION_JSON_UTF8))
	      .andExpect(MockMvcResultMatchers.status().isOk())
	      .andExpect(MockMvcResultMatchers.content()
	      .contentType(MediaType.APPLICATION_JSON_UTF8));
	}
	@Test
	public void testCreateTalk_topicIsTooLong() throws Exception {

		TalkSubmissionDto s = new TalkSubmissionDto();
		s.setEmail("foo@bar.com");
		s.setTopic(StringUtils.repeat("a", 81));
		s.setDescription("bbbbbb");
		ObjectMapper mapper = new ObjectMapper(); 
		String body = mapper.writeValueAsString(s);
		log.warn(body);
		
	    mockMvc.perform(postRequest
	      .content(body)
	      .contentType(MediaType.APPLICATION_JSON_UTF8))
	      .andExpect(MockMvcResultMatchers.status().is(400));
	}

	@Test
	public void testCreateTalk_invalidEmail() throws Exception {

		TalkSubmissionDto s = new TalkSubmissionDto();
		s.setEmail("foo.com");
		s.setTopic(StringUtils.repeat("a", 80));
		s.setDescription(StringUtils.repeat("word", " ", 120));
		ObjectMapper mapper = new ObjectMapper(); 
		String body = mapper.writeValueAsString(s);
		log.warn(body);
		
	    mockMvc.perform(postRequest
	      .content(body)
	      .contentType(MediaType.APPLICATION_JSON_UTF8))
	      .andExpect(MockMvcResultMatchers.status().is(400));
	}

	@Test
	public void testCreateTalk_descriptionTooLong() throws Exception {

		TalkSubmissionDto s = new TalkSubmissionDto();
		s.setEmail("foo@bar.com");
		s.setTopic(StringUtils.repeat("a", 80));
		s.setDescription(StringUtils.repeat("word", " ", 121));
		ObjectMapper mapper = new ObjectMapper(); 
		String body = mapper.writeValueAsString(s);
		log.warn(body);
		
	    mockMvc.perform(postRequest
	      .content(body)
	      .contentType(MediaType.APPLICATION_JSON_UTF8))
	      .andExpect(MockMvcResultMatchers.status().is(400));
	}
	
	@Test
	public void testConvertToDto() {
		
		// given
		Talk t = TestHelper.talkEntity();
		t.setId(1000L);
		
		// when
		TalkDto dto = controller.convertToDto(t);
		
		// then
		assertEquals(dto.getBrowser(), t.getBrowser());
		assertEquals(dto.getDescription(), t.getDescription());
		assertEquals(dto.getEmail(), t.getUser().getEmail());
		assertEquals(dto.getHostname(), t.getHostname());
		assertEquals(dto.getId(), t.getId());
		assertEquals(dto.getOs(), t.getOs());
		assertEquals(dto.getSessionDate(), t.getSessionDate());
		assertEquals(dto.getSubmittedAt(), t.getSubmittedAt());
		assertEquals(dto.getTopic(), t.getTopic());
	}

}
