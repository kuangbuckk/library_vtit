package com.project.library.services.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.library.dtos.GoogleBooksDTO;
import com.project.library.responses.BookResponse;

import java.util.List;

public interface IHttpClientService {
    GoogleBooksDTO getBooksFromGoogleApi() throws JsonProcessingException;
    List<BookResponse> synchronizeBookFromGoogleApi() throws JsonProcessingException;
}
