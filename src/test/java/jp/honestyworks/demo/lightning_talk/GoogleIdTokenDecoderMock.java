package jp.honestyworks.demo.lightning_talk;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;


public class GoogleIdTokenDecoderMock implements JwtDecoder {
	
	@Override
	public Jwt decode(String token) throws JwtException {
		
		Map<String,Object> dummyClaims = new HashMap<String,Object>();
		dummyClaims.put("sub", "108316424365970664158");
		dummyClaims.put("email", "foo@bar.com");
		
		Map<String,Object> dummyHeaders = new HashMap<String,Object>();
		dummyHeaders.put("alg", "RS256");
		dummyHeaders.put("kid", "28f5813e327ad14caaf1bf2a12368587e882b604");
		dummyHeaders.put("typ", "JWT");
		
		// convert idToken to Jwt object.
		return new Jwt(
			token,
			Instant.ofEpochMilli(System.currentTimeMillis() - 2000),
			Instant.ofEpochSecond(System.currentTimeMillis() + 60 * 60 * 1000),
			dummyHeaders,
			dummyClaims
		);
	}

}
