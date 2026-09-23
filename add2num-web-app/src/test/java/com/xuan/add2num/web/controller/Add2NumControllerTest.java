package com.xuan.add2num.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean; // Chuẩn SB 4.x thay cho @MockBean
import org.springframework.test.web.servlet.MockMvc; // Sửa lỗi cannot find symbol MockMvc
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.xuan.add2num.web.service.Add2NumService;

@WebMvcTest(Add2NumController.class)
public class Add2NumControllerTest {

    @Autowired
    private MockMvc mockMvc; // Đã được import chuẩn ở dòng 7

    @MockitoBean
    private Add2NumService add2NumService; // Giả lập Service theo chuẩn Spring Boot 4

    @Test
    void testAddEndpoint() throws Exception {
        mockMvc.perform(post("/api/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("stn1", "10")
                .param("stn2", "20"))
                .andExpect(status().isOk());
    }
}
