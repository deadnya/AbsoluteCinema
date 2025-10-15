package com.absolute.cinema.controller;

import com.absolute.cinema.dto.MediaDTO;
import com.absolute.cinema.entity.Media;
import com.absolute.cinema.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/media")
@CrossOrigin
public class MediaController {

    private final MediaService mediaService;

    @PostMapping("/upload")
    public ResponseEntity<MediaDTO> uploadMedia(
            @RequestParam("file") MultipartFile file,
            @RequestParam("mediaType") Media.MediaType mediaType
    ) {
        MediaDTO uploadedMedia = mediaService.uploadMedia(file, mediaType);
        return ResponseEntity.status(HttpStatus.CREATED).body(uploadedMedia);
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getMedia(@PathVariable UUID id) {
        Media media = mediaService.getMediaById(id);
        
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.setContentType(MediaType.parseMediaType(media.getContentType()));
        } catch (Exception e) {
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        }
        headers.setContentLength(media.getContent().length);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + media.getFilename() + "\"");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(media.getContent());
    }

    @GetMapping("/{id}/info")
    public ResponseEntity<MediaDTO> getMediaInfo(@PathVariable UUID id) {
        MediaDTO mediaDTO = mediaService.getMediaDTOById(id);
        return ResponseEntity.ok(mediaDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedia(@PathVariable UUID id) {
        mediaService.deleteMedia(id);
        return ResponseEntity.noContent().build();
    }
}