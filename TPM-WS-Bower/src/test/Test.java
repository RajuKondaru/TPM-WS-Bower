package test;
public class Test {
    public static void main(String[] args) {
        String text = " View with id: 'searchButton' is ";
        String trimmed = text
            .replaceAll("']*", "\"") 
           ;
        System.out.println(trimmed);
    }
}