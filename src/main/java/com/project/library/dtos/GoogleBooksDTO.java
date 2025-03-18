package com.project.library.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Muốn sử dụng ObjectMapper tương ứng với JSON data thì ta phải có các Object tương ứng level
 * Ví dụ:
 * {
 *   "kind": "books#volumes",
 *   "totalItems": 1000000,
 *   "items": [ -> class này tương ứng với level này sau đó GoogleBookItemDTO cũng sẽ có lớp con volumeInfo cùng level tương ứng
 *     {
 *       "kind": "books#volume",
 *       ...
 *       "volumeInfo": {
 *         "title": "Ngôn ngữ lập trình Java cơ bản",
 *         "authors": [
 *           "Hoang Tran"
 *         ],
 *         "publisher": "hoangtn",
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@Getter
public class GoogleBooksDTO {
    private List<GoogleBookItemDTO> items;
}

