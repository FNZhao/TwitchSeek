package com.zhaofn.twitch;


import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.sql.DataSource;

//AppConfig这个文件相当于java版本的application.ym，用于处理yml文件做不了的操作
@Configuration//告诉Spring这是个config
public class AppConfig {

    @Bean//让Spring找到//先把authentication关掉
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()
//                .authorizeHttpRequests(auth ->
//                        auth.requestMatchers("/**").permitAll()//让所有路径(/**)都permit，不需要authentication（测试用的）
//                );
            http
                .csrf().disable()
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                .requestMatchers(HttpMethod.GET, "/", "/index.html", "/*.json", "/*.png", "/static/**").permitAll()//这两句是说明前端文件可以随便下载
                                .requestMatchers("/hello/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/login", "/register", "/logout").permitAll()
                                .requestMatchers(HttpMethod.GET, "/recommendation", "/game").permitAll()
                                .anyRequest().authenticated()//路径的matching是从上到下的，如果把这一行写在上面那么login等操作也需要authentication了
                )
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()//一个连接符
                .formLogin()//formLogin是session based authentication,写了这句以后就会有login的endpoint
                .successHandler((req, res, auth) -> res.setStatus(HttpStatus.NO_CONTENT.value()))
                .failureHandler(new SimpleUrlAuthenticationFailureHandler())
                .and()
                .logout()
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT));
            //这一连串的...都是fluent API

        return http.build();
    }

    @Bean//如果sql里定义的users和里面的名字和规范不一样就需要额外写代码说明是哪一个
    UserDetailsManager users(DataSource dataSource) {//存储或者读取用户信息
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    PasswordEncoder passwordEncoder() {//把要存在db里的密码进行加（hash）,防止黑客窃取，反推特别复杂，存进去的就是加密的
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
