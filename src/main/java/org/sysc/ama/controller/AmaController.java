package org.sysc.ama.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.sysc.ama.controller.exception.BadRequestError;
import org.sysc.ama.controller.exception.EntityNotFoundException;
import org.sysc.ama.controller.exception.UnauthorizedAccessException;
import org.sysc.ama.model.Ama;
import org.sysc.ama.model.Question;
import org.sysc.ama.model.User;
import org.sysc.ama.repo.AmaRepository;
import org.sysc.ama.repo.QuestionRepository;
import org.sysc.ama.repo.UserRepository;
import org.sysc.ama.services.CustomUserDetails;


@RestController
@RequestMapping("/ama")
public class AmaController {

    @Autowired
    private AmaRepository amaRepo;

    @Autowired
    private QuestionRepository questionRepo;

    @Autowired
    private UserRepository userRepo;

    @PostMapping(value = "")
    public Ama create (
            @RequestParam("title") String title,
            @RequestParam(value="userId", defaultValue = "") Long userId,
            @RequestParam("public") Boolean isPublic,
            @AuthenticationPrincipal CustomUserDetails user
        ) {
        return create(title, userId, isPublic, Optional.empty() ,user);
    }

    @PostMapping(value = "", headers="Content-Type=application/json")
    public Ama create (
            @RequestParam("title") String title,
            @RequestParam(value="userId", defaultValue = "") Long userId,
            @RequestParam("public") Boolean isPublic,
            @RequestBody Optional<String[]> allowedUsers,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        Ama ama = new Ama(title, user.getUser(), isPublic);

        if (!ama.isPublic()){
            Set<User> allowed = new HashSet<>();
            for (String name : allowedUsers.orElseThrow(()->new BadRequestError("List of allowed users must be specified for private AMAs"))){
                allowed.add(userRepo.findByName(name).orElseThrow(()->new BadRequestError("User with name " + name + " does not exist")));
            }
            User currentUser = userRepo.findById(user.getUser().getId()).orElseThrow(()-> new EntityNotFoundException("user"));
            allowed.add(currentUser);
            ama.setAllowedUsers(allowed);
        }

        amaRepo.findByTitle(title).ifPresent(x->{
            if (x.getSubject().getId() == user.getId())
                throw new BadRequestError("The user has already created an AMA with that title");
        });

        amaRepo.save(ama);

        return ama;
    }




    @GetMapping("/list")
    public Map<String,Object> list (
            @RequestParam(value = "start", defaultValue="0") Integer start,
            @RequestParam(value= "count", defaultValue="10") Integer count,
            @RequestParam(value = "sort", defaultValue = "updated", required = false) String column,
            @RequestParam(value = "asc", defaultValue = "false", required = false) Boolean asc,
            @AuthenticationPrincipal CustomUserDetails principal
        ) {
    	Map<String,Object> vals= new HashMap<String,Object>();
        List<Ama> results;
        Sort sort = new Sort(asc ? Sort.Direction.ASC : Sort.Direction.DESC, column);
        PageRequest request = new PageRequest(start, count, sort);

        results = amaRepo.findByAllowedUsersOrIsPublic(principal.getUser(), true, request);
        vals.put("data", results);
        vals.put("total_count", amaRepo.countByAllowedUsersOrIsPublic(principal.getUser(), true));
        vals.put("pos", start);
        return vals;

    }

    @DeleteMapping("/{id}")
    public Ama delete (@PathVariable("id") Long amaId,
                       @AuthenticationPrincipal CustomUserDetails principal) {
        Ama ama = amaRepo.findById(amaId).orElseThrow(() -> new EntityNotFoundException("ama"));

        if (ama.getSubject().getId() != principal.getId()){
            throw new UnauthorizedAccessException("Only the user who created an AMA may delete it");
        }

        for (Question q : questionRepo.findByAma(ama)){
            questionRepo.delete(q);
        }
        amaRepo.delete(ama);

        return ama;
    }

    @GetMapping("/{id}")
    public Ama view ( @PathVariable("id") Long id,
                      @AuthenticationPrincipal CustomUserDetails principal ) {
        Ama ama = amaRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("ama"));

        if (!ama.userIsAllowedToView(principal.getUser())){
            throw new UnauthorizedAccessException("User is not allowed to view this private AMA");
        }

        return ama;
    }
}

