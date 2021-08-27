package com.ibm.mediaservice.model;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class Comment {
	@Id
	private String id;
	private String mediaId;
    private String parent;
    private String comment;
    private String userID;
    private LocalDateTime createdDate;
    private List<Integer> like;
	private List<Integer> unlike;
}
