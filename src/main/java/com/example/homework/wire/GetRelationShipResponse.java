package com.example.homework.wire;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
@Data
public class GetRelationShipResponse {
    String storyMessage;
    UUID id;
}
