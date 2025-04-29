package com.bulish.melnikov.converter.model;

import com.bulish.melnikov.converter.enums.FileType;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ConvertRequestMsgDTO implements Serializable {

    private String id;

    private byte[] file;

    private String formatTo;

    private String formatFrom;

    private FileType type;
}
