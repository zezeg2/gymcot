package com.example.gymcot.error;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionResult {
    private String code;
    private String message;
}
