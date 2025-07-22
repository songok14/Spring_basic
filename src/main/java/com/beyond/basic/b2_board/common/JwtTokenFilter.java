package com.beyond.basic.b2_board.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtTokenFilter extends GenericFilterBean {
    @Value("${jwt.secretKeyAt}")
    private String secretKey;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken == null) {
            // token이 없는 경우 다시 filterchanin으로 되돌아가는 로직
            chain.doFilter(request, response);
            return;
        }

        // token이 있는 경우 토큰 검증 후 Authentication 객체 생성
        String token = bearerToken.substring(7);
        // token 검증 및 claims 추출
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();

//        claims.getSubject();
//        claims.get("name");
//        claims.get("role");

        // authentication 객체를 만들 때 권한은 ROLE_ 라는 키워들를 붙여서 만들어 주는 것이 추후 문제 발생 X
        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("ROLE_" + claims.get("role")));
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(claims.getSubject(), "", authorityList);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }
}
