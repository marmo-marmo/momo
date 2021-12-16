package com.momo.user.domain.model;

import com.momo.common.domain.BaseEntity;
import com.momo.group.domain.model.Category;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SocialProvider provider;

    @Column(nullable = false)
    private String providerId;

    @Column(nullable = false)
    private String refreshToken;

    private String nickname;

    private String imageUrl;

    private String city;

    private String district;

    private String university;

    @Embedded
    private final FavoriteCategories favoriteCategories = FavoriteCategories.empty();

    @Builder
    public User(String providerId, SocialProvider provider, String nickname, String imageUrl, String city,
        String district, String university, String refreshToken) {
        this.providerId = providerId;
        this.provider = provider;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.city = city;
        this.district = district;
        this.university = university;
        this.refreshToken = refreshToken;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public boolean isSameNickname(String nickname) {
        return this.nickname.equals(nickname);
    }

    public boolean isSameUser(User user) {
        return id.equals(user.getId());
    }

    public void update(User user) {
        this.nickname = user.getNickname();
        this.city = user.getCity();
        this.district = user.getDistrict();
        this.university = user.getUniversity();
    }

    public void updateFavoriteCategories(List<Category> categories) {
        favoriteCategories.updateAll(this, categories);
    }
}
