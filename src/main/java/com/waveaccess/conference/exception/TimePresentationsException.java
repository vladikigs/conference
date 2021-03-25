package com.waveaccess.conference.exception;


import com.waveaccess.conference.entity.Presentation;

import java.util.List;

public class TimePresentationsException extends IllegalArgumentException {

    public List<Presentation> getPresentations() {
        return presentations;
    }

    private final transient List<Presentation> presentations;

    public TimePresentationsException(String msg, List<Presentation> presentations) {
        super(msg);
        this.presentations = presentations;
    }

}
