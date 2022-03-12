package org.pieszku.mongodb;

import org.pieszku.mongodb.common.MongoCommon;
import org.pieszku.mongodb.object.User;
import org.pieszku.mongodb.object.repository.UserRepository;
import org.pieszku.mongodb.object.service.UserService;

public class MongoServer {

    private static MongoServer instance;
    private final MongoCommon mongoCommon;
    private final UserService userService =  new UserService();
    private final UserRepository userRepository = new UserRepository();

    public MongoServer(){
        instance = this;
        this.mongoCommon = new MongoCommon("mongodb://localhost:27017/?readPreference=primary&appname=MongoDB%20Compass&directConnection=true&ssl=false");
        this.userRepository.findAll().forEach(user -> this.userService.getUserMap().put(user.getId(), user));

        User user = this.userService.findOrCreateByNickName("Pieszku");
        user.addPoints(10);
        System.out.println("User points: " + user.getPoints());
    }
    public static void main(String[] args){
        new MongoServer();
    }

    public MongoCommon getMongoCommon() {
        return mongoCommon;
    }

    public static MongoServer getInstance() {
        return instance;
    }

    public UserService getUserService() {
        return userService;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }
}
