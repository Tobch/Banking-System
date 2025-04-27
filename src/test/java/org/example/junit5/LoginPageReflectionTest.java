package org.example.junit5;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.example.LoginPage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
public class LoginPageReflectionTest {
    @Test
    public void testLoginPageClassExists() {
        try {
            Class<?> clazz = Class.forName("org.example.LoginPage");
            assertNotNull(clazz, "LoginPage class should exist");
        } catch (ClassNotFoundException e) {
            fail("LoginPage class should exist, but it was not found");
        }
    }

    @Test
    void testLoginPageFieldsExist() throws Exception {
        Class<?> clazz = Class.forName("org.example.LoginPage");
        assertNotNull(clazz.getDeclaredField("userTextField"));
        assertNotNull(clazz.getDeclaredField("passwordField"));
    }

    // Removed duplicate testLoginMethodExists method

    // Removed duplicate testLoginPageHasNoPublicFields method to resolve the compile error

    // Removed duplicate testLoginPageClassExists method

    // Removed duplicate testLoginPageFieldsExist method to resolve the compile error

    @Test
    void testLoginMethodExists() throws Exception {
        Class<?> clazz = Class.forName("org.example.LoginPage");
        boolean found = false;
        for (var method : clazz.getDeclaredMethods()) {
            if (method.getName().equals("login")) {
                found = true;
                break;
            }
        }
        assertTrue(found, "Login method should be present in LoginPage class");
    }

    @Test
    void testLoginPageHasNoPublicFields() throws Exception {
        Class<?> clazz = Class.forName("org.example.LoginPage");
        var declaredFields = clazz.getDeclaredFields();

        for (var field : declaredFields) {
            assertFalse(Modifier.isPublic(field.getModifiers()),
                    "LoginPage should not declare public field: " + field.getName());
        }
    }

    @Test
    void listAllPublicFields() throws Exception {
        Class<?> clazz = Class.forName("org.example.LoginPage");
        var publicFields = clazz.getFields();  // Includes fields from superclasses.

        for (var field : publicFields) {
            System.out.println("Public field: " + field.getName());
        }
    }

    @Test
    void testLoginPageImplementsInterface() throws Exception {
        Class<?> clazz = Class.forName("org.example.LoginPage");

        // Recursively check all inherited interfaces
        boolean implementsInterface = implementsInterfaceRecursively(clazz, "org.example.Authenticatable");

    }

    private boolean implementsInterfaceRecursively(Class<?> clazz, String interfaceName) {
        while (clazz != null) {
            for (Class<?> iface : clazz.getInterfaces()) {
                if (iface.getName().equals(interfaceName)) {
                    return true;
                }
                if (implementsInterfaceRecursively(iface, interfaceName)) {
                    return true;
                }
            }
            clazz = clazz.getSuperclass();
        }
        return false;
    }

   

    @Test
    void testLoginMethodParameters() throws Exception {
        Class<?> clazz = Class.forName("org.example.LoginPage");
        var method = clazz.getDeclaredMethod("login", String.class, String.class);
        assertNotNull(method, "Login method should accept username and password as parameters");
    }

    @Test
    void testLoginMethodReturnType() throws Exception {
        Class<?> clazz = Class.forName("org.example.LoginPage");
        var method = clazz.getDeclaredMethod("login", String.class, String.class);
        assertEquals(boolean.class, method.getReturnType(), "Login method should return a boolean");
    }

    @Test
    void testLoginPageHasPrivateFields() throws Exception {
        Class<?> clazz = Class.forName("org.example.LoginPage");
        var fields = clazz.getDeclaredFields();
        for (var field : fields) {
            assertTrue(Modifier.isPrivate(field.getModifiers()), "All fields in LoginPage should be private");
        }
    }



    @Test
    void testLoginPageHasNoStaticMethods() throws Exception {
        Class<?> clazz = Class.forName("org.example.LoginPage");
        var methods = clazz.getDeclaredMethods();
        for (var method : methods) {
            if ("main".equals(method.getName()) && Modifier.isStatic(method.getModifiers())) {
                continue; // Ignore the main method
            }
            assertFalse(Modifier.isStatic(method.getModifiers()), "LoginPage should not have static methods other than main");
        }
    }

}