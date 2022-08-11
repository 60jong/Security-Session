package com.jongky.sessionprac.service;

import com.jongky.sessionprac.model.PrincipalDetails;
import com.jongky.sessionprac.model.User;
import com.jongky.sessionprac.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PrincipalUserDetailsService implements UserDetailsService {
    private final UserDao userDao;

    @Autowired
    public PrincipalUserDetailsService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userDao.findByEmail(email);
        if (user != null) {
            return new PrincipalDetails(user);
        }
        return null;
    }
}
