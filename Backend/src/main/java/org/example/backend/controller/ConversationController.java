package org.example.backend.controller;

import org.example.backend.dto.response.ConversationResponse;
import org.example.backend.entity.AdvertisementEntity;
import org.example.backend.entity.UserEntity;
import org.example.backend.service.AdvertisementService;
import org.example.backend.service.ConversationService;
import org.example.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

    private final ConversationService conversationService;
    private final AdvertisementService advertisementService;
    private final UserService userService;

    public ConversationController(ConversationService conversationService,
                                  AdvertisementService advertisementService,
                                  UserService userService) {
        this.conversationService = conversationService;
        this.advertisementService = advertisementService;
        this.userService = userService;
    }

    // Resolves the authenticated User entity from the JWT-backed Authentication.
    private UserEntity currentUser(Authentication authentication) {
        return userService.getUserEntityByUsername(authentication.getName());
    }

    // POST /api/conversations/advertisements/{advertisementId} -> self, start a
    // new conversation with the ad owner, or return the existing one
    @PostMapping("/advertisements/{advertisementId}")
    public ResponseEntity<ConversationResponse> startOrGetConversation(
            @PathVariable Long advertisementId, Authentication authentication) {
        AdvertisementEntity advertisement = advertisementService.getAdvertisementEntityById(advertisementId);
        ConversationResponse response = conversationService.startOrGetConversation(currentUser(authentication), advertisement);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /api/conversations -> self, all conversations for the current user
    // (for both buyer and seller), most recently active first
    @GetMapping
    public ResponseEntity<List<ConversationResponse>> getMyConversations(Authentication authentication) {
        return ResponseEntity.ok(conversationService.getUserConversations(currentUser(authentication)));
    }

    // GET /api/conversations/{id} -> self, single conversation (participant only)
    @GetMapping("/{id}")
    public ResponseEntity<ConversationResponse> getConversationById(
            @PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(conversationService.getConversationById(id, currentUser(authentication)));
    }
}