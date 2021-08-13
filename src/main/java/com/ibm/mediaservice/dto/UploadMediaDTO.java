package com.ibm.mediaservice.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.springframework.web.multipart.MultipartFile;

import com.googlecode.jmapper.annotations.JMap;


@Setter
@Getter
public class UploadMediaDTO {
	
	private MultipartFile image;
	
	@JMap("caption")
	private String mediaTitle;
	@JMap("description")
	private String mediaDescription;
	@JMap
	private String effects;
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
	
}
