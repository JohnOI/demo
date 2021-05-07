package com.example.demo;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class FileProcessor implements Processor {

    private StatementMetaRepo repo;

    private StatementRepo statementRepo;

    @Autowired
    public void setRepo(StatementMetaRepo repo){
        this.repo = repo;
    }

    @Autowired
    public void setStatementRepo(StatementRepo statementRepo) {this.statementRepo = statementRepo;}

    public void process(Exchange exchange) throws Exception {
        String originalFileName = (String) exchange.getIn().getHeader(
                Exchange.FILE_NAME, String.class);
        File file = exchange.getIn().getBody(File.class);

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        int lines = 0;
        while (reader.readLine() != null) lines++;

        reader.close();

        // saving file metadata
        StatementMeta meta = new StatementMeta();
        meta.setCount(lines);
        meta.setFilename(originalFileName);
        meta.setProcessingDate(LocalDate.now());

        repo.save(meta);

        // reopen file for parsing
        reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

        // parse  message file header
        String fileHeaderLine = reader.readLine();
        String headerPart = fileHeaderLine.substring(0,4);  // 4 chars
        String sortPart = fileHeaderLine.substring(4,87);   // 83 chars
        String dataPart = fileHeaderLine.substring(87,1024);

        // parse headerPart
        String recordId = headerPart.substring(2,3);
        String origin = headerPart.substring(3,4);

        // parse sortPart
        String recordId2 = sortPart.substring(82,83);

        // parse dataPart
        String application = dataPart.substring(0,10);
        String platform = dataPart.substring(10,20);
        String version = dataPart.substring(20,22);
        String release = dataPart.substring(22,24);
        String operationMode = dataPart.substring(24,25);
        String creationDate = dataPart.substring(25,33);
        String creationTime = dataPart.substring(33,41);
        String uniqueIdentifier = dataPart.substring(41,65);
        String loadOptions = dataPart.substring(65,215);

        // *** parse statement opening balance
        String openingBalanceLine =  reader.readLine();
        String balanceHeader = openingBalanceLine.substring(0,4);
        String balanceSort = openingBalanceLine.substring(4,87);
        String balanceData = openingBalanceLine.substring(87,1024);

        // parse headerPart
        String openingRecordId = balanceHeader.substring(2,3);
        String openingOrigin = balanceHeader.substring(3,4);

        // parse sortPart
        String destinationBankId = balanceSort.substring(0,11);
        String senderBankId = balanceSort.substring(11,22);
        String accountId = balanceSort.substring(22,57);
        String currencyId = balanceSort.substring(57,60);
        String statementDate = balanceSort.substring(60,68);
        String statementNo = balanceSort.substring(68,73);
        String statementPageNo = balanceSort.substring(73,78);
        String statementCounter = balanceSort.substring(78,82);
        String recordIdInternal = balanceSort.substring(82,83);

        // parse dataPart
        String openingBalanceDate = balanceData.substring(0,8);
        String openingBalanceAmount = balanceData.substring(8,26);
        String openingBalanceSign = balanceData.substring(26,27);
        String basicHeaderReference = balanceData.substring(27,49);
        String bankAddress = balanceData.substring(49,61);
        String sessionNumber = balanceData.substring(61,65);
        String sequenceNumber = balanceData.substring(65,71); // ***
        String applicationHeaderReference = balanceData.substring(71,103);
        String IOIdentifier = balanceData.substring(103,104);
        String messageType = balanceData.substring(104,107);
        String sendingDateMIR = balanceData.substring(107,113);
        String bankAddressMIR = balanceData.substring(113,125);
        String sessionNumberMIR = balanceData.substring(125,129);
        String sequenceNumberMIR = balanceData.substring(129,135); //*****
        String messageUserReference = balanceData.substring(135,151);
        String transactionReference = balanceData.substring(151,167);
        String relatedReference = balanceData.substring(167,183);
        String metalIdentification = balanceData.substring(183,226);
        String comment = balanceData.substring(226,376);
        String statementChecksum = balanceData.substring(376, 394);
        String sendingTimestamp = balanceData.substring(394,414);
        String receivingTimestamp = balanceData.substring(414,434);
        String originalMessageType = balanceData.substring(434,437);

        // set available values in a statement object
        Statement statement = new Statement();
        statement.setFileName( originalFileName );
        // opening balance
        // @todo cast openingBalanceAmount into a BigDecimal

        // *** parse statement lines
        String statementLine = null;
        statementLine = reader.readLine();
        List<StatementLine> statementLines = new ArrayList<>();
        do {
            String statementHeader = statementLine.substring(0,4);
            String statementSort = statementLine.substring(4,87);
            String statementData = statementLine.substring(87,1024);

            // parse header
            String lineRecordId = statementHeader.substring(2,3);
            String lineOrigin = statementHeader.substring(3,4);

            // parse sort
            String lineDestinationBankId = statementSort.substring(0,11);
            String lineSenderBankId = statementSort.substring(11,22);
            String lineAccountId = statementSort.substring(22,57);
            String lineCurrencyId = statementSort.substring(57,60);
            String lineStatementDate = statementSort.substring(60,68);
            String lineStatementNo = statementSort.substring(68,73);
            String lineStatementPageNo = statementSort.substring(73,78);
            String lineStatementCounter = statementSort.substring(78,82);
            String lineRecordIdInternal = statementSort.substring(82,83);

            // parse data
            String lineAmount = statementData.substring(0,18);
            String lineSign  = statementData.substring(18,20);
            String lineValueDate = statementData.substring(20,28);
            String lineBookingDate = statementData.substring(28,36);
            String lineOurReference1 = statementData.substring(26,42);
            String lineThrReference1 = statementData.substring(42,58);
            String lineOurReference2 = statementData.substring(58,74);
            String lineThrReference2 = statementData.substring(74,90);
            String lineTransactionType = statementData.substring(90,94);
            String lineUserTransactionType = statementData.substring(94,98);
            String lineFundsCode = statementData.substring(98,99);
            String lineDepartmentId = statementData.substring(99,104);
            String lineBookingText1 = statementData.substring(104,145);
            String lineBookingText2 = statementData.substring(145,545);
            String lineComment = statementData.substring(545,795);
            String lineTransactionCode1 = statementData.substring(795,803);
            String lineTransactionCode2 = statementData.substring(803,811);
            String lineTransactionCode3 = statementData.substring(811,819);
            String lineCategoryCode1 = statementData.substring(819,820);
            String lineCategoryCode2 = statementData.substring(820,821);
            String lineCategoryCode3 = statementData.substring(821,822);
            String lineCategoryCode4 = statementData.substring(822,823);
            String lineCategoryCode5 = statementData.substring(823,824);
            String lineCategoryCode6 = statementData.substring(824,825);
            String lineCategoryCode7 = statementData.substring(825,826);
            String lineCategoryCode8 = statementData.substring(826,827);
            String lineCategoryCode9 = statementData.substring(827,828);
            String lineCategoryCode10 = statementData.substring(828,829);
            String lineCategoryCode11 = statementData.substring(829,830);
            String lineCategoryCode12 = statementData.substring(830,831);
            String lineCategoryCode13 = statementData.substring(831,832);
            String lineCategoryCode14 = statementData.substring(832,833);
            String lineCategoryCode15 = statementData.substring(833,834);
            String lineCategoryCode16 = statementData.substring(834,835);
            String lineLiquidityTimestampDate = statementData.substring(835,843);
            String lineLiquidityTimestampTime = statementData.substring(843,847);
            String lineLiquidityTimezoneSign = statementData.substring(847,848);
            String lineLiquidityTimezoneValue = statementData.substring(848,852);

            statementLine = reader.readLine();

            // create StatementLine object and add fields
            StatementLine statementLine1 = new StatementLine();
            // statementLine1.

            statementLines.add(statementLine1);
        }
        while (Utils.isStatementLine( statementLine ) /* line is a statement line */);

        // set list of statementlines in statement
        statement.setStatementLines(statementLines);


        // parse closing balance
        // String closingBalanceLine = reader.readLine();
        String closingBalanceLine = statementLine; // last line read that's not a statement line
        String closingBalanceHeader = closingBalanceLine.substring(0,4);
        String closingBalanceSort = closingBalanceLine.substring(4,87);
        String closingBalanceData = closingBalanceLine.substring(87,1024);

        // parse header part
        String closingRecordId = closingBalanceData.substring(2,3);
        String closingOrigin = closingBalanceData.substring(3,4);

        // parse sort part
        String closingDestinationBankId = closingBalanceSort.substring(0,11);
        String closingSenderBankId = closingBalanceSort.substring(11,22);
        String closingAccountId = closingBalanceSort.substring(22,57);
        String closingCurrencyId = closingBalanceSort.substring(57,60);
        String closingStatementDate = closingBalanceSort.substring(60,68);
        String closingStatementNo = closingBalanceSort.substring(68,73);
        String closingStatementPageNo = closingBalanceSort.substring(73,78);
        String closingStatementCounter = closingBalanceSort.substring(78,82);
        String closingRecordIdInternal = closingBalanceSort.substring(82,83);

        // parse data
        String closingBalanceDate = closingBalanceData.substring(0,8);
        String closingBalanceAmount = closingBalanceData.substring(8,26);
        String closingBalanceSign = closingBalanceData.substring(26,27);
        String closingAvailableBalanceDate = closingBalanceData.substring(27,35);
        String closingAvailableBalanceSign = closingBalanceData.substring(35,36);
        String forwardAvailableBalanceArray = closingBalanceData.substring(36,171);
        String forwardAvailableBalanceDate = closingBalanceData.substring(171,179);
        String forwardAvailableBalanceAmount  = closingBalanceData.substring(179,197);
        String forwardAvailableBalanceSign = closingBalanceData.substring(197,198);
        String AccountOwnerInformation = closingBalanceData.substring(198,588);
        String messageTrailer = closingBalanceData.substring(588,840);

        reader.close();

        // save the statement with statement lines
        // statementRepo.save(statement);

        // adding date to filename
//        Date date = new Date();
//        SimpleDateFormat dateFormat = new SimpleDateFormat(
//                "yyyy-MM-dd HH-mm-ss");
//        String changedFileName = dateFormat.format(date) + originalFileName;
//        exchange.getIn().setHeader(Exchange.FILE_NAME, changedFileName);
    }
}