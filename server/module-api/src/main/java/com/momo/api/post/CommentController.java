package com.momo.api.post;

import com.momo.common.CurrentUser;
import com.momo.domain.post.dto.CommentCreateRequest;
import com.momo.domain.post.dto.CommentResponse;
import com.momo.domain.post.dto.CommentsRequest;
import com.momo.domain.post.dto.CommentsResponse;
import com.momo.domain.post.service.CommentService;
import com.momo.domain.user.entity.User;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @ApiOperation(value = "게시물 댓글 등록")
    @PostMapping("/comment")
    public ResponseEntity<CommentResponse> create(@CurrentUser User user,
                                                  @Valid @RequestBody CommentCreateRequest commentCreateRequest) {
        CommentResponse commentResponse = commentService.create(user, commentCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentResponse);
    }

    @ApiOperation(value = "게시물 댓글 목록 조회")
    @GetMapping("/comments/paging")
    public ResponseEntity<CommentsResponse> findPageByPost(@CurrentUser User user,
                                                           @Valid @ModelAttribute CommentsRequest commentsRequest) {
        CommentsResponse response = commentService.findPageByPostId(user, commentsRequest);
        return ResponseEntity.ok(response);
    }
}
