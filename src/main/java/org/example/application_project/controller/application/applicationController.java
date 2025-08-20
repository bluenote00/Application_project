package org.example.application_project.controller.application;

import lombok.RequiredArgsConstructor;
import org.example.application_project.entity.application.ApplicationEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/appliction")
@RequiredArgsConstructor
public class applicationController {

    @GetMapping("/")
    public ApplicationEntity<ApplicationEntity> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }
}
