import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;

public class TierarztGui extends JFrame {
    // Attribute
    JTextField txt_bid, txt_bname, txt_bnachname, txt_btelefon;
    JTextField txt_hid, txt_hname, txt_halter, txt_htierart, txt_hanzahl, txt_hgewicht, txt_hgroesse;
    JTextField txt_tid, txt_timpfung, txt_tdatum, txt_trasse, txt_tname, txt_thid;

    JButton btn_speichern, btn_reset, btn_beenden;

    public TierarztGui() {
        this.setTitle("Tierarzt Verwaltungsformular");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(800, 1000));
        initComponents();
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void initComponents() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        Font fontLabel = new Font("Sans Serif", Font.PLAIN, 14);
        Font fontText = new Font("Sans Serif", Font.PLAIN, 16);
        int row = 0;

        // Besitzer
        row = addSectionTitle("Besitzer", row, c);
        txt_bid = addLabeledField("BID:", row++, c, fontLabel, fontText);
        txt_bname = addLabeledField("Name:", row++, c, fontLabel, fontText);
        txt_bnachname = addLabeledField("Nachname:", row++, c, fontLabel, fontText);
        txt_btelefon = addLabeledField("Telefon:", row++, c, fontLabel, fontText);

        // Haustier
        row = addSectionTitle("Haustier", row, c);
        txt_hid = addLabeledField("HID:", row++, c, fontLabel, fontText);
        txt_hname = addLabeledField("Name:", row++, c, fontLabel, fontText);
        txt_halter = addLabeledField("Alter:", row++, c, fontLabel, fontText);
        txt_htierart = addLabeledField("Tierart:", row++, c, fontLabel, fontText);
        txt_hanzahl = addLabeledField("Anzahl:", row++, c, fontLabel, fontText);
        txt_hgewicht = addLabeledField("Gewicht:", row++, c, fontLabel, fontText);
        txt_hgroesse = addLabeledField("Größe:", row++, c, fontLabel, fontText);

        // Termine
        row = addSectionTitle("Termine", row, c);
        txt_tid = addLabeledField("TID:", row++, c, fontLabel, fontText);
        txt_timpfung = addLabeledField("Impfung:", row++, c, fontLabel, fontText);
        txt_tdatum = addLabeledField("Datum:", row++, c, fontLabel, fontText);
        txt_trasse = addLabeledField("Rasse:", row++, c, fontLabel, fontText);
        txt_tname = addLabeledField("Name:", row++, c, fontLabel, fontText);
        txt_thid = addLabeledField("Haustier-ID:", row++, c, fontLabel, fontText);

        // Buttons
        btn_speichern = new JButton("Speichern in Datenbank");
        btn_reset = new JButton("Zurücksetzen");
        btn_beenden = new JButton("Beenden");

        MyActionListener listener = new MyActionListener();

        c.gridx = 0;
        c.gridy = row;
        c.gridwidth = 2;
        c.insets = new Insets(15, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;
        this.add(btn_speichern, c);
        btn_speichern.addActionListener(listener);

        c.gridy++;
        this.add(btn_reset, c);
        btn_reset.addActionListener(listener);

        c.gridy++;
        this.add(btn_beenden, c);
        btn_beenden.addActionListener(listener);
    }

    private int addSectionTitle(String title, int row, GridBagConstraints c) {
        JLabel label = new JLabel(" " + title + " ");
        label.setFont(new Font("Sans Serif", Font.BOLD, 18));
        c.gridx = 0;
        c.gridy = row;
        c.gridwidth = 2;
        c.insets = new Insets(15, 10, 5, 10);
        this.add(label, c);
        return row + 1;
    }

    private JTextField addLabeledField(String labelText, int row, GridBagConstraints c, Font labelFont, Font fieldFont) {
        JLabel label = new JLabel(labelText);
        label.setFont(labelFont);
        c.gridx = 0;
        c.gridy = row;
        c.gridwidth = 1;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(5, 10, 0, 5);
        this.add(label, c);

        JTextField textField = new JTextField();
        textField.setFont(fieldFont);
        textField.setPreferredSize(new Dimension(400, 40));
        c.gridx = 1;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        this.add(textField, c);

        return textField;
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
        for (Component comp : this.getContentPane().getComponents()) {
            if (comp instanceof JTextField) {
                ((JTextField) comp).setText("");
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TierarztGui());
    }
}
