package jp.honestyworks.demo.lightning_talk.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.ValidationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;

import jp.honestyworks.demo.lightning_talk.dto.TalkDto;
import jp.honestyworks.demo.lightning_talk.dto.TalkSubmissionDto;
import jp.honestyworks.demo.lightning_talk.entity.Talk;
import jp.honestyworks.demo.lightning_talk.service.TalksService;

@RestController
@RequestMapping("/api/v1/talks")
public class TalksApiController {
	
	private static Log log = LogFactory.getLog(TalksApiController.class);
	
	@Autowired
	private TalksService talksService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	/**
	 * Get all topics of next session.
	 * @return
	 */
	@GetMapping
	public List<TalkDto> findTalks() {
		
		List<Talk> talks = talksService.getTalksOfNextSession();
		
		return talks
			.stream()
			.map(t -> convertToDto(t))
			.collect(Collectors.toList());
	}
	
	/**
	 * Post new topics to next session.
	 * 
	 * @param submission
	 * @return
	 */
	@PostMapping
	public TalkDto createTalk(
		@Valid
		@RequestBody
		TalkSubmissionDto submission
	) {
		// get authentication
		/*
		GoogleIdAuthentication token = (GoogleIdAuthentication)SecurityContextHolder.getContext().getAuthentication();
		if (!submission.getEmail().equals(token.getEmail())) {
			log.warn("Submitted email does not match to authenticated token.");
			throw new ValidationException("Submitted email does not match to authenticated token.");
		}
		*/
		JwtAuthenticationToken token = (JwtAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
		String userEmail = token.getToken().getClaimAsString("email");
		if (!submission.getEmail().equals(userEmail)) {
			log.warn("Submitted email does not match to authenticated token.");
			throw new ValidationException("Submitted email does not match to authenticated token.");
		}
		
		// convert to entity
		Talk t = modelMapper.map(submission, Talk.class);
		
		// TODO set environment data
		// t.setIpaddr(request.getHeader("REMOTE_ADDR")); // TODO "X-Forwarded-For"
		//t.setBrowser(browser);
		
		// persist
		talksService.createTalk(submission.getEmail(), "google|" + token.getName(), t);
		
		return convertToDto(t);
	}
	
	/**
	 * Convert Entity to Dto
	 * @param entity
	 * @return dto
	 */
	public TalkDto convertToDto(Talk entity) {
		
		TalkDto dto = modelMapper.map(entity, TalkDto.class);
		dto.setEmail(entity.getUser().getEmail());
		
		return dto;
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ValidationException.class)
	public Map<String, String> handleValidationExceptions(ValidationException ex) {
		Map<String, String> errors = new HashMap<>();
		errors.put("msg", ex.getMessage());
		return errors;
	}

}
