package com.zhaofn.twitch.user;

import com.zhaofn.twitch.db.UserRepository;
import com.zhaofn.twitch.db.entity.UserEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(UserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Transactional
    public void register(String username, String password, String firstName, String lastName) {
        UserDetails user = User.builder() //底下的顺序无所谓,也可以加新的选项也可以删除//这个是通过builder()创建的，很方便
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles("USER")//Spring Security强制需要有这个东西
                .build();
        userDetailsManager.createUser(user);//create一个user
        userRepository.updateNameByUsername(username, firstName, lastName);//Spring security只关心security部分，剩下的内容需要update
    }

    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}

