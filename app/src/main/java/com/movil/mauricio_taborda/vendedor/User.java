package com.movil.mauricio_taborda.vendedor;

/**
 * Created by AForce on 20/05/2017.
 */

public class User {
    public String email;
    public String uid;
    public String role;

    public User(String email, String uid, String role) {
        this.email = email;
        this.uid = uid;
        this.role = role;
    }

    public User() {
    }
}
