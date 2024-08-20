import java.util.Objects;

public class Flashcard {
    private String question;
    private String answer;

    public Flashcard(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void displayQuestion() {
        System.out.println(question);
    }

    public void displayAnswer() {
        System.out.println(answer);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Flashcard flashcard = (Flashcard) obj;
        return question.equals(flashcard.question) && answer.equals(flashcard.answer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(question, answer);
    }
}
