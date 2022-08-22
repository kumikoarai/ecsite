package com.example.config;

public class AdminSecurityConfig {
/*
	@Bean

	public PasswordEncoder passwordEncoder2() {
		return new BCryptPasswordEncoder();
	}

	private PasswordEncoder passwordEncoder;

	public AdminSecurityConfig(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}*/





    /*インメモリ認証

    @Bean
    public InMemoryUserDetailsManager userDetailsService2() {
            PasswordEncoder encoder = passwordEncoder2();
            UserDetails user = User.withUsername("user2")
                            .password(encoder.encode("user"))
                            .roles("GENERAL")
                            .build();
            UserDetails admin = User.withUsername("admin2")
                            .password(encoder.encode("admin"))
                            .roles("ADMIN")
                            .build();
            return new InMemoryUserDetailsManager(user, admin);
    }



    private InMemoryUserDetailsManager userDetailsService2;*/


}
