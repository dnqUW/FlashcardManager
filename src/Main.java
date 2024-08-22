import javax.swing.*;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.event.DocumentEvent;

public class Main {
    // Data structure to store flashcard decks
    private static Map<String, ArrayList<Flashcard>> decks = new HashMap<>();
    private static JPanel cardPanel = new JPanel(new CardLayout()); // Panel for different screens

    public static void main(String[] args) {
        // Creating the frame
        JFrame frame = new JFrame("Flashcard Manager");
        frame.setLayout(new BorderLayout());
        frame.setBounds(400, 400, 1200, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Load the decks in
        decks = DataManager.loadDecks();

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

    // Creates and returns the title page "Flashcard Manager"
    private static JPanel createTitlePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.DARK_GRAY);

        // Title card initialization
        JLabel titleCard = new JLabel("Flashcard Manager", SwingConstants.CENTER);
        titleCard.setForeground(Color.WHITE);
        titleCard.setFont(new Font("Arial", Font.BOLD, 120));
        panel.add(titleCard, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.DARK_GRAY);

        // Create initial buttons for functionality
        JButton createButton = createStyledButton("Create");
        createButton.addActionListener(e -> {
            // Remove the existing "CreateDeckScreen" panel if it exists
            cardPanel.remove(cardPanel.getComponent(1));

            // Recreate the "CreateDeckScreen" panel
            JPanel newCreateDeckPanel = createDeckPanel();
            cardPanel.add(newCreateDeckPanel, "CreateDeckScreen");

            // Show the newly created "CreateDeckScreen"
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, "CreateDeckScreen");
        });

        JButton studyButton = createStyledButton("Study");
        studyButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, "StudyDeckScreen");
        });

        JButton modifyButton = createStyledButton("Modify");
        modifyButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, "ModifyDeckScreen");
        });

        JButton deleteButton = createStyledButton("Delete");
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

    // Returns a JPanel of the create page
    private static JPanel createDeckPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.DARK_GRAY);

        JPanel backButtonPanel = createBackButtonPanel("TitleScreen");

        // Create a container for input fields and buttons
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.DARK_GRAY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel askDeckName = new JLabel("Enter the name for your new deck:");
        askDeckName.setForeground(Color.WHITE);

        JTextField deckNameField = new JTextField(20);
        JButton saveDeckButton = createStyledButton("Save Deck");
        saveDeckButton.setEnabled(false);

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

        JLabel questionLabel = new JLabel("Question:");
        questionLabel.setForeground(Color.WHITE);

        JLabel answerLabel = new JLabel("Answer:");
        answerLabel.setForeground(Color.WHITE);

        JTextField questionField = new JTextField(20);
        JTextField answerField = new JTextField(20);

        JButton addFlashcardButton = createStyledButton("Add Flashcard");
        addFlashcardButton.setEnabled(false);

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

        JTextArea flashcardDisplay = new JTextArea(10, 40);
        flashcardDisplay.setEditable(false);
        flashcardDisplay.setBackground(Color.LIGHT_GRAY);
        flashcardDisplay.setForeground(Color.BLACK);
        JScrollPane scrollPane = new JScrollPane(flashcardDisplay);
        scrollPane.setPreferredSize(new Dimension(400, 400));

        panel.add(backButtonPanel, BorderLayout.NORTH);
        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(scrollPane, BorderLayout.SOUTH);

        deckNameField.getDocument().addDocumentListener((SimpleDocumentListener) e -> {
            boolean isDeckNameEntered = !deckNameField.getText().isEmpty();
            addFlashcardButton.setEnabled(isDeckNameEntered);
            saveDeckButton.setEnabled(isDeckNameEntered);
        });

        ArrayList<Flashcard> deck = new ArrayList<>();
        addFlashcardButton.addActionListener(e -> {
            String question = questionField.getText();
            String answer = answerField.getText();
            int response = 0;
            if (!question.isEmpty() && !answer.isEmpty()) {
                Flashcard flashcard = new Flashcard(question.trim(), answer.trim());
                if (!deck.contains(flashcard)) {
                    deck.add(flashcard);
                } else {
                    response = JOptionPane.showConfirmDialog(panel, "This flashcard already exists. " +
                            "Would you like to add it again?");
                }

                if (response == JOptionPane.YES_OPTION) {
                    flashcardDisplay.append("Question: " + flashcard.getQuestion() + "\nAnswer: " +
                            flashcard.getAnswer() + "\n\n");

                    JOptionPane.showMessageDialog(panel, "Flashcard Added!");
                    questionField.setText("");
                    answerField.setText("");
                }
                DataManager.saveDecks(decks);
            } else {
                JOptionPane.showMessageDialog(panel, "Please enter both question and answer.");
            }
            System.out.println(deck);
        });

        saveDeckButton.addActionListener(e -> {
            String deckName = deckNameField.getText();
            if (!deckName.isEmpty()) {
                decks.put(deckName, deck);
                DataManager.saveDecks(decks);
                JOptionPane.showMessageDialog(panel, "Deck Saved!");

                deckNameField.setText("");
                addFlashcardButton.setEnabled(false);
                saveDeckButton.setEnabled(false);
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

                updateDeckList();
            } else {
                JOptionPane.showMessageDialog(panel, "Please enter a deck name.");
            }
        });

        return panel;
    }

    private static JPanel createStudyPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.DARK_GRAY);

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setBackground(Color.DARK_GRAY);

        JPanel backButtonPanel = createBackButtonPanel("TitleScreen");

        JLabel label = new JLabel("Select a deck to study.");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 30));

        northPanel.add(backButtonPanel, BorderLayout.WEST);
        northPanel.add(label, BorderLayout.CENTER);

        panel.add(northPanel, BorderLayout.NORTH);

        JPanel deckListPanel = new JPanel();
        deckListPanel.setLayout(new GridLayout(0, 1, 10, 10));
        deckListPanel.setBackground(Color.DARK_GRAY);

        for (String deckName : decks.keySet()) {
            JButton deckButton = createStyledButton(deckName);
            deckButton.addActionListener(e -> {
                JPanel studyPanel = new JPanel(new BorderLayout());
                studyPanel.setBackground(Color.DARK_GRAY);

                JLabel questionLabel = new JLabel();
                questionLabel.setHorizontalAlignment(SwingConstants.CENTER);
                questionLabel.setForeground(Color.WHITE);
                questionLabel.setFont(new Font("Arial", Font.PLAIN, 20));

                JTextField answerField = new JTextField(20);
                JButton showAnswerButton = createStyledButton("Show Answer");
                JButton nextButton = createStyledButton("Next");

                studyPanel.add(questionLabel, BorderLayout.NORTH);
                studyPanel.add(answerField, BorderLayout.CENTER);

                JPanel buttonPanel = new JPanel();
                buttonPanel.setBackground(Color.DARK_GRAY);
                buttonPanel.add(showAnswerButton);
                buttonPanel.add(nextButton);

                studyPanel.add(buttonPanel, BorderLayout.SOUTH);

                // Study deck logic
                ArrayList<Flashcard> deck = decks.get(deckName);
                int[] currentCardIndex = {0};
                displayFlashcard(deck, currentCardIndex, questionLabel);

                showAnswerButton.addActionListener(showAnswerEvent -> {
                    Flashcard currentCard = deck.get(currentCardIndex[0]);
                    answerField.setText(currentCard.getAnswer());
                });

                nextButton.addActionListener(nextEvent -> {
                    currentCardIndex[0]++;
                    if (currentCardIndex[0] < deck.size()) {
                        displayFlashcard(deck, currentCardIndex, questionLabel);
                        answerField.setText("");
                    } else {
                        JOptionPane.showMessageDialog(panel, "You have completed the deck!");
                        CardLayout cl = (CardLayout) cardPanel.getLayout();
                        cl.show(cardPanel, "StudyDeckScreen");
                    }
                });

                cardPanel.add(studyPanel, "StudyPanel");
                CardLayout cl = (CardLayout) cardPanel.getLayout();
                cl.show(cardPanel, "StudyPanel");
            });

            deckListPanel.add(deckButton);
        }

        JScrollPane scrollPane = new JScrollPane(deckListPanel);
        scrollPane.setPreferredSize(new Dimension(400, 400));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // Creates a "Back" button panel that can switch between screens
    private static JPanel createBackButtonPanel(String targetScreen) {
        JPanel backButtonPanel = new JPanel(new BorderLayout());
        backButtonPanel.setBackground(Color.DARK_GRAY);

        JButton backButton = createStyledButton("Back");
        backButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, targetScreen);
        });

        backButtonPanel.add(backButton, BorderLayout.WEST);
        return backButtonPanel;
    }

    // Returns a JPanel for deck modification
    private static JPanel createModifyPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.DARK_GRAY);

        JPanel backButtonPanel = createBackButtonPanel("TitleScreen");

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(0, 1, 10, 10));
        centerPanel.setBackground(Color.DARK_GRAY);

        JLabel label = new JLabel("Select a deck to modify.");
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 30));
        centerPanel.add(label);

        for (String deckName : decks.keySet()) {
            JButton deckButton = createStyledButton(deckName);
            deckButton.addActionListener(e -> {
                ArrayList<Flashcard> deck = decks.get(deckName);
                if (deck != null) {
                    JPanel modifyPanel = new JPanel(new BorderLayout());
                    modifyPanel.setBackground(Color.DARK_GRAY);

                    JLabel titleLabel = new JLabel("Modify Deck: " + deckName, SwingConstants.CENTER);
                    titleLabel.setForeground(Color.WHITE);
                    titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
                    modifyPanel.add(titleLabel, BorderLayout.NORTH);

                    JPanel flashcardPanel = new JPanel();
                    flashcardPanel.setLayout(new GridLayout(0, 1, 10, 10));
                    flashcardPanel.setBackground(Color.DARK_GRAY);

                    for (Flashcard flashcard : deck) {
                        JPanel flashcardEntry = new JPanel();
                        flashcardEntry.setBackground(Color.LIGHT_GRAY);

                        JLabel questionLabel = new JLabel("Q: " + flashcard.getQuestion());
                        questionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                        JLabel answerLabel = new JLabel("A: " + flashcard.getAnswer());
                        answerLabel.setFont(new Font("Arial", Font.PLAIN, 16));

                        JButton deleteButton = createStyledButton("Delete");
                        deleteButton.addActionListener(deleteEvent -> {
                            deck.remove(flashcard);
                            flashcardPanel.remove(flashcardEntry);
                            flashcardPanel.revalidate();
                            flashcardPanel.repaint();
                        });

                        flashcardEntry.add(questionLabel);
                        flashcardEntry.add(answerLabel);
                        flashcardEntry.add(deleteButton);

                        flashcardPanel.add(flashcardEntry);
                    }

                    JScrollPane scrollPane = new JScrollPane(flashcardPanel);
                    scrollPane.setPreferredSize(new Dimension(400, 400));

                    modifyPanel.add(scrollPane, BorderLayout.CENTER);

                    JButton saveChangesButton = createStyledButton("Save Changes");
                    saveChangesButton.addActionListener(saveEvent -> {
                        decks.put(deckName, deck);
                        DataManager.saveDecks(decks);
                        JOptionPane.showMessageDialog(panel, "Deck changes saved!");
                        CardLayout cl = (CardLayout) cardPanel.getLayout();
                        cl.show(cardPanel, "ModifyDeckScreen");
                    });

                    modifyPanel.add(saveChangesButton, BorderLayout.SOUTH);

                    cardPanel.add(modifyPanel, "ModifyPanel");
                    CardLayout cl = (CardLayout) cardPanel.getLayout();
                    cl.show(cardPanel, "ModifyPanel");
                }
            });

            centerPanel.add(deckButton);
        }

        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setPreferredSize(new Dimension(400, 400));

        panel.add(backButtonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // Returns a JPanel for deleting a deck
    private static JPanel createDeletePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.DARK_GRAY);

        JPanel backButtonPanel = createBackButtonPanel("TitleScreen");

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(0, 1, 10, 10));
        centerPanel.setBackground(Color.DARK_GRAY);

        JLabel label = new JLabel("Select a deck to delete.");
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 30));
        centerPanel.add(label);

        for (String deckName : decks.keySet()) {
            JButton deckButton = createStyledButton(deckName);
            deckButton.addActionListener(e -> {
                int response = JOptionPane.showConfirmDialog(panel, "Are you sure you want to delete the deck \"" +
                        deckName + "\"?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

                if (response == JOptionPane.YES_OPTION) {
                    decks.remove(deckName);
                    DataManager.saveDecks(decks);
                    JOptionPane.showMessageDialog(panel, "Deck \"" + deckName + "\" deleted!");
                    updateDeckList();
                }
            });

            centerPanel.add(deckButton);
        }

        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setPreferredSize(new Dimension(400, 400));

        panel.add(backButtonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // Updates the deck list in the Study and Modify panels
    private static void updateDeckList() {
        cardPanel.remove(cardPanel.getComponent(2));
        cardPanel.remove(cardPanel.getComponent(3));

        JPanel studyDeckScreen = createStudyPanel();
        JPanel modifyDeckScreen = createModifyPanel();

        cardPanel.add(studyDeckScreen, "StudyDeckScreen");
        cardPanel.add(modifyDeckScreen, "ModifyDeckScreen");

        cardPanel.revalidate();
        cardPanel.repaint();
    }

    // Creates a styled button with a consistent look
    private static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(Color.BLACK);
        button.setBackground(Color.LIGHT_GRAY);
        button.setFont(new Font("Arial", Font.PLAIN, 20));
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        button.setPreferredSize(new Dimension(200, 50));
        button.setFocusPainted(false);

        // Additional hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.GRAY);
                button.setForeground(Color.WHITE);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.LIGHT_GRAY);
                button.setForeground(Color.BLACK);
            }
        });

        return button;
    }

    // Display the current flashcard
    private static void displayFlashcard(ArrayList<Flashcard> deck, int[] currentCardIndex, JLabel questionLabel) {
        Flashcard currentCard = deck.get(currentCardIndex[0]);
        questionLabel.setText("Question: " + currentCard.getQuestion());
    }

    @FunctionalInterface
    interface SimpleDocumentListener extends DocumentListener {
        void update(DocumentEvent e);

        @Override
        default void insertUpdate(DocumentEvent e) {
            update(e);
        }

        @Override
        default void removeUpdate(DocumentEvent e) {
            update(e);
        }

        @Override
        default void changedUpdate(DocumentEvent e) {
            update(e);
        }
    }


}

