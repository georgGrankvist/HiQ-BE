package com.example.HiQ.BE.wrappers;

import lombok.*;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@Getter
@Setter
public class TextProcessResponse {
    private String transformedText;
}
