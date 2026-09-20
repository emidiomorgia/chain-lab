package com.chainlab.backend.controller;

import com.chainlab.backend.controller.dto.BlockCreatedResponse;
import com.chainlab.backend.service.BlockService;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BlockControllerTest {

    @Mock
    BlockService blockService;

    @Mock
    FileUpload fileUpload;

    @InjectMocks
    BlockController blockController;

    @Test
    void uploadFileReturnsCreatedWithIndexWhenFileIsValid() {
        when(fileUpload.size()).thenReturn(11L);
        when(fileUpload.fileName()).thenReturn("hello.txt");
        when(fileUpload.uploadedFile()).thenReturn(Path.of("hello.txt"));
        when(blockService.createBlock(anyString(), anyLong(), any(Path.class))).thenReturn(1L);

        Response response = blockController.uploadFile(fileUpload);

        assertEquals(201, response.getStatus());
        assertEquals(1L, ((BlockCreatedResponse) response.getEntity()).index());
        verify(blockService).createBlock("hello.txt", 11L, Path.of("hello.txt"));
    }

    @Test
    void uploadFileReturnsBadRequestWhenFileIsNull() {
        Response response = blockController.uploadFile(null);

        assertEquals(400, response.getStatus());
        verify(blockService, never()).createBlock(anyString(), anyLong(), any(Path.class));
    }

    @Test
    void uploadFileReturnsBadRequestWhenFileIsEmpty() {
        when(fileUpload.size()).thenReturn(0L);

        Response response = blockController.uploadFile(fileUpload);

        assertEquals(400, response.getStatus());
        verify(blockService, never()).createBlock(anyString(), anyLong(), any(Path.class));
    }
}
