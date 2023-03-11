package io.boxhit.socket.users;

import io.boxhit.logic.Controller;

import java.security.Principal;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class UserManager {

    private static HashMap<Principal, String> users = new HashMap<>();

    public static void addUser(Principal user) {
        String token = SessionToken.generateToken(200);
        users.put(user, token);
    }

    public static String getUserToken(Principal user) {
        return users.get(user);
    }

    public static String getUserNameByToken(String token) {
        for (Principal user : users.keySet()) {
            if (users.get(user).equals(token)) {
                return user.getName();
            }
        }
        return null;
    }

    public static void removeUser(Principal user) {
        Controller.getPlayerInstanceHandler().prepareUnregisterPlayer(user.getName());
        users.remove(user);
    }


}
