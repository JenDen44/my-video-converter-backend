package com.bulish.melnikov.converter.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ConvertResponseMsgDTO implements Serializable {

    private byte [] file;

    private String formatTo;

    private String formatFrom;
}
