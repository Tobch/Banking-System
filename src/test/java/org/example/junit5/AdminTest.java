package org.example.junit5;

import org.example.Admin;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AdminTest {

    @BeforeEach
    public void setup() {
        Admin.clearAdmin(); // Clear admin list before each test
    }

    @AfterAll
    public static void tearDownAfterAll() {
        Admin.clearAdmin(); // Ensure admin list is cleared after all tests
        System.out.println("All tests have been executed.");
    }

    @Test
    public void testAdminCreation() {
        Admin admin = new Admin("Omar Ahmed", "Cairo-Ainshams", "omar1234", 5000, 54321, "Senior Admin");
        assertNotNull(admin);
        assertTrue(admin.isValidAccountNumber(admin.getAccountNumber()));
        assertTrue(admin.isValidPassword(admin.getPassword()));
        admin.setBalance(1000);
        assertTrue(admin.isBalanceValid(admin.getBalance()));
        assertEquals("Senior Admin", admin.getAdminRole()); // Correct role assertion
        assertTrue(Admin.addAdmin1(admin)); // Use addAdmin1 to add admin
        assertTrue(Admin.getAdmins().contains(admin)); // Verify admin is added to the list
    }

    @Test
    public void testInvalidAccountNumber() {
        Admin admin = new Admin("Test", "TestAddress", "TestPass", 1000, 12345, "TestRole");
        assertFalse(admin.isValidAccountNumber(1234)); // Too short
        assertFalse(admin.isValidAccountNumber(123456)); // Too long
        assertTrue(admin.isValidAccountNumber(12345)); // Valid
    }

    @Test
    public void testInvalidPassword() {
        Admin admin = new Admin("Test", "TestAddress", "TestPass", 1000, 12345, "TestRole");
        assertFalse(admin.isValidPassword("short"));
        assertTrue(admin.isValidPassword("longEnough"));
    }
    @Test
    public void testInvalidBalance() {
        Admin admin = new Admin("Test", "TestAddress", "TestPass", 1000, 12345, "TestRole");
        assertFalse(admin.isBalanceValid(0));
        assertTrue(admin.isBalanceValid(100));
    }

    @Test
    public void testAccountNumberUniqueness() {
        Admin admin1 = new Admin("Ahmed", "Cairo", "Admin123", 100, 12345, "Admin");
        Admin admin2 = new Admin("Omar", "Alex", "Admin456", 200, 67890, "Senior Admin");
        Admin.addAdmin1(admin1);
        Admin.addAdmin1(admin2);

        // Verify uniqueness
        assertTrue(Admin.isAccountNumberTaken(12345));
        assertFalse(Admin.isAccountNumberTaken(67891));
        assertTrue(Admin.isAccountNumberTaken(67890));
    }

    @Test
    void testAdminLoginWithIncorrectPassword() {
        Admin admin = new Admin("admin1", "Cairo", "correctPass", 1000, 12345, "Admin");
        boolean result = admin.getPassword().equals("wrongPass");
        assertFalse(result, "Login should fail with incorrect password");
    }

    @Test
    void testAdminBonusCalculationWithZeroYears() {
        Admin admin = new Admin("admin2", "Cairo", "pass", 1000, 12345, "Admin");
        double bonus = admin.calculateBonus(0);
        assertEquals(0.0, bonus, "Bonus should be zero for 0 years");
    }

    @Test
    void testAdminBonusCalculationWithNegativeYears() {
        Admin admin = new Admin("admin3", "Cairo", "pass", 1000, 12345, "Admin");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            admin.calculateBonus(-2);
        });
        assertEquals("Bonus percentage must be positive", exception.getMessage());
    }
    @Test
    public void testAdminCreationWithEmptyName() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Admin("", "Cairo", "password123", 1000, 12345, "Admin");
        });
        assertEquals("Username cannot be empty", exception.getMessage());
    }

    @Test
    public void testAdminCreationWithNullRole() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Admin("Ahmed", "Cairo", "password123", 1000, 12345, null);
        });
        assertEquals("Role cannot be empty", exception.getMessage());
    }

    @Test
    public void testAdminLoginWithCorrectPassword() {
        Admin admin = new Admin("admin1", "Cairo", "correctPass", 1000, 12345, "Admin");
        boolean result = admin.getPassword().equals("correctPass");
        assertTrue(result, "Login should succeed with correct password");
    }
    @Test
    public void testAdminBonusCalculationWithPositiveYears() {
        Admin admin = new Admin("admin2", "Cairo", "pass", 1000, 12345, "Admin");
        admin.setBalance(1000);
        double bonus = admin.calculateBonus(5);
        assertEquals(50.0, bonus, "Bonus should be 500 for 5 years with 10% of salary");
    }

    @Test
    public void testAdminBonusCalculationWithHighYears() {
        Admin admin = new Admin("admin3", "Cairo", "pass", 1000, 12345, "Admin");
        admin.setBalance(10000);
        double bonus = admin.calculateBonus(50);
        assertEquals(5000.0, bonus, "Bonus should be 5000 for 50 years with 10% of salary");
    }

    @Test
    public void testAdminToStringIncludesAllDetails() {
        Admin admin = new Admin("Ahmed", "Cairo", "password123", 1000, 12345, "Admin");
        String adminString = admin.toString();
        assertTrue(adminString.contains("Ahmed"));
        assertTrue(adminString.contains("Cairo"));
        assertTrue(adminString.contains("Admin"));
        assertTrue(adminString.contains("1000"));
        assertTrue(adminString.contains("12345"));
    }
    @Test
    public void testAdminListIsImmutable() {
        Admin admin = new Admin("Ahmed", "Cairo", "password123", 1000, 12345, "Admin");
        Admin.addAdmin1(admin);
        assertThrows(UnsupportedOperationException.class, () -> {
            Admin.getAdmins().add(new Admin("Omar", "Alex", "password456", 2000, 67890, "Senior Admin"));
        });
    }

    @Test
    public void testAdminWithDuplicateAccountNumber() {
        Admin admin1 = new Admin("Ahmed", "Cairo", "password123", 1000, 12345, "Admin");
        Admin admin2 = new Admin("Omar", "Alex", "password456", 2000, 12345, "Senior Admin");
        Admin.addAdmin1(admin1);
        assertFalse(Admin.addAdmin1(admin2), "Admin with duplicate account number should not be added");
    }

    @Test
    public void testAdminWithDuplicateName() {
        Admin admin1 = new Admin("Ahmed", "Cairo", "password123", 1000, 12345, "Admin");
        Admin admin2 = new Admin("Ahmed", "Alex", "password456", 2000, 67890, "Senior Admin");
        Admin.addAdmin1(admin1);
        assertTrue(Admin.addAdmin1(admin2), "Admins with the same name but different account numbers should be allowed");
    }
    @Test
    public void testAdminSalaryUpdate() {
        Admin admin = new Admin("Ahmed", "Cairo", "password123", 1000, 12345, "Admin");
        admin.setSalary(2000);
        assertEquals(2000, admin.getSalary(), "Salary should be updated to 2000");
    }

    @Test
    public void testAdminSalaryUpdateWithNegativeValue() {
        Admin admin = new Admin("Ahmed", "Cairo", "password123", 1000, 12345, "Admin");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            admin.setSalary(-500);
        });
        assertEquals("Salary cannot be negative", exception.getMessage());
    }

    @Test
    public void testAdminRoleUpdate() {
        Admin admin = new Admin("Ahmed", "Cairo", "password123", 1000, 12345, "Admin");
        admin.setAdminRole("Senior Admin");
        assertEquals("Senior Admin", admin.getAdminRole(), "Role should be updated to Senior Admin");
    }
    @Test
    public void testAdminRoleUpdateWithEmptyValue() {
        Admin admin = new Admin("Ahmed", "Cairo", "password123", 1000, 12345, "Admin");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            admin.setAdminRole("");
        });
        assertEquals("Role cannot be empty", exception.getMessage());
    }

    @Test
    public void testAdminBalanceUpdate() {
        Admin admin = new Admin("Ahmed", "Cairo", "password123", 1000, 12345, "Admin");
        admin.setBalance(5000);
        assertEquals(5000, admin.getBalance(), "Balance should be updated to 5000");
    }

    @Test
    public void testAdminBalanceUpdateWithNegativeValue() {
        Admin admin = new Admin("Ahmed", "Cairo", "password123", 1000, 12345, "Admin");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            admin.setBalance(-100);
        });
        assertEquals("Balance cannot be negative", exception.getMessage());
    }
}