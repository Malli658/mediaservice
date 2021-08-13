package com.ibm.mediaservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "MEDIA")
public class Tag {
	@Id
	private Long id;
	private String tag;

}
