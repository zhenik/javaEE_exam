package ejb;


import entity.User;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import util.DeleterEJB;

import javax.ejb.EJB;

public abstract class EjbTestBase {

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, "entity","ejb", "validation")
                .addClass(DeleterEJB.class)
                .addPackages(true, "org.apache.commons.codec")
                .addAsResource("META-INF/persistence.xml");
    }

    @EJB
    protected UserEJB userEJB;

    @EJB
    private DeleterEJB deleterEJB;


    @Before
    @After
    public void emptyDatabase(){
        deleterEJB.deleteEntities(User.class);// doesn't work due to @ElementCollection
    }

    protected boolean createUser(String user){
        return userEJB.createUser(user,"foo","a","b","c");
    }

    protected boolean createUser(String user, String password){
        return userEJB.createUser(user,password,"a","b","c");
    }

}
