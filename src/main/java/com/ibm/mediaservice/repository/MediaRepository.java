package com.ibm.mediaservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ibm.mediaservice.model.Media;

@Repository
public interface MediaRepository extends MongoRepository<Media, String>{
	
	List<Media> getMediaByUserId(Integer userId);

}
