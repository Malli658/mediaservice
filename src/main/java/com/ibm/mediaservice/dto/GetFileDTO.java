package com.ibm.mediaservice.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GetFileDTO {
 private byte[] file;
 private String contentType;
}
