import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DeleteContact {

    private String filePath;

    public DeleteContact(String filePath) {
        this.filePath = filePath;
    }

    public void deleteContact(String name) throws IOException {
        List<String> contacts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("!");
                if (!parts[0].equals(name)) {
                    contacts.add(line);
                }
            }
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String contact : contacts) {
                writer.write(contact);
                writer.newLine();
            }
        }
    }
}