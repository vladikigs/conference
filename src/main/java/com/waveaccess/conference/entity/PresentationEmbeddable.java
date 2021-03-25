package com.waveaccess.conference.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PresentationEmbeddable implements Serializable {

    @OneToOne(cascade = {CascadeType.REMOVE, CascadeType.ALL})
    private Presentation presentation;
    @OneToOne
    private User user;

}
