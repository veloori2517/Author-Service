package com.example.author.service.service;

import com.example.author.service.entity.Author;
import com.example.author.service.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {


    @Autowired
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author addAuthorToDb(Author author){
        return authorRepository.save(author);
    }

    public List<Author> getAllAuthors(){
        return authorRepository.findAll();
    }

    public Author getAuthorById(Long id){
        return authorRepository.findById(id).orElseThrow(() -> new RuntimeException("Author not found"));
    }
}
