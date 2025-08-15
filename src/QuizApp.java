import java.io.*;
import java.util.*;

class Question {
    String question;
    List<String> options;
    int answerIndex;

    public Question(String question, List<String> options, int answerIndex) {
        this.question = question;
        this.options = options;
        this.answerIndex = answerIndex;
    }
}

public class QuizApp {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Load questions
        List<Question> questions = loadQuestions("questions.csv");
        if (questions.isEmpty()) {
            System.out.println("No questions found!");
            return;
        }

        // Ask how many questions user wants
        System.out.print("How many questions do you want to attempt? ");
        int numQuestions = sc.nextInt();
        sc.nextLine(); // consume newline

        if (numQuestions > questions.size()) {
            System.out.println("Only " + questions.size() + " questions available. Using all.");
            numQuestions = questions.size();
        }

        // Shuffle questions
        Collections.shuffle(questions);
        List<Question> selected = questions.subList(0, numQuestions);

        int score = 0;

        for (int i = 0; i < selected.size(); i++) {
            Question q = selected.get(i);

            // Shuffle options
            List<String> shuffledOptions = new ArrayList<>(q.options);
            Collections.shuffle(shuffledOptions);
            int newCorrectIndex = shuffledOptions.indexOf(q.options.get(q.answerIndex));

            System.out.println("\nQ" + (i + 1) + ": " + q.question);
            for (int j = 0; j < shuffledOptions.size(); j++) {
                System.out.println((j + 1) + ". " + shuffledOptions.get(j));
            }

            System.out.print("Your answer (1-4): ");
            int ans = sc.nextInt();

            if (ans - 1 == newCorrectIndex) {
                System.out.println("✅ Correct!");
                score++;
            } else {
                System.out.println("❌ Wrong! Correct answer: " + shuffledOptions.get(newCorrectIndex));
            }
        }

        System.out.println("\nQuiz Over! Your score: " + score + "/" + numQuestions);
    }

    private static List<Question> loadQuestions(String fileName) {
        List<Question> questions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";", 6); // 6 columns: question + 4 options + answerIndex
                if (parts.length < 6) continue;

                String questionText = parts[0];
                List<String> options = Arrays.asList(parts[1], parts[2], parts[3], parts[4]);
                int answerIndex = Integer.parseInt(parts[5]);
                questions.add(new Question(questionText, options, answerIndex));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return questions;
    }
}
