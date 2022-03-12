package org.pieszku.mongodb.object.repository;

import org.pieszku.mongodb.object.User;
import org.pieszku.mongodb.repository.json.JsonRepository;

public class UserRepository extends JsonRepository<String, User> {


    public UserRepository() {
        super(User.class, "nickName", "users");
    }
}
