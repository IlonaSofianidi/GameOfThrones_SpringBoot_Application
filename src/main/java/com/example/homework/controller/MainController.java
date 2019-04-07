package com.example.homework.controller;

import com.example.homework.config.WebProperties;
import com.example.homework.entity.RelationStory;
import com.example.homework.service.RelationshipMatcherService;
import com.example.homework.util.utilImpl.RelationShipResourceAssembler;
import com.example.homework.wire.GetRelationShipResponse;
import com.example.homework.wire.RelationShipResource;
import com.example.homework.wire.RelationShipUUIDResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stories")
public class MainController {
    private final RelationshipMatcherService relationshipMatcherService;
    private final RelationShipResourceAssembler assemblerRes;
    private final WebProperties webProperties;
    private int INITIAL_PAGE;
    private int INITIAL_PAGE_SIZE;

    @PostConstruct
    public void initProp() {
        INITIAL_PAGE = webProperties.getInitPage();
        INITIAL_PAGE_SIZE = webProperties.getPageSize();

    }

    @PostMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RelationShipUUIDResponse>> postGameCharactersToFindRelationship(
            @RequestParam(value = "name1") String name1,
            @RequestParam(value = "name2") String name2) {
        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/UUID").build()
                .toUri();
        return ResponseEntity.created(location)
                .body(relationshipMatcherService.findRelationshipBetweenCharacters(name1, name2));
    }


    @GetMapping(value = "/{relationStoryId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<GetRelationShipResponse> getRelationShipResultsById(@PathVariable UUID relationStoryId) {
        return ResponseEntity.ok()
                .body(relationshipMatcherService.getRelationShipStory(relationStoryId));
    }

    @GetMapping
    public PagedResources<List<RelationShipResource>> getRelationShipResultsList(@RequestParam("size") Optional<Integer> size,
                                                                                 @RequestParam("page") Optional<Integer> page,
                                                                                 PagedResourcesAssembler assembler) {
        // Evaluate page size. If requested parameter is null, return initial
        int evalPageSize = size.orElse(INITIAL_PAGE_SIZE);
        // Evaluate page. If requested parameter is null or less than 0 (to
        // prevent exception), return initial size. Otherwise, return value of
        // param. decreased by 1.
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get();

        Page<RelationStory> stories = relationshipMatcherService.getFullRelationshipStoryTest(PageRequest.of(evalPage, evalPageSize));
        return assembler.toResource(stories, assemblerRes);
    }
}

