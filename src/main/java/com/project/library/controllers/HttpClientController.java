package com.project.library.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.library.responses.GenericResponse;
import com.project.library.services.HttpClientService;
import com.project.library.utils.LocalizationUtils;
import com.project.library.utils.MessageKeys;
import com.project.library.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@RestController
@RequestMapping("${api.prefix}/http-client")
@RequiredArgsConstructor
public class HttpClientController {
    private final HttpClientService httpClientService;
    private final RestTemplate restTemplate;
    private final LocalizationUtils localizationUtils;

//    @GetMapping("/")
//    public ResponseEntity<?> getBooksFromGoogleApi() throws JsonProcessingException {
//        return ResponseUtil.success(
//                MessageKeys.GET_BOOK_SUCCESSFULLY,
//                localizationUtils.
//        )
//        return ResponseEntity.ok(GenericResponse.success(httpClientService.getBooksFromGoogleApi()));
//    }

    @PostMapping("/")
    public ResponseEntity<?> synchronizeBooksFromGoogle() throws JsonProcessingException {
        return ResponseUtil.success(
                MessageKeys.SYNC_BOOK_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.SYNC_BOOK_SUCCESSFULLY),
                httpClientService.synchronizeBookFromGoogleApi()
        );
    }

//    @GetMapping("/test")
//    public String getJsonResponse(){
//        HttpHeaders headers = new HttpHeaders();
//        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//        return restTemplate.exchange("https://www.googleapis.com/books/v1/volumes?q=java", HttpMethod.GET, entity, String.class).getBody();
//    }
}
