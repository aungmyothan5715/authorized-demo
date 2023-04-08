package com.example.authorizeddemo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        //In hasAuthority() can write expression string
        String expression = "hasAuthority('read') and !hasAuthority('write')"; // this is mean read only.
        String timeExpression = "T(java.time.LocalTime).now().isBefore(T(java.time.LocalTime).of(12,0))"; // .of(12,0) is 12o'clock and then you can use in -> c.anyRequest().access(new WebExpressionAuthorizationManager(timeExpression))  .
        //String expression = "hasAuthority('read') and hasAuthority('admin'); so yin
        // you can use that .authorities("read", "admin")

        // and then you can use -> new WebExpressionAuthorizationManager(expression)

        //Security lo at tine hmar SecurityFilterChan ko write pay ya tal.
        //UI nae ma use lo httpBasic() method ko use tr
        http.httpBasic()
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/hello").hasRole("USER")
                .requestMatchers("/greeting").hasRole("ADMIN")
                .anyRequest().authenticated();
        return http.build();
        //1
        // c.anyRequest().authenticated() ka .authenticated() hmar .hasAuthority() shi tal
        //.hasAuthority() method has two parts,there are  .hasAnyAuthority(), hasAuthority(), hasRole and so on...

        //2
        //.hasAuthority() method has one parameter, there is "read" or "write" or ...
        // read can access only read only user and write can access write only user.
        // hasAnyAuthority() method can use ... hasAnyAuthority("write", "read");

        //3
        //You can also use access() method instead of hasAuthority().
        //Example -> .access(new WebExpressionAuthorizationManager("hasAuthority('write')")




    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
        //BCryptPasswordEncoder() so yin  User.withUsername().password() hmar d tine ma ya.
        //User.withUsername().password(passwordEncoder().encoder("12345") lo write pay ya.
    }
    @Bean
    public UserDetailsService userDetailsService(){
        var uds = new InMemoryUserDetailsManager();
        var user1 = User.withUsername("john")
                .password(passwordEncoder().encode("12345"))
                .authorities("read", "admin").build();
        // .authorities() method can access more parameter eg -> ("read", "admin")
        //because of .authorities() is as varargs.
        var user2 = User.withUsername("mary")
                .password(passwordEncoder().encode("12345"))
                .authorities("write", "delete").build();
        //you can define user role .authorities("ROLE_USER") or ("ROLE_USER", "ROLE_ADMIN")
        //if you can use c.anyRequest().hasRole("USER")
        //if you don't want to use hasRole("ROLE_USER", "ROLE_ADMIN") method, you can use -> User.withUsername().password().roles("USER", "ADMIN").build(); lo use pay.
        //and then c.anyRequest().hasAnyRole("USER", "ADMIN") OR hasRole("ADMIN") lo use pay.
        uds.createUser(user1);
        uds.createUser(user2);
        return uds;
    }
}
