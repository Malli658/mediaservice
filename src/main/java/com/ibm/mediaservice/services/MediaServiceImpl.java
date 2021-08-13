package com.ibm.mediaservice.services;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.googlecode.jmapper.JMapper;
import com.ibm.mediaservice.dto.GetFileDTO;
import com.ibm.mediaservice.dto.UploadFileResponseDTO;
import com.ibm.mediaservice.dto.UploadMediaDTO;
import com.ibm.mediaservice.exception.MediaNotFoundException;
import com.ibm.mediaservice.model.Media;
import com.ibm.mediaservice.repository.MediaRepository;
import com.mongodb.client.gridfs.model.GridFSFile;

@Service
public class MediaServiceImpl implements MediaService{

	@Autowired
	private MediaRepository mediaRepository;

	@Autowired
	private GridFsTemplate gridFsTemplate;

	@Autowired
	private GridFsOperations operations;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public String saveMedia(UploadMediaDTO mediaDTO) throws IOException {
		uploadMedia(mediaDTO);
		return "success";
	}


	private Media uploadMedia(UploadMediaDTO mediaDTO) throws IOException{
		JMapper<Media, UploadMediaDTO> mediaMapper=new JMapper<>(Media.class, UploadMediaDTO.class);
		Media media=mediaMapper.getDestination(mediaDTO);
		MultipartFile file=mediaDTO.getImage();
		media.setMediaID(saveMediaFile(file.getInputStream(), file.getContentType(), file.getName()));
		media.setUploadedDateTime(LocalDateTime.now());
		media.setFileName(file.getOriginalFilename());
		media.setFileType(file.getContentType());
		media.setFileSize(file.getSize());
		if(	getMediaByUserId(mediaDTO.getUserId()).size()<=0)
			media.setDefualtProfile(true);
		media=mediaRepository.save(media);
		return media;
	}

	private String saveMediaFile(InputStream inputStream, String contentType, String filename) {
		ObjectId  id=gridFsTemplate.store(inputStream, filename, contentType);
		return id.toString();
	}


	@Override
	public UploadFileResponseDTO saveFile(MultipartFile file) throws IOException {
		UploadFileResponseDTO result=new UploadFileResponseDTO();
		result.setFileName(file.getOriginalFilename());
		result.setFileSize(file.getSize());
		result.setFileType(file.getContentType());
		String id=saveMediaFile(file.getInputStream(), file.getContentType(), file.getName());
		result.setMediaId(id);
		return result;
	}


	@Override
	public String saveMedias(List<UploadMediaDTO> mediasDTO) throws IOException {
		JMapper<Media, UploadMediaDTO> mediaMapper=new JMapper<>(Media.class, UploadMediaDTO.class);
		mediasDTO.stream().parallel().map(mdto->mediaMapper.getDestination(mdto)).forEach(media->{mediaRepository.save(media);});
		return "success";
	}

	@Override
	public GetFileDTO getFile(String fileId) throws IOException {
		GetFileDTO dto=new GetFileDTO();
		GridFSFile gridFsFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(fileId)));
		dto.setContentType(gridFsFile.getMetadata().getString("_contentType"));
		InputStream is=operations.getResource(gridFsFile).getInputStream();
		dto.setFile(IOUtils.toByteArray(is));
		return dto;
	}

	@Override
	public String deleteFile(String fileID) {
		gridFsTemplate.delete(new Query(Criteria.where("_id").in(fileID)));
		return "success";
	}

	@Override
	public Page<Media> getMyMedia(String mediaType,Integer userId,Pageable pageable,Integer owner){
		Query query=null;
		if(owner==userId){
			if(mediaType.equals("ALL"))
				query=new Query(Criteria.where("userId").is(userId));
			else
				query=new Query(new Criteria().andOperator(Criteria.where("userId").is(userId),Criteria.where("fileType").regex(mediaType,"i")));
		}else{
			if(mediaType.equals("ALL"))
				query=new Query(new Criteria().andOperator(Criteria.where("userId").is(userId),Criteria.where("hidden").is(false)));
			else
				query=new Query(new Criteria().andOperator(Criteria.where("userId").is(userId),Criteria.where("fileType").regex(mediaType,"i"),Criteria.where("hidden").is(false)));
		}



		Long count=mongoTemplate.count(query, Media.class);
		List<Media> medias=mongoTemplate.find(query.with(pageable), Media.class);

		return	new PageImpl<>(medias, pageable, count);
	}

	@Override
	public Media getMedia(String id){
		Optional<Media> result=mediaRepository.findById(id);
		if(result.isPresent())
			return result.get();
		else
			throw new MediaNotFoundException("id:"+id);
	}


	@Override
	public Media updateMedia(UploadMediaDTO mediaDTO, String mediaId)
			throws IOException {
		Media media=mediaRepository.findById(mediaId).orElseThrow(()->(new MediaNotFoundException("mediaID:"+mediaId)));
		if(mediaDTO.getImage()!=null){
			String gridFsId=saveMediaFile(mediaDTO.getImage().getInputStream(), mediaDTO.getImage().getContentType(), mediaDTO.getImage().getOriginalFilename());
			deleteFile(media.getMediaID());
			media.setMediaID(gridFsId);
		}
		media.setCaption(mediaDTO.getMediaTitle());
		media.setDescription(mediaDTO.getMediaDescription());
		media.setTags(mediaDTO.getTags());
		media.setHidden(mediaDTO.getHidden());
		media.setDefualtProfile(mediaDTO.getDefualtProfile());
		media=mediaRepository.save(media);
		return media;
	}

	@Override
	public String addLikeUnlike(String mediaID, Integer userID, String type){
		mediaRepository.findById(mediaID).ifPresent(media->{
			if(type.equalsIgnoreCase("Like"))
				media.getLike().add(userID);
			else if(type.equalsIgnoreCase("unlike"))
				media.getUnlike().add(userID);
			mediaRepository.save(media);
		});
		return "success";
	}

	@Override
	public String removesLikeUnlike(String mediaID, Integer userID, String type){
		mediaRepository.findById(mediaID).ifPresent(media->{
			if(type.equalsIgnoreCase("Like"))
				media.getLike().remove(userID);
			else if(type.equalsIgnoreCase("unlike"))
				media.getUnlike().remove(userID);
			mediaRepository.save(media);
		});
		return "success";
	}


	@Override
	public List<Media> getMediaByUserId(Integer userId) {
		return mediaRepository.getMediaByUserId(userId);
	}

	@Override
	public String updateDefualtProfile(String mediaID,Integer userID){
		Media media=mongoTemplate.findOne(new Query(new Criteria().andOperator(Criteria.where("userId").is(userID),Criteria.where("defualtProfile").is(true))), Media.class);
		if(media!=null){
			media.setDefualtProfile(false);
			mediaRepository.save(media);
		}
		mediaRepository.findById(mediaID).ifPresent(med->{
			med.setDefualtProfile(true);
			mediaRepository.save(med);
		});
		return "success";
	}


}
