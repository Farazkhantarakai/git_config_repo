package com.tech.gateway.authfilter;

import com.tech.gateway.services.JwtServices.JwtService;
import com.tech.gateway.services.JwtServices.MyUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private  final JwtService jwtService;

    private  final MyUserDetailService userDetailService;

    JwtAuthenticationFilter(JwtService jwtService, MyUserDetailService userDetailService) {
        this.jwtService=jwtService;
        this.userDetailService=userDetailService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
    String authorization=    request.getHeader("Authorization");
    if((authorization == null || !authorization.startsWith("Bearer "))) {
        filterChain.doFilter(request,response);
return ;
    }
    String  jwt=   authorization.substring(7);
    String username=jwtService.extractUserName(jwt);
    //this means that if username is null and is not authenticated then you have to forward the request further
     if(username!=null && SecurityContextHolder.getContext().getAuthentication() == null) {
         UserDetails userDetails= userDetailService.loadUserByUsername(username);
         if(userDetails != null && jwtService.isTokenValid(jwt)){
             UsernamePasswordAuthenticationToken authenticationDetails=
                     new UsernamePasswordAuthenticationToken(username,userDetails.getPassword(),userDetails.getAuthorities());
             authenticationDetails.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
         SecurityContextHolder.getContext().setAuthentication(authenticationDetails);
         }
filterChain.doFilter(request,response);
     }



    }
}
