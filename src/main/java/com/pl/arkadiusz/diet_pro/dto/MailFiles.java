package com.pl.arkadiusz.diet_pro.dto;

import lombok.Data;
import org.springframework.core.io.Resource;

@Data
public class MailFiles {
    String name;
    Resource resource;
    String context;

    public MailFiles(String name, Resource resource, String context) {
        this.name = name;
        this.resource = resource;
        this.context = context;
    }
}
