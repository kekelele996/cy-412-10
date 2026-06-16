package com.smartestate.dto;

import lombok.Data;

@Data
public class RepairRequest {
    private String title;
    private String description;
    private String type;
}
