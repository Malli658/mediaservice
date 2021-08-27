package com.ibm.mediaservice.dto.model;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.springframework.data.annotation.Id;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.googlecode.jmapper.annotations.JMap;
import com.ibm.mediaservice.dto.constants.MediaType;
import com.ibm.mediaservice.dto.model.MediaModel.MediaModelBuilder;


@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Relation(collectionRelation = "comments", itemRelation = "comments")
@JsonInclude(Include.NON_NULL)
public class CommentModel extends RepresentationModel<CommentModel>{
	@JMap
	private String id;
	@JMap
    private String comment;
	@JMap
    private LocalDateTime createdDate;
	@JMap
    private List<Integer> like;
	@JMap
	private List<Integer> unlike;

}
