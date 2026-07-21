package org.example.backend.controller;

import jakarta.validation.Valid;
import org.example.backend.dto.request.SendMessageRequest;
import org.example.backend.dto.response.ChatMessageResponse;
import org.example.backend.entity.UserEntity;
import org.example.backend.service.MessageService;
import org.example.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Represents message controller.
 */
@RestController
@RequestMapping("/api/conversations/{conversationId}/messages")
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;

    /**
     * Constructs a new MessageController.
     * @param messageService the message service
     * @param userService the user service
     */
    public MessageController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    /**
     * Resolves the authenticated User entity from the JWT-backed Authentication.
     * @param authentication the authentication
     * @return the result
     */
    private UserEntity currentUser(Authentication authentication) {
        return userService.getUserEntityByUsername(authentication.getName());
    }

    /**
     * GET /api/conversations/{conversationId}/messages -> self, all messages in the conversation, chronological order (participant only).
     * @param conversationId the conversation id
     * @param authentication the authentication
     * @return the result
     */
    @GetMapping
    public ResponseEntity<List<ChatMessageResponse>> getConversationMessages(
            @PathVariable Long conversationId, Authentication authentication) {
        return ResponseEntity.ok(
                messageService.getConversationMessages(conversationId, currentUser(authentication)));
    }

    /**
     * POST /api/conversations/{conversationId}/messages -> self, send a message Both sides must be ACTIVE.
     * @param conversationId the conversation id
     * @param authentication the authentication
     * @param request the request
     * @return the result
     */
    @PostMapping
    public ResponseEntity<ChatMessageResponse> sendMessage(
            @PathVariable Long conversationId, Authentication authentication,
            @Valid @RequestBody SendMessageRequest request) {
        ChatMessageResponse response =
                messageService.sendMessage(conversationId, currentUser(authentication), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * PUT /api/conversations/{conversationId}/messages/seen -> self, mark all unseen messages from the other participant as seen.
     * @param conversationId the conversation id
     * @param authentication the authentication
     * @return the result
     */
    @PutMapping("/seen")
    public ResponseEntity<Void> markMessagesAsSeen(
            @PathVariable Long conversationId, Authentication authentication) {
        messageService.markMessagesAsSeen(conversationId, currentUser(authentication));
        return ResponseEntity.noContent().build();
    }
}