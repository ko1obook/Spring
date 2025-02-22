package ru.education.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.education.security.SecurityUserDetailsManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

import static org.springframework.util.StringUtils.hasText;

@Service
public class TokenAuthenticationService {
    private static final String SECRET = "Secret";
    private static final long EXPIRATION_TIME = 864_000_000;  // 10 days
    private static final String TOKEN_PREFIX = "Bearer";
    private static final String HEADER_STRING = "Authorization";

    private static Logger LOG = LoggerFactory.getLogger(TokenAuthenticationService.class);

    private static SecurityUserDetailsManager securityUserDerailsManager;

    @Autowired
    public TokenAuthenticationService(SecurityUserDetailsManager securityUserDerailsManager) {
        this.securityUserDerailsManager = securityUserDerailsManager;
    }

    static void addAuthentication(HttpServletResponse response, String username) {
        response.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + generateToken(username));
    }

    static Authentication getAuthentication(HttpServletRequest request) {
        String token = getToken(request);

        if (!hasText(token)) {
            return null;
        }

        /*проверка наличия сессии по токену*/

        String userName = getUsername(token);

        UserDetails user = securityUserDerailsManager.loadUserByUsername(userName);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user,
                null, user.getAuthorities());
        return authentication;
    }

    private static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    private static String getToken(HttpServletRequest request) {
        if (request.getHeader(HEADER_STRING) != null)
            return request.getHeader(HEADER_STRING).replace(TOKEN_PREFIX + " ", "");
        else
            return null;
    }

    public static String getUsername(String token) {
        try {
            return token != null ? Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject() : null;
        } catch (JwtException e) {
            LOG.info("Ошибка обработки токена: {}", token);
            return null;
        }
    }
}
