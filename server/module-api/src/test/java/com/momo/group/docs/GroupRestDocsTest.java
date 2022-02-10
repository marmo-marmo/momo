package com.momo.group.docs;


import static com.momo.GroupFixture.getGroupCreateRequest;
import static com.momo.GroupFixture.getGroupResponse;
import static com.momo.common.CommonFileUploadSupport.uploadMockSupport;
import static com.momo.domain.group.entity.Category.LIFE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.fileUpload;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.momo.api.group.GroupController;
import com.momo.common.RestDocsControllerTest;
import com.momo.domain.group.dto.GroupCreateRequest;
import com.momo.domain.group.service.impl.GroupServiceImpl;
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
    public GroupServiceImpl groupService;

    @Test
    void 모임_생성_테스트() throws Exception {
        GroupCreateRequest request = getGroupCreateRequest(LIFE, true);
        when(groupService.create(any(), any())).thenReturn(getGroupResponse());

        super.mockMvc.perform(uploadMockSupport(fileUpload("/api/group"), request)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isCreated())
            .andDo(GroupDocumentation.createGroup());
    }

    @Test
    void 모임_상세_조회() throws Exception {
        when(groupService.findById(any(), any())).thenReturn(getGroupResponse());

        super.mockMvc.perform(get("/api/group/{id}", 1L))
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(GroupDocumentation.findGroup());
    }

    @Test
    void 모임_카테고리_목록_조회() throws Exception {
        super.mockMvc.perform(get("/api/group/categories"))
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(GroupDocumentation.findGroupCategories());
    }

    @Test
    void 모임장_권한_양도() throws Exception {
        super.mockMvc.perform(patch("/api/group/{id}/manager/{userId}", 1L, 2L))
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(GroupDocumentation.updateManagerByUserId());
    }

    @Test
    void 모임_종료() throws Exception {
        super.mockMvc.perform(patch("/api/group/{id}/end", 1L))
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(GroupDocumentation.endGroupById());
    }
}
