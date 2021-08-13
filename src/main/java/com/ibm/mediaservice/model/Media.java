package com.ibm.mediaservice.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ibm.mediaservice.dto.constants.MediaType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Media implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private String id;
	private Integer userId;
	private String fileName;
	private String caption;
	private Boolean hidden;
	private String description;
	private String mediaID;
	private String effects;
	private List<String> tags=new ArrayList<>();
	private MediaType type;
	private String videoPoster;
	private String fileType;
	private Long fileSize;
	private LocalDateTime uploadedDateTime;
	private Boolean defualtProfile;
	private List<Integer> like;
	private List<Integer> unlike;
	private Long shares;
	private Long noOfComments;
}
