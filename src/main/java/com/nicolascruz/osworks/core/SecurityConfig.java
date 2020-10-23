package com.nicolascruz.osworks.core;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.nicolascruz.osworks.security.JWTAuthenticationFilter;
import com.nicolascruz.osworks.security.JWTAuthorizationFilter;
import com.nicolascruz.osworks.security.JWTUtil;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //para liberar a autorização aos endpoints
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private Environment env; //para liberar o H2 console 
	
	@Autowired
	private JWTUtil jwtUtil;
	
	private static final String[] PUBLIC_MATCHERS = { //vetor de acesso público, ou seja, qualquer tipo de usuário pode manipular
			"/h2-console/**"
			
	};
	private static final String[] PUBLIC_MATCHERS_GET = { //vetor de acesso somente de leitura, caso não exista autenticação, o usuário pode acessar apenas para recuperar dados
			"/h2-console/**",
			"/estado/**"
			
	}; //No neu caso, se o usuário não estiver logado, não pode fazer nada. Se fosse um app de vendas poderia listar produtos
	
	private static final String[] PUBLIC_MATCHERS_POST = { //vetor de acesso somente de leitura, caso não exista autenticação, o usuário pode acessar apenas para recuperar dados
			"/auth/forgot/**",
			"/clientes/**"
			
	};
	//Se for um app que o cliente pode se cadastrar, criar um outro Array com os endpoints que podem ser acessados e adicionar no método abaixo com o tipo "POST", por exemplo
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		if(Arrays.asList(env.getActiveProfiles()).contains("test")) {
			http.headers().frameOptions().disable(); //se estiver no profile de teste terá que acessar o H2, então precisa desse comando pra ativá-lo
		}
		
		http.cors().and().csrf().disable(); //aplica as configurações do método/bean abaixo e desabilita a proteção CSRF porque o sistema não gera sessão de usuario
		http.authorizeRequests()
		.antMatchers(PUBLIC_MATCHERS).permitAll()
		.antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll()
		.antMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
		.anyRequest().authenticated();
		http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil)); //está registrando o filtro de autenticação
		http.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetailsService)); //está registrando o filtro de autorização
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //garante que o sistema não vai gerar seessão de usuário
	}
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
		configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS")); //liberando o CORS para aceitar esses métodos
		final UrlBasedCorsConfigurationSource source = new  UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
