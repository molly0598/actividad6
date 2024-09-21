import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;

public class ContactManagerApp extends JFrame {

    private JTextField nameField;
    private JTextField phoneField;
    private JTextField emailField;
    private JTable contactsTable;
    private DefaultTableModel tableModel;
    private String filePath = "contacts.txt";

    private CreateContact createContact;
    private ReadContacts readContacts;
    private UpdateContact updateContact;
    private DeleteContact deleteContact;

    public ContactManagerApp() {
        setTitle("Gestor de Contactos");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createContact = new CreateContact(filePath);
        readContacts = new ReadContacts(filePath);
        updateContact = new UpdateContact(filePath);
        deleteContact = new DeleteContact(filePath);

        initComponents();
        loadContacts();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Panel de entradas
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Datos del Contacto"));

        nameField = new JTextField(20);
        phoneField = new JTextField(15);
        emailField = new JTextField(20);

        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(phoneField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(inputPanel, gbc);

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addButton = new JButton("Agregar");
        JButton updateButton = new JButton("Actualizar");
        JButton deleteButton = new JButton("Eliminar");
        JButton clearButton = new JButton("Limpiar");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        gbc.gridy = 1;
        add(buttonPanel, gbc);

        // Tabla
        String[] columns = {"Nombre", "Teléfono", "Email"};
        tableModel = new DefaultTableModel(columns, 0);
        contactsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(contactsTable);

        gbc.gridy = 2; gbc.weightx = 1; gbc.weighty = 1; gbc.fill = GridBagConstraints.BOTH;
        add(scrollPane, gbc);

        // Eventos
        addButton.addActionListener(e -> addContact());
        updateButton.addActionListener(e -> updateContact());
        deleteButton.addActionListener(e -> deleteContact());
        clearButton.addActionListener(e -> clearFields());

        contactsTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = contactsTable.getSelectedRow();
                if (selectedRow >= 0) {
                    nameField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    phoneField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    emailField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                }
            }
        });
    }

    private void loadContacts() {
        try {
            List<String[]> contacts = readContacts.getContacts();
            for (String[] contact : contacts) {
                tableModel.addRow(contact);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los contactos: " + e.getMessage());
        }
    }

    private void addContact() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre y teléfono son obligatorios.");
            return;
        }

        try {
            createContact.addContact(name, phone, email);
            tableModel.addRow(new Object[]{name, phone, email});
            clearFields();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al agregar el contacto: " + e.getMessage());
        }
    }

    private void updateContact() {
        int selectedRow = contactsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un contacto para actualizar.");
            return;
        }

        String oldName = tableModel.getValueAt(selectedRow, 0).toString();
        String newName = nameField.getText().trim();
        String newPhone = phoneField.getText().trim();
        String newEmail = emailField.getText().trim();

        try {
            updateContact.updateContact(oldName, newName, newPhone, newEmail);
            tableModel.setValueAt(newName, selectedRow, 0);
            tableModel.setValueAt(newPhone, selectedRow, 1);
            tableModel.setValueAt(newEmail, selectedRow, 2);
            clearFields();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar el contacto: " + e.getMessage());
        }
    }

    private void deleteContact() {
        int selectedRow = contactsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un contacto para eliminar.");
            return;
        }

        String name = tableModel.getValueAt(selectedRow, 0).toString();

        int confirm = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de que desea eliminar este contacto?", 
            "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                deleteContact.deleteContact(name);
                tableModel.removeRow(selectedRow);
                clearFields();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar el contacto: " + e.getMessage());
            }
        }
    }

    private void clearFields() {
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        contactsTable.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ContactManagerApp().setVisible(true));
    }
}