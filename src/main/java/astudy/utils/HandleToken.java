package astudy.utils;

public class HandleToken {
    public static String getUsernameFromToken(String token) {
        if (token.length() <= 11 ) return null;
        return token.substring("Bearer access_token_".length());
    }
}
