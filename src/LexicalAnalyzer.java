import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

class Token {
    enum TokenType {
        GPR, FPR, KEYWORD, ERROR
    }

    public TokenType tokenType;
    public String lexeme;

    public Token (String word){
        this.tokenType = identifyToken(word);
        this.lexeme = word;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public String getLexeme() {
        return lexeme;
    }

    // returns the corresponding token type identified for a string
    public static TokenType identifyToken(String word) {

        DFA dfa = new DFA();
        // Creating array of string length
        char[] ch = new char[word.length()];

        // Copy character by character into array
        for (int x = 0; x < word.length(); x++) {
            ch[x] = word.charAt(x);
        }

        String final_state = dfa.identifyState(word);

        if (final_state.equals("Q16") || final_state.equals("Q17"))
            return TokenType.KEYWORD;
        else if (final_state.equals("Q10") || final_state.equals("Q12") || final_state.equals("Q18"))
            return TokenType.GPR;
        else if (final_state.equals("Q14") || final_state.equals("Q15") || final_state.equals("Q19"))
            return TokenType.FPR;
        else
            return TokenType.ERROR;
    }
}

class LexicalAnalyzer {
    static ArrayList<Token> process(String sourceCode){
        // assume source code is MIPS Language, split first using whitespace delimiter
        String[] words = sourceCode.split(" ");     // still contains comma values
        ArrayList<Token> tokenList = new ArrayList<>();
        ArrayList<String> words2 = new ArrayList<>();      // still contains \n values
        String output = "";                                // output string to print

        // treat comma as a delimiter next
        for (String w: words){
            String[] temp = w.split(",");

            for (String w2: temp) {
                if(!w2.isEmpty()) {
                    words2.add(w2);
                }
            }
        }

        for (String w: words2){
            if(w.contains("\n")){
                String[] w2 = w.split("\n");   // lastly split using \n delimiter
                int count = 0;
                for (String w3: w2) {
                    if(!w3.isEmpty()) {
                        Token t = new Token(w3);
                        tokenList.add(t);
                        output = output.concat(t.getTokenType().toString() + " ");
                    }

                    count++;
                    if (count <= w2.length-1){           // insert new lines to match format with the input
                        output = output.concat("\n");
                    }
                }
            }
            else {
                if (!w.isEmpty()) {             // avoids stacked delimiters such as ",,,,,,," input
                    Token t = new Token(w);
                    tokenList.add(t);

                    output = output.concat(t.getTokenType().toString() + " ");
                }
            }
        }

        try {
            FileWriter fw = new FileWriter("output.txt");
            fw.write(output);                   // write the entire stored string to the file
            fw.flush();
            System.out.println(output);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tokenList;
    }

    public static void main(String[] args)throws Exception{
        ArrayList<Token> tokenList = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader("input.txt"));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            tokenList.addAll(process(sb.toString()));
        } finally {
            br.close();
        }
    }
}

