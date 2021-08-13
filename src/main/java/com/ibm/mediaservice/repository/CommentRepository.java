package com.ibm.mediaservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ibm.mediaservice.model.Comment;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String>{
}
