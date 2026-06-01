package com.generation153.harmonyfree.core.dto.playlist;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreatePlaylistRequest {

    private Long userId;
    private String title;
    private String description;
}