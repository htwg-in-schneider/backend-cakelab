package cakelab.backend.controller;

import org.springframework.web.bind.annotation.*;

import cakelab.backend.model.Category;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @GetMapping()
    public List<Category> getCategories() {
        return Arrays.asList(Category.values());
    }
}