import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
        private Image backgroundImage;

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
        JButton createButton = createStyledButton("Create");
        createButton.addActionListener(e -> {
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

        createButton.addActionListener(e -> {
            System.out.println("Create button clicked");

            // Create a new panel
            JPanel newCreateDeckPanel = createDeckPanel();

            // Add the new panel to cardPanel with an identifier
            cardPanel.add(newCreateDeckPanel, "CreateDeckScreen");

            // Switch to the new panel
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, "CreateDeckScreen");

            // Revalidate and repaint to ensure proper layout update
            cardPanel.revalidate();
            cardPanel.repaint();
        });

        studyButton.addActionListener(e -> {
            System.out.println("Study button clicked");
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, "StudyDeckScreen");
        });

        modifyButton.addActionListener(e -> {
            System.out.println("Modify button clicked");
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, "ModifyDeckScreen");
        });

        deleteButton.addActionListener(e -> {
            System.out.println("Delete button clicked");
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, "DeleteDeckScreen");
        });

        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }



    // Returns JPanel of the create deck panel
    private static JPanel createDeckPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.DARK_GRAY);

        JPanel backButtonPanel = createBackButtonPanel("TitleScreen");
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
        inputPanel.add(addFlashcardButton, gbc);

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
                SwingUtilities.invokeLater(() -> enableButtons());
            }

            public void removeUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(() -> enableButtons());
            }

            public void insertUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(() -> enableButtons());
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
        addFlashcardButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
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
            });
        });

        JLabel deckNameLabel = new JLabel();
        // Action for saving deck
        saveDeckButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                String deckName = deckNameField.getText();
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
            });
        });

        // Action for editing deck name on create panel
        editDeckNameButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
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


                // Action for saving the new deck name
                saveDeckButton.addActionListener(ev -> {
                    String newDeckName = deckNameField.getText();
                    if (!newDeckName.isEmpty()) {
                        // Remove the old deck and add it with the new name
                        if (decks.containsKey(currentDeckName)) {
                            ArrayList<Flashcard> existingDeck = decks.remove(currentDeckName);
                            decks.put(newDeckName, existingDeck);
                            DataManager.saveDecks(decks);
                            JOptionPane.showMessageDialog(panel, "Deck name updated to \"" + newDeckName + "\"!");
                            inputPanel.add(editDeckNameButton, gbc);
                            // Refresh the panels to reflect the name change
                            updateAllPanels();
                        }
                    } else {
                        JOptionPane.showMessageDialog(panel, "Please enter a new deck name.");
                    }
                });
            });
        });

        return panel;
    }

    // Creates the study panel, returns a JPanel
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

            // Action button for the decks on the list
            deckButton.addActionListener(e -> {
                JPanel studyPanel = new JPanel(new BorderLayout());
                studyPanel.setBackground(Color.DARK_GRAY);

                JButton backButton = createStyledButton("Back");
                // Back button listener
                backButton.addActionListener(backEvent -> {
                    CardLayout cl = (CardLayout) cardPanel.getLayout();
                    cl.show(cardPanel, "StudyDeckScreen"); // Go back to the deck list panel
                });

                JPanel backButtonPanelStudy = new JPanel(new BorderLayout());
                backButtonPanelStudy.setBackground(Color.DARK_GRAY);
                backButtonPanelStudy.add(backButton, BorderLayout.WEST);

                JLabel questionLabel = new JLabel();
                questionLabel.setHorizontalAlignment(SwingConstants.CENTER);
                questionLabel.setForeground(Color.WHITE);
                questionLabel.setFont(new Font("Arial", Font.PLAIN, 20));

                JTextField answerField = new JTextField(20);
                answerField.setEditable(false); // Make answerField non-editable
                JButton showAnswerButton = createStyledButton("Show Answer");
                JButton nextButton = createStyledButton("Next");

                JPanel buttonPanel = new JPanel();
                buttonPanel.setBackground(Color.DARK_GRAY);
                buttonPanel.add(showAnswerButton);
                buttonPanel.add(nextButton);

                JPanel centerPanel = new JPanel(new BorderLayout());
                centerPanel.setBackground(Color.DARK_GRAY);
                centerPanel.add(questionLabel, BorderLayout.NORTH); // Place questionLabel at the top
                centerPanel.add(answerField, BorderLayout.CENTER); // Place answerField in the center

                // Add components to the studyPanel
                studyPanel.add(backButtonPanelStudy, BorderLayout.NORTH); // Back button at the top
                studyPanel.add(centerPanel, BorderLayout.CENTER); // Center panel for question and answer field
                studyPanel.add(buttonPanel, BorderLayout.SOUTH); // Button panel at the bottom

                // Study deck logic
                ArrayList<Flashcard> deck = decks.get(deckName);
                Collections.shuffle(deck); // Shuffles the deck on every call
                ArrayList<Flashcard> incorrectAnswers = new ArrayList<>();
                int[] currentCardIndex = {0};

                if (!deck.isEmpty()) {
                    displayFlashcard(deck, currentCardIndex, questionLabel);
                    cardPanel.add(studyPanel, "StudyPanel");
                    CardLayout cl = (CardLayout) cardPanel.getLayout();
                    cl.show(cardPanel, "StudyPanel");
                } else {
                    JOptionPane.showMessageDialog(studyPanel, "There are no cards in this deck.");
                    return; // Exit the method if there are no cards
                }

                // Action listener to Show answer for each card
                showAnswerButton.addActionListener(showAnswerEvent -> {
                    if (currentCardIndex[0] < deck.size()) {
                        Flashcard currentCard = deck.get(currentCardIndex[0]);
                        answerField.setFont(new Font("Arial", Font.BOLD, 30));
                        answerField.setHorizontalAlignment(SwingConstants.CENTER);
                        answerField.setText(currentCard.getAnswer());
                        JButton incorrectAnswerButton = createStyledButton("Incorrect Answer");

                        buttonPanel.remove(showAnswerButton);
                        buttonPanel.add(incorrectAnswerButton);


                        // Action listener allows users to add incorrect cards to the end
                        incorrectAnswerButton.addActionListener(incorrectEvent -> {
                            incorrectAnswers.add(currentCard);
                            nextButton.doClick(); // Automatically move to the next card
                            buttonPanel.add(showAnswerButton);
                            buttonPanel.remove(incorrectAnswerButton);
                        });


                        buttonPanel.revalidate();
                        buttonPanel.repaint();
                    } else {
                        JOptionPane.showMessageDialog(studyPanel, "No more cards in the deck!");
                    }
                });

                // Next button allows users to go to next card
                nextButton.addActionListener(nextEvent -> {
                    currentCardIndex[0]++;
                    if (currentCardIndex[0] < deck.size()) {
                        displayFlashcard(deck, currentCardIndex, questionLabel);
                        answerField.setText(""); // Clear the answer field
                    } else if (!incorrectAnswers.isEmpty()) {
                        // Replace the current deck with the incorrect answers
                        deck.clear();
                        deck.addAll(incorrectAnswers);
                        incorrectAnswers.clear();
                        currentCardIndex[0] = 0;
                        buttonPanel.add(showAnswerButton);
                        JOptionPane.showMessageDialog(studyPanel, "Let's review the ones you got wrong.");
                        displayFlashcard(deck, currentCardIndex, questionLabel);
                        answerField.setText(""); // Clear the answer field
                        buttonPanel.revalidate();
                        buttonPanel.repaint();
                    } else {
                        JOptionPane.showMessageDialog(studyPanel,"You've completed the deck!");
                        // Navigate back to the deck list panel
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

        // Create a panel for the back button
        JPanel backButtonPanel = createBackButtonPanel("TitleScreen");
        JButton backButton = (JButton) backButtonPanel.getComponent(0); // Assume the back button is the first component
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

                JPanel modifyDeckPanelBackButton = createBackButtonPanel("TitleScreen");
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

                    JLabel questionLabel = new JLabel("Q: " + flashcard.getQuestion());
                    questionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                    JLabel answerLabel = new JLabel("A: " + flashcard.getAnswer());
                    answerLabel.setFont(new Font("Arial", Font.PLAIN, 16));

                    // Button to delete the flashcard
                    JButton deleteButton = createStyledButton("Delete");
                    deleteButton.addActionListener(deleteEvent -> {
                        deck.remove(flashcard);
                        flashcardPanel.remove(flashcardEntry);
                        flashcardPanel.revalidate();
                        flashcardPanel.repaint();
                    });

                    // Button to edit the flashcard
                    JButton editButton = createStyledButton("Edit");
                    editButton.addActionListener(editEvent -> {
                        JTextField newQuestionField = new JTextField(flashcard.getQuestion());
                        JTextField newAnswerField = new JTextField(flashcard.getAnswer());
                        JPanel editPanel = new JPanel(new GridLayout(0, 1));
                        editPanel.add(new JLabel("Edit Question:"));
                        editPanel.add(newQuestionField);
                        editPanel.add(new JLabel("Edit Answer:"));
                        editPanel.add(newAnswerField);

                        int result = JOptionPane.showConfirmDialog(null, editPanel, "Edit Flashcard", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                        if (result == JOptionPane.OK_OPTION) {
                            flashcard.setQuestion(newQuestionField.getText());
                            flashcard.setAnswer(newAnswerField.getText());
                            questionLabel.setText("Q: " + flashcard.getQuestion());
                            answerLabel.setText("A: " + flashcard.getAnswer());
                        }
                    });

                    flashcardEntry.add(questionLabel);
                    flashcardEntry.add(answerLabel);
                    flashcardEntry.add(editButton);
                    flashcardEntry.add(deleteButton);

                    flashcardPanel.add(flashcardEntry);
                }

                JScrollPane scrollPane = new JScrollPane(flashcardPanel);
                scrollPane.setPreferredSize(new Dimension(400, 400));

                modifyPanel.add(scrollPane, BorderLayout.CENTER);

                // Action to save changes when modifying
                JButton saveChangesButton = createStyledButton("Save Changes");
                saveChangesButton.addActionListener(saveEvent -> {
                    decks.put(deckName, deck);
                    DataManager.saveDecks(decks);
                    JOptionPane.showMessageDialog(modifyPanel, "Deck changes saved!");
                    CardLayout cl = (CardLayout) cardPanel.getLayout();
                    cl.show(cardPanel, "ModifyDeckScreen");
                });

                // Action listener to Edit deck name
                JButton editDeckNameButton = createStyledButton("Edit Deck Name");
                editDeckNameButton.addActionListener(editNameEvent -> {
                    String newDeckName = JOptionPane.showInputDialog(modifyPanel, "Enter a new name for the deck:", deckName);
                    if (newDeckName != null && !newDeckName.trim().isEmpty() && !newDeckName.equals(deckName)) {
                        if (!decks.containsKey(newDeckName)) {
                            ArrayList<Flashcard> tempDeck = decks.remove(deckName);
                            decks.put(newDeckName, tempDeck);
                            titleLabel.setText("Modify Deck: " + newDeckName);
                            JOptionPane.showMessageDialog(modifyPanel, "Deck name updated successfully!");
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

                    int result = JOptionPane.showConfirmDialog(modifyPanel, inputPanel, "Add Flashcard", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (result == JOptionPane.OK_OPTION) {
                        String question = questionField.getText();
                        String answer = answerField.getText();
                        if (!question.trim().isEmpty() && !answer.trim().isEmpty()) {
                            Flashcard newFlashcard = new Flashcard(question.trim(), answer.trim());
                            if (!deck.contains(newFlashcard)) {
                                deck.add(newFlashcard);
                                // Update display
                                JPanel flashcardEntry = new JPanel();
                                flashcardEntry.setBackground(Color.LIGHT_GRAY);
                                JLabel newQuestionLabel = new JLabel("Q: " + newFlashcard.getQuestion());
                                newQuestionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                                JLabel newAnswerLabel = new JLabel("A: " + newFlashcard.getAnswer());
                                newAnswerLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                                JButton newDeleteButton = createStyledButton("Delete");
                                // delete button action listener
                                newDeleteButton.addActionListener(deleteEvent -> {
                                    deck.remove(newFlashcard);
                                    flashcardPanel.remove(flashcardEntry);
                                    flashcardPanel.revalidate();
                                    flashcardPanel.repaint();
                                });
                                JButton newEditButton = createStyledButton("Edit");
                                // new edit action listener
                                newEditButton.addActionListener(editEvent -> {
                                    JTextField newQuestionField = new JTextField(newFlashcard.getQuestion());
                                    JTextField newAnswerField = new JTextField(newFlashcard.getAnswer());
                                    JPanel editPanel = new JPanel(new GridLayout(0, 1));
                                    editPanel.add(new JLabel("Edit Question:"));
                                    editPanel.add(newQuestionField);
                                    editPanel.add(new JLabel("Edit Answer:"));
                                    editPanel.add(newAnswerField);

                                    int editResult = JOptionPane.showConfirmDialog(null, editPanel, "Edit Flashcard", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                                    if (editResult == JOptionPane.OK_OPTION) {
                                        newFlashcard.setQuestion(newQuestionField.getText());
                                        newFlashcard.setAnswer(newAnswerField.getText());
                                        newQuestionLabel.setText("Q: " + newFlashcard.getQuestion());
                                        newAnswerLabel.setText("A: " + newFlashcard.getAnswer());
                                    }
                                });

                                flashcardEntry.add(newQuestionLabel);
                                flashcardEntry.add(newAnswerLabel);
                                flashcardEntry.add(newEditButton);
                                flashcardEntry.add(newDeleteButton);
                                flashcardPanel.add(flashcardEntry);
                                flashcardPanel.revalidate();
                                flashcardPanel.repaint();
                            } else {
                                JOptionPane.showMessageDialog(modifyPanel, "This flashcard already exists.");
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
                southPanel.add(saveChangesButton);

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
        JPanel backButtonPanel = createBackButtonPanel("TitleScreen");
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
        JPanel deckCheckboxPanel = new JPanel(new GridLayout(0, 1, 10, 10)); // 1 column, variable rows
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
    private static void displayFlashcard(ArrayList<Flashcard> deck, int[] currentCardIndex, JLabel questionLabel) {
        Flashcard currentCard = deck.get(currentCardIndex[0]);
        questionLabel.setText("Question: " + currentCard.getQuestion());
    }

}

