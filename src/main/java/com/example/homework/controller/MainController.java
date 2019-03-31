package com.example.homework.controller;

import com.example.homework.service.RelationshipMatcherService;
import com.example.homework.wire.GetRelationShipResponse;
import com.example.homework.wire.RelationShipUUIDResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
public class MainController {
    private final RelationshipMatcherService relationshipMatcherService;

    @PostMapping(value = "/find", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<RelationShipUUIDResponse> postGameCharactersToFindRelationship(
            @RequestParam(value = "name1") String name1,
            @RequestParam(value = "name2") String name2){

        return ResponseEntity.ok()
                .body(relationshipMatcherService.findRelationshipBetweenCharacters(name1, name2));
    }


    @GetMapping(value = "/{relationStoryId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<GetRelationShipResponse> getRelationShipResultsById(@PathVariable UUID relationStoryId) {
        return ResponseEntity.ok()
                .body(relationshipMatcherService.getRelationShipStory(relationStoryId));
    }

    @GetMapping(value = "/all", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GetRelationShipResponse>>getRelationShipResults() {
        return ResponseEntity.ok()
                .body(relationshipMatcherService.getFullRelationshipStory());
    }
}
