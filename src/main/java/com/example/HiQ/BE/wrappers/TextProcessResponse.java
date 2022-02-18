package com.example.HiQ.BE.wrappers;
import lombok.*;

/**
 * @author Georg Grankvist
 * Entity class used to build ResponseEntity objects
 */

@Data
@AllArgsConstructor
public class TextProcessResponse {
    private String transformedText;
}
