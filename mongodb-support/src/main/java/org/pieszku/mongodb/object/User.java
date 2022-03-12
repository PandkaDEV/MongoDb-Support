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
