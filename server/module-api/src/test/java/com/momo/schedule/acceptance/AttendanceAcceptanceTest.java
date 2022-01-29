package com.momo.schedule.acceptance;

import static com.momo.common.acceptance.step.AcceptanceStep.assertThatStatusIsOk;
import static com.momo.fixture.AttendanceFixture.getAttendanceCreateRequest;
import static com.momo.fixture.AttendanceFixture.getAttendanceCreateRequests;
import static com.momo.fixture.GroupFixture.GROUP_CREATE_REQUEST1;
import static com.momo.fixture.ScheduleFixture.getScheduleCreateRequest1;
import static com.momo.fixture.UserFixture.getUser1;
import static com.momo.fixture.UserFixture.getUser2;
import static com.momo.fixture.UserFixture.getUser3;
import static com.momo.group.acceptance.step.GroupAcceptanceStep.requestToCreateGroup;
import static com.momo.group.acceptance.step.ParticipantAcceptanceStep.requestToApplyParticipant;
import static com.momo.group.acceptance.step.ParticipantAcceptanceStep.requestToFindParticipants;
import static com.momo.schedule.acceptance.step.AttendanceAcceptanceStep.assertThatFindAttendance;
import static com.momo.schedule.acceptance.step.AttendanceAcceptanceStep.requestToAttendances;
import static com.momo.schedule.acceptance.step.AttendanceAcceptanceStep.requestToCreateAttendance;
import static com.momo.schedule.acceptance.step.ScheduleAcceptanceStep.requestToCreateSchedule;
import static com.momo.user.acceptance.step.UserAcceptanceStep.requestToFindMyInformation;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.momo.common.acceptance.AcceptanceTest;
import com.momo.common.acceptance.step.AcceptanceStep;
import com.momo.domain.schedule.dto.AttendanceResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;

import com.momo.domain.schedule.dto.AttendanceCreateRequests;
import com.momo.domain.user.dto.UserResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class AttendanceAcceptanceTest extends AcceptanceTest {

    @Test
    void 모임_관리자가_일정_출석_체크를_한다() {
        String managerToken = getAccessToken(getUser1());
        String userToken1 = getAccessToken(getUser2());
        String userToken2 = getAccessToken(getUser3());

        Long groupId = extractId(requestToCreateGroup(managerToken, GROUP_CREATE_REQUEST1));

        requestToApplyParticipant(userToken1, groupId);
        requestToApplyParticipant(userToken2, groupId);

        Long scheduleId = extractId(requestToCreateSchedule(managerToken, getScheduleCreateRequest1(groupId)));

        Long managerId = getObject(requestToFindMyInformation(managerToken), UserResponse.class).getId();
        Long userId1 = getObject(requestToFindMyInformation(userToken1), UserResponse.class).getId();
        Long userId2 = getObject(requestToFindMyInformation(userToken2), UserResponse.class).getId();

        AttendanceCreateRequests attendanceCreateRequests = getAttendanceCreateRequests(
            groupId,
            scheduleId,
            List.of(getAttendanceCreateRequest(managerId, true),
                getAttendanceCreateRequest(userId1, true),
                getAttendanceCreateRequest(userId2, false)
            )
        );

        requestToCreateAttendance(managerToken, attendanceCreateRequests);
        requestToFindParticipants(managerToken, groupId);
    }
    @Test
    void 모임_관리자가_일정_출석체크_목록을_조회한다() {
        String managerToken = getAccessToken(getUser1());
        String userToken1 = getAccessToken(getUser2());
        String userToken2 = getAccessToken(getUser3());

        Long groupId = extractId(requestToCreateGroup(managerToken, GROUP_CREATE_REQUEST1));

        requestToApplyParticipant(userToken1, groupId);
        requestToApplyParticipant(userToken2, groupId);

        Long scheduleId = extractId(requestToCreateSchedule(managerToken, getScheduleCreateRequest1(groupId)));

        Long managerId = getObject(requestToFindMyInformation(managerToken), UserResponse.class).getId();
        Long userId1 = getObject(requestToFindMyInformation(userToken1), UserResponse.class).getId();
        Long userId2 = getObject(requestToFindMyInformation(userToken2), UserResponse.class).getId();

        AttendanceCreateRequests attendanceCreateRequests = getAttendanceCreateRequests(
            groupId,
            scheduleId,
            List.of(
                getAttendanceCreateRequest(managerId, true),
                getAttendanceCreateRequest(userId1, true),
                getAttendanceCreateRequest(userId2, false)
            )
        );

        requestToCreateAttendance(managerToken, attendanceCreateRequests);

        ExtractableResponse<Response> response = requestToAttendances(managerToken, groupId);
        List<AttendanceResponse> attendanceResponses = getObjects(response, AttendanceResponse.class);
        assertThatStatusIsOk(response);
        assertThatFindAttendance(attendanceCreateRequests.getAttendanceCreateRequests(), attendanceResponses);
    }
}
