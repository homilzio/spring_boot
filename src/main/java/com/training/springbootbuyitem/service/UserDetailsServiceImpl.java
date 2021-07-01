package com.training.springbootbuyitem.service;


import com.training.springbootbuyitem.entity.model.Role;
import com.training.springbootbuyitem.repository.jpa.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserJpaRepository userJpaRepository;

  /*  @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.training.springbootbuyitem.entity.model.User user = userJpaRepository.findByUsername(username);
        if (user != null) {
            return new User(user.getUsername(), user.getPassword(), buildSimpleGrantedAuthorities(user.getRoles()));
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }*/

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        com.training.springbootbuyitem.entity.model.User  user = userJpaRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), buildSimpleGrantedAuthorities(user.getRoles()));
    }



    protected static List<SimpleGrantedAuthority> buildSimpleGrantedAuthorities(final Set<Role> roles) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }
}
