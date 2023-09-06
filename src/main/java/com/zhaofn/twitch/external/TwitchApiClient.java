package com.zhaofn.twitch.external;

import com.zhaofn.twitch.external.model.ClipResponse;
import com.zhaofn.twitch.external.model.GameResponse;
import com.zhaofn.twitch.external.model.StreamResponse;
import com.zhaofn.twitch.external.model.VideoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

//我们并没有在这里具体实现
@FeignClient(name = "twitch-api")//这里的是我们给的名字，而不是下面那个interface名//Feign会自动实现下面的function,市面上还有springboot自带的retrofit等，但是OpenFeign支持oauth2
public interface TwitchApiClient {

    @GetMapping("/games")//这些都是根据js body来的，比如games里面就有一个name，我们的参数就输入这个name
    GameResponse getGames(@RequestParam("name") String name);

    @GetMapping("/games/top")
    GameResponse getTopGames();//获取靠前的game，所以不需要parameters

    @GetMapping("/videos/")
    VideoResponse getVideos(@RequestParam("game_id") String gameId, @RequestParam("first") int first);//first是指返回几个，默认20，最多100


    @GetMapping("/clips/")
    ClipResponse getClips(@RequestParam("game_id") String gameId, @RequestParam("first") int first);


    @GetMapping("/streams/")
    StreamResponse getStreams(@RequestParam("game_id") List<String> gameIds, @RequestParam("first") int first);

}
