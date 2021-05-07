package com.example.demo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document
@Data
public class StatementMeta {
    @Id
    private String filename;
    private LocalDate processingDate;
    private int count;

}
