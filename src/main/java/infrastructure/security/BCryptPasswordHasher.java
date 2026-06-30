package infrastructure.security;

import org.mindrot.jbcrypt.BCrypt;

public class BCryptPasswordHasher {
    public String hash(String plaintext) {
        return BCrypt.hashpw(plaintext, BCrypt.gensalt());
    }

    public boolean verify(String plaintext, String hash) {
        return BCrypt.checkpw(plaintext, hash);
    }
}
