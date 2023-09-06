package com.zhaofn.twitch.external;

//用于和twitch平台交互的是TwitchApi，但是我们的项目还需要一点自己的逻辑，所以需要service
//ApiClient里的内容是给Feign自动实现的，并且是interface

import com.zhaofn.twitch.external.model.Clip;
import com.zhaofn.twitch.external.model.Game;
import com.zhaofn.twitch.external.model.Stream;
import com.zhaofn.twitch.external.model.Video;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service //spring通过这个找到service
public class TwitchService {
    private final TwitchApiClient twitchApiClient;


    public TwitchService(TwitchApiClient twitchApiClient) {
        this.twitchApiClient = twitchApiClient;
    }


    @Cacheable("top_games") //创建一个名为top_games的cache，把数据存进去，expiration之前如果再次访问的话就在这里寻找，大大缩减响应时间
    public List<Game> getTopGames() {
        return twitchApiClient.getTopGames().data();
    }


    @Cacheable("games_by_name")
    public List<Game> getGames(String name) {
        return twitchApiClient.getGames(name).data();
    }


    public List<Stream> getStreams(List<String> gameIds, int first) {
        return twitchApiClient.getStreams(gameIds, first).data();
    }


    public List<Video> getVideos(String gameId, int first) {
        return twitchApiClient.getVideos(gameId, first).data();
    }


    public List<Clip> getClips(String gameId, int first) {
        return twitchApiClient.getClips(gameId, first).data();
    }


    public List<String> getTopGameIds() {
        List<String> topGameIds = new ArrayList<>();
        for (Game game : getTopGames()) {
            topGameIds.add(game.id());
        }
        return topGameIds;
    }
}
