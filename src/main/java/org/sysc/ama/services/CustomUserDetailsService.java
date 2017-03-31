package org.sysc.ama.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.sysc.ama.controller.exception.EntityNotFoundException;
import org.sysc.ama.model.User;
import org.sysc.ama.repo.UserRepository;


@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public CustomUserDetails loadUserByUsername(String name) throws UsernameNotFoundException {

        User user = userRepo.findByName(name).orElseThrow(()->new EntityNotFoundException("user"));

        if (user == null){
            throw new UsernameNotFoundException("User with name " + name);
        }
        return new CustomUserDetails(user);
    }
}
