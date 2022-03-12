package org.pieszku.mongodb.object.service;

import org.pieszku.mongodb.object.User;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class UserService {

    private final ConcurrentHashMap<String, User> userMap = new ConcurrentHashMap<>();


    public Optional<User> findUserByNickName(String nickName){
        return this.userMap
                .values()
                .stream()
                .filter(user -> user.getNickName().equalsIgnoreCase(nickName))
                .findFirst();
    }
    public User findOrCreateByNickName(String nickName){
        return this.userMap.computeIfAbsent(nickName, User::new);
    }


    public ConcurrentHashMap<String, User> getUserMap() {
        return userMap;
    }
}
