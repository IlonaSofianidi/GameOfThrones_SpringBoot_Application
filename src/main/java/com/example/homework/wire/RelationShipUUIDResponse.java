package com.example.homework.wire;

import lombok.Builder;
import lombok.Value;
import java.util.UUID;

@Builder
@Value
public class RelationShipUUIDResponse {
    String characterName1;
    String characterName2;
    UUID id;
}
