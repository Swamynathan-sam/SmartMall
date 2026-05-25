package com.smartmall.productservice.command.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileStorageServiceTest {

    private FileStorageService fileStorageService;

    @BeforeEach
    void setup() {
        fileStorageService = new FileStorageService();

        ReflectionTestUtils.setField(
                fileStorageService,
                "uploadDir",
                "test-uploads"
        );
    }

    @Test
    void shouldUploadFile() throws IOException {

        MockMultipartFile file =
                new MockMultipartFile(
                        "file",
                        "test.jpg",
                        "image/jpeg",
                        "dummy data".getBytes()
                );

        String result =
                fileStorageService.uploadFile(file);

        assertNotNull(result);

        assertTrue(
                result.endsWith(".jpg")
        );
    }

    @Test
    void shouldThrowExceptionForBlankFileName() {

        MockMultipartFile file =
                new MockMultipartFile(
                        "file",
                        "",
                        "image/jpeg",
                        "dummy data".getBytes()
                );

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> fileStorageService.uploadFile(file)
                );

        assertEquals(
                "Invalid file name",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionForLargeFile() {

        byte[] data =
                new byte[6 * 1024 * 1024];

        MockMultipartFile file =
                new MockMultipartFile(
                        "file",
                        "large.jpg",
                        "image/jpeg",
                        data
                );

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> fileStorageService.uploadFile(file)
                );

        assertEquals(
                "File too large",
                exception.getMessage()
        );
    }
}