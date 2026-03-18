package com.cadpdemo.backend.controller;

import com.cadpdemo.backend.model.FileMeta;
import com.cadpdemo.backend.repository.FileMetaRepository;
import com.cadpdemo.backend.service.EncryptionService;
import java.time.Instant;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
@CrossOrigin("*")
public class FileController {

    private final FileMetaRepository fileRepo;
    private final EncryptionService enc;

    public FileController(FileMetaRepository fileRepo, EncryptionService enc) {
        this.fileRepo = fileRepo;
        this.enc = enc;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file)
        throws Exception {
        if (file.isEmpty()) return ResponseEntity.badRequest().body(
            Map.of("error", "File is empty.")
        );
        if (
            file.getSize() > 50L * 1024 * 1024
        ) return ResponseEntity.badRequest().body(
            Map.of("error", "File exceeds 50MB limit.")
        );

        byte[] encryptedBytes = enc.encryptFile(file.getInputStream());

        FileMeta meta = new FileMeta();
        meta.setOriginalName(file.getOriginalFilename());
        meta.setContentType(file.getContentType());
        meta.setEncryptedData(encryptedBytes);
        meta.setUploadedAt(Instant.now());
        fileRepo.save(meta);

        return ResponseEntity.ok(
            Map.of(
                "id",
                meta.getId(),
                "message",
                "File encrypted and stored successfully."
            )
        );
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> download(@PathVariable Long id)
        throws Exception {
        FileMeta meta = fileRepo
            .findById(id)
            .orElseThrow(() -> new RuntimeException("File not found: " + id));
        byte[] decrypted = enc.decryptFile(meta.getEncryptedData());

        return ResponseEntity.ok()
            .header(
                "Content-Disposition",
                "attachment; filename=\"" + meta.getOriginalName() + "\""
            )
            .contentType(MediaType.parseMediaType(meta.getContentType()))
            .body(decrypted);
    }
}
