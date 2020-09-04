package com.pl.arkadiusz.diet_pro.dto;

import lombok.Data;

import java.util.Date;

@Data
public class VerificationTokenDTO {
    private Long id;

    private String token;

    private Long userId;

    private Date expiryDate;
}
