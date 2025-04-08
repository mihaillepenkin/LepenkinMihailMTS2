package org.example.controller;

import org.example.exception.BucketNotFoundException;
import org.example.exception.FileNotFoundException;
import org.example.service.BucketService;
import org.example.service.FileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FileControllerImplementationRate.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(FileControllerImplementationRate.class)
@ContextConfiguration(classes = {FileControllerImplementationRate.class, FileService.class})
class FilesControllerImplementationTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FileService fileService;
    @MockBean
    private BucketService bucketService;

    @Test
    void shouldSuccessfullyCreateFile() throws Exception {
        mockMvc.perform(post("/files/create/Company/Google")).andExpect(status().isOk());
    }

    @Test
    void shouldSuccessfullyRenameFile() throws Exception {
        mockMvc.perform(patch("/files/info/Google/Yandex")).andExpect(status().isOk());
    }

    @Test
    void shouldFailToRenameFile() throws Exception {
        doThrow(FileNotFoundException.class).when(fileService).renameFile(any(String.class), any(String.class));
        mockMvc.perform(patch("/files/info/notFoundOldFileName/newFileName")).andExpect(status().isNotFound());
    }

    @Test
    void shouldSuccessfullyReplaceFile() throws Exception {
        mockMvc.perform(patch("/files/info/Company/Yandex/Games")).andExpect(status().isOk());
    }

    @Test
    void shouldFailToReplaceFile() throws Exception {
        doThrow(FileNotFoundException.class).when(fileService).replaceFile(any(String.class), any(String.class), any(String.class));
        mockMvc.perform(patch("/files/info/notFoundOldBucketName/fileName/newBucketName")).andExpect(status().isNotFound());
    }

    @Test
    void shouldSuccessfullyDeleteFile() throws Exception {
        mockMvc.perform(delete("/files/info/Yandex")).andExpect(status().isOk());
    }

    @Test
    void shouldFailToDeleteFile() throws Exception {
        doThrow(FileNotFoundException.class).when(fileService).deleteFile(any(String.class));
        mockMvc.perform(delete("/files/info/notFoundFileName")).andExpect(status().isNotFound());
    }
}