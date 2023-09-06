package com.zhaofn.twitch.db;

import com.zhaofn.twitch.db.entity.UserEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

//crud就是create read update delete
public interface UserRepository extends ListCrudRepository<UserEntity, Long> {

    List<UserEntity> findAllByLastName(String lastName);//findByLastName也可以

    List<UserEntity> findByFirstName(String firstName);//和上面的同理

    UserEntity findByUsername(String username);//

    @Modifying
    @Query("UPDATE users SET first_name = :firstName, last_name = :lastName WHERE username = :username")
    void updateNameByUsername(String username, String firstName, String lastName);
}
