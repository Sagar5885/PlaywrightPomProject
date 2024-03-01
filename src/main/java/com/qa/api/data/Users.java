package com.qa.api.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    private String id;
    private String name;
    private String email;
    private String gender;
    private String status;
}
