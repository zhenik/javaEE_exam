package ejb;

import entity.User;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJBException;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class UserEJBTest extends EjbTestBase {

    @Test
    public void testCanCreateAUser_notChef(){
        //Arrange
        String user = "user";
        String password = "password";

        //Act
        boolean created = createUser(user,password);

        //Assert
        assertTrue(created);
    }

    @Test
    public void testCreateAUserWithWrongId(){
        //Arrange
        String user = "user!!!";
        String password = "password";

        //Act
        try {
            createUser(user,password);
            fail();
        }catch (EJBException e){}
    }

    @Test
    public void testCreateAUserWithEmpty(){
        //Arrange
        String user = "    ";
        String password = "password";

        //Act
        try {
            createUser(user,password);
            fail();
        }catch (EJBException e){}
    }

    @Test
    public void testNoTwoUsersWithSameId(){
        //Arrange
        String user = "user";

        //Act
        boolean created1 = createUser(user,"a");
        boolean created2 = createUser(user,"b");

        //Assert
        assertTrue(created1);
        assertFalse(created2);
    }

    @Test
    public void testGetUser(){
        //Arrange
        String userId = "user";
        boolean created1 = createUser(userId,"a");
        assertTrue(created1);

        //Act
        User user = userEJB.getUser(userId);

        //Assert
        assertEquals("a", user.getFirstName());
        assertEquals("b",user.getMiddleName());
        assertEquals("c", user.getLastName());
        assertEquals(false, user.getChef());
    }

    @Test
    public void testSamePassword_DifferentHashAndSalt(){
        //Arrange
        String password = "password";
        String first = "first";
        String second = "second";

        createUser(first,password);
        createUser(second,password); //same password

        User f = userEJB.getUser(first);
        User s = userEJB.getUser(second);

        assertNotEquals(f.getHash(), s.getHash());
        assertNotEquals(f.getSalt(), s.getSalt());
    }



}