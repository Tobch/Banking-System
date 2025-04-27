package org.example.junit5;

import org.example.Client;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ClientTest {

    @BeforeEach
    public void setup() {
        Client.clearClients();
    }

    @AfterAll
    public static void tearDownAfterAll() {
        Client.clearClients();
        System.out.println("All tests have been executed.");
    }

    @Test
    public void testClientCreation() {
        Client client = new Client("John Doe", "123 Elm Street", "password123", 1000, 12345, "5551234", "Engineer");
        assertNotNull(client);
        assertEquals("John Doe", client.getName());
        assertEquals("123 Elm Street", client.getAddress());
        assertEquals("password123", client.getPassword());
        assertEquals(1000, client.getBalance());
        assertEquals(12345, client.getAccountNumber());
        assertEquals("5551234", client.getPhoneNumber());
        assertEquals("Engineer", client.getProfession());

        assertTrue(Client.addClient(client));
        assertTrue(Client.getClients().contains(client));
    }

    @Test
    public void testAccountNumberUniqueness() {
        Client client1 = new Client("Alice", "456 Oak Street", "securepass", 500, 67890, "55596789", "Teacher");
        Client client2 = new Client("Bob", "789 Pine Street", "anotherpass", 9000, 67890, "98776544", "Doctor");

        assertTrue(Client.addClient(client1));
        assertTrue(Client.getClients().contains(client1));
        assertTrue(Client.isAccountNumberTaken(67890));

        assertFalse(Client.addClient(client2));
    }

    @Test
    public void testClientCreationWithValidAccountNumber() {
        Client client = new Client("Eve", "101 Maple Street", "password321", 2000, 23456, "555984567", "Nurse");
        assertTrue(Client.isValidAccountNumber(23456));

        assertTrue(Client.addClient(client));
        assertTrue(Client.getClients().contains(client));
    }

    @Test
    public void testClientCreationWithInvalidAccountNumber() {
        Client client = new Client("Eve", "101 Maple Street", "password321", 2000, 1234, "555984567", "Nurse");
        assertFalse(Client.isValidAccountNumber(1234));

        assertFalse(Client.addClient(client));
        assertFalse(Client.getClients().contains(client));
    }

    @Test
    public void testClientCreationWithInvalidBalance() {
        Client client = new Client("Gina", "103 Maple Street", "password789", 0, 23456, "5556789", "Artist");

        assertFalse(Client.addClient(client));
        assertFalse(Client.getClients().contains(client));
    }

    @Test
    public void testClientCreationWithShortPassword() {
        Client client = new Client("Hank", "104 Maple Street", "short", 3000, 34567, "555123456", "Mechanic");

        assertFalse(Client.addClient(client));
        assertFalse(Client.getClients().contains(client));
    }

    @Test
    void testClientLoginSuccess() {
        Client client = new Client("user1", "123 Elm Street", "mypassword", 300, 12345, "5551234", "Engineer");
        assertTrue(client.login("user1", "mypassword"), "Login should succeed with correct credentials");
    }

    @Test
    void testClientLoginFailure() {
        Client client = new Client("user2", "secret", 100.0);
        assertFalse(client.login("user2", "wrong"), "Login should fail with wrong password");
    }

    @Test
    void testLoanRequestWithHighAmount() {
        Client client = new Client("user3", "pass", 500.0);
        boolean result = client.requestLoan(100000.0);
        assertFalse(result, "Loan should be rejected if the amount is too high");
    }
@Test
    public void testClientCreationWithEmptyName() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Client("", "123 Elm Street", "password123", 1000, 12345, "5551234", "Engineer");
        });
        assertEquals("Name cannot be empty", exception.getMessage());
    }

    @Test
    public void testClientCreationWithNegativeBalance() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Client("John Doe", "123 Elm Street", "password123", -1000, 12345, "5551234", "Engineer");
        });
        assertEquals("Balance cannot be negative", exception.getMessage());
    }

    @Test
    public void testClientCreationWithInvalidPhoneNumber() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Client("John Doe", "123 Elm Street", "password123", 1000, 12345, "123", "Engineer");
        });
        assertEquals("Phone number must be at least 7 digits", exception.getMessage());
    }
    @Test
    public void testClientLoginWithIncorrectUsername() {
        Client client = new Client("user1", "123 Elm Street", "mypassword", 300, 12345, "5551234", "Engineer");
        assertFalse(client.login("wronguser", "mypassword"), "Login should fail with incorrect username");
    }

    @Test
    public void testClientLoginWithIncorrectPassword() {
        Client client = new Client("user1", "123 Elm Street", "mypassword", 300, 12345, "5551234", "Engineer");
        assertFalse(client.login("user1", "wrongpassword"), "Login should fail with incorrect password");
    }

    @Test
    public void testClientBalanceUpdate() {
        Client client = new Client("John Doe", "123 Elm Street", "password123", 1000, 12345, "5551234", "Engineer");
        client.setBalance(2000);
        assertEquals(2000, client.getBalance(), "Balance should be updated to 2000");
    }

    @Test
    public void testClientBalanceUpdateWithNegativeValue() {
        Client client = new Client("John Doe", "123 Elm Street", "password123", 1000, 12345, "5551234", "Engineer");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            client.setBalance(-500);
        });
        assertEquals("Balance cannot be negative", exception.getMessage());
    }
    @Test
    public void testClientPhoneNumberUpdate() {
        Client client = new Client("John Doe", "123 Elm Street", "password123", 1000, 12345, "5551234", "Engineer");
        client.setPhoneNumber("5551234");
        assertEquals("5551234", client.getPhoneNumber(), "Phone number should be updated");
    }

    @Test
    public void testClientPhoneNumberUpdateWithInvalidValue() {
        Client client = new Client("John Doe", "123 Elm Street", "password123", 1000, 12345, "5551234", "Engineer");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            client.setPhoneNumber("123");
        });
        assertEquals("Phone number must be 7 digits", exception.getMessage());
    }

    @Test
    public void testClientJobUpdate() {
        Client client = new Client("John Doe", "123 Elm Street", "password123", 1000, 12345, "5551234", "Engineer");
        client.setJob("Doctor");
        assertEquals("Doctor", client.getJob(), "Job should be updated to Doctor");
    }
    @Test
    public void testClientJobUpdateWithEmptyValue() {
        Client client = new Client("John Doe", "123 Elm Street", "password123", 1000, 12345, "5551234", "Engineer");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            client.setJob(null);
        });
        assertEquals("Job cannot be empty", exception.getMessage());
    }

    @Test
    public void testClientEquality() {
        Client client1 = new Client("John Doe", "123 Elm Street", "password123", 1000, 12345, "5551234", "Engineer");
        Client client2 = new Client("John Doe", "123 Elm Street", "password123", 1000, 12345, "5551234", "Engineer");
        assertTrue(client1.equals(client2), "Clients with the same details should be equal");
    }

    @Test
    public void testClientInequality() {
        Client client1 = new Client("John Doe", "123 Elm Street", "password123", 1000, 12345, "5551234", "Engineer");
        Client client2 = new Client("Jane Doe", "456 Oak Street", "password456", 2000, 67890, "9876543", "Doctor");
        assertFalse(client1.equals(client2), "Clients with different details should not be equal");
    }

    @Test
    public void testClientToStringIncludesDetails() {
        Client client = new Client("John Doe", "123 Elm Street", "password123", 1000, 12345, "5551234", "Engineer");
        String clientString = client.toString();
        assertTrue(clientString.contains("John Doe"));
        assertTrue(clientString.contains("123 Elm Street"));
        assertTrue(clientString.contains("Engineer"));
        assertTrue(clientString.contains("5551234"));
        assertTrue(clientString.contains("1000"));
}
@Test
    public void testClientListIsImmutable() {
        Client client = new Client("John Doe", "123 Elm Street", "password123", 1000, 12345, "5551234", "Engineer");
        Client.addClient(client);
        assertThrows(UnsupportedOperationException.class, () -> {
            Client.getClients().add(new Client("Jane Doe", "456 Oak Street", "password456", 2000, 67890, "9876543", "Doctor"));
        });
    }
}