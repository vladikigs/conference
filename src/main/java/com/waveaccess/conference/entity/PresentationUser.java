package com.waveaccess.conference.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PresentationUser {

    @EmbeddedId
    private PresentationEmbeddable presentation;
    @Enumerated(EnumType.STRING)
    private PresentationRole role = PresentationRole.Listener;

}
