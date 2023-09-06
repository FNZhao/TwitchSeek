package com.zhaofn.twitch.db.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("users")
public record UserEntity(
        @Id Long id,
        String username,
        String firstName,
        String lastName,
        String password
        //这些field会自动和db里的column匹配
) {
}
