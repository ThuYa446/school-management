package com.astarel.school;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.astarel.school.auth.CustomAccessDeniedHandler;
import com.astarel.school.auth.JwtAuthenticationFilter;
import com.astarel.school.service.impl.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	@Autowired
	private CustomAccessDeniedHandler customAccessDeniedHandler;
	
	// PUBLIC endpoints
    List<RequestMatcher> PUBLIC_URLS = List.of(
        new AntPathRequestMatcher("/api/auth/login"),
        new AntPathRequestMatcher("/api/auth/register"),
        new AntPathRequestMatcher("/swagger-ui/**"),
        new AntPathRequestMatcher("/v3/api-docs/**"),
        new AntPathRequestMatcher("/actuator/**")
    );

    // PRIVATE endpoints (must be logged in)
    List<RequestMatcher> PRIVATE_URLS = List.of(
        new AntPathRequestMatcher("/api/teachers/**"),
        new AntPathRequestMatcher("/api/class-rooms/**"),
        new AntPathRequestMatcher("/api/students/**"),
        new AntPathRequestMatcher("/api/subjects/**")
    );

	@Autowired
	JwtAuthenticationFilter jwtTokenFilter;

	@Bean
	UserDetailsService userDetailsService() {
		return new UserDetailsServiceImpl();
	}
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
	    CorsConfiguration config = new CorsConfiguration();
	    config.setAllowedOrigins(List.of("http://localhost:4300"));
	    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
	    config.setAllowedHeaders(List.of("*"));
	    config.setAllowCredentials(true);

	    UrlBasedCorsConfigurationSource source =
	            new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", config);
	    return source;
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http, UserDetailsService userDetailsService) throws Exception {
		CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
	    requestHandler.setCsrfRequestAttributeName("_csrf");
	    return http
	    		.cors(Customizer.withDefaults())
	            .csrf(csrf -> csrf
	                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
	                .csrfTokenRequestHandler(requestHandler)
	                .ignoringRequestMatchers(PUBLIC_URLS.toArray(new RequestMatcher[0])))
	            	.authorizeHttpRequests(auth -> auth
	            	.requestMatchers(PUBLIC_URLS.toArray(new RequestMatcher[0])).permitAll() // public
	            	//.requestMatchers(HttpMethod.GET, "/**").permitAll()
	            	.requestMatchers(PRIVATE_URLS.toArray(new RequestMatcher[0])).authenticated() // private
	            	.anyRequest().denyAll())
	            	.exceptionHandling(ex -> ex
	                        .accessDeniedHandler(customAccessDeniedHandler)
	                    )
	            .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
	            .build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService());
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;

	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}
