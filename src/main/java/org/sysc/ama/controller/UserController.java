package org.sysc.ama.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

import javax.validation.*;
import javax.validation.constraints.Pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.sysc.ama.model.User;
import org.sysc.ama.repo.UserRepository;

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

    @PostMapping("/create")
    public User create( User user,  BindingResult result) {
        log.debug("Got user ["+user+"]");
    	userRepo.save(user);
    	log.debug("saved user ["+user+"]");

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
