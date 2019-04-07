package com.example.homework.controller;

import com.example.homework.config.WebProperties;
import com.example.homework.entity.RelationStory;
import com.example.homework.entity.Relationship;
import com.example.homework.exception.RelationShipStoryNotFound;
import com.example.homework.service.serviceImpl.RelationshipMatcherServiceImpl;
import com.example.homework.util.utilImpl.RelationShipResourceAssembler;
import com.example.homework.wire.GetRelationShipResponse;
import com.example.homework.wire.RelationShipUUIDResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MissingServletRequestParameterException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MainControllerTest {
    @MockBean
    RelationShipResourceAssembler relationShipResourceAssembler;
    @MockBean
    RelationshipMatcherServiceImpl relationshipMatcherService;
    @MockBean
    WebProperties webProperties;
    @InjectMocks
    @Autowired
    MainController unit;

    private MockMvc mockMvc;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(unit)
                .setControllerAdvice(new GameCharacterControllerAdvise())
                .build();
    }
    @Test
    public void contexLoads() throws Exception {
        assertThat(unit).isNotNull();
    }

    @Test
    public void whenPostGameCharactersToFindRelationshipThenReturnStatusCreated() throws Exception {
        String expected = "[{\"characterName1\":\"Alice\",\"characterName2\":\"Bob\",\"id\":\"0b6d6fbd-c911-4c01-b5d0-0ff5b319f9c3\"}]";
        UUID id = UUID.fromString("0b6d6fbd-c911-4c01-b5d0-0ff5b319f9c3");
        List<RelationShipUUIDResponse> mockedList = new ArrayList<>();
        mockedList.add(RelationShipUUIDResponse.builder()
                .characterName1("Alice")
                .characterName2("Bob")
                .id(id)
                .build());
        when(relationshipMatcherService.findRelationshipBetweenCharacters("Alice", "Bob")).thenReturn(mockedList);
        MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/stories/")
                .param("name1", "Alice")
                .param("name2", "Bob")).andDo(print()).andExpect(status().isCreated())
                .andReturn().getResponse();
        assertThat(response.getContentType()).isEqualTo("application/json;charset=UTF-8");
        assertThat(response.getContentAsString()).isEqualTo(expected);

    }

    @Test
    public void whenPostGameCharactersToFindRelationShipParameterIsEmptyReturnBadRequest() throws Exception {
        this.mockMvc.perform(post("/api/v1/stories/")
                .param("name1", "Alice"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(status().reason(containsString("Required String parameter 'name2' is not present")));
    }

    @Test
    public void whenGetRelationShipResultsByIdThenReturnStatusOk() throws Exception {
        UUID id = UUID.fromString("0b6d6fbd-c911-4c01-b5d0-0ff5b319f9c3");
        GetRelationShipResponse responseRelation = GetRelationShipResponse.builder()
                .storyMessage("Story message")
                .id(id)
                .build();
        String expected = "{\"storyMessage\":\"Story message\",\"id\":\"0b6d6fbd-c911-4c01-b5d0-0ff5b319f9c3\"}";

        when(relationshipMatcherService.getRelationShipStory(id)).thenReturn(responseRelation);
        MockHttpServletResponse response = this.mockMvc.perform(get("/api/v1/stories/{relationStoryId}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertThat(response.getContentType()).isEqualTo("application/json;charset=UTF-8");
        assertThat(response.getContentAsString()).isEqualTo(expected);

    }


    @Test
    public void getRelationShipResults() throws Exception {
        UUID id = UUID.fromString("0b6d6fbd-c911-4c01-b5d0-0ff5b319f9c3");
        List<RelationStory> list = new ArrayList<>();
        RelationStory relationStory = new RelationStory();
        relationStory.setId(id);
        relationStory.setCharacterName1("Alice");
        relationStory.setCharacterName2("Bob");
        relationStory.setIdCharacter1(1);
        relationStory.setIdCharacter2(2);
        relationStory.setRelationship(Relationship.NONE);
        list.add(relationStory);
        PageImpl page = new PageImpl(list, PageRequest.of(0, 5), list.size());
        when(relationshipMatcherService.getFullRelationshipStoryTest(PageRequest.of(0, 5)))
                .thenReturn(page);
        MockHttpServletResponse response = this.mockMvc.perform(get("/api/v1/stories")
                .param("size", "5")
                .param("page", "0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse();
        ArgumentCaptor<Pageable> pageableCaptor =
                ArgumentCaptor.forClass(Pageable.class);
        verify(relationshipMatcherService).getFullRelationshipStoryTest(pageableCaptor.capture());
        PageRequest pageable = (PageRequest) pageableCaptor.getValue();

        assertThat(pageable.getPageNumber()).isEqualTo(0);
        assertThat(pageable.getPageSize()).isEqualTo(5);
    }

    @Test
    public void whenGetRelationShipNotFoundReturnStatusNotFound() throws Exception {
        UUID id = UUID.fromString("0b6d6fbd-c911-4c01-b5d0-0ff5b319f9c3");
        when(unit.getRelationShipResultsById(id)).thenThrow(new RelationShipStoryNotFound(id.toString()));
        this.mockMvc.perform(get("/api/v1/stories/{relationStoryId}", id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn().getResponse();
    }
}