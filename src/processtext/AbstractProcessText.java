package processtext;


import java.util.function.Function;
import java.util.function.UnaryOperator;

public abstract class AbstractProcessText {

    private String input;

    public void input(String input){
        this.input = input;
    }

    public abstract UnaryOperator<String> inputClenning();
    public abstract UnaryOperator<String> normalizeAccent();
    public abstract Function<String,String[]> tokenize();

    public abstract UnaryOperator<String> trimToken();
    public abstract UnaryOperator<String> identifyKeyWords();
    public abstract UnaryOperator<String> removeDirtyChars();
    public abstract UnaryOperator<String> removeDuplicatedChars();
    public abstract UnaryOperator<String> removeStopWords();
    public abstract UnaryOperator<String> removeOneChar();
    public abstract UnaryOperator<String> stemmingOnVowels();
    public abstract UnaryOperator<String> stemmingOnSufix();
    public abstract UnaryOperator<String> stemmingOnRules();
    public abstract UnaryOperator<String> lemmatizationOnSounds();
    public abstract UnaryOperator<String> lemmatizationOnRules();
    public abstract  UnaryOperator<String> removeNonVowalAndNumbersLessThen2Chars();

    public String getBuiltText(){
        if(input == null) return null;
        
        var tokenArrays = inputClenning()
                            .andThen(normalizeAccent())
                            .andThen(tokenize())
                            .apply(input);
        var textBuilt = new StringBuilder();

        for (int i = 0; i < tokenArrays.length; i++) {
           textBuilt
            .append(
                trimToken()
                .andThen(removeDirtyChars())
                    .andThen(identifyKeyWords())
                    .andThen(removeDuplicatedChars())
                        .andThen(removeStopWords())
                        .andThen(removeOneChar())
                        .andThen(removeNonVowalAndNumbersLessThen2Chars())
                            .andThen(stemmingOnVowels())
                            .andThen(stemmingOnSufix())
                            .andThen(stemmingOnRules())
                    .andThen(removeDuplicatedChars())
                        .andThen(lemmatizationOnSounds())
                        .andThen(lemmatizationOnRules())
                    .apply(tokenArrays[i])
            );
        }
        return textBuilt.toString();
    }

}
