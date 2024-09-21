import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UpdateContact {

    private String filePath;

    public UpdateContact(String filePath) {
        this.filePath = filePath;
    }

    public void updateContact(String oldName, String newName, String newPhone, String newEmail) throws IOException {
        List<String> contacts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("!");
                if (parts[0].equals(oldName)) {
                    contacts.add(newName + "!" + newPhone + "!" + newEmail);
                } else {
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