package com.ibm.mediaservice.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.googlecode.jmapper.JMapper;
import com.ibm.mediaservice.dto.UploadCommentDTO;
import com.ibm.mediaservice.dto.UploadMediaDTO;
import com.ibm.mediaservice.model.Comment;
import com.ibm.mediaservice.model.Media;
import com.ibm.mediaservice.repository.CommentRepository;
import com.ibm.mediaservice.repository.MediaRepository;

@Service
public class CommentServiceImpl implements CommentService{

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private MediaService mediaService;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public Comment saveComments(UploadCommentDTO dto){
		JMapper<Comment, UploadCommentDTO> commentMapper=new JMapper<>(Comment.class, UploadCommentDTO.class);
		Comment comment=commentMapper.getDestination(dto);
		comment.setCreatedDate(LocalDateTime.now());
		comment=commentRepository.save(comment);
		if(comment.getId()!=null)
			mediaService.updateCommentComments(comment.getMediaId());
		return comment;
	}

	@Override
	public Page<Comment> getCommentsByMediaId(String mediaId,Pageable pageable){

		Query query=new Query(Criteria.where("mediaId").is(mediaId));
		Long count=mongoTemplate.count(query, Comment.class);
		List<Comment> list=mongoTemplate.find(query.with(pageable), Comment.class);
		return new PageImpl<Comment>(list, pageable, count);
	}

	@Override
	public Page<Comment> getCommentsByParent(String parent,Pageable pageable){
		Query query=new Query(Criteria.where("parent").is(parent));
		Long count=mongoTemplate.count(query, Comment.class);
		List<Comment> list=mongoTemplate.find(query.with(pageable), Comment.class);
		return new PageImpl<Comment>(list, pageable, count);
	}

	@Override
	public String addLikeUnlike(String commentID, Integer userID, String type) {
		commentRepository.findById(commentID).ifPresent(comment->{
			if(type.equalsIgnoreCase("Like"))
				comment.getLike().add(userID);
			else if(type.equalsIgnoreCase("unlike"))
				comment.getUnlike().add(userID);
			commentRepository.save(comment);
		});
		return "success";
	}

	@Override
	public String removesLikeUnlike(String commentID, Integer userID, String type){
		commentRepository.findById(commentID).ifPresent(comment->{
			if(type.equalsIgnoreCase("Like"))
				comment.getLike().remove(userID);
			else if(type.equalsIgnoreCase("unlike"))
				comment.getUnlike().remove(userID);
			commentRepository.save(comment);
		});
		return "success";
	}
}
