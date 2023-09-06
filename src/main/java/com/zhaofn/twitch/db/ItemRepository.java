package com.zhaofn.twitch.db;

import com.zhaofn.twitch.db.entity.ItemEntity;
import org.springframework.data.repository.ListCrudRepository;

//用于Sql Query，这个interface是spring下面的JDBC去具体实现的
public interface ItemRepository extends ListCrudRepository<ItemEntity, Long> {//ItemEntity的pk是Long

    //spring与数据库交互的query
    ItemEntity findByTwitchId(String twitchId);//这里和controller不一样，这里是根据函数名来的，所以函数名不能出错
    //函数名是根据Spring的规范来的，是Spring JDBC，与数据库交互的函数名


}
