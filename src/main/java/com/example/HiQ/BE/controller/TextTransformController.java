package com.example.HiQ.BE.controller;
import com.example.HiQ.BE.service.TextProcessingService;
import com.example.HiQ.BE.wrappers.TextProcessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/text")
@RequiredArgsConstructor

public class TextTransformController {

    private final TextProcessingService textProcessingService;

    @PostMapping("/process")
    public ResponseEntity <TextProcessResponse> processText (@RequestParam("textFile") MultipartFile file) {
        try {
            return textProcessingService.returnText(file);
        } catch (Exception e ) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
