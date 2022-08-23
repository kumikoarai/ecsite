package com.example.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration

public class UserSecurityConfig {

	@Bean
	@Primary
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


    /*インメモリ認証*/
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
            PasswordEncoder encoder = passwordEncoder();
            UserDetails user = User.withUsername("user")
                            .password(encoder.encode("user"))
                            .roles("ENDUSER")
                            .build();
            UserDetails admin = User.withUsername("admin")
                            .password(encoder.encode("admin"))
                            .roles("ADMIN")
                            .build();
            UserDetails admin_gene = User.withUsername("admin_gene")
		                    .password(encoder.encode("admin_gene"))
		                    .roles("GENERAL")
		                    .build();
            return new InMemoryUserDetailsManager(user, admin, admin_gene);
    }


	/*セキュリティの各種設定*/
    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            //ログイン不要ページの設定
             http.antMatcher("/user/**").formLogin(login -> login
                    .loginProcessingUrl("/user/login")
                    .loginPage("/login")
                    .failureUrl("/login?error")
                    .usernameParameter("userId")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/user/login", true)
                    .permitAll()
            ).logout(logout -> logout
            		.logoutUrl("/user/logout")
                    .logoutSuccessUrl("/top")
            ).authorizeHttpRequests(authz -> authz
                    .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                    .antMatchers("/webjars/*").permitAll()
                    .antMatchers("/css/*").permitAll()
                    .antMatchers("/js/*").permitAll()
                    .antMatchers("/login").permitAll()//直リンクOK
                    .antMatchers("/admin/login").permitAll()//直リンクOK
                    .antMatchers("/top").permitAll()//直リンクOK
                    .antMatchers("/top/search").permitAll()//直リンクOK
                    .antMatchers("/user/**").hasRole("ENDUSER")
                    .anyRequest().authenticated()
            );


            //CSRF対策を無効に設定（一時的）
            http.csrf().disable();

            return http.build();
    }



    /*セキュリティの各種設定*/
    @Bean
    public SecurityFilterChain securityFilterChain2(HttpSecurity http) throws Exception {
            //ログイン不要ページの設定
             http.formLogin(login -> login
                    .loginProcessingUrl("/admin/login/login")
                    .loginPage("/admin/login")
                    .failureUrl("/admin/login?error")
                    .usernameParameter("userId")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/admin/login/login", true)
                    .permitAll()
            ).logout(logout -> logout
            		.logoutUrl("/logout")
                    .logoutSuccessUrl("/top")
            ).authorizeHttpRequests(authz -> authz
                    .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                    //.antMatchers("/login/admin").permitAll()//直リンクOK
                    .antMatchers("/webjars/*").permitAll()
                    .antMatchers("/css/*").permitAll()
                    .antMatchers("/js/*").permitAll()
                    .antMatchers("/login").permitAll()//直リンクOK
                    .antMatchers("/admin/login").permitAll()//直リンクOK
                    .antMatchers("/top").permitAll()//直リンクOK
                    .antMatchers("/top/search").permitAll()//直リンクOK
                    .antMatchers("/admin/add").hasRole("ADMIN")
                    .antMatchers("/admin/**").hasAnyRole("ADMIN", "GENERAL")
                    .anyRequest().authenticated()
            );

            //CSRF対策を無効に設定（一時的）
            http.csrf().disable();

            return http.build();
    }



}
