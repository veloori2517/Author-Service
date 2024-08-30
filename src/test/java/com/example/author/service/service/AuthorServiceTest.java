package com.example.author.service.service;

import com.example.author.service.entity.Author;
import com.example.author.service.repository.AuthorRepository;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    private Author author1;
    private Author author2;
    private List<Author> authors;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);

        author1 = new Author();
        author1.setId(1);
        author1.setName("Vamsi");

        author2 = new Author();
        author2.setId(2);
        author2.setName("Krishna");
        authors = new ArrayList<>();
        authors.add(author1);
        authors.add(author2);
    }

    @Test
    public void testGetAllAuthors(){
        when(authorRepository.findAll()).thenReturn(authors);

        List<Author> authorList = authorService.getAllAuthors();

        assertEquals(2,authorList.size());
    }

    @Test
    public void testGetAuthorById(){
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author1));

        Author author = authorService.getAuthorById(1L);

        assertNotNull(author);
        assertEquals(1,author.getId());
        assertEquals("Vamsi",author.getName());
    }

    @Test
    public void testAddAuthor(){
        when(authorRepository.save(author2)).thenReturn(author2);

        Author author = authorService.addAuthorToDb(author2);

        assertEquals("Krishna",author.getName());
        verify(authorRepository,times(1)).save(author2);
    }


}
