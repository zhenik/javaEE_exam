package ejb;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class UserEJBTest extends EjbTestBase {

    @Test
    public void testCanCreateAUser(){

        String user = "user";
        String password = "password";

        boolean created = createUser(user,password);
        assertTrue(created);
    }
}