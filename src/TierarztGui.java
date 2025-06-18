import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class TierarztGui extends JFrame {

    // Textfelder
    JTextField txt_bid, txt_bname, txt_bnachname, txt_btelefon;
    JTextField txt_hid, txt_hname, txt_halter, txt_htierart, txt_hanzahl, txt_hgewicht, txt_hgroesse;
    JTextField txt_tid, txt_timpfung, txt_tdatum, txt_trasse, txt_tname, txt_thid;

    // Buttons
    JButton btn_speichern, btn_reset, btn_beenden;

    public TierarztGui() {
        setTitle("Tierarzt Verwaltungsformular");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 1000));
        initComponents();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(245, 245, 245));
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        add(scrollPane, BorderLayout.CENTER);

        GridBagConstraints c = new GridBagConstraints();
        Font fontLabel = new Font("Sans Serif", Font.BOLD, 14);
        Font fontText = new Font("Sans Serif", Font.PLAIN, 16);
        int row = 0;

        // Sektionen hinzufügen
        row = addSection(mainPanel, "Besitzer", row, c, fontLabel, fontText,
                new String[]{"BID", "Name", "Nachname", "Telefon"},
                new JTextField[]{txt_bid = new JTextField(), txt_bname = new JTextField(), txt_bnachname = new JTextField(), txt_btelefon = new JTextField()}
        );

        row = addSection(mainPanel, "Haustier", row, c, fontLabel, fontText,
                new String[]{"HID", "Name", "Alter", "Tierart", "Anzahl", "Gewicht", "Größe"},
                new JTextField[]{txt_hid = new JTextField(), txt_hname = new JTextField(), txt_halter = new JTextField(),
                        txt_htierart = new JTextField(), txt_hanzahl = new JTextField(), txt_hgewicht = new JTextField(), txt_hgroesse = new JTextField()}
        );

        row = addSection(mainPanel, "Termine", row, c, fontLabel, fontText,
                new String[]{"TID", "Impfung", "Datum", "Rasse", "Name", "Haustier-ID"},
                new JTextField[]{txt_tid = new JTextField(), txt_timpfung = new JTextField(), txt_tdatum = new JTextField(),
                        txt_trasse = new JTextField(), txt_tname = new JTextField(), txt_thid = new JTextField()}
        );

        // Buttons
        btn_speichern = createStyledButton("Speichern in Datenbank", new Color(34, 139, 34)); // Dunkelgrün
        btn_reset = createStyledButton("Zurücksetzen", new Color(30, 144, 255)); 
        btn_beenden = createStyledButton("Beenden", new Color(220, 20, 60));

        MyActionListener listener = new MyActionListener();
        btn_speichern.addActionListener(listener);
        btn_reset.addActionListener(listener);
        btn_beenden.addActionListener(listener);

        c.gridx = 0;
        c.gridy = row;
        c.gridwidth = 2;
        c.insets = new Insets(20, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(btn_speichern, c);
        c.gridy++;
        mainPanel.add(btn_reset, c);
        c.gridy++;
        mainPanel.add(btn_beenden, c);
    }

    private int addSection(JPanel panel, String title, int startRow, GridBagConstraints c, Font labelFont, Font fieldFont, String[] labels, JTextField[] fields) {
        JPanel sectionPanel = new JPanel(new GridBagLayout());
        sectionPanel.setBackground(new Color(230, 240, 255));
        sectionPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), " " + title + " "));

        GridBagConstraints sc = new GridBagConstraints();
        sc.insets = new Insets(5, 5, 5, 5);
        sc.fill = GridBagConstraints.HORIZONTAL;

        for (int i = 0; i < labels.length; i++) {
            JLabel lbl = new JLabel(labels[i] + ":");
            lbl.setFont(labelFont);
            sc.gridx = 0;
            sc.gridy = i;
            sc.weightx = 0.3;
            sectionPanel.add(lbl, sc);

            fields[i].setFont(fieldFont);
            fields[i].setPreferredSize(new Dimension(300, 30));
            sc.gridx = 1;
            sc.weightx = 0.7;
            sectionPanel.add(fields[i], sc);
        }

        c.gridx = 0;
        c.gridy = startRow;
        c.gridwidth = 2;
        c.insets = new Insets(15, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(sectionPanel, c);

        return startRow + 1;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font("Sans Serif", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private class MyActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btn_speichern) {
                speichereInDatenbank();
            } else if (e.getSource() == btn_reset) {
                resetFields();
            } else if (e.getSource() == btn_beenden) {
                System.exit(0);
            }
        }
    }

    private void resetFields() {
        clearTextFields(getContentPane());
    }

    private void clearTextFields(Container container) {
        for (Component c : container.getComponents()) {
            if (c instanceof JTextField) {
                ((JTextField) c).setText("");
            } else if (c instanceof Container) {
                clearTextFields((Container) c);
            }
        }
    }

    private Connection connectToDatabase() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/petcare";
        String user = "root";       // <- Anpassen
        String password = "";       // <- Anpassen
        return DriverManager.getConnection(url, user, password);
    }

    private void speichereInDatenbank() {
        String sql = "INSERT INTO tierarztdaten (bid, bname, bnachname, btelefon, hid, hname, halter, tierart, anzahl, gewicht, groesse, tid, impfung, datum, rasse, tname, thid) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = connectToDatabase(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, txt_bid.getText());
            stmt.setString(2, txt_bname.getText());
            stmt.setString(3, txt_bnachname.getText());
            stmt.setString(4, txt_btelefon.getText());
            stmt.setString(5, txt_hid.getText());
            stmt.setString(6, txt_hname.getText());
            stmt.setString(7, txt_halter.getText());
            stmt.setString(8, txt_htierart.getText());
            stmt.setString(9, txt_hanzahl.getText());
            stmt.setString(10, txt_hgewicht.getText());
            stmt.setString(11, txt_hgroesse.getText());
            stmt.setString(12, txt_tid.getText());
            stmt.setString(13, txt_timpfung.getText());
            stmt.setString(14, txt_tdatum.getText());
            stmt.setString(15, txt_trasse.getText());
            stmt.setString(16, txt_tname.getText());
            stmt.setString(17, txt_thid.getText());

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Daten erfolgreich in der Datenbank gespeichert.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Fehler beim Speichern in DB: " + ex.getMessage());
        }
    }

   
}