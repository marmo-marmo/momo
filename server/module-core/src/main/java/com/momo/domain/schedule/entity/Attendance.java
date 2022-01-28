package com.momo.domain.schedule.entity;

import com.momo.domain.group.entity.Group;
import com.momo.domain.user.entity.User;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
    TODO
    sql, table 변경 !
    groupId 와 userId 를 participantId 로 바꿀 수 있지 않을까?
    */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "group_tb_fk_attendance"))
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "schedule_fk_attendance"))
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "user_fk_attendance"))
    private User user;

    private boolean isAttend;

    @Builder
    public Attendance(Long id, Group group, Schedule schedule, User user, boolean isAttend) {
        this.id = id;
        this.group = group;
        this.schedule = schedule;
        this.user = user;
        this.isAttend = isAttend;
    }

    private static Attendance create(Attendance attendance, Group group, Schedule schedule) {
        return Attendance.builder()
            .group(group)
            .schedule(schedule)
            .user(attendance.getUser())
            .isAttend(attendance.isAttend())
            .build();
    }

    public static List<Attendance> createAttendances(List<Attendance> attendances, Group group, Schedule schedule) {
        return attendances.stream()
            .map(attendance -> Attendance.create(attendance, group, schedule))
            .collect(Collectors.toList());
    }

    public void updateAttend(boolean isAttend) {
        this.isAttend = isAttend;
    }
}
