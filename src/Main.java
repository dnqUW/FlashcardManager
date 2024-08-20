import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {
    // Data structure to store flashcard decks
    private static Map<String, ArrayList<Flashcard>> decks = new HashMap<>();
    private static JPanel cardPanel = new JPanel(new CardLayout()); // Panel for different screens

    public static void main(String[] args) {
        JFrame frame = new JFrame("Flashcard Manager"); // Creating the frame
        frame.setLayout(new BorderLayout());
        frame.setBounds(400, 400, 1200, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create main panel with CardLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        cardPanel.setPreferredSize(new Dimension(1200, 800));

        // Create and add all screens to cardPanel
        cardPanel.add(createTitlePanel(), "TitleScreen");
        cardPanel.add(createDeckPanel(), "CreateDeckScreen");
        cardPanel.add(createStudyPanel(), "StudyDeckScreen");
        cardPanel.add(createModifyPanel(), "ModifyDeckScreen");
        cardPanel.add(createDeletePanel(), "DeleteDeckScreen");

        // Add the cardPanel to the main panel and frame
        mainPanel.add(cardPanel, BorderLayout.CENTER);
        frame.add(mainPanel);

        // Make the frame visible
        frame.setVisible(true);
    }

    private static JPanel createTitlePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.DARK_GRAY);

        JLabel titleCard = new JLabel("Flashcard Manager", SwingConstants.CENTER);
        titleCard.setForeground(Color.WHITE);
        titleCard.setFont(new Font("Arial", Font.BOLD, 120));
        panel.add(titleCard, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.DARK_GRAY);

        // Create initial buttons for functionality
        JButton createButton = new JButton("Create");
        createButton.setBackground(Color.BLACK);
        createButton.setForeground(Color.WHITE);
        createButton.setPreferredSize(new Dimension(290, 200));
        createButton.addActionListener(e -> {
            // Remove the existing "CreateDeckScreen" panel if it exists
            cardPanel.remove(cardPanel.getComponent(1)); // Assuming "CreateDeckScreen" is the second panel

            // Recreate the "CreateDeckScreen" panel
            JPanel newCreateDeckPanel = createDeckPanel(); // Call the method to create a new deck panel
            cardPanel.add(newCreateDeckPanel, "CreateDeckScreen");

            // Show the newly created "CreateDeckScreen"
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, "CreateDeckScreen");
        });


        JButton studyButton = new JButton("Study");
        studyButton.setBackground(Color.BLACK);
        studyButton.setForeground(Color.WHITE);
        studyButton.setPreferredSize(new Dimension(290, 200));
        studyButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, "StudyDeckScreen");
        });

        JButton modifyButton = new JButton("Modify");
        modifyButton.setBackground(Color.BLACK);
        modifyButton.setForeground(Color.WHITE);
        modifyButton.setPreferredSize(new Dimension(290, 200));
        modifyButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, "ModifyDeckScreen");
        });

        JButton deleteButton = new JButton("Delete");
        deleteButton.setBackground(Color.BLACK);
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setPreferredSize(new Dimension(290, 200));
        deleteButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, "DeleteDeckScreen");
        });

        buttonPanel.add(createButton);
        buttonPanel.add(studyButton);
        buttonPanel.add(modifyButton);
        buttonPanel.add(deleteButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }


    private static JPanel createDeckPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.DARK_GRAY);

        // Create back button panel
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButtonPanel.setBackground(Color.DARK_GRAY);

        JButton backButton = new JButton("Back");
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(Color.BLACK);
        backButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, "TitleScreen");
        });

        backButtonPanel.add(backButton);

        // Create a container for input fields and buttons
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.DARK_GRAY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel askDeckName = new JLabel("Enter the name for your new deck:");
        askDeckName.setForeground(Color.WHITE);

        JTextField deckNameField = new JTextField(20);
        JButton saveDeckButton = new JButton("Save Deck");
        saveDeckButton.setBackground(Color.BLACK);
        saveDeckButton.setForeground(Color.WHITE);
        saveDeckButton.setEnabled(false);  // Initially disable the save button

        // Add deck name components
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        inputPanel.add(askDeckName, gbc);

        gbc.gridy = 1;
        inputPanel.add(deckNameField, gbc);

        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(saveDeckButton, gbc);

        // Add flashcard fields and buttons
        JLabel questionLabel = new JLabel("Question:");
        questionLabel.setForeground(Color.WHITE);

        JLabel answerLabel = new JLabel("Answer:");
        answerLabel.setForeground(Color.WHITE);

        JTextField questionField = new JTextField(20);
        JTextField answerField = new JTextField(20);

        JButton addFlashcardButton = new JButton("Add Flashcard");
        addFlashcardButton.setBackground(Color.BLACK);
        addFlashcardButton.setForeground(Color.WHITE);
        addFlashcardButton.setEnabled(false);  // Initially disable the add flashcard button

        gbc.gridy = 3;
        gbc.gridwidth = 2;
        inputPanel.add(questionLabel, gbc);

        gbc.gridy = 4;
        inputPanel.add(questionField, gbc);

        gbc.gridy = 5;
        inputPanel.add(answerLabel, gbc);

        gbc.gridy = 6;
        inputPanel.add(answerField, gbc);

        gbc.gridy = 7;
        gbc.gridwidth = 1;
        inputPanel.add(addFlashcardButton, gbc);

        // Create a JTextArea to display added flashcards
        JTextArea flashcardDisplay = new JTextArea(10, 40);
        flashcardDisplay.setEditable(false);
        flashcardDisplay.setBackground(Color.LIGHT_GRAY);
        flashcardDisplay.setForeground(Color.BLACK);
        JScrollPane scrollPane = new JScrollPane(flashcardDisplay);
        scrollPane.setPreferredSize(new Dimension(400, 400)); // Set preferred size for the scroll pane

        // Add components to the panel
        panel.add(backButtonPanel, BorderLayout.NORTH);
        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(scrollPane, BorderLayout.SOUTH);

        // Enable flashcard fields and buttons after deck name is entered
        deckNameField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                enableButtons();
            }
            public void removeUpdate(DocumentEvent e) {
                enableButtons();
            }
            public void insertUpdate(DocumentEvent e) {
                enableButtons();
            }

            public void enableButtons() {
                boolean isDeckNameEntered = !deckNameField.getText().isEmpty();
                addFlashcardButton.setEnabled(isDeckNameEntered);
                saveDeckButton.setEnabled(isDeckNameEntered);
            }
        });

        ArrayList<Flashcard> deck = new ArrayList<>();
        // Actions for adding flashcards and saving the deck
        addFlashcardButton.addActionListener(e -> {
            String question = questionField.getText();
            String answer = answerField.getText();
            int response = 0;
            if (!question.isEmpty() && !answer.isEmpty()) {
                Flashcard flashcard = new Flashcard(question, answer);
                if(!deck.contains(flashcard)) {
                    deck.add(flashcard);
                    System.out.println("DNE");
                } else {
                    System.out.println("Exists");
                    response = JOptionPane.showConfirmDialog(panel, "This flashcard already exists. Would you like to " +
                            "add it again?");
                    if(response == JOptionPane.NO_OPTION) {
                        JOptionPane.showMessageDialog(panel, "User selected no.");
                    }
                }


                // Update JTextArea to display the added flashcard
                if(response == JOptionPane.YES_OPTION) {
                    flashcardDisplay.append("Question: " + flashcard.getQuestion() + "\nAnswer: " + flashcard.getAnswer()
                            + "\n\n");

                    JOptionPane.showMessageDialog(panel, "Flashcard Added!");
                    questionField.setText("");
                    answerField.setText("");
                }

            } else {
                JOptionPane.showMessageDialog(panel, "Please enter both question and answer.");
            }
        });

        saveDeckButton.addActionListener(e -> {
            String deckName = deckNameField.getText();
            if (!deckName.isEmpty()) {

                // Notify user
                JOptionPane.showMessageDialog(panel, "Deck Saved!");

                // Clear the text fields
                deckNameField.setText("");

                // Disable flashcard fields and buttons until a new deck name is entered
                addFlashcardButton.setEnabled(false);
                saveDeckButton.setEnabled(false);

                // Clear the JTextArea display
                flashcardDisplay.setText("");
                inputPanel.remove(saveDeckButton);
                inputPanel.remove(deckNameField);
                inputPanel.remove(askDeckName);

                JPanel deckCreated = new JPanel();
                deckCreated.add(new JLabel("Deck created: " + deckName));
                deckCreated.setForeground(Color.white);
                inputPanel.add(deckCreated);

                addFlashcardButton.setEnabled(true);
                saveDeckButton.setEnabled(true);

                inputPanel.revalidate();
                inputPanel.repaint();


                CardLayout cl = (CardLayout) cardPanel.getLayout();
                cl.show(cardPanel, "CreateDeckScreen");
            } else {
                JOptionPane.showMessageDialog(panel, "Please enter a deck name.");
            }
        });

        return panel;
    }






    private static JPanel createStudyPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.DARK_GRAY);

        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButtonPanel.setBackground(Color.DARK_GRAY);

        JButton backButton = new JButton("Back");
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(Color.BLACK);
        backButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, "TitleScreen");
        });

        backButtonPanel.add(backButton);

        JLabel label = new JLabel("Study Decks", SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 60));

        panel.add(backButtonPanel, BorderLayout.NORTH);
        panel.add(label, BorderLayout.CENTER);

        // Add functionality and UI components for studying decks here

        return panel;
    }

    private static JPanel createModifyPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.DARK_GRAY);

        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButtonPanel.setBackground(Color.DARK_GRAY);

        JButton backButton = new JButton("Back");
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(Color.BLACK);
        backButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, "TitleScreen");
        });

        backButtonPanel.add(backButton);

        JLabel label = new JLabel("Modify Decks", SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 60));

        panel.add(backButtonPanel, BorderLayout.NORTH);
        panel.add(label, BorderLayout.CENTER);

        // Add functionality and UI components for modifying decks here

        return panel;
    }

    private static JPanel createDeletePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.DARK_GRAY);

        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButtonPanel.setBackground(Color.DARK_GRAY);

        JButton backButton = new JButton("Back");
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(Color.BLACK);
        backButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, "TitleScreen");
        });

        backButtonPanel.add(backButton);

        JLabel label = new JLabel("Delete Decks", SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 60));

        panel.add(backButtonPanel, BorderLayout.NORTH);
        panel.add(label, BorderLayout.CENTER);

        // Add functionality and UI components for deleting decks here

        return panel;
    }
}
