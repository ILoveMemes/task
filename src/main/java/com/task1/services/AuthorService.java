/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.task1.services;

import com.task1.models.Author;
import java.util.ArrayList;

/**
 *
 * @author ILoveMemes
 */
public class AuthorService {
    
    public Author create() {
        return new Author();
    }
    
    public void update(Author author) {
        // 
    }
    
    public void delete(Author author) {
        //
    }
    
    public Author getById(int id) {
        return null;
    }
    
    public ArrayList<Author> getByFirstName(String firstName) {
        return null;
    }
    
    public ArrayList<Author> getByLastName(String lastName) {
        return null;
    }
    
    public ArrayList<Author> getAllAuthors() {
        return null;
    }
    
}
