package com.pl.arkadiusz.diet_pro.errors;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;
import java.util.*;

@Setter
@Getter
public class ValidErrors {
    private Map<String, String> errors;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime timestamp;
    private String path;

    public ValidErrors(String path) {
        this.timestamp = LocalDateTime.now();
        this.path = path;
    }


}
