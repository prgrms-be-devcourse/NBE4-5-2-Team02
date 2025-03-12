package com.snackoverflow.toolgether.global.filter;

import com.snackoverflow.toolgether.domain.user.entity.User;
import com.snackoverflow.toolgether.domain.user.repository.UserRepository;
import com.snackoverflow.toolgether.global.exception.custom.user.UserNotFoundException;
import com.snackoverflow.toolgether.global.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            // 쿠키에서 JWT 토큰 추출
            Optional<String> token = jwtUtil.getJwtFromCookies(request);
            log.info("JWT Token={}", token.orElse("No Token Found"));

            if (token.isPresent()) {
                // 페이로드 조회
                Claims claims = jwtUtil.getPayload(token.get());
                log.info("JWT claims={}", claims);

                // 사용자 정보 추출
                Long userId = ((Integer) claims.get("userId")).longValue();
                User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

                String username = user.getUsername(); // 사용자의 아이디
                String email = user.getEmail();

                log.debug("token -> username: {}", username);
                log.debug("token -> email: {}", email);

                // 인증 객체 생성 및 저장
                CustomUserDetails customUserDetails = new CustomUserDetails(username, email, userId);
                Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails,
                        null, Collections.emptyList());
                log.info("authentication={}", authentication);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (UserNotFoundException | JwtException e) {
            log.error("오류 발생: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized 반환
            response.getWriter().write("Unauthorized: " + e.getMessage());
            filterChain.doFilter(request, response);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();
        return requestURI.startsWith("/h2-console") ||
                requestURI.startsWith("/login/oauth2/code/google") ||
                requestURI.matches(".*\\.(css|js|gif|png|jpg|ico)$");
    }
}