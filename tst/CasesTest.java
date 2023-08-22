import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import processtext.ProcessText;

class CasesTest {
    

    @Test
    void test1(){
        var text = "Opções de materiais de construções";
        var builtText = ProcessText.init(text).getBuiltText();
        System.out.println("builtText = " + builtText);

        var testCase = new String[]{
            "Opcao di material d construcao",
            "Opcoes materials de construcao",
            "Opções de mmmaaatterialsss . df construcao",
        };

        for (String string : testCase) {
            var s = ProcessText.init(string).getBuiltText();
            System.out.println("%s =  '%s' -> '%s'".formatted(
                builtText.contains(s),
                string,
                s
            ));
            assertTrue(builtText.contains(s));
        }
    
    }


    @Test
    void test2(){
        var text = """
            Nunca é demais lembrar o peso e o significado destes problemas,
            uma vez que a necessidade de renovação processual 
            representa uma abertura para a melhoria de todos 
            os recursos funcionais envolvidos.
        """;

        var builtText = ProcessText.init(text).getBuiltText();
        System.out.println("builtText = " + builtText);

        var testCase = new String[]{
            "nunca e  demais lembr",
            "abertura melhorar todos",
            "recursos funcional envolvido",
            "peso significado problema",
        };

        for (String string : testCase) {
            var s = ProcessText.init(string).getBuiltText();
            System.out.println("%s =  '%s' -> '%s'".formatted(
                builtText.contains(s),
                string,
                s
            ));
            assertTrue(builtText.contains(s));
        }
    
    }

    @Test
    void test3(){

        var text = """
            joão possuia R$ 5.000,00 e transferiu em dolar por .%*U$ 6,00.00
        """;

        var builtText = ProcessText.init(text).getBuiltText();
        System.out.println("builtText = " + builtText);

        var testCase = new String[]{
            "joao posuia 500000 e transferiu dolar 600",
            "joao poosssuuiiaaa  500000 transferi dolar 600"
        };

        for (String string : testCase) {
            var s = ProcessText.init(string).getBuiltText();
            System.out.println("%s =  '%s' -> '%s'".formatted(
                builtText.contains(s),
                string,
                s
            ));
            assertTrue(builtText.contains(s));
        }
    
    }



  


}
