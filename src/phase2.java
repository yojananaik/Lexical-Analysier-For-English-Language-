import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.LogManager;
import java.io.FileInputStream;
import java.io.IOException;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class phase2 {
    public static void main(String[] args) {
        // Configure Java logging to suppress INFO messages
        configureLogging();
    
        try (Scanner scanner = new Scanner(System.in)) {
            String text;
    
            while (true) {
                System.out.print("Enter the text: ");
                text = scanner.nextLine();
    
                if (containsCommaFollowedBySpace(text) || containsSpaceBeforeExclamationMark(text)) {
                    System.out.println("Error: Input contains a comma followed by a space or a space before an exclamation mark. Please try again.");
                    continue;
                }
    
                StanfordCoreNLP pipeline = createPipeline();
    
                Annotation document = new Annotation(text);
                pipeline.annotate(document);
    
                ArrayList<Token> tokens = extractTokens(document);
    
                if (tokens.isEmpty()) {
                    System.out.println("No tokens found for this text.");
                    return;
                }
    
                System.out.println("Nouns:");
                displayTokensOfType(tokens, "NN", "NNS", "NNP", "NNPS");
                System.out.println("-------------------------------------------------");
    
                System.out.println("Verbs:");
                displayTokensOfType(tokens, "VB", "VBD", "VBG", "VBN", "VBP", "VBZ");
                System.out.println("-------------------------------------------------");
    
                System.out.println("Pronouns:");
                displayTokensOfType(tokens, "PRP", "PRP$", "WP", "WP$");
                System.out.println("-------------------------------------------------");
    
                System.out.println("Adjectives:");
                displayTokensOfType(tokens, "JJ", "JJR", "JJS");
                System.out.println("-------------------------------------------------");
    
                System.out.println("Adverbs:");
                displayTokensOfType(tokens, "RB", "RBR", "RBS");
                System.out.println("-------------------------------------------------");
    
                System.out.println("Numbers:");
                displayTokensOfType(tokens, "CD");
                System.out.println("-------------------------------------------------");
    
                System.out.println("Punctuation:");
                displayPunctuation(tokens);
                System.out.println("-------------------------------------------------");
    
                System.out.println("Operators:");
                displayOperators(tokens);
                System.out.println("-------------------------------------------------");
    
                System.out.println("Special Tokens:");
                displaySpecialTokens(tokens);
                System.out.println("-------------------------------------------------");
    
                break;
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
    
    static StanfordCoreNLP createPipeline() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos");
        return new StanfordCoreNLP(props);
    }

    static ArrayList<Token> extractTokens(Annotation document) {
        ArrayList<Token> tokens = new ArrayList<>();

        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        for (CoreMap sentence : sentences) {
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);

                tokens.add(new Token(pos, word));
            }
        }
        return tokens;
    }

    static void displayTokensOfType(ArrayList<Token> tokens, String... posTags) {
        int count = 0;
        ArrayList<String> tokenList = new ArrayList<>();

        for (Token token : tokens) {
            for (String posTag : posTags) {
                if (token.type.equals(posTag)) {
                    tokenList.add(token.value);
                    count++;
                    break;
                }
            }
        }

        System.out.println("Token count: " + count);
        System.out.println("Tokens: " + tokenList);
    }

    static void displayPunctuation(ArrayList<Token> tokens) {
        int count = 0;
        ArrayList<String> punctuationList = new ArrayList<>();

        for (Token token : tokens) {
            if (isPunctuation(token.value)) {
                punctuationList.add(token.value);
                count++;
            }
        }

        System.out.println("Punctuation count: " + count);
        System.out.println("Punctuation tokens: " + punctuationList);
    }

    static void displayOperators(ArrayList<Token> tokens) {
        int count = 0;
        ArrayList<String> operatorList = new ArrayList<>();

        for (Token token : tokens) {
            // Modify the regular expression as needed to match operators
            if (isOperator(token.value)) {
                operatorList.add(token.value);
                count++;
            }
        }

        System.out.println("Operator count: " + count);
        System.out.println("Operator tokens: " + operatorList);
    }

    static void displaySpecialTokens(ArrayList<Token> tokens) {
        int count = 0;
        ArrayList<String> specialTokenList = new ArrayList<>();

        for (Token token : tokens) {
            // Modify the regular expression as needed to match special tokens
            if (isSpecialCharacter(token.value)) {
                specialTokenList.add(token.value);
                count++;
            }
        }

        System.out.println("Special Token count: " + count);
        System.out.println("Special Token tokens: " + specialTokenList);
    }

    static boolean isPunctuation(String word) {
        // Define a regular expression to match punctuation
        String punctuationRegex = "^[.,!?;]$";
        return word.matches(punctuationRegex);
    }

    static boolean isSpecialCharacter(String word) {
        // Define a regular expression to match special characters
        String specialCharacterRegex = "^[@#$%&*()]$";
        return word.matches(specialCharacterRegex);
    }

    static boolean isOperator(String word) 
    {
        // Define a regular expression to match operators
        String operatorRegex = "^(==|!=|<|>|<=|>=|&&|\\|\\||\\^|\\&|\\||\\^|\\~)$";
        return word.matches(operatorRegex);
    }
    

    static void configureLogging() {
        try {
            LogManager.getLogManager().readConfiguration(
                    new FileInputStream("D:\\Study\\Sem V\\Lab\\CIE_3_FINAL\\src\\logging.properties"));
        } catch (IOException e) {
            System.err.println("Could not configure logging: " + e.getMessage());
        }
    }
}

class Token {
    String type;
    String value;

    public Token(String type, String value) {
        this.type = type;
        this.value = value;
    }
}
