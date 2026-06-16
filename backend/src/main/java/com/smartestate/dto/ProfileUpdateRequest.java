package com.smartestate.dto;

import lombok.Data;

@Data
public class ProfileUpdateRequest {
    private String nickname;
    private String avatar;
    private String building;
    private String unit;
    private String room;
}
