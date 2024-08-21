import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataManager {
    private static final String FILE_NAME = "decks.json";

    // Save decks to a file
    public static void saveDecks(Map<String, ArrayList<Flashcard>> decks) {
        try (Writer writer = new FileWriter(FILE_NAME)) {
            Gson gson = new Gson();
            gson.toJson(decks, writer);
            System.out.println("Decks saved successfully: " + decks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load decks from a file
    public static Map<String, ArrayList<Flashcard>> loadDecks() {
        Map<String, ArrayList<Flashcard>> decks = new HashMap<>();
        try (Reader reader = new FileReader(FILE_NAME)) {
            Gson gson = new Gson();
            Type deckMapType = new TypeToken<Map<String, ArrayList<Flashcard>>>(){}.getType();
            decks = gson.fromJson(reader, deckMapType);
        } catch (FileNotFoundException e) {
            // File does not exist, initialize with an empty map
            System.out.println("File not found, initializing with an empty deck list.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return decks;
    }

}
