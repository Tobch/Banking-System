package org.example;

public interface Authenticatable {
    boolean authenticate(String username, String password);
}