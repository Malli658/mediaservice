package com.ibm.mediaservice.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.io.IOException;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.googlecode.jmapper.JMapper;
import com.ibm.mediaservice.dto.model.MediaModel;
import com.ibm.mediaservice.model.Media;
import com.ibm.mediaservice.resources.CommentResource;
import com.ibm.mediaservice.resources.MediaResource;
import com.ibm.mediaservice.resources.UserResources;

@Component
public class MediaAssembler extends RepresentationModelAssemblerSupport<Media,MediaModel>{

	public MediaAssembler() {
		super(Media.class, MediaModel.class);
	}

	@Override
	public MediaModel toModel(Media entity) {
		MediaModel resource=instantiateModel(entity);
		
		resource.setLikeCount(resource.getLike().size());
		resource.setUnlikeCount(resource.getUnlike().size());
		try {
			resource.add(linkTo(methodOn(MediaResource.class).getFile(entity.getMediaID())).withRel("mediafile"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		resource.add(linkTo(methodOn(UserResources.class).getUser(new Long(entity.getUserId()))).withRel("owner"));
		resource.add(linkTo(methodOn(CommentResource.class).getCommentsByMedia(entity.getId(),null)).withRel("comments"));
		
		try {
			resource.add(linkTo(methodOn(MediaResource.class).getMedia(entity.getId())).withSelfRel());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resource;
	}
	
	@Override
	public CollectionModel<MediaModel> toCollectionModel(Iterable<? extends Media> entities) {
		CollectionModel<MediaModel> mediaModels = super.toCollectionModel(entities);
		return mediaModels;
	}
	
	@Override
	protected MediaModel instantiateModel(Media entity) {
		JMapper<MediaModel, Media> mediaMapper=new JMapper<>(MediaModel.class, Media.class);
		MediaModel mediaModel=mediaMapper.getDestination(entity);
		return mediaModel;
	}

}
