package com.cbfacademy.apiassessment.controller;

/*import com.cbfacademy.apiassessment.service.ChatGPTOutputService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chatGPT")
public class ChatGPTOutputController {

    @Autowired
    private final ChatGPTOutputService chatGPTOutputService;

    public ChatGPTOutputController(
            ChatGPTOutputService chatGPTOutputService) {
        this.chatGPTOutputService = chatGPTOutputService;
    }

    @GetMapping
    public ResponseEntity<String> readChatGPTResponse() {
        try {
            String chatGPTResponse = chatGPTOutputService.getChatGPTClientOutput();
            return ResponseEntity.ok(chatGPTResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

}
*/