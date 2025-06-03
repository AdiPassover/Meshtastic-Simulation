package logic.communication;

import logic.communication.transmitters.Transmitter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Scheduler {

    public static void scheduleFromFile(Transmitter transmitter, String csvFilePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false; // Skip header line
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length != 3) continue; // Skip invalid lines

                long sendTick = Long.parseLong(parts[Constants.CSV_TICK_COL].trim());
                String payload = parts[Constants.CSV_PAYLOAD_COL].trim();
                int receiverId = Integer.parseInt(parts[Constants.CSV_RECV_COL].trim());

                transmitter.scheduleMessage(payload, receiverId, sendTick);
            }
        } catch (FileNotFoundException e) {
            System.err.println("CSV file not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format in CSV file: " + e.getMessage());
        }
    }

}
