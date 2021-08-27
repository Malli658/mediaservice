package com.ibm.mediaservice.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.ibm.mediaservice.dto.GetFileDTO;
import com.ibm.mediaservice.dto.UploadFileResponseDTO;
import com.ibm.mediaservice.dto.UploadMediaDTO;
import com.ibm.mediaservice.model.Media;

public interface MediaService  {
	public String saveMedia(UploadMediaDTO mediaDTO) throws IOException;
	public Media updateMedia(UploadMediaDTO mediaDTO,String id) throws IOException;
	public String saveMedias(List<UploadMediaDTO> mediasDTO) throws IOException;
	public UploadFileResponseDTO saveFile(MultipartFile file) throws IOException;
	public GetFileDTO getFile(String fileId) throws IOException;
	public String deleteFile(String videoID);
	public Page<Media> getMyMedia(String mediaType,Integer userId,Pageable pageable,Integer owner);
	public Media getMedia(String id);
	public String addLikeUnlike(String mediaID, Integer userID, String type);
	public String removesLikeUnlike(String mediaID, Integer userID, String type);
	List<Media> getMediaByUserId(Integer userId);
	public String updateDefualtProfile(String mediaID,Integer userID);
	public String updateCommentComments(String mediaID);
}
