package com.tech.gateway.config;


import com.tech.gateway.AuthController.CustomLogoutHandler;
import com.tech.gateway.authfilter.JwtAuthenticationFilter;
import com.tech.gateway.services.JwtServices.MyUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {
    private final MyUserDetailService myUserDetailService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomLogoutHandler logoutHandler;
//    private final  OAuth2LoginSucessHandler oAuth2LoginSuccessHandler;
//    @Value("${frontend.url}")
//    private String frontendUrl;


    public SecurityConfig(MyUserDetailService myUserDetailService , JwtAuthenticationFilter jwtAuthenticationFilter, CustomLogoutHandler customLogoutHandler) {
        this.myUserDetailService = myUserDetailService;
        this.jwtAuthenticationFilter=jwtAuthenticationFilter;
this.logoutHandler=customLogoutHandler;
//this.oAuth2LoginSuccessHandler=oAuth2LoginSuccessHandler;
    }
    @Bean
public SecurityFilterChain configure(HttpSecurity http) throws Exception {
    http.csrf((AbstractHttpConfigurer::disable))
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests((athrz) ->  {
        athrz.requestMatchers("/v2/api-docs" ,
                        "/v3/api-docs" ,
                        "/swagger-resources/**" ,
                        "/swagger-ui.html" ,
                        "/swagger-ui/**" ,
                        "/webjars/**" ,
                        "/v3/api-docs/swagger-config")
                .permitAll();
        athrz.requestMatchers("/registerUser","/loginUser","/swagger-ui.html" ,"http://localhost:5173","/logout","/resetPassword","/refresh").permitAll();
        athrz.requestMatchers("/saveProfileDetails","/getHome","/property-listing/").hasAnyRole("USER").anyRequest().authenticated();
    } ).logout(l->l
                .logoutUrl("/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()));
    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
////    oauth login
//        http.oauth2Login(oauth -> {
//            oauth.loginPage("/loginGoogle").permitAll()
//                    //this is the class for the oauth2loginsucceshandling
//                     .successHandler(oAuth2LoginSuccessHandler);
//        });
return http.build();
}


@Bean
public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
   provider.setUserDetailsService(myUserDetailService);
    provider.setPasswordEncoder(passwordEncoder());

return provider;
}



@Bean
public AuthenticationManager authenticationManager(){
    return new ProviderManager(authenticationProvider());
}

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(List.of(frontendUrl));
        configuration.setAllowedMethods(Arrays.asList("GET","POST"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

//this is for encypting and decrypting the password
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}



@Bean
public UserDetailsService userDetailsService(){
    return myUserDetailService;
}


}
