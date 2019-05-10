package jp.honestyworks.demo.lightning_talk;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jp.honestyworks.demo.lightning_talk.security.GoogleIdTokenDecoder;

@Profile("!SECURITY_MOCK")
@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private final static Log log = LogFactory.getLog(SecurityConfig.class);
	
	@Value(value = "${gapi.clientId}")
	private String clientId;
	
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080","http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","PATCH","OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        //configuration.addAllowedHeader("Authorization");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
    
    @Bean
    public JwtDecoder jwtDecoder() {
    	return new GoogleIdTokenDecoder(clientId);
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	
    	log.info("configure start...");
    	
        http
    		.exceptionHandling()
    	.and()
    		.csrf().disable()
    		.cors()
    	.and()
    		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    	.and()
    		.authorizeRequests()
    		.antMatchers(HttpMethod.POST, "/api/v1/talks").authenticated()
    		.antMatchers("/api/v1/**").permitAll()
    		.anyRequest().denyAll()
    	.and()
    		.oauth2ResourceServer()
    		.jwt();
    }

}
