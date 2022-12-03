package com.example.demo_tdd_security.item;

import com.example.demo_tdd_security.item.rest.ItemController;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;

public class ItemControllerTests {

    private ItemStore mockItemStore;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(){
        mockItemStore = mock(ItemStore.class);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new ItemController(mockItemStore))
                .build();
    }
}
