package com.example.author.service.controller;
import static org.mockito.Mockito.*;
import com.example.author.service.entity.Author;
import com.example.author.service.service.AuthorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.ArrayList;
import java.util.List;

@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {

    @MockBean
    AuthorService authorService;

    @Autowired
    private MockMvc mockMvc;

    private Author author1;
    private Author author2;
    private List<Author> authors;

    @BeforeEach
    public void setUp(){
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
    public void testGetAllAuthors() throws Exception {
        when(authorService.getAllAuthors()).thenReturn(authors);

        mockMvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Vamsi"))
                .andExpect(jsonPath("$[1].name").value("Krishna"))
                .andDo(print());
    }

    @Test
    public void testGetAuthorById() throws Exception{
        when(authorService.getAuthorById(1L)).thenReturn(author1);

        mockMvc.perform(get("/authors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Vamsi"))
                .andExpect(jsonPath("$.id").value(1))
                .andDo(print());
    }

    @Test
    public void testAddAuthor() throws Exception{
        when(authorService.addAuthorToDb(author1)).thenReturn(author1);

        mockMvc.perform(post("/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(author1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Vamsi"))
                .andDo(print());

    }


}
