package com.chronoledger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Ledger {
    private List<Block> chain;

    public Ledger() {
        // Create Genesis Blocchain = new ArrayList<>();k
        chain = new ArrayList<>();
        chain.add(new Block("Genesis Block", "0"));
    }

    // ✅ Add activity to the chain (used in ChronoLedgerApp)
    public void addActivity(String activity) {
        Block lastBlock = chain.get(chain.size() - 1);
        String prevHash = lastBlock.getHash();
        Block newBlock = new Block(activity, prevHash);
        chain.add(newBlock);
    }

    // ✅ Access full chain
    public List<Block> getChain() {
        return chain;
    }

    // ✅ Return ActivityLog objects for GUI Table
    public List<ActivityLog> getActivityLogs() {
        List<ActivityLog> logs = new ArrayList<>();
        for (Block block : chain) {
            logs.add(new ActivityLog(
                block.getActivity(),
                block.getTimestamp(),
                block.getHash(),
                block.getPrevHash()
            ));
        }
        return logs;
    }

    // ✅ Optional: Print to console
    public void printLedger() {
        for (Block block : chain) {
            System.out.println("Activity: " + block.getActivity());
            System.out.println("Time: " + block.getTimestamp());
            System.out.println("Hash: " + block.getHash());
            System.out.println("PrevHash: " + block.getPrevHash());
            System.out.println("--------------------------");
        }
    }

    // ✅ Ledger tampering validation
    public boolean isLedgerValid() {
        for (int i = 1; i < chain.size(); i++) {
            Block current = chain.get(i);
            Block previous = chain.get(i - 1);

            if (!current.getPrevHash().equals(previous.getHash())) {
                return false; // Broken link
            }

            String recalculatedHash = Utils.applySHA256(
                current.getActivity() + current.getTimestamp() + current.getPrevHash()
            );
            if (!current.getHash().equals(recalculatedHash)) {
                return false; // Data changed
            }
        }
        return true;
    }

    // ✅ Save ledger to file
    public void saveToFile(String filename) {
    try (FileWriter writer = new FileWriter(filename)) {
        for (Block block : chain) {
            writer.write("Activity: " + block.getActivity() + "\n");
            writer.write("Timestamp: " + block.getTimestamp() + "\n");
            writer.write("Hash: " + block.getHash() + "\n");
            writer.write("Previous Hash: " + block.getPrevHash() + "\n");
            writer.write("----------------------------\n");

            System.out.println("Saving Block: " + block.getActivity());
        }
        System.out.println("Ledger saved to file: " + filename);
    } catch (IOException e) {
        System.out.println("Error saving file: " + e.getMessage());
    }
}
// Add inside Ledger.java
public void loadFromFile(String filename) {
    File file = new File(filename);
    if (!file.exists()) return;

    chain.clear(); // clear current
    try (Scanner scanner = new Scanner(file)) {
        while (scanner.hasNextLine()) {
            String activity = scanner.nextLine().replace("Activity: ", "");
            String timestamp = scanner.nextLine().replace("Timestamp: ", "");
            String hash = scanner.nextLine().replace("Hash: ", "");
            String prevHash = scanner.nextLine().replace("Previous Hash: ", "");
            scanner.nextLine(); // skip dashed line

            Block block = new Block(activity, timestamp, hash, prevHash);
            chain.add(block);
        }
    } catch (IOException e) {
        System.out.println("Error loading ledger: " + e.getMessage());
    }
}

}


