package com.linkedin.linkedin.authentication.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorResponse {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String company;
    private String position;
    private String location;
    private String profilePicture;
    private Boolean profileComplete;

}
