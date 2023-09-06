package com.zhaofn.twitch.model;

import com.zhaofn.twitch.db.entity.ItemEntity;
import com.zhaofn.twitch.external.model.Clip;
import com.zhaofn.twitch.external.model.Stream;
import com.zhaofn.twitch.external.model.Video;

import java.util.ArrayList;
import java.util.List;

public record TypeGroupedItemList(//这个决定了返回给前端的json文件格式，比如返回文件里会出现streams
        List<ItemEntity> streams,
        List<ItemEntity> videos,
        List<ItemEntity> clips
) {
    public TypeGroupedItemList(List<ItemEntity> items) { //这个的作用是调用自己的构造函数（this()）并按照定义格式把items分类,相当于分好类之后再构造
        this(
                filterForType(items, ItemType.STREAM),
                filterForType(items, ItemType.VIDEO),
                filterForType(items, ItemType.CLIP)
        );
    }

    public TypeGroupedItemList(String gameId, List<Stream> streams, List<Video> videos, List<Clip> clips) {//虽然有分类但是需要把他们都转成ItemEntity的List而非Stream、Video的list
        this(
                fromStreams(streams),
                fromVideos(gameId, videos),
                fromClips(clips)
        );
    }

    private static List<ItemEntity> filterForType(List<ItemEntity> items, ItemType type) {
        List<ItemEntity> filtered = new ArrayList<>();
        for (ItemEntity item : items) {
            if (item.type() == type) {
                filtered.add(item);
            }
        }
        return filtered;
    }

    private static List<ItemEntity> fromStreams(List<Stream> streams) {
        List<ItemEntity> items = new ArrayList<>();
        for (Stream stream : streams) {
            items.add(new ItemEntity(stream));
        }
        return items;
    }

    private static List<ItemEntity> fromVideos(String gameId, List<Video> videos) {
        List<ItemEntity> items = new ArrayList<>();
        for (Video video : videos) {
            items.add(new ItemEntity(gameId, video));
        }
        return items;
    }

    private static List<ItemEntity> fromClips(List<Clip> clips) {
        List<ItemEntity> items = new ArrayList<>();
        for (Clip clip : clips) {
            items.add(new ItemEntity(clip));
        }
        return items;
    }

}
