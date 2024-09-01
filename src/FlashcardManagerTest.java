import javax.swing.*;
import java.awt.*;

public class FlashcardManagerTest {
    private static JPanel cardPanel;
    private static CardLayout cardLayout;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Flashcard Manager Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // First panel with a button to switch to the second panel
        JPanel firstPanel = new JPanel();
        firstPanel.setBackground(Color.LIGHT_GRAY);
        JButton switchToSecondPanelButton = new JButton("Go to Study Panel");
        switchToSecondPanelButton.addActionListener(e -> cardLayout.show(cardPanel, "SecondPanel"));
        firstPanel.add(switchToSecondPanelButton);

        // Second panel with a back button to switch to the first panel
        JPanel secondPanel = new JPanel();
        secondPanel.setBackground(Color.DARK_GRAY);
        JButton backButton = new JButton("Back to Main Screen");
        backButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "FirstPanel");
            // Here you would call your update method like updateAllPanels();
        });
        secondPanel.add(backButton);

        cardPanel.add(firstPanel, "FirstPanel");
        cardPanel.add(secondPanel, "SecondPanel");

        frame.add(cardPanel);
        cardLayout.show(cardPanel, "FirstPanel");

        frame.setVisible(true);

    }


}
