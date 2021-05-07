package com.example.demo;

public class Utils {

    static boolean isStatementLine(String line) {
        // check if line string is a statement line
//        String rSign = line.substring(106,108);
//        if ( (rSign.equalsIgnoreCase("C")) ||
//                (rSign.equalsIgnoreCase("D")) ||
//                    (rSign.equalsIgnoreCase("RC")) ||
//                        (rSign.equalsIgnoreCase("RD")) ){
//            return true;
//        }
        String lineRecordId = line.substring(2,3);
        if (lineRecordId.equalsIgnoreCase("2")) {
            return true;
        }
        return false;
    }

}
