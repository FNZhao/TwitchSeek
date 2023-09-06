package com.zhaofn.twitch.db.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zhaofn.twitch.external.model.Clip;
import com.zhaofn.twitch.external.model.Stream;
import com.zhaofn.twitch.external.model.Video;
import com.zhaofn.twitch.model.ItemType;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("items") //把ItemEntity连到db里的items这个table
public record ItemEntity(
        //这个也决定了返回的json格式,比如会出现twitch_id
        @Id Long id,//这个是用于db的primary key,@Id 表示这个在table里是primary key
        @JsonProperty("twitch_id") String twitchId,
        String title,
        String url,
        @JsonProperty("thumbnail_url") String thumbnailUrl, //改名，从snake case变成camel case
        @JsonProperty("broadcaster_name") String broadcasterName,
        @JsonProperty("game_id") String gameId,
        @JsonProperty("item_type") ItemType type
        //这些json数据是返回给前端的
) {

    public ItemEntity(String gameId, Video video) {
        this(null, video.id(), video.title(), video.url(), video.thumbnailUrl(), video.userName(), gameId, ItemType.VIDEO);
    }//video里没有broadcasterName，有userName，作用一样，所以直接video.userName()；

    public ItemEntity(Clip clip) {
        this(null, clip.id(), clip.title(), clip.url(), clip.thumbnailUrl(), clip.broadcasterName(), clip.gameId(), ItemType.CLIP);
    }//this()是java class调用自己构造函数的方法,即，在我们自定义的constructor里调用上面格式的constructor

    public ItemEntity(Stream stream) {
        this(null, stream.id(), stream.title(), null, stream.thumbnailUrl(), stream.userName(), stream.gameId(), ItemType.STREAM);
    }

}
