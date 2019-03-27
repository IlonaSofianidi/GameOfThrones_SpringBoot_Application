package com.example.SpringBoot_GameOfThrones.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameCharacter {
    //    @JsonProperty("name")
    private String name;
    private String url;
    private String mother;
    private String father;
    private String spouse;
}
