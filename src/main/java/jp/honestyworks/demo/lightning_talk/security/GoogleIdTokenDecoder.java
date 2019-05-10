package jp.honestyworks.demo.lightning_talk.security;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.util.Collections;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

public class GoogleIdTokenDecoder implements JwtDecoder {
	
	private final GoogleIdTokenVerifier verifier;
	
	public GoogleIdTokenDecoder(String clientId) {
		
		NetHttpTransport TRANSPORT = new NetHttpTransport();
        JacksonFactory JSON_FACTORY = new JacksonFactory();
    	verifier = new GoogleIdTokenVerifier.Builder(TRANSPORT, JSON_FACTORY)
    		.setAudience(Collections.singletonList(clientId))
    		.build();
	}

	@Override
	public Jwt decode(String token) throws JwtException {
		
		GoogleIdToken idToken = null;
		try {
			idToken = verifier.verify(token);
		} catch (GeneralSecurityException e) {
			throw new JwtException("Token is invalid.");
		} catch (IOException e) {
			// TODO retry
			throw new JwtException("Failed to authenticate.");
		}
		if (idToken == null) {
			throw new JwtException("Token is invalid.");
		}
		
		// convert idToken to Jwt object.
		return new Jwt(
			token,
			Instant.ofEpochSecond(idToken.getPayload().getIssuedAtTimeSeconds()),
			Instant.ofEpochSecond(idToken.getPayload().getExpirationTimeSeconds()),
			idToken.getHeader(),
			idToken.getPayload());
	}

}
