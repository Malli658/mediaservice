package com.ibm.mediaservice.dto.model;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.googlecode.jmapper.annotations.JMap;
import com.ibm.mediaservice.dto.constants.MediaType;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Relation(collectionRelation = "media", itemRelation = "media")
@JsonInclude(Include.NON_NULL)

public class MediaModel extends RepresentationModel<MediaModel>{
	@JMap
	private String id;
	@JMap
	private String fileName;
	@JMap
	private String caption;
	@JMap
	private Boolean hidden;
	@JMap
	private String description;
	@JMap
	private String effects;
	@JMap
	private List<String> tags;
	@JMap
	private MediaType type;
	@JMap
	private String videoPoster;
	@JMap
	private String fileType;
	@JMap
	private Long fileSize;
	@JMap
	private LocalDateTime uploadedDateTime;
	@JMap
	private Boolean defualtProfile;
	
	@JMap
	private Long shares;
	@JMap
	private Long noOfComments;
	@JMap
	private List<Integer> like;
	@JMap
	private List<Integer> unlike;
	
} 
