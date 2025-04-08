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

@WebMvcTest(BucketControllerImplementationRate.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(BucketControllerImplementationRate.class)
@ContextConfiguration(classes = {BucketControllerImplementationRate.class, BucketService.class})
class BucketControllerImplementationTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private FileService fileService;
    @MockBean private BucketService bucketService;



    @Test
    void shouldSuccessfullyCreateBucket() throws Exception {
        mockMvc.perform(post("/buckets/create/Company")).andExpect(status().isOk());
        mockMvc.perform(post("/buckets/create/Games")).andExpect(status().isOk());
        mockMvc.perform(post("/buckets/create/Books")).andExpect(status().isOk());
    }


    @Test
    void shouldSuccessfullyRenameBucket() throws Exception {
        mockMvc.perform(patch("/buckets/info/Company/Paper")).andExpect(status().isOk());
    }

    @Test
    void shouldFailToRenameBucket() throws Exception {
        doThrow(BucketNotFoundException.class).when(bucketService).renameBucket(any(String.class), any(String.class));
        mockMvc.perform(patch("/buckets/info/notFoundOldBucketName/newBucketName")).andExpect(status().isNotFound());
    }
    @Test
    void shouldSuccessfullyDeleteBucket() throws Exception {
        mockMvc.perform(delete("/buckets/info/Books")).andExpect(status().isOk());
    }

    @Test
    void shouldFailToDeleteBucket() throws Exception {
        doThrow(BucketNotFoundException.class).when(bucketService).deleteBucket(any(String.class));
        mockMvc.perform(delete("/buckets/info/notFoundBucketName")).andExpect(status().isNotFound());
    }
}