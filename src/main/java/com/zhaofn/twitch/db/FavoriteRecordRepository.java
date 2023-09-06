package com.zhaofn.twitch.db;

import com.zhaofn.twitch.db.entity.FavoriteRecordEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

//ListCrudRepository是最基础的interface，基础操作都在里面
//尖括号里第一个是对应的table，第二个是primary key的数据类型
public interface FavoriteRecordRepository extends ListCrudRepository<FavoriteRecordEntity, Long> {

    List<FavoriteRecordEntity> findAllByUserId(Long userId);

    boolean existsByUserIdAndItemId(Long userId, Long itemId);//某个用户有没有favorite了一个item

    //当需要的query不能用简单的query执行的时候就需要自己@Query来写了
    @Query("SELECT item_id FROM favorite_records WHERE user_id = :userId")
    List<Long> findFavoriteItemIdsByUserId(Long userId);
    //自己写的Query的话函数名就没有固定的规范了，但是Query里的 :userId 名字需要与下面函数arg里的userId一致

    @Modifying//这里需要一个modifying annotation，因为这是一个写操作，所有写操作都需要这个annotation
    @Query("DELETE FROM favorite_records WHERE user_id = :userId AND item_id = :itemId")
    void delete(Long userId, Long itemId);

}
