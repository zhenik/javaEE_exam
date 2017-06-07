package ejb;

import entity.User;
import org.apache.commons.codec.digest.DigestUtils;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Iterator;

@Stateless
public class UserEJB implements Serializable{
    @PersistenceContext
    private EntityManager em;

    public UserEJB(){}

    public boolean createUser(String userId, String password, String firstName, String middleName, String lastName, Boolean chef) {
        if (userId == null || userId.isEmpty() || password == null || password.isEmpty()) {
            return false;
        }

        User user = getUser(userId);
        if (user != null) {
            //a user with same id already exists
            return false;
        }

        user = new User();
        user.setUserId(userId);

        //create a "strong" random string of at least 128 bits, needed for the "salt"
        String salt = getSalt();
        user.setSalt(salt);

        String hash = computeHash(password, salt);
        user.setHash(hash);

        user.setFirstName(firstName);
        user.setMiddleName(middleName);
        user.setLastName(lastName);
        user.setChef(chef);

        em.persist(user);

        return true;
    }

    public boolean login(String userId, String password) {
        if (userId == null || userId.isEmpty() || password == null || password.isEmpty()) {
            return false;
        }

        User userDetails = getUser(userId);
        if (userDetails == null) {
            return false;
        }

        String hash = computeHash(password, userDetails.getSalt());

        boolean isOK = hash.equals(userDetails.getHash());
        return isOK;
    }


    public User getUser(String userId){
        return em.find(User.class, userId);
    }


    //------------------------------------------------------------------------


    @NotNull
    protected String computeHash(String password, String salt){
        String combined = password + salt;
        String hash = DigestUtils.sha256Hex(combined);
        return hash;
    }

    @NotNull
    protected String getSalt(){
        SecureRandom random = new SecureRandom();
        int bitsPerChar = 5;
        int twoPowerOfBits = 32; // 2^5
        int n = 26;
        assert n * bitsPerChar >= 128;

        String salt = new BigInteger(n * bitsPerChar, random).toString(twoPowerOfBits);
        return salt;
    }
}
