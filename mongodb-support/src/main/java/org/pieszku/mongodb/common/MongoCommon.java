package org.pieszku.mongodb.common;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.pieszku.mongodb.common.impl.MongoCommonImpl;

public class MongoCommon implements MongoCommonImpl {



    private final MongoClient mongoClient;
    private final MongoDatabase mongoDatabase;


    public MongoCommon(String connectionWithName){
        this.mongoClient = new MongoClient(new MongoClientURI(connectionWithName));
        this.mongoDatabase = mongoClient.getDatabase("test");
    }


    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }


    @Override
    public void disconnect() {
        this.mongoClient.close();
    }

    @Override
    public boolean isConnected() {
        return this.mongoClient != null;
    }
}
