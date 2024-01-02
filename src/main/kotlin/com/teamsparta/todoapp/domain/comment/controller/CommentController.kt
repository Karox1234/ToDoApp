package com.teamsparta.todoapp.domain.comment.controller


import com.teamsparta.todoapp.domain.comment.dto.CommentResponse
import com.teamsparta.todoapp.domain.comment.dto.CreateCommentRequest
import com.teamsparta.todoapp.domain.comment.dto.UpdateCommentRequest
import com.teamsparta.todoapp.domain.comment.service.CommentService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/comment")
@RestController
//TODO: 카드 컨트롤러를 뼈대로 수정하면서 필요한 부분,필요없는 부분 생성 및 삭제
class CommentController(private val commentService: CommentService)
{

//    @GetMapping()
//    fun getCommentList(): ResponseEntity<List<CommentResponse>> {
//        return ResponseEntity
//            .status(HttpStatus.OK).body(commentService.getAllCommentList())
//
//    }
    //TODO: 카드같은 경우는 모든 카드를 보여주는게 필요했지만, 댓글은 각 id에 해당하는 카드에 해당하는 댓글만 보여주면 되니 모든 댓글을 가져올 필요가 있을까?
    //TODO: 아마 id에 대응되는 댓글만 불러오면 되니 이 밑에 기능인 getComment 만 있으면 되지 않을까?..

    @GetMapping("/{commentId}")
    fun getComment(@PathVariable commentId: Long): ResponseEntity<CommentResponse> {
        return ResponseEntity
            .status(HttpStatus.OK).body(commentService.getCommentById(commentId))

    }

    @PostMapping
    fun createComment(@RequestBody createCommentRequest: CreateCommentRequest): ResponseEntity<CommentResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED).body(commentService.createComment(createCommentRequest))

    }

    @PutMapping("/{commentId}")
    fun updateComment(
        @PathVariable commentId: Long,
        @RequestBody updateCommentRequest: UpdateCommentRequest
    ): ResponseEntity<CommentResponse> {
        return ResponseEntity
            .status(HttpStatus.OK).body(commentService.updateComment(commentId,updateCommentRequest))

    }

    @DeleteMapping("/{commentId}")
    fun deleteComment(@PathVariable commentId: Long): ResponseEntity<Unit> {
        commentService.deleteComment(commentId)
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT).build()

    }

}