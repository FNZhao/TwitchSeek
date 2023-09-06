package com.zhaofn.twitch.favorite;

import com.zhaofn.twitch.db.entity.UserEntity;
import com.zhaofn.twitch.external.model.FavoriteRequestBody;
import com.zhaofn.twitch.model.TypeGroupedItemList;
import com.zhaofn.twitch.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/favorite")//底下的方法共享/favorite这个路径
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final UserService userService;

    // Hard-coded user for temporary use, will be replaced in future
    //private final UserEntity userEntity = new UserEntity(1L, "user0", "Foo", "Bar", "password");

    public FavoriteController(FavoriteService favoriteService, UserService userService) {
        this.favoriteService = favoriteService;
        this.userService = userService;
    }

    @GetMapping
    public TypeGroupedItemList getFavoriteItems(@AuthenticationPrincipal User user) {
        UserEntity userEntity = userService.findByUsername(user.getUsername());//需要把Spring jdbc提供的当前登陆的user转换成自己定义的userEntity，因为提供的只有username
        return favoriteService.getGroupedFavoriteItems(userEntity);
    }

    @PostMapping
    public void setFavoriteItem(@AuthenticationPrincipal User user, @RequestBody FavoriteRequestBody body) throws DuplicateFavoriteException {
        UserEntity userEntity = userService.findByUsername(user.getUsername());
        try {//try catch是controller要做的事情，而service里不需要，service可能在其他地方被调用，而我们需要在这个controller里抛出bad request的错误
            favoriteService.setFavoriteItem(userEntity, body.favorite());
        } catch (DuplicateFavoriteException e) {//catch了一个exception又throw了一个的原因是分开logic，下一层的exception不要在controller这一层写出来，因为下面的exception在其他地方被调用的时候需要的报错不一样
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duplicate entry for favorite record", e);
        }
    }

    @DeleteMapping
    public void unsetFavoriteItem(@AuthenticationPrincipal User user, @RequestBody FavoriteRequestBody body) {
        UserEntity userEntity = userService.findByUsername(user.getUsername());
        favoriteService.unsetFavoriteItem(userEntity, body.favorite().twitchId());
    }
}

