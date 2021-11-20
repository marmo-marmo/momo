package com.momo.fixture;

import com.momo.user.domain.model.Role;
import com.momo.user.domain.model.SocialProvider;
import com.momo.user.domain.model.User;

public class UserFixture {

    public static final User USER1 = User.builder()
        .nickname("user1")
        .university("대학교1")
        .categories("HEALTH,SELF_DEVELOPMENT")
        .city("서울시")
        .district("강동구")
        .providerId("1")
        .provider(SocialProvider.KAKAO)
        .role(Role.ROLE_USER)
        .build();

    public static final User USER2 = User.builder()
        .nickname("user2")
        .university("대학교2")
        .city("서울시")
        .district("강동구")
        .providerId("2")
        .provider(SocialProvider.KAKAO)
        .role(Role.ROLE_USER)
        .build();

    public static final User USER3 = User.builder()
        .nickname("user3")
        .university("대학교1")
        .city("서울시")
        .district("강남구")
        .providerId("3")
        .provider(SocialProvider.KAKAO)
        .role(Role.ROLE_USER)
        .build();
}
