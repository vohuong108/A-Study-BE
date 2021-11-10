package astudy.utils;

public class HandleToken {
    public static String getUsernameFromToken(String token) {
        return token.substring("Bearer access_token_".length());
    }
}
