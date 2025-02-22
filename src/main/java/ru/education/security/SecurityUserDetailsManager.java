package ru.education.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class SecurityUserDetailsManager implements UserDetailsManager {
    @Override
    public void createUser(UserDetails userDetails) {

    }

    @Override
    public void updateUser(UserDetails userDetails) {

    }

    @Override
    public void deleteUser(String s) {

    }

    @Override
    public void changePassword(String s, String s1) {

    }

    @Override
    public boolean userExists(String s) {
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        // тут должна ыть реализована логика по вычитке пользователя из хранилища данных

        if (!userName.equals("user")) {
            return null;
        }

        List<SecurityPermission> permissions = new ArrayList<>();
        permissions.add(new SecurityPermission("product.read"));
        permissions.add(new SecurityPermission("product.readById"));
        permissions.add(new SecurityPermission("product.create"));
        permissions.add(new SecurityPermission("product.update"));
        permissions.add(new SecurityPermission("product.delete"));
        permissions.add(new SecurityPermission("sales_period.read"));
        permissions.add(new SecurityPermission("sales_period.readById"));
        permissions.add(new SecurityPermission("sales_period.create"));
        permissions.add(new SecurityPermission("sales_period.update"));
        permissions.add(new SecurityPermission("sales_period.delete"));

        return new SecurityUser(userName, "123", permissions);
    }
}
