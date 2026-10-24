package com.taskmaster.controller;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.Controller;
@Controller
public class NotificationController {
    @MessageMapping("/teams/{teamId}/subscribe")
    @SendTo("/topic/teams/{teamId}/presence")
    public String subscribe(@DestinationVariable Long teamId,String username){return username+" joined team "+teamId;}
}
