package cakelab.backend.controller;

import org.springframework.web.bind.annotation.*;

import cakelab.backend.model.Role;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/role")
public class RoleController {

    @GetMapping()
    public List<Role> getRoles() {
        return Arrays.asList(Role.values());
    }
}
