package com.ibm.mediaservice.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.springframework.web.multipart.MultipartFile;

import com.googlecode.jmapper.annotations.JMap;
import com.ibm.mediaservice.dto.constants.MediaType;


@Setter
@Getter
@ToString
public class UploadMediaDTO {
	
	@JMap("caption")
	private String mediaTitle;
	@JMap("description")
	private String mediaDescription;
	@JMap
	private List<String> effects=new ArrayList<>();;
	@JMap
	private List<String> tags=new ArrayList<>();
	@JMap
	private String mediaID;
	@JMap
	private Integer userId;
	@JMap
	private Boolean hidden;
	@JMap
	private Boolean defualtProfile;
	@JMap
	private String fileType;
	@JMap
	private Long fileSize;
	@JMap
	private MediaType type;
	@JMap
	private String fileName;
	
	
	
}
