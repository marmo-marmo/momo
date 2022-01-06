package com.momo.user.docs;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.momo.RestDocsControllerTest;
import com.momo.api.user.UserController;
import com.momo.domain.district.entity.City;
import com.momo.domain.group.dto.GroupCardResponse;
import com.momo.domain.group.entity.Category;
import com.momo.domain.user.dto.FavoriteCategoriesUpdateRequest;
import com.momo.domain.user.dto.FavoriteGroupCardResponse;
import com.momo.domain.user.dto.FavoriteGroupCreateRequest;
import com.momo.domain.user.dto.UserUpdateRequest;
import com.momo.domain.user.entity.User;
import com.momo.domain.user.service.FavoriteGroupService;
import com.momo.domain.user.service.UserService;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(UserController.class)
@DisplayName("사용자 문서화 테스트")
public class UserRestDocsTest extends RestDocsControllerTest {

    @InjectMocks
    private UserController userController;

    @MockBean
    private UserService userService;

    @MockBean
    private FavoriteGroupService favoriteGroupService;

    @Test
    void 관심_모임_등록() throws Exception {
        FavoriteGroupCreateRequest request = FavoriteGroupCreateRequest.builder()
            .groupId(1L)
            .build();
        String content = super.objectMapper.writeValueAsString(request);
        super.mockMvc.perform(post("/api/user/favorite-group")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(UserDocumentation.createFavoriteGroup());
    }

    @Test
    void 내_정보_조회() throws Exception {
        User user = User.builder()
            .id(1L)
            .nickname("테스트맨")
            .imageUrl("이미지 URL")
            .city(City.SEOUL)
            .district("마포구")
            .university("한국대")
            .build();
        user.updateFavoriteCategories(List.of(Category.LIFE));

        when(oAuthService.findLoginUserByAccessToken(any()))
            .thenReturn(user);

        super.mockMvc.perform(get("/api/user"))
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(UserDocumentation.findMyInformation());
    }

    @Test
    void 닉네임_중복_확인() throws Exception {
        super.mockMvc.perform(get("/api/user/duplicate-nickname")
                .param("nickname", "테스트 이름")
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(UserDocumentation.validateDuplicateNickname());
    }

    @Test
    void 관심_모임_목록_조회() throws Exception {
        List<FavoriteGroupCardResponse> responses = List.of(
            FavoriteGroupCardResponse.builder()
                .id(1L)
                .groupCardResponse(
                    GroupCardResponse.builder()
                        .id(1L)
                        .name("모임 이름")
                        .imageUrl("이미지 URL")
                        .startDate(LocalDate.of(2022, 1, 6))
                        .isOffline(true)
                        .participantCnt(5L)
                        .build()
                )
                .build()
        );

        when(favoriteGroupService.findAll(any())).thenReturn(responses);

        super.mockMvc.perform(get("/api/user/favorite-groups"))
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(UserDocumentation.findFavoriteGroups());
    }

    @Test
    void 내_정보_수정() throws Exception {
        UserUpdateRequest request = UserUpdateRequest.builder()
            .nickname("테스트 이름")
            .university("한국대")
            .city(City.SEOUL)
            .district("마포구")
            .build();
        String content = super.objectMapper.writeValueAsString(request);
        super.mockMvc.perform(patch("/api/user")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(UserDocumentation.update());
    }

    @Test
    void 관심_카테고리_수정() throws Exception {
        FavoriteCategoriesUpdateRequest request = new FavoriteCategoriesUpdateRequest(List.of(Category.LIFE));
        String content = super.objectMapper.writeValueAsString(request);
        super.mockMvc.perform(patch("/api/user/favorite-categories")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(UserDocumentation.updateFavoriteCategories());
    }
}
