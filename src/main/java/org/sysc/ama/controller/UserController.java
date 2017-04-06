package org.sysc.ama.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import javax.validation.*;

import org.sysc.ama.controller.exception.UserCreationException;
import org.sysc.ama.model.User;
import org.sysc.ama.repo.UserRepository;
import org.sysc.ama.services.CustomUserDetails;
import org.sysc.ama.controller.exception.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/user")
public class UserController {

	Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserRepository userRepo;

    private Validator validator;

    public UserController()
    {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @GetMapping("/nameList")
    public List<String> getUsernames() {
        List<String> usernames = new ArrayList<String>();
        userRepo.findAll().forEach((x)->usernames.add(x.getName()));
        return usernames;
    }

    @PostMapping("/create")
    // TODO: When the UI no longer attempts to create users without passwords, remove the default empty password value
    public User create( @RequestParam(value="name") String name, @RequestParam(value="password", defaultValue="") String password) {

        userRepo.findByName(name).ifPresent(x->{throw new UserCreationException("A user with that name already exists");});

        User user = new User();
        user.setName(name);
        user.setPassword(password);

        userRepo.save(user);
        return user;
    }

    @GetMapping("/{id}")
    public User get ( @PathVariable(value="id") Long id) {
        User user = userRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("user"));

        return user;
    }

    @DeleteMapping("/{id}")
    public User delete ( @PathVariable(value="id") Long id) {
        User user = userRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("user"));

        userRepo.delete(user.getId());

        return user;
    }
}
