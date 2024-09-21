import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadContacts {

    private String filePath;

    public ReadContacts(String filePath) {
        this.filePath = filePath;
    }

    public List<String[]> getContacts() throws IOException {
        List<String[]> contacts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] contact = line.split("!");
                contacts.add(contact);
            }
        }
        return contacts;
    }
}