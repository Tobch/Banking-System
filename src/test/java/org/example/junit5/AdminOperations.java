package org.example.junit5;

import java.util.List;

import org.example.Admin;
import org.example.Client;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AdminOperations {

    private Admin admin1;
    private Admin admin2;
    private Client client1;
    private Client client2;

    @BeforeEach
    public void setUp() {
        Admin.clearAdmin();
        Client.clearClients();

        admin1 = new Admin("Ahmed", "cairo", "Admin123", 100, 12345, "Admin");
        admin2 = new Admin("Omar", "alex", "Admin456", 200, 67890, "Senior Admin");
        client1 = new Client("Ali", "cairo", "client123", 500, 12349, "99988776", "Engineer");
        client2 = new Client("Osama", "alex", "client456", 600, 67880, "4668798", "Doctor");
    }

    @AfterEach
    public void clearAdminAndClient() {
        Admin.clearAdmin();
        Client.clearClients();
    }

    @Test
    public void testAddAdmin() {
        assertTrue(Admin.addAdmin1(admin1));
        assertTrue(Admin.addAdmin1(admin2));
        assertFalse(Admin.addAdmin1(admin1)); // Duplicate admin

        List<Admin> admins = Admin.getAdmins();
        assertEquals(2, admins.size());
        assertEquals("Ahmed", admins.get(0).getName());
        assertEquals("Omar", admins.get(1).getName());
    }

    @Test
    public void testAddClient() {
        assertTrue(Client.addClient(client1));
        assertTrue(Client.addClient(client2));
        assertFalse(Client.addClient(client1)); // Duplicate client

        List<Client> clients = Client.getClients();
        assertEquals(2, clients.size());
        assertEquals("Ali", clients.get(0).getName());
        assertEquals("Osama", clients.get(1).getName());
    }

    @Test
    public void testDisplayClientAccounts() {
        Client.addClient(client1);
        Client.addClient(client2);

        List<Client> clients = Client.getClients();
        assertEquals(2, clients.size());
        assertEquals("Ali", clients.get(0).getName());
        assertEquals("Osama", clients.get(1).getName());
    }

    @Test
    public void testDisplayAdminAccounts() {
        Admin.addAdmin1(admin1);
        Admin.addAdmin1(admin2);

        List<Admin> admins = Admin.getAdmins();
        assertEquals(2, admins.size());
        assertEquals("Ahmed", admins.get(0).getName());
        assertEquals("Omar", admins.get(1).getName());
    }

    @Test
    void testCreateAdminWithEmptyUsername() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Admin("", "cairo", "password", 0, 0, "Admin");
        });
        assertEquals("Username cannot be empty", exception.getMessage());
    }

    @Test
    void testCreateAdminWithNullPassword() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new Admin("admin1", "cairo", null, 0, 0, "Admin");
        });
        assertEquals("Password cannot be null", exception.getMessage());
    }

    @Test
    void testAdminToStringIncludesUsername() {
        Admin admin = new Admin("adminUser", "cairo", "adminPass", 0, 0, "Admin");
        assertTrue(admin.toString().contains("adminUser"));
    }

    @Test
    public void testRemoveAdmin() {
        Admin.addAdmin1(admin1);
        Admin.addAdmin1(admin2);

        assertTrue(Admin.removeAdmin(admin1)); // Admin successfully removed
    }

    @Test
    public void testRemoveClient() {
        Client.addClient(client1);
        Client.addClient(client2);

        assertTrue(Client.removeClient(client1));
        assertFalse(Client.removeClient(client1)); // Client already removed

        List<Client> clients = Client.getClients();
        assertEquals(1, clients.size());
        assertEquals("Osama", clients.get(0).getName());
    }

    @Test
    public void testAdminEquality() {
        Admin duplicateAdmin = new Admin("Ahmed", "cairo", "Admin123", 100, 12345, "Admin");
        assertTrue(admin1.equals(duplicateAdmin));
        assertFalse(admin1.equals(admin2));
    }

    @Test
    public void testClientEquality() {
        Client duplicateClient = new Client("Ali", "cairo", "client123", 500, 12349, "99988776", "Engineer");
        assertTrue(client1.equals(duplicateClient));
        assertFalse(client1.equals(client2));
    }

    @Test
    public void testAdminInvalidSalary() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Admin("Ahmed", "cairo", "Admin123", -100, 12345, "Admin");
        });
        assertEquals("Salary cannot be negative", exception.getMessage());
    }

    @Test
    public void testClientInvalidBalance() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Client("Ali", "cairo", "client123", -500, 12349, "99988776", "Engineer");
        });
        assertEquals("Balance cannot be negative", exception.getMessage());
    }

    @Test
    public void testAdminToStringIncludesRole() {
        assertTrue(admin1.toString().contains("Admin"));
        assertTrue(admin2.toString().contains("Senior Admin"));
    }

    @Test
    public void testClientToStringIncludesProfession() {
        assertTrue(client1.toString().contains("Engineer"));
        assertTrue(client2.toString().contains("Doctor"));
    }

    @Test
    public void testAddMultipleAdmins() {
        for (int i = 0; i < 100; i++) {
            Admin admin = new Admin("Admin" + i, "city" + i, "password" + i, i * 100, i, "Role" + i);
            assertTrue(Admin.addAdmin1(admin));
        }
        assertEquals(100, Admin.getAdmins().size());
    }

    @Test
    public void testAdminWithDuplicateID() {
        Admin duplicateIDAdmin = new Admin("Duplicate", "city", "password", 100, 12345, "Admin");
        Admin.addAdmin1(admin1);
        assertFalse(Admin.addAdmin1(duplicateIDAdmin)); // Duplicate ID
    }

    @Test
    public void testClientWithDuplicateID() {
        Client duplicateIDClient = new Client("Duplicate", "city", "password", 100, 12349, "1234567", "Profession");
        Client.addClient(client1);
        assertFalse(Client.addClient(duplicateIDClient)); // Duplicate ID
    }

    @Test
    public void testAdminWithEmptyRole() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Admin("Ahmed", "cairo", "Admin123", 100, 12345, "");
        });
        assertEquals("Role cannot be empty", exception.getMessage());
    }

    @Test
    public void testClientWithEmptyProfession() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Client("Ali", "cairo", "client123", 500, 12349, "99988776", "");
        });
        assertEquals("Profession cannot be empty", exception.getMessage());
    }

    @Test
    public void testAdminListIsImmutable() {
        Admin.addAdmin1(admin1);
        List<Admin> admins = Admin.getAdmins();
        assertThrows(UnsupportedOperationException.class, () -> {
            admins.add(admin2);
        });
    }

    @Test
    public void testClientListIsImmutable() {
        Client.addClient(client1);
        List<Client> clients = Client.getClients();
        assertThrows(UnsupportedOperationException.class, () -> {
            clients.add(client2);
        });
    }
}