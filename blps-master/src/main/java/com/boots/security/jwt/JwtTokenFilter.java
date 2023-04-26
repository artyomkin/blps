package com.boots.security.jwt;

import com.boots.exceptions.ForbiddenException;
import com.boots.exceptions.JwtAuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.web.filter.GenericFilterBean;

    import javax.servlet.FilterChain;
    import javax.servlet.ServletException;
    import javax.servlet.ServletRequest;
    import javax.servlet.ServletResponse;
    import javax.servlet.http.HttpServletRequest;
    import java.io.IOException;

    @Slf4j
    public class JwtTokenFilter extends GenericFilterBean {

        private JwtTokenProvider jwtTokenProvider;

        public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
            this.jwtTokenProvider = jwtTokenProvider;
        }

        @Override
        public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
                throws IOException, ServletException {

            String token = jwtTokenProvider.resolveToken((HttpServletRequest) req);
            try{
                if (token != null && jwtTokenProvider.validateToken(token)) {
                    Authentication auth = jwtTokenProvider.getAuthentication(token);
                    if (auth != null) {
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }
            } catch (JwtAuthenticationException e){
                log.info("Jwt token has expired or invalid.");
            } catch (IllegalArgumentException e){
                throw new ForbiddenException();
            }
            filterChain.doFilter(req, res);
        }

    }