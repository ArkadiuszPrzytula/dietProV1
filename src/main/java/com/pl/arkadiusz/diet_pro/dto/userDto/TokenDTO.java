package com.pl.arkadiusz.diet_pro.dto.userDto;

import lombok.Data;

import java.util.Date;

@Data
public class TokenDTO {
    private Long id;

    private String token;

    private Long userId;

    private Date expiryDate;
}
