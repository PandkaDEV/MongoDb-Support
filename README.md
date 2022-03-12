# MongoDb-Support

Connection in Main class
```
this.mongoCommon = new MongoCommon("mongodb://localhost:27017/?readPreference=primary&appname=MongoDB%20Compass&directConnection=true&ssl=false");
        
```
Load data from mongodb in Main class
```
        this.userRepository.findAll().forEach(user -> this.userService.getUserMap().put(user.getId(), user));
```
UserRepository:
```
package org.pieszku.mongodb.object.repository;

import org.pieszku.mongodb.object.User;
import org.pieszku.mongodb.repository.json.JsonRepository;

public class UserRepository extends JsonRepository<String, User> {


    public UserRepository() {
        super(User.class, "nickName", "users");
    }
}
```
User class:
```
package org.pieszku.mongodb.object;

import org.pieszku.mongodb.MongoServer;
import org.pieszku.mongodb.object.impl.UserImpl;
import org.pieszku.mongodb.repository.Identifiable;
import org.pieszku.mongodb.type.SaveType;

public class User implements UserImpl, Identifiable<String> {

    private final String nickName;
    private int points;

    public User(String nickName){
        this.nickName = nickName;
        this.points = 1000;
        this.saveToDatabase(SaveType.CREATE);
    }

    public String getNickName() {
        return nickName;
    }
    public int getPoints() {
        return points;
    }

    @Override
    public void addPoints(int points) {
        this.points += points;
        this.saveToDatabase(SaveType.UPDATE);
    }

    @Override
    public void removePoints(int points) {
        this.points -= points;
        this.saveToDatabase(SaveType.UPDATE);
    }

    @Override
    public String getId() {
        return this.nickName;
    }
    public void saveToDatabase(SaveType saveType){
        MongoServer.getInstance().getUserRepository().update(this, this.nickName, saveType);
    }
}
```
UserService:
```
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
```
