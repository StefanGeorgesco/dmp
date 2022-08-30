package fr.cnam.stefangeorgesco.rnipp.configuration;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
public class WebSecurityConfig {

	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().cors()
				.configurationSource(new CorsConfigurationSource() {
					@Override
					public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
						CorsConfiguration config = new CorsConfiguration();
						config.setAllowedOriginPatterns(Collections.singletonList("*"));
						config.setAllowedMethods(Collections.singletonList("*"));
						config.setMaxAge(3600L);
						return config;
					}
				}).and().csrf().disable()
				.authorizeHttpRequests((auth) -> auth.mvcMatchers(HttpMethod.POST, "/check").permitAll())
				.httpBasic(Customizer.withDefaults());

		return http.build();
	}

}
