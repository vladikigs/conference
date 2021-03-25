package com.waveaccess.conference.dto;

import com.waveaccess.conference.entity.Presentation;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@AllArgsConstructor
public class ExceptionDto {

    private String message;
    private List<Presentation> presentations;
    private HttpStatus httpStatus;

}
