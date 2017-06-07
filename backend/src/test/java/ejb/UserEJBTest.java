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
    public void testCreateAUserWithEmpty_IdOrPassword(){
        //Arrange
        String userId = "    ";
        String password = "password";

        //Act 2 cases
        try {
            userEJB.createUser(userId,password,"name","middlename","lastname", false);
            fail();
        }catch (EJBException e){}

        userId="new";
        password="  ";
        boolean created1 = userEJB.createUser(userId,password,"name","middlename","lastname", false);

        //Assert
        assertFalse(created1);


    }

    @Test
    public void testCreateAUserWithEmpty_NameOrSurname(){
        //Arrange
        String user = "user";
        String password = "password";
        String name ="";
        String lastname ="";

        //Act 3 cases
        try {
            name="";
            lastname="valid";
            userEJB.createUser(user,password,name,"middlename",lastname, false);
            fail();
        }catch (EJBException e){}

        try {
            name="valid";
            lastname="";
            userEJB.createUser(user,password,name,"middlename",lastname, false);
            fail();
        }catch (EJBException e){}

        try {
            name=null;
            lastname="";
            userEJB.createUser(user,password,name,"middlename",lastname, false);
            fail();
        }catch (EJBException e){}
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
        assertEquals(userId, user.getUserId());
        assertEquals("a", user.getFirstName());
        assertEquals("b",user.getMiddleName());
        assertEquals("c", user.getLastName());
        assertEquals(false, user.getChef());
    }

    @Test
    public void testVerifyPassword(){
        //Arrange
        String user = "user";
        String userNotExistId = "not exist";
        String correct = "correct";
        String wrong = "wrong";
        createUser(user, correct);

        //Act
        boolean canLogin1 = userEJB.login(user, correct);
        boolean canLogin2 = userEJB.login(user, wrong);
        boolean canLogin3 = userEJB.login(userNotExistId, correct);
        boolean canLogin4 = userEJB.login(null, correct);

        //Assert
        assertTrue(canLogin1);
        assertFalse(canLogin2);
        assertFalse(canLogin3);
        assertFalse(canLogin4);
    }






}