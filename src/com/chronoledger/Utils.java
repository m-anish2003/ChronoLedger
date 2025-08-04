package com.chronoledger;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Utils {

    public static String applySHA256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes("UTF-8"));

            StringBuilder hexString = new StringBuilder(); 
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getCurrentTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
    public static void exportLedgerToPDF(String filePath, List<Block> chain) {
    try (FileWriter writer = new FileWriter(filePath)) {
        writer.write("ChronoLedger - Freelance Task Ledger\n\n");
        for (Block block : chain) {
            writer.write("Activity: " + block.getActivity() + "\n");
            writer.write("Timestamp: " + block.getTimestamp() + "\n");
            writer.write("Hash: " + block.getHash() + "\n");
            writer.write("PrevHash: " + block.getPrevHash() + "\n");
            writer.write("------------------------------\n");
        }
        System.out.println("PDF (text) exported: " + filePath);
    } catch (IOException e) {
        System.out.println("Export failed: " + e.getMessage());
    }
}

public static void exportLedgerToCSV(String filePath, List<Block> chain) {
    try (FileWriter writer = new FileWriter(filePath)) {
        writer.write("Activity,Timestamp,Hash,PrevHash\n");
        for (Block block : chain) {
            writer.write("\"" + block.getActivity() + "\","
                    + "\"" + block.getTimestamp() + "\","
                    + "\"" + block.getHash() + "\","
                    + "\"" + block.getPrevHash() + "\"\n");
        }
        System.out.println("CSV exported to: " + filePath);
    } catch (IOException e) {
        System.out.println("Export failed: " + e.getMessage());
    }
}

}
