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

/**
 * @author Georg Grankvist
 * This class is responsible for turning the files received from the Controller
 * into String objects and transforming the content.
 */

public class TextProcessingService {

    /**
     * Recieves file from controller, builds and returns a ResponseEntity built
     * from a String instantiated from ProcessText method return
     * @param textFile
     * @return
     * @throws IOException
     */

    public ResponseEntity<TextProcessResponse> returnText(MultipartFile textFile) throws IOException {
        String processedText = processText(textFile);

        TextProcessResponse processResponse = new TextProcessResponse(processedText);

        return new ResponseEntity<>(processResponse, HttpStatus.OK);
    }

    /**
     * This method creates a String object out of the textfile bytes,
     * parses it line by line using the Scanner class, and puts each
     * unique word and its number of occurrences into a <Key, Value>
     * keyset of a TreeMap. It then passes the TreeMap and text to the
     * transformText method
     * @param textFile
     * @returns the String return of the transformText method.
     * @throws IOException
     */

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

    /**
     * Finds the most frequently used word(s) using Collections.max on the TreeMap,
     * then uses Regex and replaceAll() to append foo and bar to that word.
     * @param wordOccurrenceMap
     * @param extractedText
     * @return the transformed text
     */

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
