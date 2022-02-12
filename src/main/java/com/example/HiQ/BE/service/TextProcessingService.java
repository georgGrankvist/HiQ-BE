package com.example.HiQ.BE.service;
import com.example.HiQ.BE.wrappers.TextProcessResponse;
import com.sun.source.tree.Tree;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor

public class TextProcessingService {

    public ResponseEntity<TextProcessResponse> returnText(MultipartFile textFile) throws IOException {
        String processedText = processText(textFile);
        TextProcessResponse processResponse = new TextProcessResponse(processedText);

        return new ResponseEntity<>(processResponse, HttpStatus.OK);
    }

    private String processText(MultipartFile textFile) throws IOException {

        String extractedText = new String(textFile.getBytes());

       Map <String, Integer> wordOccurrenceMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        Scanner scanner = new Scanner(extractedText);

        while (scanner.hasNext()) {

            String line = scanner.nextLine();
            String [] words = line.split(" ");


            for (String word : words) {

                if (!wordOccurrenceMap.containsKey(word) && word.matches("[a-zA-Z]+")) {
                    wordOccurrenceMap.put(word,1);
                }

                else if (wordOccurrenceMap.containsKey(word) && word.matches("[a-zA-Z]+")) {
                    wordOccurrenceMap.put(word, wordOccurrenceMap.get(word)+1);
                }
            }
        }

        return transformText(wordOccurrenceMap, extractedText);

    }

    private String transformText(Map<String, Integer> wordOccurrenceMap, String extractedText) {

        String mostFrequentWord = Collections.max(wordOccurrenceMap.entrySet(), Map.Entry.comparingByValue()).getKey();


        System.out.println(mostFrequentWord + " MFW");
        return extractedText.replaceAll(mostFrequentWord, "foo" + mostFrequentWord + "bar");
    }

}
