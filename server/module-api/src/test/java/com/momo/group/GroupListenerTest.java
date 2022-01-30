package com.momo.group;

import static org.assertj.core.api.Assertions.assertThat;

import com.momo.common.SpringContainerTest;
import com.momo.domain.achievementrate.entity.GroupAchievementRate;
import com.momo.domain.achievementrate.repository.GroupAchievementRateRepository;
import com.momo.domain.group.entity.Group;
import com.momo.domain.group.repository.GroupRepository;
import com.momo.domain.user.entity.User;
import com.momo.domain.user.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("모임 리스너 테스트")
public class GroupListenerTest extends SpringContainerTest {

    @Autowired
    private GroupAchievementRateRepository groupAchievementRateRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    private Group group;

    @BeforeEach
    void before() {
        User user = userRepository.save(
            User.builder()
                .providerId("test")
                .nickname("유저")
                .build()
        );
        group = groupRepository.save(
            Group.builder()
                .name("모임 이름")
                .manager(user)
                .build()
        );
    }

    @Test
    void 모임을_저장하면_저장_리스너가_실행된다() {
        verifyGroupPersistListener();
        verifyExistsGroupAchievementRate();
    }

    private void verifyGroupPersistListener() {
        List<GroupAchievementRate> actual = groupAchievementRateRepository.findAll();
        Assertions.assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.size()).isEqualTo(1),
            () -> assertThat(actual.get(0).getId()).isNotNull(),
            () -> assertThat(actual.get(0).getGroup().getId()).isEqualTo(group.getId()),
            () -> assertThat(actual.get(0).getRate()).isNotNull()
        );
    }

    private void verifyExistsGroupAchievementRate() {
        Group actual = groupRepository.findById(this.group.getId()).get();
        assertThat(actual.getAchievementRate()).isNotNull();
    }
}
