package com.example.demo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document
@Data
public class Statement {
    @Id
    String fileName;
    BigDecimal openingBalance;
    BigDecimal closingBalance;
    LocalDate processDate;
    List<StatementLine> statementLines = new ArrayList<>();
}
