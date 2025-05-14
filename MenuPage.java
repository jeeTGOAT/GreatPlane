import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class MenuPage extends JPanel {
    private static final Color BUTTON_COLOR = new Color(0, 122, 204);
    private static final Color BUTTON_HOVER_COLOR = new Color(0, 153, 255);
    private static final Color BUTTON_PRESS_COLOR = new Color(0, 102, 179);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 48);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final int BUTTON_HEIGHT = 60;
    private static final int BUTTON_WIDTH = 250;
    private static final int BUTTON_SPACING = 20;
    
    private JFrame parentFrame;
    private Image backgroundImage;
    private float titleY = -50;
    private float buttonOpacity = 0;
    private Timer animationTimer;
    
    private JButton simulateButton;
    private JButton quitButton;
    private JButton flightHistoryButton;
    
    public MenuPage(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(null);
        
        // Charger l'image de fond
        try {
            backgroundImage = new ImageIcon("resources/pixel_plane_bg.png").getImage();
        } catch (Exception e) {
            System.err.println("Impossible de charger l'image de fond");
        }
        
        createButtons();
        setupAnimations();
        
        setFocusable(true);
        requestFocusInWindow();
    }
    
    private void setupAnimations() {
        animationTimer = new Timer(1000/60, e -> {
            // Animation du titre
            if (titleY < 100) {
                titleY += (100 - titleY) * 0.1;
            }
            
            // Fondu des boutons
            if (buttonOpacity < 1) {
                buttonOpacity += 0.05;
            }
            
            repaint();
        });
        animationTimer.start();
    }
    
    private void createButtons() {
        // Créer des boutons standards
        simulateButton = new JButton("SIMULER");
        quitButton = new JButton("QUITTER");
        flightHistoryButton = new JButton("HISTORIQUE DES VOLS");

        // Configurer les boutons
        Font buttonFont = new Font("Arial", Font.BOLD, 20);
        Color buttonColor = new Color(0, 122, 204);
        
        for (JButton button : new JButton[]{simulateButton, quitButton, flightHistoryButton}) {
            button.setFont(buttonFont);
            button.setBackground(buttonColor);
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setBorderPainted(true);
        }

        // Ajouter les listeners
        simulateButton.addActionListener(e -> startSimulation());
        quitButton.addActionListener(e -> quitGame());
        flightHistoryButton.addActionListener(e -> showFlightHistory());

        // Ajouter les boutons au panel
        add(simulateButton);
        add(quitButton);
        add(flightHistoryButton);

        // Positionner les boutons
        int buttonWidth = 200;
        int buttonHeight = 50;
        int spacing = 20;
        int startY = 320;
        int centerX = (getWidth() - buttonWidth) / 2;

        simulateButton.setBounds(centerX, startY, buttonWidth, buttonHeight);
        quitButton.setBounds(centerX, startY + buttonHeight + spacing, buttonWidth, buttonHeight);
        flightHistoryButton.setBounds(centerX, startY + 2 * (buttonHeight + spacing), buttonWidth, buttonHeight);

        // Forcer le rafraîchissement
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Dessiner le fond
        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        }
        
        // Dessiner le titre
        drawTitle(g2d);
        
        // Mettre à jour la position des boutons
        updateButtonLayout();
        
        g2d.dispose();
        
        // In paintComponent(), set the background to transparent
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));
    }
    
    private void drawTitle(Graphics2D g2d) {
        String title = "SIMULATEUR D'AVION";
        g2d.setFont(TITLE_FONT);
        FontMetrics fm = g2d.getFontMetrics();
        int titleX = (getWidth() - fm.stringWidth(title)) / 2;
        
        g2d.setColor(new Color(0, 153, 255, 50));
        for (int i = 0; i < 5; i++) {
            g2d.drawString(title, titleX, titleY + i);
        }
        
        g2d.setColor(Color.WHITE);
        g2d.drawString(title, titleX, titleY);
    }
    
    private void updateButtonLayout() {
        if (simulateButton == null) return;
        
        int totalHeight = 3 * BUTTON_HEIGHT + 2 * BUTTON_SPACING;
        int startY = (int)((getHeight() - totalHeight) * 0.6);
        int centerX = (getWidth() - BUTTON_WIDTH) / 2;
        
        float alpha = 1.0f;
        simulateButton.setForeground(new Color(1f, 1f, 1f, alpha));
        quitButton.setForeground(new Color(1f, 1f, 1f, alpha));
        flightHistoryButton.setForeground(new Color(1f, 1f, 1f, alpha));
        
        // Position SIMULER and HISTORIQUE DES VOLS buttons
        simulateButton.setBounds(centerX, startY, BUTTON_WIDTH, BUTTON_HEIGHT);
        flightHistoryButton.setBounds(centerX, startY + BUTTON_HEIGHT + BUTTON_SPACING, BUTTON_WIDTH, BUTTON_HEIGHT);
        
        // Position QUITTER button at the bottom with some margin
        int bottomMargin = 50;
        quitButton.setBounds(centerX, getHeight() - BUTTON_HEIGHT - bottomMargin, BUTTON_WIDTH, BUTTON_HEIGHT);
    }
    
    private Color interpolateColor(Color c1, Color c2, float ratio) {
        float[] comp1 = c1.getRGBComponents(null);
        float[] comp2 = c2.getRGBComponents(null);
        float[] result = new float[4];
        for (int i = 0; i < 4; i++) {
            result[i] = comp1[i] + (comp2[i] - comp1[i]) * ratio;
        }
        return new Color(result[0], result[1], result[2], result[3]);
    }
    
    private void startSimulation() {
        System.out.println("Démarrage de la simulation...");
        animationTimer.stop();
        
        Timer fadeOutTimer = new Timer(1000/60, new ActionListener() {
            float alpha = 1.0f;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                alpha -= 0.05f;
                if (alpha <= 0) {
                    ((Timer)e.getSource()).stop();
                    parentFrame.getContentPane().removeAll();
                    parentFrame.add(new SimulationPanel());
                    parentFrame.revalidate();
                    parentFrame.repaint();
                } else {
                    setOpaque(false);
                    setBackground(new Color(0,0,0,0));
                    repaint();
                }
            }
        });
        fadeOutTimer.start();
    }
    
    private void quitGame() {
        int result = JOptionPane.showConfirmDialog(
            parentFrame,
            "Êtes-vous sûr de vouloir quitter ?",
            "Quitter",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (result == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    
    private void showFlightHistory() {
        java.util.List<Object[]> history = SimulationPanel.getLastFlightHistory();
        if (history.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucun vol terminé pour l'instant.", "Historique des vols", JOptionPane.INFORMATION_MESSAGE);
        } else {
            FlightHistoryPanel panel = new FlightHistoryPanel(parentFrame, history);
            panel.setVisible(true);
        }
    }
} 