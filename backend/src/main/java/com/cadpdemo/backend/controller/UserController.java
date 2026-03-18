package com.cadpdemo.backend.controller;

import com.cadpdemo.backend.model.User;
import com.cadpdemo.backend.repository.UserRepository;
import com.cadpdemo.backend.service.EncryptionService;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserRepository userRepo;
    private final EncryptionService enc;

    public UserController(UserRepository userRepo, EncryptionService enc) {
        this.userRepo = userRepo;
        this.enc = enc;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody Map<String, String> req)
        throws Exception {
        User user = new User();
        user.setName(req.get("name"));
        user.setEncryptedEmail(enc.encryptField(req.get("email")));
        user.setEncryptedSsn(enc.encryptField(req.get("ssn")));
        User saved = userRepo.save(user);
        return ResponseEntity.ok(
            Map.of("id", saved.getId(), "message", "User saved securely.")
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) throws Exception {
        User user = userRepo
            .findById(id)
            .orElseThrow(() -> new RuntimeException("User not found: " + id));
        return ResponseEntity.ok(
            Map.of(
                "id",
                user.getId(),
                "name",
                user.getName(),
                "email",
                enc.decryptField(user.getEncryptedEmail()),
                "ssn",
                enc.decryptField(user.getEncryptedSsn())
            )
        );
    }
}
