package processtext;
import java.text.Normalizer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class ProcessText extends AbstractProcessText {

    private static final String [] DIRTY_CHARS_TO_BE_REMOVED = new String[]{
        "-", "\\", "\"", "=", "?", "'", "°", "`", "$", "&", "*", "%", ":", "/", "(", ")", "|", "}", "{", "!", ",", ";", "_", ".", "-", "[", "]", "<", ">", "^", "+"
    };

    private static final String [] ONLY_VOWELS_AND_NUMBERS = new String[]{
        "a","e","i","o","u","0","1","2","3","4","5","6","7","8","9"
    };

    private static final char [] ALLOWED_REPEAT_TWICE_WHEN_REMOVING_DUPLICATED_CHARS = new char[]{};

    private static final char [] ALLOWED_REPEAT_WHEN_REMOVING_DUPLICATED_CHARS = new char[]{
        '0','1','2','3','4','5','6','7','8','9'
    };


    private static final String KEYWORD_VOWELS = "@";

    private static final String [] STOP_WORDS_TO_BE_REMOVED =  new String[]{
	    "alguns","algumas","destes","destas","desta","these","some","those","about","deste","neles","nelas",
        "from","this","that","with","also","then","than","umas","para","nele","nela","for",
        "the","but","any","not","uns","uma","das","dos","pra","pro","por","aos","an","em","um","in","of","on",
        "at","by","it","to","de","da","do","du","di","ao","no"
    };

    private static final String [] SUFIX_TO_BE_REPLACED =  new String[]{
        "ds","ls","ts","ws","ys","ms","ns","rs",
        "ar","er","or","ir","ur",
        "ando","endo","indo","ondo","undo",
        "ado","edo","ido","odo","udo",
        "ados","edos","idos","odos","udos",
        "ing","ed",
        "as","es","is","os","us",
        "al","el","il","ol","ul",
        "als","els","ils","ols","uls",
        "a","e","i","o","u",
        "a" + KEYWORD_VOWELS,"e" + KEYWORD_VOWELS,"i" + KEYWORD_VOWELS,"o" + KEYWORD_VOWELS,"u" + KEYWORD_VOWELS,
        KEYWORD_VOWELS + "a",  KEYWORD_VOWELS + "e",  KEYWORD_VOWELS + "i", KEYWORD_VOWELS + "o", KEYWORD_VOWELS + "u",
        KEYWORD_VOWELS + "s", KEYWORD_VOWELS + "ls" , KEYWORD_VOWELS + "l", KEYWORD_VOWELS + "d",
        
    };

    private static final UnaryOperator<String> STEMMING_ON_SOUNDS_TO_BE_REPLACED = text ->
            text
                .replace("ha", "a")
                .replace("he", "e")
                .replace("hi", "i")
                .replace("ho", "o")
                .replace("hu", "u")
                .replace("sc", "s")
                .replace("cs", "s")
                .replace("xc", "x")
                .replace("cx", "x")
                .replace("xs", "x")
                .replace("sx", "x")
                .replace("ck", "k")
                .replace("th", "t");


    public static ProcessText init(){
        return new ProcessText();
    }

    public static ProcessText init(String input) {
        var p = init();
        p.input(input);
        return p;
    }

    @Override
    public UnaryOperator<String> inputClenning() {
        return t -> t;
    }

    @Override
    public UnaryOperator<String> normalizeAccent() {
       return t -> Normalizer.normalize(t.toLowerCase(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

    @Override
    public Function<String, String[]> tokenize() {
        return t -> t.replaceAll("[ \t  \n \r ]", " ").split(" ");
    }

    @Override
    public UnaryOperator<String> identifyKeyWords() {
        return t -> t;
    }

    @Override
    public UnaryOperator<String> removeDirtyChars() {
        return t -> {
            for (String dirtyChar : DIRTY_CHARS_TO_BE_REMOVED) {
                t = t.replace(dirtyChar, "");
            }
            return t;
        };
    }

    private static boolean arrayContains(char[] array, char value) {
        for (char each: array) if (each == value) return true;
        return false;
    }

    @Override
    public UnaryOperator<String> removeDuplicatedChars() {
        return t -> {
            final StringBuilder output = new StringBuilder();
            for (int i = 0; i < t.length(); i++) {
                char charCurrent = t.substring(i, i + 1).charAt(0);
                char lastChar = i > 0 ? t.substring(i - 1, i).charAt(0) : Character.MIN_VALUE;
                char beforeLastChar = i > 1 ? t.substring(i - 2, i - 1).charAt(0) : Character.MIN_VALUE;

                if( charCurrent != lastChar ||
                    arrayContains(ALLOWED_REPEAT_WHEN_REMOVING_DUPLICATED_CHARS, charCurrent) || 
                    (arrayContains(ALLOWED_REPEAT_TWICE_WHEN_REMOVING_DUPLICATED_CHARS, charCurrent) && charCurrent != beforeLastChar)){

                    output.append(charCurrent);
			    }
            }
            return output.toString();
        };
    }

    @Override
    public UnaryOperator<String> trimToken() {
        return String::trim;
    }

    @Override
    public UnaryOperator<String> removeStopWords() {
        return t -> {
            for (String stopWord : STOP_WORDS_TO_BE_REMOVED) {
                if( t.equals(stopWord) ) return "";
            }
            return t;
        };
    }

    @Override
    public UnaryOperator<String> stemmingOnVowels() {
        return t -> {
            var arrayVowels = new char[]{'a','e','i','o','u','y'};
            var tcharArray = t.toCharArray();
            final StringBuilder output = new StringBuilder();

            for (int i = 0; i < tcharArray.length; i++) {
                int countVowels = 0;
                while((i + countVowels) < tcharArray.length && arrayContains(arrayVowels,tcharArray[i + countVowels]) ){
                    countVowels++;
                }

                if(countVowels >= 2){
                    output.append(KEYWORD_VOWELS.charAt(0));
                    i = (i + countVowels) - 1;
                }else{
                    output.append(tcharArray[i]);
                }
            }
            return output.toString();
        };
    }

    @Override
    public UnaryOperator<String> stemmingOnSufix() {
         return t -> {
            t = t + " ";
            for (String sufix : SUFIX_TO_BE_REPLACED) {
                sufix = sufix + " ";
                t = t.replace(sufix, KEYWORD_VOWELS);
            }
            return t.trim();
        };
    }

    @Override
    public UnaryOperator<String> stemmingOnRules() {
        return t -> t;
    }

    @Override
    public UnaryOperator<String> lemmatizationOnSounds() {
        return STEMMING_ON_SOUNDS_TO_BE_REPLACED::apply;
    }

    @Override
    public UnaryOperator<String> lemmatizationOnRules() {
        return t -> t;
    }

    @Override
    public UnaryOperator<String> removeOneChar() {
        return t -> t.length() <= 1 ? "" : t;
    }

    @Override
    public UnaryOperator<String> removeNonVowalAndNumbersLessThen2Chars() {
        return t -> {
            if(t.length() <= 2){
                boolean containsAnyVowelOrNumber = false;
                for (String c : ONLY_VOWELS_AND_NUMBERS) {
                    if(t.contains(c)){
                        containsAnyVowelOrNumber = true;
                    }
                }
                if(!containsAnyVowelOrNumber){
                    t = "";
                }
            }
            return t;
        };
    }
    
 
}
