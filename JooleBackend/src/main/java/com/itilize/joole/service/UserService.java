package com.itilize.joole.service;

import com.itilize.joole.dao.UserDao;
import com.itilize.joole.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    public Optional<List<UserEntity>> getAllUser() {
        List<UserEntity> users = userDao.findAll();
        if (!users.isEmpty())
        {
            return Optional.of(users);
        }
        return Optional.empty();
    }

    public Optional<UserEntity> getUserByUsername(String username) {
        UserEntity existUser = userDao.findByUsername(username);
        if(existUser == null) {
            return Optional.empty();
        } else {
            return Optional.of(existUser);
        }
    }

    public Optional<UserEntity> getUserById(int id) {
        return userDao.findById(id);
    }

    public Optional<UserEntity> createUser(UserEntity user) {
        UserEntity existUser = userDao.findByUsername(user.getUsername());
        if(existUser != null) {
            return Optional.empty();
        } else {
            userDao.save(user);
            UserEntity createdUserCredential = userDao.findByUsername(user.getUsername());
//            createdUserCredential.setPassword("");
            return Optional.of(createdUserCredential);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userDao.findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("Username doesn't exist!");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }
}
