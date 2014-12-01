package com.eclipsesource.jaxrs.sample.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleMessage {

    private long timeStamp;
    private String message;

}
