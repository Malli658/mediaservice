package com.ibm.mediaservice.dto;

import java.util.Date;

import com.googlecode.jmapper.annotations.JGlobalMap;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JGlobalMap
public class UploadCommentDTO {
	
	private String mediaId;
    private String parent;
    private String comment;
    private String userID;
    
}
