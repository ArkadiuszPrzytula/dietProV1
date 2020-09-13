package com.pl.arkadiusz.diet_pro.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Data
@Builder
public class Email {
//    private String from;
    private String to;
    private String subject;
    private String messageText;
    private Optional<List<MailFiles>> inLinesFiles;
    private Optional<List<MailFiles>> attachments;


}
