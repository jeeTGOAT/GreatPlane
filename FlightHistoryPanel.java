import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FlightHistoryPanel extends JDialog {
    public FlightHistoryPanel(JFrame parent, List<Object[]> flightHistory) {
        super(parent, "Historique des vols", true);
        setSize(600, 300);
        setLocationRelativeTo(parent);

        String[] columns = {"Départ", "Arrivée", "Durée (min)", "Statut"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        for (Object[] row : flightHistory) {
            model.addRow(row);
        }
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }
} 