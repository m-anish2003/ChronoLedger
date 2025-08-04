package com.chronoledger;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Ledger ledger = new Ledger();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter activity (or 'exit' to finish): ");
            String activity = scanner.nextLine();
            if (activity.equalsIgnoreCase("exit")) break;

            ledger.addActivity(activity);
        }
            System.out.println("\n--- Activity Ledger ---");
            ledger.printLedger();
            ledger.saveToFile("activity_ledger.txt");

        

        if (ledger.isLedgerValid()) {
            System.out.println("✅ Ledger is valid.");
        } else {
            System.out.println("❌ Ledger has been tampered!");
        }

        scanner.close();
    }
}
