import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.rmi.Naming;

import javax.swing.*;

public class ServiceClient extends JFrame {
    public ServiceClient() {
        setTitle("laba #8 - Oleksii Kachanov - CS24");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 200);

        JPanel main_panel = new JPanel();
        main_panel.setLayout(new BoxLayout(main_panel, BoxLayout.Y_AXIS));
        main_panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel inputs_panel = new JPanel();
        inputs_panel.setLayout(new BoxLayout(inputs_panel, BoxLayout.Y_AXIS));

        JPanel XML_buttons_panel = new JPanel();

        JLabel first_name_label = new JLabel("first name");
        JTextField first_name_field = new JTextField();
        inputs_panel.add(first_name_label);
        inputs_panel.add(first_name_field);

        JLabel last_name_label = new JLabel("last name");
        JTextField last_name_field = new JTextField();
        inputs_panel.add(last_name_label);
        inputs_panel.add(last_name_field);

        JButton add_user_button = new JButton("add user");
        add_user_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (first_name_field.getText().equals("") || last_name_field.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "first name OR last name is empty...");
                    return;
                }

                try {
                    RemoteInterface service = (RemoteInterface) Naming.lookup("rmi://localhost:7080/remote");
                    Response response = service.add_user( new ClientData(first_name_field.getText(), last_name_field.getText()) );

                    first_name_field.setText("");
                    last_name_field.setText("");

                    if (response.ok) { JOptionPane.showMessageDialog(null, "user added successfully..."); }
                    else { JOptionPane.showMessageDialog(null, response.message); }
                }
                catch (Exception err) { JOptionPane.showMessageDialog(null, "error: " + err.getMessage()); }
            }
        });
        XML_buttons_panel.add(add_user_button);

        JButton export_button = new JButton("export XML");
        export_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JFileChooser file_chooser = new JFileChooser();
                    file_chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                    int file_choosing_result = file_chooser.showOpenDialog(null);
                    if (file_choosing_result == JFileChooser.APPROVE_OPTION) {
                        File selected_folder = file_chooser.getSelectedFile();

                        RemoteInterface service = (RemoteInterface) Naming.lookup("rmi://localhost:7080/remote");
                        Response response = service.export_users_XML(selected_folder.getAbsolutePath());

                        if (response.ok) { JOptionPane.showMessageDialog(null, "XML exported successfully..."); }
                        else { JOptionPane.showMessageDialog(null, response.message); }
                    }
                }
                catch (Exception err) { JOptionPane.showMessageDialog(null, "error: " + err.getMessage()); }
            }
        });
        XML_buttons_panel.add(export_button);

        JButton import_button = new JButton("import XML");
        import_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JFileChooser file_chooser = new JFileChooser();
                    file_chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

                    int file_choosing_result = file_chooser.showOpenDialog(null);
                    if (file_choosing_result == JFileChooser.APPROVE_OPTION) {
                        File selected_file = file_chooser.getSelectedFile();

                        RemoteInterface service = (RemoteInterface) Naming.lookup("rmi://localhost:7080/remote");
                        Response response = service.import_users_XML(selected_file.getAbsolutePath());

                        if (response.ok) { JOptionPane.showMessageDialog(null, "XML imported successfully..."); }
                        else { JOptionPane.showMessageDialog(null, response.message); }

                        ServiceClient.this.requestFocus();
                    }
                }
                catch (Exception err) { JOptionPane.showMessageDialog(null, "error: " + err.getMessage()); }
            }
        });
        XML_buttons_panel.add(import_button);

        main_panel.add(inputs_panel);
        main_panel.add(XML_buttons_panel);

        add(main_panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        try {
            EventQueue.invokeLater(() -> {
                try { ServiceClient frame = new ServiceClient(); }
                catch (Exception e) { e.printStackTrace(); }
            });

        } catch (Exception e) { e.printStackTrace(); }
    }
}