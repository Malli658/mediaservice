package com.ibm.mediaservice.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ibm.mediaservice.dto.UploadCommentDTO;
import com.ibm.mediaservice.model.Comment;

public interface CommentService {
	public Comment saveComments(UploadCommentDTO dto);
	public Page<Comment> getCommentsByMediaId(String mediaId,Pageable pageable);
	public Page<Comment> getCommentsByParent(String parent,Pageable pageable);
	//public String updateLikeORDisLike(String value,String id);
	public String addLikeUnlike(String commentID, Integer userID, String type);
	public String removesLikeUnlike(String commentID, Integer userID, String type);
	
}
