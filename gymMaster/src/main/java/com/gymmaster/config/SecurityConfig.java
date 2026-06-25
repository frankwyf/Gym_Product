package com.gymmaster.config;

import com.gymmaster.filter.JwtAuthenticationTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security configuration.
 *
 * <ul>
 *   <li>Stateless JWT sessions — no HttpSession is created.</li>
 *   <li>CSRF disabled (mitigated by short-lived JWT tokens).</li>
 *   <li>Constructor injection — no field-level @Autowired.</li>
 *   <li>Wildcard /reservation/** is intentionally open because individual
 *       reservation endpoints perform their own JWT-based ownership checks.</li>
 * </ul>
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter,
                          AuthenticationEntryPoint authenticationEntryPoint,
                          AccessDeniedHandler accessDeniedHandler) {
        this.jwtAuthenticationTokenFilter = jwtAuthenticationTokenFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
                // Public auth endpoints
                .antMatchers("/loginCus/login",
                             "/loginCus/requestPasswordReset",
                             "/loginCus/resetPassword").permitAll()
                // Customer self-registration
                .antMatchers("/customer/register").anonymous()
                // Captcha (required before login/reset)
                .antMatchers("/getCaptcha", "/getCaptchaReset").anonymous()
                // Health + monitoring
                .antMatchers("/health/**",
                             "/actuator/health",
                             "/actuator/info").permitAll()
                // API documentation
                .antMatchers("/swagger-ui.html", "/swagger-ui/**",
                             "/v3/api-docs", "/v3/api-docs/**").permitAll()
                // Public read-only content
                .antMatchers("/until/**",
                             "/venue/getById",
                             "/venue/getByName",
                             "/venue/getAvailableVenues",
                             "/venue/getFid",
                             "/venue/getDate").permitAll()
                // Static assets
                .antMatchers("/static/**",
                             "/**/*.jpg", "/**/*.JPG",
                             "/**/*.png", "/**/*.mp4").permitAll()
                // All other requests require authentication
                .anyRequest().authenticated()
            .and()
            .addFilterBefore(jwtAuthenticationTokenFilter,
                             UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
            .and()
            .cors();
    }
}
