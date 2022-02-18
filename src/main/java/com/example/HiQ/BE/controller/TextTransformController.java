package com.example.HiQ.BE.controller;
import com.example.HiQ.BE.service.TextProcessingService;
import com.example.HiQ.BE.wrappers.TextProcessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Objects;


/**
 * @author Georg Grankvist
 * API controller class, establishes endpoint and its behavior
 */
@Controller
@RequestMapping("/api/text")
@RequiredArgsConstructor
public class TextTransformController {

    private final TextProcessingService textProcessingService;


    /**
     * Post endpoint that receives a textfile, forwards it to the TextProcessingService class and returns a transformed
     * version where the most commonly occuring word(s) is surrounded with "foo" and "bar"
     * @param file Textfile to be parsed
     * @return ResponseEntity object where the body is built using a TextProcessResponse object holding the transformed text
     */

    @PostMapping("/process")
    public ResponseEntity <TextProcessResponse> processText (@RequestParam("textFile") MultipartFile file) {

        if (passFileTypeCheck(file)) {
            try {
                return textProcessingService.returnText(file);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        else return new ResponseEntity<>(new TextProcessResponse("You tried to send a non textfile with type: " + file.getContentType()),HttpStatus.OK );
    }

    /**
     * Checks the content type of a file
     * @param file
     * @return true if the content type is a txt, md, rtf or .file, false otherwise.
     */


    public boolean passFileTypeCheck (MultipartFile file) {

        String fileType = file.getContentType();

        return switch (Objects.requireNonNull(fileType)) {
            case "application/rtf", "text/plain", "application/octet-stream" -> true;
            default -> false;
        };

    }

}
