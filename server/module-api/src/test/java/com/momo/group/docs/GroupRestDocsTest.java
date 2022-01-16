package com.momo.group.docs;


import static com.momo.CommonFileUploadSupport.uploadMockSupport;
import static com.momo.CommonFileUploadSupport.uploadTestFile;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.fileUpload;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.momo.RestDocsControllerTest;
import com.momo.api.group.GroupController;
import com.momo.domain.district.entity.City;
import com.momo.domain.group.dto.GroupCreateRequest;
import com.momo.domain.group.dto.GroupResponse;
import com.momo.domain.group.entity.Category;
import com.momo.domain.group.service.GroupService;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(GroupController.class)
@DisplayName("모임 문서화 테스트")
public class GroupRestDocsTest extends RestDocsControllerTest {

    @InjectMocks
    public GroupController groupController;

    @MockBean
    public GroupService groupService;

    @Test
    public void 모임_생성_테스트() throws Exception {
        when(groupService.create(any(), any())).thenReturn(1L);
        GroupCreateRequest request = GroupCreateRequest.builder()
            .name("A 모임")
            .category(Category.HOBBY)
            .isUniversity(true)
            .city(City.SEOUL)
            .district("마포구")
            .startDate(LocalDate.now())
            .recruitmentCnt(4)
            .introduction("테스트 모임입니다.")
            .isOffline(false)
            .image(uploadTestFile)
            .build();
        super.mockMvc.perform(uploadMockSupport(fileUpload("/api/group"), request)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isCreated())
            .andDo(GroupDocumentation.createGroup());
    }

    @Test
    public void 모임_상세_조회() throws Exception {
        when(groupService.findById(any(), any())).thenReturn(GroupResponse.builder()
            .id(1L)
            .managerId(2L)
            .name("A 모임")
            .imageUrl("http://~~")
            .startDate(LocalDate.now())
            .university("한국대")
            .city(City.SEOUL)
            .district("마포구")
            .isOffline(true)
            .introduction("테스트 모임입니다.")
            .recruitmentCnt(4)
            .isEnd(false)
            .participantCnt(2L)
            .isParticipant(true)
            .build()
        );
        long groupId = 1L;
        super.mockMvc.perform(get("/api/group/{id}", groupId))
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(GroupDocumentation.findGroup());
    }

    @Test
    public void 모임_카테고리_목록_조회() throws Exception {
        super.mockMvc.perform(get("/api/group/categories"))
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(GroupDocumentation.findGroupCategories());
    }

    @Test
    public void 모임장_권한_양도() throws Exception {
        super.mockMvc.perform(patch("/api/group/{id}/manager/{userId}", 1L, 2L))
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(GroupDocumentation.updateManagerByUserId());
    }

    @Test
    public void 모임_종료() throws Exception {
        super.mockMvc.perform(patch("/api/group/{id}/end", 1L))
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(GroupDocumentation.endGroupById());
    }
}
