package com.demisnack.Eduplay.Application.global.jwt.filter;

import com.demisnack.Eduplay.Application.global.jwt.customuserdetail.CustomUserDetailsService;
import com.demisnack.Eduplay.Application.global.jwt.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//Checker Token
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        //1. Pengecekan endpoint Jika tidak terdapat header "Bearer", lempar ke securityFilterChain
        //Jika terdapat header "Bearer", lanjut ke next steps
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extract Tokennya
        jwt = authHeader.substring(7);

        try {
            // Proses ekstraksi token dibungkus try-catch agar tidak 500 jika token invalid/ngasal
            userEmail = jwtService.extractUsername(jwt);

            // 3. Kalau ada email di token dan user belum tersimpan di context
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                //Cek Keaslian Token
                if (jwtService.isTokenValid(jwt, userDetails)) {

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
        }

        //Jika Proses 3 gagal atau masuk catch, lempar ke securityFilterChain
        filterChain.doFilter(request, response);
    }
}