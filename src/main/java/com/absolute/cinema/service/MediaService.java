package com.absolute.cinema.service;

import com.absolute.cinema.dto.MediaDTO;
import com.absolute.cinema.entity.Media;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface MediaService {
    MediaDTO uploadMedia(MultipartFile file, Media.MediaType mediaType);
    Media getMediaById(UUID id);
    MediaDTO getMediaDTOById(UUID id);
    void deleteMedia(UUID id);
}