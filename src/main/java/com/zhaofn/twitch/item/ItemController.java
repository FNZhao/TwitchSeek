package com.zhaofn.twitch.item;

import com.zhaofn.twitch.model.TypeGroupedItemList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//controller -> service -> repository(db操作)
@RestController//有了这个annotation，Spring就会去@Service里找dependency
public class ItemController {

    private final ItemService itemService;

    //这里是dependency rejection，我们只管用，spring会做之前的initialization，Spring会把itemService给inject进来
    //因为ItemService有一个@Service，所以spring会有dependency injection操作,如果没有这个annotation，则即使这么使用也不会inject
    //@FeignClient也有类似的操作
    //@Autowire则是明确指出这是一个dependency injection，其使用方法为直接放在 private ItemService itemService上面
    //这个controller的injection方法则是把Service放到constructor里，Spring同样会inject
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/search")
    public TypeGroupedItemList search(@RequestParam("game_id") String gameId) {
        return itemService.getItems(gameId);
    }
    //这里的RequestParam是网址上面的"？game_id=xxxx",这是我们自己制作的前端的网址，去twitch查询的时候会通过service变成twitchAPI的网址
}
