package com.example.author.service.controller;

import com.example.author.service.entity.Author;
import com.example.author.service.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @PostMapping
    public ResponseEntity<Author> addAuthor(@RequestBody Author author){
        return ResponseEntity.ok(authorService.addAuthorToDb(author));
    }

    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors(){
        return ResponseEntity.ok(authorService.getAllAuthors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable(value = "id") Long id){
        return ResponseEntity.ok(authorService.getAuthorById(id));

    }
}
