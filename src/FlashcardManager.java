import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FlashcardManager {
    // Data structure to store flashcard decks
    private static Map<String, ArrayList<Flashcard>> decks = new HashMap<>(); // Map of all decks
    private static JPanel cardPanel = new JPanel(new CardLayout()); // Panel for different screens
    private static JPanel studyPanel; // Study panel
    private static JPanel modifyPanel; // Modify panel
    private static JPanel deletePanel; // Delete panel
    // Field to store the current deck name
    private static String currentDeckName = ""; // Current deck name

    public static void main(String[] args) {
        try { // to set the look and field across all platforms
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Creating the main frame
        JFrame frame = new JFrame("Flashcard Manager");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon icon = new ImageIcon("./icons/Flashcard Manager.png");
        frame.setIconImage(icon.getImage());
        if (icon.getImageLoadStatus() != MediaTracker.COMPLETE) {
            System.err.println("Failed to load image icon.");
        }

        // Load decks
        decks = DataManager.loadDecks();

        // Create and set up cardPanel
        cardPanel = new JPanel(new CardLayout());
        cardPanel.setPreferredSize(new Dimension(1200, 800));

        // Initialize and add panels to cardPanel
        JPanel titlePanel = createTitlePanel();
        JPanel createPanel = createDeckPanel();
        studyPanel = createStudyPanel();
        modifyPanel = createModifyPanel();
        deletePanel = createDeletePanel();

        cardPanel.add(titlePanel, "TitleScreen");
        cardPanel.add(createPanel, "CreateDeckScreen");
        cardPanel.add(studyPanel, "StudyDeckScreen");
        cardPanel.add(modifyPanel, "ModifyDeckScreen");
        cardPanel.add(deletePanel, "DeleteDeckScreen");

        // Create and set up mainPanel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(cardPanel, BorderLayout.CENTER);

        // Add mainPanel to frame and set visibility
        frame.add(mainPanel);
        frame.pack(); // Adjusts frame size based on content
        frame.setVisible(true);
    }

    // extends JPanel, meant to give background image to title screen
    public static class ImagePanel extends JPanel {
        private final Image backgroundImage;

        public ImagePanel(String imagePath) {
            backgroundImage = new ImageIcon(imagePath).getImage();
            setLayout(new BorderLayout());
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    // Returns JPanel for the title panel
    private static JPanel createTitlePanel() {
        // Replace with the path to your background image
        ImagePanel panel = new ImagePanel("./icons/flashcardmanagercover.png");

        // Title card initialization
        JLabel titleCard = new JLabel("Flashcard Manager", SwingConstants.CENTER);
        titleCard.setForeground(Color.BLACK);
        titleCard.setFont(new Font("Arial", Font.BOLD, 120));
        panel.add(titleCard, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(0, 0, 0, 0)); // Transparent background for button panel

        // Create initial buttons for functionality
        JButton createButton = createStyledButton("Create Deck");
        createButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, "CreateDeckScreen");
        });

        JButton studyButton = createStyledButton("Study Deck");
        studyButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, "StudyDeckScreen");
        });

        JButton modifyButton = createStyledButton("Modify Deck");
        modifyButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, "ModifyDeckScreen");
        });

        JButton deleteButton = createStyledButton("Delete Deck");
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



    // Returns JPanel of the create deck panel
    private static JPanel createDeckPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.DARK_GRAY);

        JPanel backButtonPanel = createBackButtonPanel();
        panel.add(backButtonPanel, BorderLayout.NORTH);

        // Create a container for input fields and buttons
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.DARK_GRAY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Ensure components stretch to fill space

        JLabel askDeckName = new JLabel("Enter the name for your new deck:");
        askDeckName.setForeground(Color.WHITE);

        JTextField deckNameField = new JTextField(20);
        JButton saveDeckButton = new JButton("Save Deck");
        saveDeckButton.setBackground(Color.BLACK);
        saveDeckButton.setForeground(Color.WHITE);
        saveDeckButton.setEnabled(false);  // Initially disable the save button

        JButton editDeckNameButton = new JButton("Edit Deck Name");
        editDeckNameButton.setBackground(Color.BLACK);
        editDeckNameButton.setForeground(Color.WHITE);
        editDeckNameButton.setEnabled(false); // Initially disabled

        // Add deck name components
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        inputPanel.add(askDeckName, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER; // Allow the text field to span full width
        inputPanel.add(deckNameField, gbc);

        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(saveDeckButton, gbc);

        gbc.gridx = 1; // Position the edit button next to the save button
        inputPanel.add(editDeckNameButton, gbc);

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
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        inputPanel.add(questionLabel, gbc);

        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Ensure the text field spans full width
        inputPanel.add(questionField, gbc);

        gbc.gridy = 5;
        inputPanel.add(answerLabel, gbc);

        gbc.gridy = 6;
        inputPanel.add(answerField, gbc);

        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;


        // Creates a JTextArea to display added flashcards
        JTextArea flashcardDisplay = new JTextArea(10, 40);
        flashcardDisplay.setEditable(false);
        flashcardDisplay.setBackground(Color.LIGHT_GRAY);
        flashcardDisplay.setForeground(Color.BLACK);
        JScrollPane scrollPane = new JScrollPane(flashcardDisplay);
        scrollPane.setPreferredSize(new Dimension(400, 400)); // Set preferred size for the scroll pane

        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(scrollPane, BorderLayout.SOUTH);

        // Enable flashcard fields and buttons after deck name is entered
        deckNameField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(this::enableButtons);
            }

            public void removeUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(this::enableButtons);
            }

            public void insertUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(this::enableButtons);
            }

            public void enableButtons() {
                boolean isDeckNameEntered = !deckNameField.getText().isEmpty();
//                addFlashcardButton.setEnabled(isDeckNameEntered);
                saveDeckButton.setEnabled(isDeckNameEntered);
            }
        });

        // Current deck
        ArrayList<Flashcard> deck = new ArrayList<>();
        // Actions for adding flashcards and saving the deck
        addFlashcardButton.addActionListener(e -> SwingUtilities.invokeLater(() -> {
            String question = questionField.getText();
            String answer = answerField.getText();
            int response = 0;
            if (!question.isEmpty() && !answer.isEmpty()) {
                Flashcard flashcard = new Flashcard(question.trim(), answer.trim());
                if (!deck.contains(flashcard)) {
                    deck.add(flashcard);
                } else {
                    response = JOptionPane.showConfirmDialog(panel, "This question already exists. " +
                            "Would you like to add it again?");
                }

                // Update JTextArea to display the added flashcard
                if (response == JOptionPane.YES_OPTION || !deck.contains(flashcard)) {
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
        }));

        JLabel deckNameLabel = new JLabel();
        // Action for saving deck
        saveDeckButton.addActionListener(e ->  {
            String deckName = deckNameField.getText();
            if(!decks.containsKey(deckName)) {
                if (!deckName.isEmpty()) {
                    // Add deck to map
                    decks.put(deckName, deck);
                    currentDeckName = deckName; // Set current deck name
                    System.out.println("Decks map before saving: " + decks); // Debugging statement

                    // Save decks to file
                    DataManager.saveDecks(decks);
                    System.out.println("Decks saved to file."); // Debugging statement

                    // Notify user with deck name
                    JOptionPane.showMessageDialog(panel, "Deck \"" + deckName + "\" Saved!");

                    // Clear the text fields and disable components
                    deckNameLabel.setText("Deck name: " + deckName);
                    deckNameLabel.setBackground(Color.DARK_GRAY);
                    deckNameLabel.setForeground(Color.WHITE);
                    deckNameLabel.setFont(new Font("Arial", Font.BOLD, 30));

                    // Remove deck name field
                    inputPanel.remove(deckNameField);
                    inputPanel.remove(saveDeckButton);
                    inputPanel.remove(askDeckName);
                    inputPanel.add(deckNameLabel);
                    inputPanel.add(addFlashcardButton, gbc);

                    // Ensure layout is updated
                    inputPanel.revalidate();
                    inputPanel.repaint();

                    addFlashcardButton.setEnabled(true);
                    deck.clear();

                    // Show the edit button
                    editDeckNameButton.setEnabled(true);

                    // Refresh the other panels to show the new deck
                    updateAllPanels();
                } else {
                    JOptionPane.showMessageDialog(panel, "Please enter a deck name.");
                }
            } else {
                System.out.println("Fix this");
                JOptionPane.showMessageDialog(panel, "Deck with this name already exists. Please " +
                        "enter a new one.");
            }
        });

        // Action for editing deck name on create panel
        editDeckNameButton.addActionListener(e -> SwingUtilities.invokeLater(() -> {
            // Clear the inputPanel
            inputPanel.remove(deckNameField);
            inputPanel.remove(saveDeckButton);
            inputPanel.remove(askDeckName);
            inputPanel.remove(deckNameLabel);
            inputPanel.remove(editDeckNameButton);

            // Re-add components for editing
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            inputPanel.add(askDeckName, gbc);

            gbc.gridy = 1;
            inputPanel.add(deckNameField, gbc);

            gbc.gridy = 2;
            gbc.gridwidth = 1;
            gbc.fill = GridBagConstraints.NONE;
            inputPanel.add(saveDeckButton, gbc);

            gbc.gridx = 1; // Position the edit button next to the save button

            // Update the layout
            inputPanel.revalidate();
            inputPanel.repaint();

            // Enable the save button for renaming
            saveDeckButton.setText("Save New Deck Name");
            saveDeckButton.setEnabled(true);

            // Remove previous action listeners to avoid multiple triggers
            for (ActionListener al : saveDeckButton.getActionListeners()) {
                saveDeckButton.removeActionListener(al);
            }

            // Action for saving the new deck name
            saveDeckButton.addActionListener(ev -> {
                String newDeckName = deckNameField.getText();
                if (!newDeckName.isEmpty()) {
                    if (!decks.containsKey(newDeckName)) {
                        ArrayList<Flashcard> oldFlashcards = decks.get(currentDeckName);
                        decks.remove(currentDeckName);
                        decks.put(newDeckName, oldFlashcards);
                        currentDeckName = newDeckName;
                        System.out.println("Current decks: " + decks);
                        DataManager.saveDecks(decks);
                        JOptionPane.showMessageDialog(panel, "Deck name updated to \"" + newDeckName + "\"!");

                        // Update the deck name label and re-enable the edit button
                        deckNameLabel.setText("Deck name: " + newDeckName);
                        inputPanel.remove(saveDeckButton);
                        inputPanel.add(editDeckNameButton, gbc);

                        // Refresh the layout
                        inputPanel.revalidate();
                        inputPanel.repaint();

                        // Refresh the panels to reflect the name change
                        updateAllPanels();
                    } else {
                        JOptionPane.showMessageDialog(panel, "Deck with this name already exists. Please enter a new one.");
                    }
                } else {
                    JOptionPane.showMessageDialog(panel, "Please enter a new deck name.");
                }
            });
        }));


        return panel;
    }

    // Returns a JPanel that creates the study panel
    private static JPanel createStudyPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.DARK_GRAY);

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setBackground(Color.DARK_GRAY);

        JPanel backButtonPanel = createBackButtonPanel();

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

                JPanel backButtonPanelStudy = new JPanel(new BorderLayout());
                backButtonPanelStudy.setBackground(Color.DARK_GRAY);
                JButton backButton = createStyledButton("Back");
                backButton.addActionListener(backEvent -> {
                    CardLayout cl = (CardLayout) cardPanel.getLayout();
                    cl.show(cardPanel, "StudyDeckScreen");
                });
                backButtonPanelStudy.add(backButton, BorderLayout.WEST);

                // Use JTextArea for the question with wrapping
                JTextArea questionArea = new JTextArea();
                questionArea.setEditable(false);
                questionArea.setWrapStyleWord(true);
                questionArea.setLineWrap(true);
                questionArea.setFont(new Font("Arial", Font.PLAIN, 20));
                questionArea.setForeground(Color.WHITE);
                questionArea.setBackground(Color.DARK_GRAY);
                JScrollPane questionScrollPane = new JScrollPane(questionArea);
                questionScrollPane.setPreferredSize(new Dimension(400, 100)); // Adjust size as needed

                JTextArea answerArea = new JTextArea(3, 20);
                answerArea.setEditable(false);
                answerArea.setWrapStyleWord(true);
                answerArea.setLineWrap(true);
                answerArea.setFont(new Font("Arial", Font.PLAIN, 20));
                answerArea.setForeground(Color.DARK_GRAY);
                answerArea.setBackground(Color.WHITE);
                JScrollPane answerScrollPane = new JScrollPane(answerArea);
                answerScrollPane.setPreferredSize(new Dimension(300, 100));

                JButton showAnswerButton = createStyledButton("Show Answer");
                JButton nextButton = createStyledButton("Next");
                JButton incorrectAnswerButton = createStyledButton("Incorrect Answer");

                // Create a button panel with GridBagLayout
                JPanel buttonPanel = new JPanel(new GridBagLayout());
                buttonPanel.setBackground(Color.DARK_GRAY);
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5, 5, 5, 5);
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = 0.5;
                gbc.gridy = 0;

                gbc.gridx = 0;
                buttonPanel.add(showAnswerButton);


                gbc.gridx = 2;

                JPanel centerPanel = new JPanel(new BorderLayout());
                centerPanel.setBackground(Color.DARK_GRAY);
                centerPanel.add(questionScrollPane, BorderLayout.NORTH); // Use scroll pane for questionArea
                centerPanel.add(answerScrollPane, BorderLayout.CENTER);

                studyPanel.add(backButtonPanelStudy, BorderLayout.NORTH);
                studyPanel.add(centerPanel, BorderLayout.CENTER);
                studyPanel.add(buttonPanel, BorderLayout.SOUTH);

                // Make a copy of the deck to modify during the study session
                ArrayList<Flashcard> originalDeck = new ArrayList<>(decks.get(deckName));
                ArrayList<Flashcard> deck = new ArrayList<>(originalDeck);
                Collections.shuffle(deck);
                ArrayList<Flashcard> incorrectAnswers = new ArrayList<>();
                int[] currentCardIndex = {0};

                if (!deck.isEmpty()) {
                    displayFlashcard(deck, currentCardIndex, questionArea);
                    cardPanel.add(studyPanel, "StudyPanel");
                    CardLayout cl = (CardLayout) cardPanel.getLayout();
                    cl.show(cardPanel, "StudyPanel");
                } else {
                    JOptionPane.showMessageDialog(studyPanel, "There are no cards in this deck.");
                    return;
                }

                showAnswerButton.addActionListener(showAnswerEvent -> {
                    if (currentCardIndex[0] < deck.size()) {
                        Flashcard currentCard = deck.get(currentCardIndex[0]);
                        answerArea.setText(currentCard.getAnswer());

                        // Check if the Incorrect Answer/ show answer button is already added
                        if (buttonPanel.getComponentCount() < 3) {
                            buttonPanel.add(incorrectAnswerButton);
                            buttonPanel.remove(showAnswerButton);
                            buttonPanel.add(nextButton);
                            buttonPanel.revalidate();
                            buttonPanel.repaint();
                        }
                    } else {
                        JOptionPane.showMessageDialog(studyPanel, "No more cards in the deck!");
                    }
                });

                incorrectAnswerButton.addActionListener(incorrectEvent -> {
                    incorrectAnswers.add(deck.get(currentCardIndex[0]));
                    nextButton.doClick();
                    buttonPanel.remove(incorrectAnswerButton);
                    buttonPanel.revalidate();
                    buttonPanel.repaint();
                });

                nextButton.addActionListener(nextEvent -> {
                    if (buttonPanel.getComponentCount() < 3) { // Check if the Incorrect Answer button is already added
                        buttonPanel.removeAll();
                        buttonPanel.add(showAnswerButton);
                        buttonPanel.add(nextButton);
                        buttonPanel.revalidate();
                        buttonPanel.repaint();
                    }
                    currentCardIndex[0]++;
                    if (currentCardIndex[0] < deck.size()) {
                        displayFlashcard(deck, currentCardIndex, questionArea);
                        answerArea.setText("");
                    } else if (!incorrectAnswers.isEmpty()) {
                        deck.clear();
                        deck.addAll(incorrectAnswers);
                        incorrectAnswers.clear();
                        currentCardIndex[0] = 0;
                        JOptionPane.showMessageDialog(studyPanel, "Let's review the ones you got wrong.");
                        displayFlashcard(deck, currentCardIndex, questionArea);
                        answerArea.setText("");
                    } else {
                        JOptionPane.showMessageDialog(studyPanel, "You've completed the deck!");
                        CardLayout cl = (CardLayout) cardPanel.getLayout();
                        cl.show(cardPanel, "StudyDeckScreen");
                    }
                });
            });

            deckListPanel.add(deckButton);
        }


        JScrollPane scrollPane = new JScrollPane(deckListPanel);
        scrollPane.setPreferredSize(new Dimension(400, 400));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }


    // Creates a "Back" button panel that can switch between screens
    private static JPanel createBackButtonPanel() {
        JPanel backButtonPanel = new JPanel(new BorderLayout());
        backButtonPanel.setBackground(Color.DARK_GRAY);

        JButton backButton = createStyledButton("Back");
        backButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, "TitleScreen");
        });

        backButtonPanel.add(backButton, BorderLayout.WEST);
        return backButtonPanel;
    }

    // Creates a "Back" button panel that can switch between screens
    private static JPanel backButtonPreviousScreen(String previousScreen) {
        JPanel backButtonPanel = new JPanel(new BorderLayout());
        backButtonPanel.setBackground(Color.DARK_GRAY);

        JButton backButton = createStyledButton("Back");
        backButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, previousScreen);
        });

        backButtonPanel.add(backButton, BorderLayout.WEST);
        return backButtonPanel;
    }


    // Returns a JPanel for deck modification
    private static JPanel createModifyPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.DARK_GRAY);

        // Create a panel for the back button
        JPanel backButtonPanel = createBackButtonPanel();
        // Assume the back button is the first component
        JButton backButton = (JButton) backButtonPanel.getComponent(0);
        Dimension backButtonSize = backButton.getPreferredSize();
        int backButtonHeight = backButtonSize.height;

        // Create a panel for the content (label and deck buttons)
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.DARK_GRAY);

        // Create a panel to hold the label and back button vertically aligned
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.DARK_GRAY);

        // Add back button panel to the left of topPanel
        topPanel.add(backButtonPanel, BorderLayout.WEST);

        // Create a panel for the label with fixed height equal to the back button
        JPanel labelPanel = new JPanel();
        labelPanel.setBackground(Color.DARK_GRAY);
        labelPanel.setPreferredSize(new Dimension(400, backButtonHeight)); // Set height to match the back button

        JLabel label = new JLabel("Select a deck to modify.");
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 30));
        label.setHorizontalAlignment(SwingConstants.CENTER);

        labelPanel.add(label);

        // Add label panel to the center of topPanel
        topPanel.add(labelPanel, BorderLayout.CENTER);

        // Add topPanel to the north of centerPanel
        centerPanel.add(topPanel, BorderLayout.NORTH);

        // Create a panel to hold deck buttons
        JPanel deckButtonPanel = new JPanel();
        deckButtonPanel.setLayout(new GridLayout(0, 1, 10, 10));
        deckButtonPanel.setBackground(Color.DARK_GRAY);

        for (String deckName : decks.keySet()) {
            JButton deckButton = createStyledButton(deckName);
            deckButton.addActionListener(e -> {
                ArrayList<Flashcard> deck = decks.get(deckName);
                JPanel modifyPanel = new JPanel(new BorderLayout());
                modifyPanel.setBackground(Color.DARK_GRAY);

                JPanel northModifyPanel = new JPanel(new BorderLayout());
                northModifyPanel.setBackground(Color.DARK_GRAY);

                JPanel modifyDeckPanelBackButton = backButtonPreviousScreen("ModifyDeckScreen");
                northModifyPanel.add(modifyDeckPanelBackButton, BorderLayout.WEST);

                JLabel titleLabel = new JLabel("Modify Deck: " + deckName, SwingConstants.CENTER);
                titleLabel.setForeground(Color.WHITE);
                titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
                northModifyPanel.add(titleLabel, BorderLayout.CENTER);

                modifyPanel.add(northModifyPanel, BorderLayout.NORTH);

                JPanel flashcardPanel = new JPanel();
                flashcardPanel.setLayout(new GridLayout(0, 1, 10, 10));
                flashcardPanel.setBackground(Color.DARK_GRAY);

                for (Flashcard flashcard : deck) {
                    JPanel flashcardEntry = new JPanel();
                    flashcardEntry.setBackground(Color.LIGHT_GRAY);
                    flashcardEntry.setLayout(new BorderLayout());

                    JTextArea questionArea = new JTextArea("Q: " + flashcard.getQuestion());
                    questionArea.setFont(new Font("Arial", Font.PLAIN, 16));
                    questionArea.setLineWrap(true);
                    questionArea.setWrapStyleWord(true);
                    questionArea.setEditable(false);
                    questionArea.setOpaque(false);
                    questionArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

                    JTextArea answerArea = new JTextArea("A: " + flashcard.getAnswer());
                    answerArea.setFont(new Font("Arial", Font.PLAIN, 16));
                    answerArea.setLineWrap(true);
                    answerArea.setWrapStyleWord(true);
                    answerArea.setEditable(false);
                    answerArea.setOpaque(false);
                    answerArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

                    JPanel textPanel = new JPanel();
                    textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS)); // Stack the text areas vertically
                    textPanel.add(questionArea);
                    textPanel.add(answerArea);

                    // Update preferred size dynamically based on content
                    questionArea.setPreferredSize(null);
                    answerArea.setPreferredSize(null);

                    questionArea.setSize(new Dimension(150, Short.MAX_VALUE));
                    answerArea.setSize(new Dimension(150, Short.MAX_VALUE));

                    questionArea.setPreferredSize(new Dimension(questionArea.getPreferredSize().width, questionArea.getPreferredSize().height));
                    answerArea.setPreferredSize(new Dimension(answerArea.getPreferredSize().width, answerArea.getPreferredSize().height));

                    // Buttons to delete and edit the flashcard
                    JButton deleteButton = createStyledButton("Delete");
                    deleteButton.addActionListener(deleteEvent -> {
                        int confirm = JOptionPane.showConfirmDialog(modifyPanel,
                                "Are you sure you want to delete this flashcard?",
                                "Confirm Delete",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.WARNING_MESSAGE);

                        if (confirm == JOptionPane.YES_OPTION) {
                            deck.remove(flashcard);
                            flashcardPanel.remove(flashcardEntry);
                            flashcardPanel.revalidate();
                            flashcardPanel.repaint();
                            DataManager.saveDecks(decks);
                        }
                    });


                    JButton editButton = createStyledButton("Edit");
                    editButton.addActionListener(editEvent -> {
                        JTextField newQuestionField = new JTextField(flashcard.getQuestion());
                        JTextField newAnswerField = new JTextField(flashcard.getAnswer());
                        JPanel editPanel = new JPanel(new GridLayout(0, 1));
                        editPanel.add(new JLabel("Edit Question:"));
                        editPanel.add(newQuestionField);
                        editPanel.add(new JLabel("Edit Answer:"));
                        editPanel.add(newAnswerField);

                        int result = JOptionPane.showConfirmDialog(null, editPanel, "Edit Flashcard",
                                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                        if (result == JOptionPane.OK_OPTION) {
                            String newQuestion = newQuestionField.getText().trim();
                            String newAnswer = newAnswerField.getText().trim();

                            if (!newQuestion.isEmpty() && !newAnswer.isEmpty()) {
                                Flashcard updatedFlashcard = new Flashcard(newQuestion, newAnswer);

                                boolean isDuplicate = false;
                                for (Flashcard deckFlashcard : deck) {
                                    if (deckFlashcard != flashcard && deckFlashcard.getQuestion().equalsIgnoreCase(updatedFlashcard.getQuestion())) {
                                        isDuplicate = true;
                                        break; // Exit loop if a duplicate is found
                                    }
                                }

                                if (!isDuplicate) {
                                    // Update the flashcard
                                    flashcard.setQuestion(newQuestion);
                                    flashcard.setAnswer(newAnswer);

                                    // Update labels
                                    questionArea.setText("Q: " + flashcard.getQuestion());
                                    answerArea.setText("A: " + flashcard.getAnswer());

                                    // Save changes to the file
                                    DataManager.saveDecks(decks);

                                    System.out.println("Flashcard updated.");
                                } else {
                                    // Show a dialog if a duplicate is found
                                    int confirmResult = JOptionPane.showConfirmDialog(null,
                                            "A flashcard with this question already exists. Do you still want to update it?",
                                            "Duplicate Question", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                                    if (confirmResult == JOptionPane.YES_OPTION) {
                                        // Update the flashcard
                                        flashcard.setQuestion(newQuestion);
                                        flashcard.setAnswer(newAnswer);

                                        // Update labels
                                        questionArea.setText("Q: " + flashcard.getQuestion());
                                        answerArea.setText("A: " + flashcard.getAnswer());

                                        // Save changes to the file
                                        DataManager.saveDecks(decks);

                                        System.out.println("Flashcard updated.");
                                    } else {
                                        System.out.println("Modification canceled.");
                                    }
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Question and answer cannot be empty.");
                            }
                        } else {
                            System.out.println("Edit canceled.");
                        }
                    });


                    JPanel buttonPanel = new JPanel();
                    buttonPanel.add(editButton);
                    buttonPanel.add(deleteButton);

                    flashcardEntry.add(textPanel, BorderLayout.CENTER);
                    flashcardEntry.add(buttonPanel, BorderLayout.EAST);

                    flashcardPanel.add(flashcardEntry);
                }


                JScrollPane scrollPane = new JScrollPane(flashcardPanel);
                scrollPane.setPreferredSize(new Dimension(400, 400));

                modifyPanel.add(scrollPane, BorderLayout.CENTER);

                // Action listener to Edit deck name
                JButton editDeckNameButton = createStyledButton("Edit Deck Name");
                editDeckNameButton.addActionListener(editNameEvent -> {
                    String newDeckName = JOptionPane.showInputDialog(modifyPanel, "Enter a new name for the deck:", deckName);
                    if (newDeckName == null) {
                        return; // User canceled
                    }
                    System.out.println(deckName);
                    System.out.println(newDeckName);

                    newDeckName = newDeckName.trim(); // Trim whitespace

                    if (!newDeckName.isEmpty()) {
                        if (!decks.containsKey(newDeckName)) {
                            ArrayList<Flashcard> tempDeck = decks.remove(deckName);
                            decks.put(newDeckName, tempDeck);
                            decks.remove(deckName);
                            titleLabel.setText("Modify Deck: " + newDeckName); // Update title label

                            // Update the button that was clicked to show the new deck name
                            deckButton.setText(newDeckName);

                            JOptionPane.showMessageDialog(modifyPanel, "Deck name updated successfully!");

                            // Refresh the modify panel to reflect changes
                            updateAllPanels(); // Implement this method to recreate or refresh the modifyPanel
                            DataManager.saveDecks(decks);
                        } else {
                            JOptionPane.showMessageDialog(modifyPanel, "A deck with this name already exists.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(modifyPanel, "Invalid name or same as current name.");
                    }
                });



                // Action listener to Add flashcard to deck
                JButton addFlashcardButton = createStyledButton("Add Flashcard");
                addFlashcardButton.addActionListener(addFlashcardEvent -> {
                    JTextField questionField = new JTextField();
                    JTextField answerField = new JTextField();
                    JPanel inputPanel = new JPanel(new GridLayout(0, 1));
                    inputPanel.add(new JLabel("Question:"));
                    inputPanel.add(questionField);
                    inputPanel.add(new JLabel("Answer:"));
                    inputPanel.add(answerField);

                    int result = JOptionPane.showConfirmDialog(modifyPanel, inputPanel, "Add Flashcard",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (result == JOptionPane.OK_OPTION) {
                        String question = questionField.getText();
                        String answer = answerField.getText();

                        if (!question.trim().isEmpty() && !answer.trim().isEmpty()) {
                            Flashcard newFlashcard = new Flashcard(question.trim(), answer.trim());

                            // Check for duplicate questions in the deck
                            boolean isDuplicate = false;
                            for (Flashcard deckFlashcard : deck) {
                                if (deckFlashcard.getQuestion().equalsIgnoreCase(newFlashcard.getQuestion())) {
                                    isDuplicate = true;
                                    break; // Exit loop if a duplicate is found
                                }
                            }

                            if (!isDuplicate) {
                                deck.add(newFlashcard);

                                // Update display to match existing flashcards
                                JPanel flashcardEntry = new JPanel(new BorderLayout());
                                flashcardEntry.setBackground(Color.LIGHT_GRAY);
                                flashcardEntry.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // Match border style

                                JPanel textPanel = new JPanel(new GridLayout(0, 1));
                                textPanel.setBackground(Color.LIGHT_GRAY);

                                JLabel newQuestionLabel = new JLabel("<html>Q: " + newFlashcard.getQuestion()
                                        + "</html>");
                                newQuestionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                                JLabel newAnswerLabel = new JLabel("<html>A: " + newFlashcard.getAnswer()
                                        + "</html>");
                                newAnswerLabel.setFont(new Font("Arial", Font.PLAIN, 16));

                                textPanel.add(newQuestionLabel);
                                textPanel.add(newAnswerLabel);

                                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                                buttonPanel.setBackground(Color.LIGHT_GRAY);

                                JButton newDeleteButton = createStyledButton("Delete");
                                newDeleteButton.addActionListener(deleteEvent -> {
                                    int confirm = JOptionPane.showConfirmDialog(modifyPanel, "Are you sure " +
                                                    "you want to delete this flashcard?",
                                            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                                    if (confirm == JOptionPane.YES_OPTION) {
                                        deck.remove(newFlashcard);
                                        flashcardPanel.remove(flashcardEntry);
                                        flashcardPanel.revalidate();
                                        flashcardPanel.repaint();
                                        DataManager.saveDecks(decks); // Save changes
                                    }
                                });

                                JButton newEditButton = createStyledButton("Edit");
                                newEditButton.addActionListener(editEvent -> {
                                    JTextField newQuestionField = new JTextField(newFlashcard.getQuestion());
                                    JTextField newAnswerField = new JTextField(newFlashcard.getAnswer());
                                    JPanel editPanel = new JPanel(new GridLayout(0, 1));
                                    editPanel.add(new JLabel("Edit Question:"));
                                    editPanel.add(newQuestionField);
                                    editPanel.add(new JLabel("Edit Answer:"));
                                    editPanel.add(newAnswerField);

                                    int editResult = JOptionPane.showConfirmDialog(null, editPanel,
                                            "Edit Flashcard",
                                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                                    if (editResult == JOptionPane.OK_OPTION) {
                                        String newQuestion = newQuestionField.getText().trim();
                                        String newAnswer = newAnswerField.getText().trim();

                                        if (!newQuestion.isEmpty() && !newAnswer.isEmpty()) {
                                            Flashcard updatedFlashcard = new Flashcard(newQuestion, newAnswer);

                                            // Check for duplicate questions in the deck
                                            boolean isDuplicate2 = false;
                                            for (Flashcard deckFlashcard : deck) {
                                                if (deckFlashcard != newFlashcard && deckFlashcard.getQuestion().
                                                        equalsIgnoreCase(updatedFlashcard.getQuestion())) {
                                                    isDuplicate2 = true;
                                                    break; // Exit loop if a duplicate is found
                                                }
                                            }

                                            if (!isDuplicate2) {
                                                // Update the flashcard
                                                newFlashcard.setQuestion(newQuestion);
                                                newFlashcard.setAnswer(newAnswer);

                                                // Update labels
                                                newQuestionLabel.setText("<html>Q: " + newFlashcard.getQuestion()
                                                        + "</html>");
                                                newAnswerLabel.setText("<html>A: " + newFlashcard.getAnswer()
                                                        + "</html>");

                                                // Save changes to the file
                                                DataManager.saveDecks(decks);

                                                System.out.println("Flashcard updated.");
                                            } else {
                                                // Show a dialog if a duplicate is found
                                                int confirmResult = JOptionPane.showConfirmDialog(null,
                                                        "A flashcard with this question already exists. Do " +
                                                                "you still want to update it?",
                                                        "Duplicate Question", JOptionPane.YES_NO_OPTION,
                                                        JOptionPane.WARNING_MESSAGE);
                                                if (confirmResult == JOptionPane.YES_OPTION) {
                                                    // Update the flashcard
                                                    newFlashcard.setQuestion(newQuestion);
                                                    newFlashcard.setAnswer(newAnswer);

                                                    // Update labels
                                                    newQuestionLabel.setText("<html>Q: " + newFlashcard.getQuestion()
                                                            + "</html>");
                                                    newAnswerLabel.setText("<html>A: " + newFlashcard.getAnswer()
                                                            + "</html>");

                                                    // Save changes to the file
                                                    DataManager.saveDecks(decks);

                                                    System.out.println("Flashcard updated.");
                                                }
                                            }
                                        } else {
                                            JOptionPane.showMessageDialog(null, "Question " +
                                                    "and answer cannot be empty.");
                                        }
                                    }
                                });


                                buttonPanel.add(newEditButton);
                                buttonPanel.add(newDeleteButton);

                                flashcardEntry.add(textPanel, BorderLayout.CENTER);
                                flashcardEntry.add(buttonPanel, BorderLayout.EAST);

                                flashcardPanel.add(flashcardEntry);
                                flashcardPanel.revalidate();
                                flashcardPanel.repaint();
                                DataManager.saveDecks(decks);
                            } else {
                                JOptionPane.showMessageDialog(modifyPanel, "A flashcard with the same " +
                                        "question already exists.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(modifyPanel, "Question and answer cannot be empty.");
                        }
                    }
                });



                JPanel southPanel = new JPanel();
                southPanel.setBackground(Color.DARK_GRAY);
                southPanel.add(editDeckNameButton);
                southPanel.add(addFlashcardButton);

                modifyPanel.add(southPanel, BorderLayout.SOUTH);

                cardPanel.add(modifyPanel, "ModifyPanel");
                CardLayout cl = (CardLayout) cardPanel.getLayout();
                cl.show(cardPanel, "ModifyPanel");
            });
            deckButtonPanel.add(deckButton);
        }

        JScrollPane deckScrollPane = new JScrollPane(deckButtonPanel);
        deckScrollPane.setPreferredSize(new Dimension(400, 400));

        centerPanel.add(deckScrollPane, BorderLayout.CENTER);

        panel.add(centerPanel, BorderLayout.CENTER);

        return panel;
    }

    // Returns JPanel and creates a new delete panel
    private static JPanel createDeletePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.DARK_GRAY);

        // Create a panel to hold the back button and label
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.DARK_GRAY);

        // Create and add the back button
        JPanel backButtonPanel = createBackButtonPanel();
        topPanel.add(backButtonPanel, BorderLayout.WEST);

        // Create a panel to center the label
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false); // Make the panel transparent

        // Create and add the label
        JLabel label = new JLabel("Select decks to delete.");
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 30));

        // Use BoxLayout to center the label horizontally
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));
        centerPanel.add(Box.createHorizontalGlue()); // Push label to the center
        centerPanel.add(label);
        centerPanel.add(Box.createHorizontalGlue()); // Push label to the center

        topPanel.add(centerPanel, BorderLayout.CENTER);

        // Add the topPanel to the NORTH of the main panel
        panel.add(topPanel, BorderLayout.NORTH);

        // Create a panel for the deck checkboxes using GridLayout
        JPanel deckCheckboxPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        deckCheckboxPanel.setBackground(Color.DARK_GRAY);

        // Map to keep track of checkboxes and associated deck names
        Map<JCheckBox, String> checkBoxDeckMap = new HashMap<>();

        // Add checkboxes for each deck
        for (String deckName : decks.keySet()) {
            JCheckBox deckCheckBox = new JCheckBox(deckName);
            deckCheckBox.setBackground(Color.DARK_GRAY);
            deckCheckBox.setForeground(Color.WHITE);
            deckCheckBox.setFont(new Font("Arial", Font.PLAIN, 20));
            checkBoxDeckMap.put(deckCheckBox, deckName);
            deckCheckboxPanel.add(deckCheckBox);
        }

        // Wrap the deckCheckboxPanel in a scroll pane
        JScrollPane scrollPane = new JScrollPane(deckCheckboxPanel);
        scrollPane.setPreferredSize(new Dimension(400, 400));

        // Add a delete button at the bottom
        JButton deleteButton = createStyledButton("Delete Selected Decks");
        deleteButton.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(panel,
                    "Are you sure you want to delete the selected decks?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);

            if (response == JOptionPane.YES_OPTION) {
                // Iterate through the checkboxes and delete selected decks
                for (Map.Entry<JCheckBox, String> entry : checkBoxDeckMap.entrySet()) {
                    if (entry.getKey().isSelected()) {
                        decks.remove(entry.getValue());
                        updateAllPanels(); // Ensure all panels are updated after deletion
                    }
                }
                DataManager.saveDecks(decks);
                JOptionPane.showMessageDialog(panel, "Selected decks deleted!");
            }
        });

        // Add the scroll pane and delete button to the centerPanel
        JPanel centerPanel2 = new JPanel(new BorderLayout());
        centerPanel2.setBackground(Color.DARK_GRAY);
        centerPanel2.add(scrollPane, BorderLayout.CENTER);
        centerPanel2.add(deleteButton, BorderLayout.SOUTH);

        // Add the centerPanel2 to the main panel
        panel.add(centerPanel2, BorderLayout.CENTER);

        return panel;
    }


    private static void updateAllPanels() {
        // Update study panel
        updateStudyPanel();

        // Update modify panel
        updateModifyPanel();

        // Update delete panel
        updateDeletePanel();

        // Revalidate and repaint each panel to ensure UI consistency
        studyPanel.revalidate();
        studyPanel.repaint();

        modifyPanel.revalidate();
        modifyPanel.repaint();

        deletePanel.revalidate();
        deletePanel.repaint();
    }


    // Updates study panel. Catches exception if error occurs
    private static void updateStudyPanel() {
        try {
            if (studyPanel == null) {
                studyPanel = createStudyPanel();
                cardPanel.add(studyPanel, "StudyPanel");
            } else {
                System.out.println("Clearing study panel components...");
                studyPanel.removeAll(); // Clear existing components

                JPanel updatedPanel = createStudyPanel();
                studyPanel.add(updatedPanel, BorderLayout.CENTER); // Add updated panel components

                studyPanel.revalidate();
                studyPanel.repaint();

                System.out.println("Study panel updated and revalidated.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred while updating the study panel: " + e.getMessage());
        }
    }

    // Updates modify panel. Catches exception if error occurs
    private static void updateModifyPanel() {
        try {
            if (modifyPanel == null) {
                modifyPanel = createModifyPanel();
                cardPanel.add(modifyPanel, "ModifyPanel");
            } else {
                System.out.println("Clearing modify panel components...");
                modifyPanel.removeAll(); // Clear existing components

                JPanel updatedPanel = createModifyPanel();
                modifyPanel.add(updatedPanel, BorderLayout.CENTER); // Add updated panel components

                modifyPanel.revalidate();
                modifyPanel.repaint();

                System.out.println("Modify panel updated and revalidated.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred while updating the modify panel: " + e.getMessage());
        }
    }

    // Updates delete panel. Catches exception if error occurs
    private static void updateDeletePanel() {
        try {
            if (deletePanel == null) {
                deletePanel = createDeletePanel();
                cardPanel.add(deletePanel, "DeletePanel");
            } else {
                System.out.println("Clearing delete panel components...");
                deletePanel.removeAll(); // Clear existing components

                JPanel updatedPanel = createDeletePanel();
                deletePanel.add(updatedPanel, BorderLayout.CENTER); // Add updated panel components

                deletePanel.revalidate();
                deletePanel.repaint();

                System.out.println("Delete panel updated and revalidated.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred while updating the delete panel: " + e.getMessage());
        }
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
    private static void displayFlashcard(ArrayList<Flashcard> deck, int[] currentCardIndex, JTextArea textArea) {
        Flashcard currentCard = deck.get(currentCardIndex[0]);
        textArea.setText("Question: " + currentCard.getQuestion());
    }

}

