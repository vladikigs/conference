package com.waveaccess.conference.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PresentationDto {

    private Long id;
    private String name;
    private Date dateTimeStartPresentation;
    private Date dateTimeEndPresentation;
    private String audience;
    public void setDateTimeStartPresentation( String dateTimeStartPresentation) {
        this.dateTimeStartPresentation = parseStringDate(dateTimeStartPresentation);
    }

    public void setDateTimeEndPresentation(String dateTimeEndPresentation) {
        this.dateTimeEndPresentation = parseStringDate(dateTimeEndPresentation);
    }

    @NonNull
    private Date parseStringDate(String date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter
                .ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSSXXXXX");

        OffsetDateTime offsetDateTime = OffsetDateTime.parse(date, dateTimeFormatter)
                .withOffsetSameInstant(ZoneOffset.UTC);

        return new Date(offsetDateTime.toInstant().toEpochMilli());
    }
}
