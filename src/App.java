import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.Map;

public class App extends JFrame {
    private JTextArea inputTextArea;
    private JTextArea outputTextArea;
    private JButton runButton;
    private JButton clearButton;

    public App() {
        setTitle("Lexical Analyzer for English Language");
        setSize(1550, 820);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Text Input Box
        inputTextArea = new JTextArea(20, 40);
        inputTextArea.setLineWrap(true);
        inputTextArea.setWrapStyleWord(true);
        inputTextArea.setFont(new Font("Monospaced", Font.BOLD, 24));

        // Output Text Area
        outputTextArea = new JTextArea(20, 40);
        outputTextArea.setEditable(false);
        outputTextArea.setFont(new Font("Monospaced", Font.BOLD, 18));

        // Input Panel
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(new JScrollPane(inputTextArea), BorderLayout.CENTER);

        // Output Panel
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.add(new JScrollPane(outputTextArea), BorderLayout.CENTER);

        // Buttons Panel
        runButton = new JButton("Run");
        clearButton = new JButton("Clear");
        runButton.setFont(new Font("Arial", Font.BOLD, 20));
        clearButton.setFont(new Font("Arial", Font.BOLD, 20));

        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputText = inputTextArea.getText();
                displayTokens(inputText, outputTextArea);
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputTextArea.setText("");
                outputTextArea.setText("");
            }
        });

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(runButton);
        buttonsPanel.add(clearButton);

        // Split Pane for Input and Output
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, inputPanel, outputPanel);
        splitPane.setResizeWeight(0.5);

        // Adding Input/Output and Buttons to the Main Frame
        add(splitPane, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                App gui = new App();
                gui.setVisible(true);
            }
        });
    }

    static void displayTokens(String text, JTextArea outputTextArea) {
        // Check for input conditions before proceeding
        if (containsCommaFollowedBySpace(text) || containsSpaceBeforeExclamationMark(text)) {
            outputTextArea.setText("Error: Input contains a comma followed by a space or a space before an exclamation mark. Please follow these conditions:\n"
                    + "- No comma followed by a space\n"
                    + "- No space before an exclamation mark\n\n");
            return;
        }

        ArrayList<Token> tokens = tokenize(text);
        outputTextArea.setText("");

        Map<String, ArrayList<String>> tokenCategories = new HashMap<>();

        Map<String, String> posFullForms = new HashMap<>();
        posFullForms.put("NN", "Noun");
        posFullForms.put("NNP", "Proper Noun");
        posFullForms.put("NNS", "Noun (Plural)");
        posFullForms.put("PRP$", "Possessive Pronoun");
        posFullForms.put("VB", "Verb (Base Form)");
        posFullForms.put("VBP", "Verb (Non-3rd Person Singular)");
        posFullForms.put("VBZ", "Verb (3rd Person Singular)");
        posFullForms.put("VBD", "Verb (Past Tense)");
        posFullForms.put("VBG", "Verb (Gerund/Present Participle)");
        posFullForms.put("VBN", "Verb (Past Participle)");
        posFullForms.put("JJ", "Adjective");
        posFullForms.put("RB", "Adverb");
        posFullForms.put("CD", "Number");
        posFullForms.put("SYM", "Symbol");
        posFullForms.put("IN", "Preposition");
        posFullForms.put("PUNC", "Punctuation");
        posFullForms.put("SPECIAL", "Special Character");
        posFullForms.put("Operator", "Operator");
        posFullForms.put("Error", "Error (Invalid Punctuation Mark)");

        for (Token token : tokens) {
            if (posFullForms.containsKey(token.type)) {
                if (!tokenCategories.containsKey(token.type)) {
                    tokenCategories.put(token.type, new ArrayList<>());
                }
                tokenCategories.get(token.type).add(token.value);
            }
        }

        for (Map.Entry<String, ArrayList<String>> entry : tokenCategories.entrySet()) {
            String type = entry.getKey();
            String fullForm = posFullForms.get(type);
            ArrayList<String> tokenList = entry.getValue();
            int count = tokenList.size();

            if (count > 0) {
                outputTextArea.append(fullForm + " token count: " + count + "\n");
                outputTextArea.append(fullForm + " tokens: " + tokenList.toString() + "\n\n");
            }
        }
    }

    static boolean containsCommaFollowedBySpace(String text) {
        // Check if the input text contains a comma followed by a space
        return text.contains(", ");
    }

    static boolean containsSpaceBeforeExclamationMark(String text) {
        // Check if the input text contains a space before an exclamation mark
        return text.contains(" !");
    }

    static ArrayList<Token> tokenize(String text) {
        StanfordCoreNLP pipeline = createPipeline();
        Annotation document = new Annotation(text);
        pipeline.annotate(document);
        ArrayList<Token> tokens = extractTokens(document);
        return tokens;
    }

    static StanfordCoreNLP createPipeline() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos");
        return new StanfordCoreNLP(props);
    }

    static ArrayList<Token> extractTokens(Annotation document) {
        ArrayList<Token> tokens = new ArrayList<>();

        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        for (CoreMap sentence : sentences) {
            List<CoreLabel> sentenceTokens = sentence.get(CoreAnnotations.TokensAnnotation.class);

            for (int i = 0; i < sentenceTokens.size(); i++) {
                CoreLabel token = sentenceTokens.get(i);
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);

                if (isPunctuation(word)) {
                    // Check if the previous token (if it exists) is a space
                    boolean isInvalidPunctuation = false;
                    if (i > 0) {
                        CoreLabel prevToken = sentenceTokens.get(i - 1);
                        String prevWord = prevToken.get(CoreAnnotations.TextAnnotation.class);
                        if (prevWord.equals(" ")) {
                            isInvalidPunctuation = true;
                        }
                    }

                    if (isInvalidPunctuation) {
                        tokens.add(new Token("Error", word + " (Invalid Punctuation Mark)"));
                    } else if (isOperator(word)) {
                        tokens.add(new Token("Operator", word));
                    } else if (isSpecialCharacter(word)) {
                        tokens.add(new Token("SPECIAL", word));
                    } else {
                        tokens.add(new Token(pos, word));
                    }
                } else {
                    tokens.add(new Token(pos, word));
                }
            }
        }
        return tokens;
    }

    static boolean isPunctuation(String word) {
        // Define a regular expression to match punctuation
        String punctuationRegex = "^[.,!?;]$";
        return word.matches(punctuationRegex);
    }

    static boolean isOperator(String word) {
        // Define a regular expression to match operators
        String operatorRegex = "^(==|!=|<|>|<=|>=|&&|\\|\\||\\^|\\&|\\||\\^|\\~)$";
        return word.matches(operatorRegex);
    }

    static boolean isSpecialCharacter(String word) {
        String specialCharacterRegex = "^[@#$%&*()]$";
        return word.matches(specialCharacterRegex);
    }
}

class Token {
    String type;
    String value;

    public Token(String type, String value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return "[" + type + ": " + value + "]";
    }
}
