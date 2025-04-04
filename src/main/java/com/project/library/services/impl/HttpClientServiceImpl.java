package com.project.library.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.library.dtos.GoogleBookItemDTO;
import com.project.library.dtos.GoogleBookItemVolumeInfoDTO;
import com.project.library.dtos.GoogleBooksDTO;
import com.project.library.entities.Book;
import com.project.library.entities.Category;
import com.project.library.repositories.BookRepository;
import com.project.library.repositories.CategoryRepository;
import com.project.library.repositories.UserRepository;
import com.project.library.responses.BookResponse;
import com.project.library.services.HttpClientService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HttpClientServiceImpl implements HttpClientService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Value("${external-resource.google-book-url}")
    private String googleApiURl;

    private static final Logger logger = LoggerFactory.getLogger(HttpClientServiceImpl.class);

    @Override
    public GoogleBooksDTO getBooksFromGoogleApi() throws JsonProcessingException {
        String response = restTemplate.getForObject(googleApiURl, String.class);
        System.out.println("Raw json book response: " + response);
        ObjectMapper objectMapper = new ObjectMapper();
        GoogleBooksDTO booksResponse = objectMapper.readValue(response, GoogleBooksDTO.class);
        return booksResponse;
    }

    @Override
    @Transactional
//    @Scheduled(fixedDelay = 30000)
    @Scheduled(cron = "0 0 0 ? * 1/7")
    public List<BookResponse> synchronizeBookFromGoogleApi() throws JsonProcessingException {
        String jsonResponse = restTemplate.getForObject(googleApiURl, String.class);
        GoogleBooksDTO googleBooksDTO = objectMapper.readValue(jsonResponse, GoogleBooksDTO.class); //read in GoogleBooksDTO for more
        List<GoogleBookItemDTO> bookItemDTOS = googleBooksDTO.getItems();
        List<Book> newSyncedBooks = new ArrayList<>();
        this.authenticateAsAdmin();
        for (GoogleBookItemDTO bookItemDTO : bookItemDTOS) {
            GoogleBookItemVolumeInfoDTO volumeInfo = bookItemDTO.getVolumeInfo();
            if (bookRepository.existsByTitle(volumeInfo.getTitle())) {
                logger.info("Duplicated data -> abort");
                continue;
            }
            List<Category> categories = categoryRepository.findByCategoryNameIn(volumeInfo.getCategories());
            Book newBook = modelMapper.map(volumeInfo, Book.class);
            newBook.setAmount(80); //fake du lieu
            newBook.setCategories(categories);
            bookRepository.save(newBook);
            newSyncedBooks.add(newBook);
            logger.info("Add new sync data into DB!");
        }
        SecurityContextHolder.clearContext();
        return newSyncedBooks.stream().map(BookResponse::fromBook).toList();
    }

    private void authenticateAsAdmin(){
        UserDetails userDetails = userRepository.findByUsername("admin1") //mock data for educational purpose
                .orElseThrow(()-> new UsernameNotFoundException("Not found username"));
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new
                UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        logger.info("Authenticated as admin user");
    }
}
