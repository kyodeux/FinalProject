package Model.Components;

public class UsernameChecker {
    public static String check(String user){
        if (user.length() > 25){
            return "*Muy largo...";
        }
        
        if (user.isBlank()){
            return "*Esto esta en blanco";
        }
        
        char[] chars = user.toCharArray();
        for (int i = 0; i < chars.length; i ++){
            char c = chars[i];
            if (!Character.isAlphabetic(c) && !Character.isWhitespace(c)){
                return "*No caracteres especiales";
            }
        }
        
        return "";
    }
}
