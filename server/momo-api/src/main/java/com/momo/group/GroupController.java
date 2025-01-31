package com.momo.group;

import com.momo.auth.CurrentUser;
import com.momo.common.dto.EnumResponse;
import com.momo.group.application.GroupService;
import com.momo.group.application.dto.request.GroupCreateRequest;
import com.momo.group.application.dto.request.GroupUpdateRequest;
import com.momo.group.application.dto.response.GroupCreateResponse;
import com.momo.group.application.dto.response.GroupImageUpdateResponse;
import com.momo.group.application.dto.response.GroupResponse;
import com.momo.user.domain.User;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/group")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    public ResponseEntity<GroupCreateResponse> createGroup(
        @CurrentUser User user,
        @Valid @ModelAttribute GroupCreateRequest groupCreateRequest
    ) throws URISyntaxException {
        GroupCreateResponse response = groupService.createGroup(user, groupCreateRequest);
        return ResponseEntity.created(new URI("/api/group/" + response.getId())).body(response);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<GroupResponse> findGroup(
        @CurrentUser User user,
        @PathVariable Long groupId
    ) {
        GroupResponse groupResponse = groupService.findGroup(user, groupId);
        return ResponseEntity.ok(groupResponse);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<EnumResponse>> findGroupCategories() {
        List<EnumResponse> responses = EnumResponse.listOfCategory();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/update-information")
    public ResponseEntity<Void> updateGroupInformation(
        @CurrentUser User user,
        @Valid @RequestBody GroupUpdateRequest request
    ) {
        groupService.updateGroupInformation(user, request);
        return ResponseEntity.ok().build();
    }

    /*
    TODO : 테스트를 위해 임시로 HTTP 메서드를 POST로 설정
    */
    @PostMapping("/{groupId}/update-image")
    public ResponseEntity<GroupImageUpdateResponse> updateGroupImage(
        @CurrentUser User user,
        @PathVariable Long groupId,
        @RequestParam MultipartFile imageFile
    ) {
        GroupImageUpdateResponse response = groupService.updateGroupImage(user, groupId, imageFile);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{groupId}/manager/{userId}")
    public ResponseEntity<Void> updateManager(
        @CurrentUser User user,
        @PathVariable Long groupId, @PathVariable Long userId
    ) {
        groupService.updateManager(user, groupId, userId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{groupId}/end")
    public ResponseEntity<Void> endGroup(
        @CurrentUser User user,
        @PathVariable Long groupId
    ) {
        groupService.endGroup(user, groupId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{groupId}/delete-image")
    public ResponseEntity<Void> deleteGroupImage(
        @CurrentUser User user,
        @PathVariable Long groupId
    ) {
        groupService.deleteGroupImage(user, groupId);
        return ResponseEntity.noContent().build();
    }
}
