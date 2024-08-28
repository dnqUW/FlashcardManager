import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FlashcardManager {
    // Data structure to store flashcard decks
    private static Map<String, ArrayList<Flashcard>> decks = new HashMap<>();
    private static JPanel cardPanel = new JPanel(new CardLayout()); // Panel for different screens

    public static void main(String[] args) {

        try {
            // Set the look-and-feel to the cross-platform look-and-feel
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Creating the frame
        JFrame frame = new JFrame("Flashcard Manager");
        frame.setLayout(new BorderLayout());
        frame.setBounds(400, 400, 1200, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon icon = new ImageIcon("./icons/Flashcard Manager.png");
        frame.setIconImage(icon.getImage());
        if (icon.getImageLoadStatus() != MediaTracker.COMPLETE) {
            System.err.println("Failed to load image icon.");
        }

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

        // Back button functionality
        JPanel backButtonPanel = createBackButtonPanel("TitleScreen");
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setBackground(Color.DARK_GRAY);
        northPanel.add(backButtonPanel, BorderLayout.WEST);

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
        gbc.fill = GridBagConstraints.HORIZONTAL; // Ensure the text field spans full width
        inputPanel.add(questionField, gbc);

        gbc.gridy = 5;
        inputPanel.add(answerLabel, gbc);

        gbc.gridy = 6;
        inputPanel.add(answerField, gbc);

        gbc.gridy = 7;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(addFlashcardButton, gbc);

        // Creates a JTextArea to display added flashcards
        JTextArea flashcardDisplay = new JTextArea(10, 40);
        flashcardDisplay.setEditable(false);
        flashcardDisplay.setBackground(Color.LIGHT_GRAY);
        flashcardDisplay.setForeground(Color.BLACK);
        JScrollPane scrollPane = new JScrollPane(flashcardDisplay);
        scrollPane.setPreferredSize(new Dimension(400, 400)); // Set preferred size for the scroll pane

        // Adding components to the panel
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
                Flashcard flashcard = new Flashcard(question.trim(), answer.trim());
                if(!deck.contains(flashcard)) {
                    deck.add(flashcard);
                } else {
                    response = JOptionPane.showConfirmDialog(panel, "This flashcard already exists. " +
                            "Would you like to add it again?");
                }


                // Update JTextArea to display the added flashcard
                if(response == JOptionPane.YES_OPTION) {
                    flashcardDisplay.append("Question: " + flashcard.getQuestion()+ "\nAnswer: " +
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

                // Add deck to map
                decks.put(deckName, deck);
                System.out.println("Decks map before saving: " + decks); // Debugging statement

                // Save decks to file
                DataManager.saveDecks(decks);
                System.out.println("Decks saved to file."); // Debugging statement

                // Notify user
                JOptionPane.showMessageDialog(panel, "Deck Saved!");

                // Clear the text fields and disable components
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

                // Refresh the study panel to show the new deck
                updateDeckList(); // Ensure this method only updates the panel without changing layout
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

                JButton backButton = createStyledButton("Back");
                backButton.addActionListener(backEvent -> {
                    CardLayout cl = (CardLayout) cardPanel.getLayout();
                    cl.show(cardPanel, "TitleScreen"); // Adjust "TitleScreen" to your desired screen
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

                showAnswerButton.addActionListener(showAnswerEvent -> {
                    if (currentCardIndex[0] < deck.size()) {
                        Flashcard currentCard = deck.get(currentCardIndex[0]);
                        answerField.setFont(new Font("Arial", Font.BOLD, 30));
                        answerField.setHorizontalAlignment(SwingConstants.CENTER);
                        answerField.setText(currentCard.getAnswer());
                    } else {
                        JOptionPane.showMessageDialog(studyPanel, "No more cards in the deck!");
                    }
                });

                nextButton.addActionListener(nextEvent -> {
                    currentCardIndex[0]++;
                    if (currentCardIndex[0] < deck.size()) {
                        displayFlashcard(deck, currentCardIndex, questionLabel);
                        answerField.setText(""); // Clear the answer field
                    } else {
                        JOptionPane.showMessageDialog(studyPanel, "You have completed the deck!");
                        CardLayout cl = (CardLayout) cardPanel.getLayout();
                        cl.show(cardPanel, "TitleScreen"); // Adjust "TitleScreen" to your desired screen
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
                if (deck != null && !deck.isEmpty()) {
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
                } else {
                    JOptionPane.showMessageDialog(cardPanel, "There are no cards in this deck.");
                }
            });
            deckButtonPanel.add(deckButton);
        }

        JScrollPane deckScrollPane = new JScrollPane(deckButtonPanel);
        deckScrollPane.setPreferredSize(new Dimension(400, 400));

        centerPanel.add(deckScrollPane, BorderLayout.CENTER);

        panel.add(centerPanel, BorderLayout.CENTER);

        return panel;
    }

    // Returns a JPanel for deleting a deck
    private static JPanel createDeletePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.DARK_GRAY);

        // Create and add the back button panel at the top
        JPanel backButtonPanel = createBackButtonPanel("TitleScreen");
        panel.add(backButtonPanel, BorderLayout.NORTH);

        // Create a panel for the label with fixed height
        JPanel labelPanel = new JPanel();
        labelPanel.setBackground(Color.DARK_GRAY);
        labelPanel.setPreferredSize(new Dimension(400, 50)); // Adjust height as needed

        JLabel label = new JLabel("Select a deck to delete.");
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 30));
        label.setHorizontalAlignment(SwingConstants.CENTER);

        labelPanel.add(label);

        // Create a panel for the deck buttons using GridLayout
        JPanel deckButtonPanel = new JPanel(new GridLayout(0, 1, 10, 10)); // 1 column, variable rows
        deckButtonPanel.setBackground(Color.DARK_GRAY);

        // Add buttons to the deckButtonPanel
        for (String deckName : decks.keySet()) {
            JButton deckButton = createStyledButton(deckName);
            deckButton.addActionListener(e -> {
                // Handle deck deletion here
                decks.remove(deckName);
                DataManager.saveDecks(decks);
                updateDeleteDeckButtons(deckButtonPanel); // Refresh buttons after deletion
            });
            deckButtonPanel.add(deckButton);
        }

        // Wrap the deckButtonPanel in a scroll pane
        JScrollPane scrollPane = new JScrollPane(deckButtonPanel);
        scrollPane.setPreferredSize(new Dimension(400, 400));

        // Add the label panel and scroll pane to the centerPanel
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.DARK_GRAY);
        centerPanel.add(labelPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // Add the centerPanel to the main panel
        panel.add(centerPanel, BorderLayout.CENTER);

        return panel;
    }



    private static void updateDeleteDeckButtons(JPanel centerPanel) {
        centerPanel.removeAll(); // Clear existing buttons

        for (String deckName : decks.keySet()) {
            JButton deckButton = createStyledButton(deckName);
            deckButton.addActionListener(e -> {
                int response = JOptionPane.showConfirmDialog(centerPanel, "Are you sure you want to delete the deck \"" +
                        deckName + "\"?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

                if (response == JOptionPane.YES_OPTION) {
                    decks.remove(deckName);
                    DataManager.saveDecks(decks);
                    JOptionPane.showMessageDialog(centerPanel, "Deck \"" + deckName + "\" deleted!");
                    updateDeleteDeckButtons(centerPanel); // Refresh deck list
                    centerPanel.revalidate();
                    centerPanel.repaint();
                }
            });

            centerPanel.add(deckButton);
        }

        centerPanel.revalidate();
        centerPanel.repaint();
    }


    // Updates the deck list in the Study and Modify panels
    private static void updateDeckList() {
        try {
//            // Get the StudyDeckScreen panel
//            JPanel studyPanel = (JPanel) cardPanel.getComponent(2);
//            System.out.println("StudyPanel: " + studyPanel);
//
//            // Check if studyPanel has the expected component
//            if (studyPanel.getComponentCount() > 0) {
//                JPanel studyJPanel = (JPanel) studyPanel.getComponent(0);
//                System.out.println("StudyJPanel: " + studyJPanel);
//
//                // Check if studyJPanel has the expected component
//                if (studyJPanel.getComponentCount() > 1) {
//                    JScrollPane scrollPane = (JScrollPane) studyJPanel.getComponent(1);
//                    System.out.println("ScrollPane: " + scrollPane);
//
//                    // Get the panel within the scroll pane
//                    JPanel deckListPanel = (JPanel) scrollPane.getViewport().getView();
//                    System.out.println("DeckListPanel: " + deckListPanel);
//
//                    // Ensure deckListPanel uses GridLayout
//                    if (deckListPanel.getLayout() instanceof GridLayout) {
//                        // Clear existing components
//                        deckListPanel.removeAll();
//
//                        // Add buttons for each deck
//                        for (String deckName : decks.keySet()) {
//                            JButton deckButton = new JButton(deckName);
//                            deckButton.setForeground(Color.WHITE);
//                            deckButton.setBackground(Color.BLACK);
//                            deckButton.setFont(new Font("Arial", Font.BOLD, 30));
//                            deckButton.setPreferredSize(new Dimension(300, 50));
//                            deckButton.addActionListener(e -> studyDeck(deckName)); // Handle deck selection
//
//                            deckListPanel.add(deckButton);
//                        }
//
//                        // Refresh the panel to show new buttons
//                        deckListPanel.revalidate();
//                        deckListPanel.repaint();
//                    } else {
//                        System.err.println("deckListPanel does not use GridLayout.");
//                    }
//                } else {
//                    System.err.println("studyJPanel does not have enough components.");
//                }
//            } else {
//                System.err.println("studyPanel does not have enough components.");
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void studyDeck(String deckName) {
        ArrayList<Flashcard> deck = decks.get(deckName);
        if (deck == null) {
            JOptionPane.showMessageDialog(null, "Deck not found: " + deckName);
        } else {
            // Implement the logic to display the flashcards for the selected deck
            JPanel panel = new JPanel();
            for (Flashcard card : deck) {
                panel.add(new JLabel(card.getQuestion() + ": " + card.getAnswer()));
            }

            // Add the panel to cardPanel with a unique name or identifier
            cardPanel.add(panel, "StudyDeck_" + deckName);

            // Show the study deck screen
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, "StudyDeck_" + deckName);
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

    private static JButton createStyledButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setForeground(Color.BLACK);
        button.setBackground(Color.LIGHT_GRAY);
        button.setFont(new Font("Arial", Font.PLAIN, 20));
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        button.setPreferredSize(new Dimension(200, 50));
        button.setFocusPainted(false);
        button.addActionListener(actionListener);
        addButtonHoverEffect(button);
        return button;
    }

    private static void addButtonHoverEffect(JButton button) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.DARK_GRAY); // Change to a different color on hover
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.LIGHT_GRAY); // Revert to the original color when not hovering
            }
        });
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
