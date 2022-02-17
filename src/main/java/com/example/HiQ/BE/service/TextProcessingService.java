package com.example.HiQ.BE.service;
import com.example.HiQ.BE.wrappers.TextProcessResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.util.*;

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

        if (!extractedText.isEmpty()) {

            while (scanner.hasNext()) {

                String line = scanner.nextLine();

                String[] words = line.split(" ");
                String pattern = "[a-zA-Z]+";

                for (String word : words) {

                    if (!wordOccurrenceMap.containsKey(word) && word.matches(pattern)) {

                        wordOccurrenceMap.put(word, 1);

                    } else if (wordOccurrenceMap.containsKey(word) && word.matches(pattern)) {

                        wordOccurrenceMap.put(word, wordOccurrenceMap.get(word) + 1);
                    }
                }
            }

            return transformText(wordOccurrenceMap, extractedText);

        }

        else return "The textfile was empty!";

    }

    private String transformText(Map<String, Integer> wordOccurrenceMap, String extractedText) {

        String highestFrequencyWord = Collections.max(wordOccurrenceMap.entrySet(), Map.Entry.comparingByValue()).getKey();

        int wordOccurrence = wordOccurrenceMap.get(highestFrequencyWord);


        // (?i) is regex for case-insensitive matching
        // \\b defines word boundaries so that no other words containing a substring of the highest frequency word will match
        // $0 is a back-reference to the word that matched, which will allow us to keep the original case in the replacement

        extractedText = extractedText.replaceAll(("(?i)"+"\\b") + highestFrequencyWord + "(\\b)","foo" + "$0" + "bar");


        //Check for duplicate max values in keyset

        for (Map.Entry<String, Integer> entry : wordOccurrenceMap.entrySet()) {

            // If other words occur same amount of times in text as max, perform replace on them too

            if (entry.getValue() == wordOccurrence && !entry.getKey().equals(highestFrequencyWord)) {

                extractedText = extractedText.replaceAll(("(?i)" + "\\b") + entry.getKey() + "(\\b)", "foo" + "$0" + "bar");
            }

        }

       return extractedText;
    }

}
