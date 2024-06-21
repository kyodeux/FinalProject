package Model.Components;

public class InputChecker {
    public static String checkDisplayName(String user){
        if (user.length() > 25){
            return "*Muy largo...";
        }
        
        if (user.isBlank()){
            return "*Esto esta en blanco";
        }
        
        char[] chars = user.toCharArray();
        for (int i = 0; i < chars.length; i ++){
            char c = chars[i];
            if (!Character.isAlphabetic(c) && 
                    !Character.isWhitespace(c) &&
                    !Character.isDigit(c)){
                return "*No caracteres especiales";
            }
        }
        
        return "";
    }
    public static String checkUsername(String user){
        if (user.length() > 25){
            return "*Muy largo...";
        }
        
        char[] chars = user.toCharArray();
        for (int i = 0; i < chars.length; i ++){
            char c = chars[i];
            if (Character.isWhitespace(c)){
                return "*No espacios";
            }
            if (!Character.isAlphabetic(c) &&
                    !Character.isDigit(c)){
                return "*No caracteres especiales";
            }
        }
        
        return "";
    }
    
    public static boolean checkPassCode(String passCode){
        if (passCode.length() > 6){
            return false;
        }
        char[] chars = passCode.toCharArray();
        for (int i = 0; i < chars.length; i ++){
            char c = chars[i];
            
            if (!Character.isDigit(c)){
                return false;
            }
        }
        
        return true;
    }
}
