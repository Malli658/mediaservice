package com.ibm.mediaservice.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.googlecode.jmapper.JMapper;
import com.ibm.mediaservice.dto.model.CommentModel;
import com.ibm.mediaservice.model.Comment;
import com.ibm.mediaservice.resources.CommentResource;

@Component
public class CommentAssembler extends RepresentationModelAssemblerSupport<Comment,CommentModel>{

	public CommentAssembler() {
		super(Comment.class, CommentModel.class);
	}

	@Override
	public CommentModel toModel(Comment entity) {
		CommentModel commentModel=instantiateModel(entity);
		commentModel.add(linkTo(methodOn(CommentResource.class).getCommentsByParent(entity.getId(),null)).withRel("replay"));
		return commentModel;
	}
	
	@Override
	public CollectionModel<CommentModel> toCollectionModel(
			Iterable<? extends Comment> entities) {
		CollectionModel<CommentModel> commentModels = super.toCollectionModel(entities);
		return commentModels;
	}
	
	@Override
	protected CommentModel instantiateModel(Comment entity) {
		JMapper<CommentModel, Comment> commentMapper=new JMapper<>(CommentModel.class, Comment.class);
		CommentModel commentModel=commentMapper.getDestination(entity);
		return commentModel;
	}
	

}
