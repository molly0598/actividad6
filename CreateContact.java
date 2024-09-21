import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CreateContact {

    private String filePath;

    public CreateContact(String filePath) {
        this.filePath = filePath;
    }

    public void addContact(String name, String phone, String email) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(name + "!" + phone + "!" + email);
            writer.newLine();
        }
    }
}