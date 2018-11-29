package ca.sfu.greenfoodchallenge.ui;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import ca.sfu.greenfoodchallenge.database.User;

import static org.junit.Assert.*;
/*
Tests the User class
 */
public class UserTest {

    private User user;
    private String userName;
    private String name;
    private String URL;
    private String lastI;
    private String email;
    private double pledge;
    private String municipality;
    @Before
    public void setUp() throws Exception {
        userName = "John D";
        name = "John";
        lastI = "D";
        municipality = "Abbotsford";
        URL = "android.resource://ca.sfu.greenfoodchallenge.ui/R.mipmap.ic_app_round";
        pledge = 45000;
        email = "j.d@email.com";
        user = new User();
        user.setPhotoUrl(URL);
        user.setCo2PledgeAmount(pledge);
        user.setFirstName(name);
        user.setLastNameInitial(lastI);
        user.setEmail(email);
        user.setUserName(userName);
        user.setMunicipality(municipality);
    }

    @After
    public void tearDown() throws Exception {
        user = null;
        name = null;
        userName = null;
        municipality = null;
        URL = null;
        lastI = null;
        email = null;
        pledge = 0;
    }

    @Test
    public void totalPledgeCarbon() {
        assertEquals(pledge, user.getCo2PledgeAmount(), 0.1);
    }

    @Test
    public void totalPledges() {
        User user2 = new User();
        user2.setCo2PledgeAmount(pledge);
        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user2);
        assertEquals(User.totalPledges(users), 2);
    }

    @Test
    public void averagePledgeCarbon() {
        User user2 = new User();
        user2.setCo2PledgeAmount(2*pledge);
        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user2);
        assertEquals(User.averagePledgeCarbon(users), 3*pledge/2, 0.000001);
    }

    @Test
    public void pledgeEquivalence() {
        List<User> users = new ArrayList<>();
        users.add(user);
        double carbon = User.totalPledgeCarbon(users);
        double[] pledgeEquivalence = User.pledgeEquivalence(carbon);
        double[] expected = {carbon/0.254, carbon/0.914, carbon/(6672.0/365.0), carbon/39.0};
        assertArrayEquals(expected, pledgeEquivalence, 0.00001);

    }

    @Test
    public void getMunicipality() {
        assertEquals(user.getMunicipality(), municipality);
    }

    @Test
    public void setMunicipality() {
        user.setMunicipality("Burnaby");
        assertEquals(user.getMunicipality(), "Burnaby");
    }

    @Test
    public void getCo2PledgeAmount() {
        assertEquals(user.getCo2PledgeAmount(), pledge, 0.1);
    }

    @Test
    public void setCo2PledgeAmount() {
        user.setCo2PledgeAmount(10);
        assertEquals(user.getCo2PledgeAmount(), 10, 0.1);
    }

    @Test
    public void getPhotoUrl() {
        assertEquals(user.getPhotoUrl(), URL);
    }

    @Test
    public void setPhotoUrl() {
        user.setPhotoUrl("changed");
        assertEquals(user.getPhotoUrl(), "changed");
    }

    @Test
    public void getUserName() {
        assertEquals(user.getUserName(), userName);
    }

    @Test
    public void setUserName() {
        user.setUserName("changed");
        assertEquals(user.getUserName(), "changed");
    }

    @Test
    public void getEmail() {
        assertEquals(user.getEmail(), email);
    }

    @Test
    public void setEmail() {
        user.setEmail("changed");
        assertEquals(user.getEmail(), "changed");
    }

    @Test
    public void getFirstName() {
        assertEquals(user.getFirstName(), name);
    }

    @Test
    public void setFirstName() {
        user.setFirstName("changed");
        assertEquals(user.getFirstName(), "changed");
    }

    @Test
    public void getLastNameInitial() {
        assertEquals(user.getLastNameInitial(), lastI);
    }

    @Test
    public void setLastNameInitial() {
        user.setLastNameInitial("changed");
        assertEquals(user.getLastNameInitial(), "changed");
    }
}
