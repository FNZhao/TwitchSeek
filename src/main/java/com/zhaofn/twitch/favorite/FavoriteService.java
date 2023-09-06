package com.zhaofn.twitch.favorite;

import com.zhaofn.twitch.db.FavoriteRecordRepository;
import com.zhaofn.twitch.db.ItemRepository;
import com.zhaofn.twitch.db.entity.FavoriteRecordEntity;
import com.zhaofn.twitch.db.entity.ItemEntity;
import com.zhaofn.twitch.db.entity.UserEntity;
import com.zhaofn.twitch.model.TypeGroupedItemList;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class FavoriteService {

    private final ItemRepository itemRepository;
    private final FavoriteRecordRepository favoriteRecordRepository;

    public FavoriteService(ItemRepository itemRepository,
                           FavoriteRecordRepository favoriteRecordRepository) {
        this.itemRepository = itemRepository;
        this.favoriteRecordRepository = favoriteRecordRepository;
    }

    @CacheEvict(cacheNames = "recommend_items", key = "#root.args[0]") //当这个操作发生的时候，这个cache就要被清掉，因为重新写了一遍，cache的数据就不准确了. key的意思是以args(下面那个method的参数)里的第0个作为key(此method里，args是[user, item],所以第0位就是user)，意思是当清除时，清楚掉此user的
    @Transactional//里面有写操作，如果发生错误的话会回滚，有一些重要的操作需要保证是transactional
    public void setFavoriteItem(UserEntity user, ItemEntity item) throws DuplicateFavoriteException {
        //因为目前不知道db里有没有存这个item，先去db查一下，如果是null就代表没有保存
        ItemEntity persistedItem = itemRepository.findByTwitchId(item.twitchId());
        if (persistedItem == null) {//没有保存，先存到db里
            persistedItem = itemRepository.save(item);
        }

        if (favoriteRecordRepository.existsByUserIdAndItemId(user.id(), persistedItem.id())) {//如果已经favorite了
            throw new DuplicateFavoriteException();
        }

        FavoriteRecordEntity favoriteRecord = new FavoriteRecordEntity(null, user.id(), persistedItem.id(), Instant.now());
        favoriteRecordRepository.save(favoriteRecord);
    }

    @CacheEvict(cacheNames = "recommend_items", key = "#root.args[0]")
    public void unsetFavoriteItem(UserEntity user, String twitchId) {
        ItemEntity item = itemRepository.findByTwitchId(twitchId);
        if (item != null) {
            favoriteRecordRepository.delete(user.id(), item.id());
        }
    }

    public List<ItemEntity> getFavoriteItems(UserEntity user) {
        List<Long> favoriteItemIds = favoriteRecordRepository.findFavoriteItemIdsByUserId(user.id());
        return itemRepository.findAllById(favoriteItemIds);
    }

    public TypeGroupedItemList getGroupedFavoriteItems(UserEntity user) {
        List<ItemEntity> items = getFavoriteItems(user);
        return new TypeGroupedItemList(items);
    }
}
