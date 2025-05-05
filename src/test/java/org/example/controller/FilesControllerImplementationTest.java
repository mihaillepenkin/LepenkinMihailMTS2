package org.example.controller;

import org.example.exception.BucketNotFoundException;
import org.example.exception.FileNotFoundException;
import org.example.service.FilesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FilesControllerImplementation.class)
class FilesControllerImplementationTest {

    @Autowired private MockMvc mockMvc;
    @MockitoBean private FilesService filesService;

    @Test
    void shouldSuccessfullyRenameFile() throws Exception {
        mockMvc.perform(get("/files/info/oldFileName/newFileName")).andExpect(status().isOk());
    }

    @Test
    void shouldFailToRenameFile() throws Exception {
        doThrow(FileNotFoundException.class).when(filesService).renameFile(any(String.class), any(String.class));
        mockMvc.perform(get("/files/info/notFoundOldFileName/newFileName")).andExpect(status().isNotFound());
    }

    @Test
    void shouldSuccessfullyReplaceFile() throws Exception {
        mockMvc.perform(get("/files/info/oldBucketName/fileName/newBucketName")).andExpect(status().isOk());
    }

    @Test
    void shouldFailToReplaceFile() throws Exception {
        doThrow(FileNotFoundException.class).when(filesService).replaceFile(any(String.class), any(String.class), any(String.class));
        mockMvc.perform(get("/files/info/notFoundOldBucketName/fileName/newBucketName")).andExpect(status().isNotFound());
    }
    @Test
    void shouldSuccessfullyDeleteFile() throws Exception {
        mockMvc.perform(get("/files/info/fileName")).andExpect(status().isOk());
    }

    @Test
    void shouldFailToDeleteFile() throws Exception {
        doThrow(FileNotFoundException.class).when(filesService).deliteFile(any(String.class));
        mockMvc.perform(get("/files/info/notFoundFileName")).andExpect(status().isNotFound());
    }

    @Test
    void shouldSuccessfullyRenameBucket() throws Exception {
        mockMvc.perform(get("/buckets/info/oldBucketName/newBucketName")).andExpect(status().isOk());
    }

    @Test
    void shouldFailToRenameBucket() throws Exception {
        doThrow(BucketNotFoundException.class).when(filesService).renameBucket(any(String.class), any(String.class));
        mockMvc.perform(get("/buckets/info/notFoundOldBucketName/newBucketName")).andExpect(status().isNotFound());
    }
    @Test
    void shouldSuccessfullyDeleteBucket() throws Exception {
        mockMvc.perform(get("/buckets/info/bucketName")).andExpect(status().isOk());
    }

    @Test
    void shouldFailToDeleteBucket() throws Exception {
        doThrow(BucketNotFoundException.class).when(filesService).deliteBucket(any(String.class));
        mockMvc.perform(get("/buckets/info/notFoundBucketName")).andExpect(status().isNotFound());
    }
}