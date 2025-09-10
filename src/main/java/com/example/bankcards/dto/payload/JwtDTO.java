package com.example.bankcards.dto.payload;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class JwtDTO {
    private String token;
    private Date expirationDate;
}
