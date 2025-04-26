package com.bulish.melnikov.converter.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
@NoArgsConstructor
public class ConvertRequestMsgDTO implements Serializable {

    private byte [] file;

    private String formatTo;

    private String formatFrom;
}
