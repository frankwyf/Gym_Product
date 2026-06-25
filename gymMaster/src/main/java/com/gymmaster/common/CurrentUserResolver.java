package com.gymmaster.common;

import com.gymmaster.entity.LoginUser;
import com.gymmaster.utils.JwtUtil;
import com.gymmaster.utils.RedisCache;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

/**
 * Centralises extraction of the authenticated user from either:
 * <ol>
 *   <li>the Spring {@link SecurityContextHolder} (preferred — populated by the JWT filter), or</li>
 *   <li>a direct JWT header parse (fallback for controllers called before the filter runs).</li>
 * </ol>
 *
 * <p>Controllers should use {@link #getUserId(HttpServletRequest)} or
 * {@link #getLoginUser(HttpServletRequest)} instead of duplicating JWT-parse logic.</p>
 */
@Component
@RequiredArgsConstructor
public class CurrentUserResolver {

    private final RedisCache redisCache;

    /**
     * Returns the authenticated user's numeric ID.
     *
     * @param request used as fallback if SecurityContext is empty
     * @throws ResponseStatusException 401 if no valid session is found
     */
    public int getUserId(HttpServletRequest request) {
        return getLoginUser(request).getCustomer().getUid();
    }

    /**
     * Returns the full {@link LoginUser} from the security context (or Redis via token header).
     *
     * @throws ResponseStatusException 401 if no valid session is found
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        // 1. Prefer Spring Security context (populated by JwtAuthenticationTokenFilter)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof LoginUser) {
            return (LoginUser) auth.getPrincipal();
        }

        // 2. Fallback: parse token header directly
        String token = resolveToken(request);
        if (token == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required.");
        }
        try {
            Claims claims = JwtUtil.parseJWT(token);
            String userid = claims.getSubject();
            LoginUser user = redisCache.getCacheObject("login" + userid);
            if (user == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Session expired.");
            }
            return user;
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token.");
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (token != null && !token.isEmpty()) return token;
        String auth = request.getHeader("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) return auth.substring(7);
        return null;
    }
}
