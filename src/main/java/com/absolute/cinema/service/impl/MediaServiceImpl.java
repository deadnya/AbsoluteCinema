package com.absolute.cinema.service.impl;

import com.absolute.cinema.common.exception.custom.BadRequestException;
import com.absolute.cinema.common.exception.custom.NotFoundException;
import com.absolute.cinema.dto.MediaDTO;
import com.absolute.cinema.entity.Media;
import com.absolute.cinema.mapper.MediaMapper;
import com.absolute.cinema.repository.MediaRepository;
import com.absolute.cinema.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {

    private final MediaRepository mediaRepository;
    private final MediaMapper mediaMapper;

    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp"
    );

    private static final List<String> ALLOWED_AUDIO_TYPES = Arrays.asList(
            "audio/mpeg", "audio/wav", "audio/ogg", "audio/mp3"
    );

    private static final List<String> ALLOWED_VIDEO_TYPES = Arrays.asList(
            "video/mp4", "video/avi", "video/mov", "video/webm"
    );

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    @Override
    @Transactional
    public MediaDTO uploadMedia(MultipartFile file, Media.MediaType mediaType) {
        validateFile(file, mediaType);

        try {
            Media media = new Media();
            media.setFilename(generateUniqueFilename(file.getOriginalFilename()));
            media.setContentType(file.getContentType());
            media.setMediaType(mediaType);
            media.setContent(file.getBytes());

            Media savedMedia = mediaRepository.save(media);
            
            return mediaMapper.toDTO(savedMedia);
        } catch (IOException e) {
            throw new BadRequestException("Failed to process uploaded file");
        }
    }

    @Override
    public Media getMediaById(UUID id) {
        return mediaRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Media not found with ID: " + id));
    }

    @Override
    public MediaDTO getMediaDTOById(UUID id) {
        Media media = getMediaById(id);
        return mediaMapper.toDTO(media);
    }

    @Override
    @Transactional
    public void deleteMedia(UUID id) {
        Media media = getMediaById(id);
        mediaRepository.delete(media);
    }

    private void validateFile(MultipartFile file, Media.MediaType mediaType) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File cannot be empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BadRequestException("File size exceeds maximum allowed size of 10MB");
        }

        String contentType = file.getContentType();
        if (contentType == null) {
            throw new BadRequestException("File content type cannot be determined");
        }

        switch (mediaType) {
            case IMAGE:
                if (!ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase())) {
                    throw new BadRequestException("Invalid image file type. Allowed types: " + ALLOWED_IMAGE_TYPES);
                }
                break;
            case AUDIO:
                if (!ALLOWED_AUDIO_TYPES.contains(contentType.toLowerCase())) {
                    throw new BadRequestException("Invalid audio file type. Allowed types: " + ALLOWED_AUDIO_TYPES);
                }
                break;
            case VIDEO:
                if (!ALLOWED_VIDEO_TYPES.contains(contentType.toLowerCase())) {
                    throw new BadRequestException("Invalid video file type. Allowed types: " + ALLOWED_VIDEO_TYPES);
                }
                break;
            default:
                throw new BadRequestException("Unsupported media type: " + mediaType);
        }
    }

    private String generateUniqueFilename(String originalFilename) {
        if (originalFilename == null || originalFilename.isEmpty()) {
            return UUID.randomUUID().toString();
        }
        
        String extension = "";
        int lastDot = originalFilename.lastIndexOf('.');
        if (lastDot > 0) {
            extension = originalFilename.substring(lastDot);
        }
        
        return UUID.randomUUID().toString() + extension;
    }
}