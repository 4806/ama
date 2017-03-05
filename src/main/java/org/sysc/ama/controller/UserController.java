package org.sysc.ama.controller;

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
        userRepo.save(user);
        return user;
    }

    @GetMapping("/{id}")
    public User view ( @PathVariable(value="id") Long id) {
        User user = userRepo.findById(id);
        if (user == null)
            throw new EntityNotFoundException();

        return user;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<FieldError> processValidationError(ConstraintViolationException ex) {
        List<FieldError> errors = new ArrayList<FieldError>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations())
        {
            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            errors.add(new FieldError("name",propertyPath,

                    "Invalid "+ propertyPath + "(" + message + ")"));
        }
        return errors;
    }
}
