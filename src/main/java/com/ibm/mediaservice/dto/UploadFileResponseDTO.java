package com.ibm.mediaservice.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UploadFileResponseDTO {
   private String fileName;
   private String fileType;
   private Long fileSize;
   private String mediaId;
}
