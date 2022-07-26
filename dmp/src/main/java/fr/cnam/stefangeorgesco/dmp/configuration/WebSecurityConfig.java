package fr.cnam.stefangeorgesco.dmp.configuration;

import java.util.Arrays;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.Customizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import fr.cnam.stefangeorgesco.dmp.utils.JWTTokenGeneratorFilter;
import fr.cnam.stefangeorgesco.dmp.utils.JWTTokenValidatorFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {

	@Value("${frontend.url}")
	String frontEndUrl;

	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().cors()
				.configurationSource(new CorsConfigurationSource() {
					@Override
					public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
						CorsConfiguration config = new CorsConfiguration();
						config.setAllowedOrigins(Collections.singletonList(frontEndUrl));
						config.setAllowedMethods(Collections.singletonList("*"));
						config.setAllowCredentials(true);
						config.setAllowedHeaders(Collections.singletonList("*"));
						config.setExposedHeaders(Arrays.asList("Authorization"));
						config.setMaxAge(3600L);
						return config;
					}
				}).and().csrf().disable()
				.addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class)
				.addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class)
				.authorizeHttpRequests((auth) -> auth
						.mvcMatchers(HttpMethod.POST, "/login").permitAll()
						.mvcMatchers(HttpMethod.POST, "/user").permitAll()
						.mvcMatchers(HttpMethod.POST, "/doctor").hasRole("ADMIN")
						.mvcMatchers(HttpMethod.POST, "/patient-file").hasRole("DOCTOR")
						.mvcMatchers(HttpMethod.GET, "/doctor/details").hasRole("DOCTOR")
						.mvcMatchers(HttpMethod.PUT, "/doctor/details").hasRole("DOCTOR")
						.mvcMatchers(HttpMethod.GET, "/patient-file/details").hasRole("PATIENT")
						.mvcMatchers(HttpMethod.PUT, "/patient-file/details").hasRole("PATIENT")
						.mvcMatchers(HttpMethod.PUT, "/patient-file/{id}/referring-doctor").hasRole("ADMIN")
						.mvcMatchers(HttpMethod.GET, "/doctor/{id}").authenticated()
						.mvcMatchers(HttpMethod.GET, "/patient-file/{id}").hasAnyRole("ADMIN", "DOCTOR")
						.mvcMatchers(HttpMethod.DELETE, "/doctor/{id}").hasRole("ADMIN")
						.mvcMatchers(HttpMethod.GET, "/doctor").hasAnyRole("ADMIN", "DOCTOR")
						.mvcMatchers(HttpMethod.GET, "/patient-file").hasAnyRole("ADMIN", "DOCTOR")
						.mvcMatchers(HttpMethod.POST, "/patient-file/{id}/correspondance").hasRole("DOCTOR")
						.anyRequest().denyAll())
				.httpBasic(Customizer.withDefaults());
		return http.build();

	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
