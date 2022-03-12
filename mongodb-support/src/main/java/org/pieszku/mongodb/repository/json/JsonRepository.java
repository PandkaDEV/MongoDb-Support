package org.pieszku.mongodb.repository.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.Block;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.json.JsonWriterSettings;
import org.pieszku.mongodb.MongoServer;
import org.pieszku.mongodb.repository.CrudRepository;
import org.pieszku.mongodb.repository.Identifiable;
import org.pieszku.mongodb.type.SaveType;

import java.util.Collection;
import java.util.HashSet;

public class JsonRepository<ID, T extends Identifiable<ID>> implements CrudRepository<ID, T> {


    private final Class<T> type;
    private final String documentName;
    private final String collection;
    protected Gson gson;

    public JsonRepository(Class<T> type, String documentName, String collection) {
        this(type, documentName,  collection, new GsonBuilder().setPrettyPrinting().create());
    }
    public JsonRepository(Class<T> type, String documentName, String collection, Gson gson){
        this.type = type;
        this.documentName = documentName;
        this.collection = collection;
        this.gson = gson;
    }

    @Override
    public void update(T object, ID field, SaveType saveType) {
        if(saveType.equals(SaveType.CREATE)){
            MongoServer.getInstance()
                    .getMongoCommon()
                    .getMongoDatabase()
                    .getCollection(this.collection)
                    .insertOne(
                            Document.parse(
                                    new Gson().toJson(object)));
            return;
        }
        if(saveType.equals(SaveType.UPDATE)){
            MongoServer.getInstance().getMongoCommon().getMongoDatabase().getCollection(this.collection).findOneAndUpdate(new Document(this.documentName, field), new Document("$set", Document.parse(new Gson().toJson(object))));
            return;
        }
        if(saveType.equals(SaveType.REMOVE)){
            MongoServer.getInstance().getMongoCommon().getMongoDatabase().getCollection(this.collection).findOneAndDelete(new Document(this.documentName, field));
        }

    }


    @Override
    public T find(ID id) {
        Object object = new Gson().fromJson(MongoServer.getInstance().getMongoCommon().getMongoDatabase().getCollection(this.collection)
                .find(Filters.eq(this.documentName, id)).first().toJson(JsonWriterSettings.builder().build()), this.type);
        return (T) object;
    }

    @Override
    public Collection<T> findAll() {
        Collection<T> collection = new HashSet<>();
        MongoServer.getInstance().getMongoCommon().getMongoDatabase().getCollection(this.collection).find().forEach((Block<? super Document>) (Document document) -> {
            Object object = new Gson().fromJson(document.toJson(JsonWriterSettings.builder().build()), this.getType());
            collection.add((T) object);
        });
        System.out.println("[MONGODB-SERVER] " + this.getType().getSimpleName() + " loaded from mongodb: " + collection.size());
        return collection;
    }

    public Class<T> getType() {
        return type;
    }

    public Gson getGson() {
        return gson;
    }

    public String getCollection() {
        return collection;
    }


    public String getDocumentName() {
        return documentName;
    }
}
