package com.ibm.mediaservice.resources;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import io.micrometer.core.ipc.http.HttpSender.Response;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ibm.mediaservice.assembler.MediaAssembler;
import com.ibm.mediaservice.dto.GetFileDTO;
import com.ibm.mediaservice.dto.UploadFileResponseDTO;
import com.ibm.mediaservice.dto.UploadMediaDTO;
import com.ibm.mediaservice.dto.model.MediaModel;
import com.ibm.mediaservice.model.Media;
import com.ibm.mediaservice.services.MediaService;

@RestController
@RequestMapping("/media")
public class MediaResource {
	@Autowired
	private MediaService mediaService;

	@Autowired
	private MediaAssembler mediaAssembler;
	
	@Autowired
    private PagedResourcesAssembler<Media> pagedResourcesAssembler;


	@GetMapping(value="/msg")
	public String getMessage(HttpServletRequest req){
		System.out.println(req.toString());
		return "Hi Boss!";
	}

	@PostMapping(value="/save", consumes="application/json")
	public ResponseEntity<String> saveMedia(@RequestBody UploadMediaDTO media) throws IOException{
		System.out.println(media);
		mediaService.saveMedia(media);
		
		return new ResponseEntity<String>("success",HttpStatus.ACCEPTED);
	}

	@PostMapping(value="/save/list", consumes="application/json")
	public ResponseEntity<String> saveMedia(@RequestBody List<UploadMediaDTO> medias) throws IOException{
		String result=mediaService.saveMedias(medias);
		return new ResponseEntity<String>(result,HttpStatus.ACCEPTED);
	}

	@PostMapping(value="/save/file", consumes="multipart/form-data")
	public ResponseEntity<UploadFileResponseDTO> saveFile(@RequestPart MultipartFile file) throws IOException{
		UploadFileResponseDTO result=mediaService.saveFile(file);
		return new ResponseEntity<UploadFileResponseDTO>(result,HttpStatus.OK);
	}

	@DeleteMapping(value="/delete/{id}")
	public ResponseEntity<String> deleteFile(@PathVariable String id){
		String result=mediaService.deleteFile(id);
		return new ResponseEntity<>(result,HttpStatus.OK);
	}

	@GetMapping(value = "/get/file/{fileID}")
	public HttpEntity<byte[]> getFile(@PathVariable(name = "fileID") String fileID) throws IOException{
		GetFileDTO file = mediaService.getFile(fileID);
		HttpHeaders headers = new HttpHeaders();
		if(file.getContentType().equals("image/jpeg"))
			headers.setContentType(MediaType.IMAGE_PNG);
		if(file.getContentType().equals("video/mp4"))
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		return new ResponseEntity<byte[]>(file.getFile(), headers, HttpStatus.OK);
	}

	@GetMapping(value="/get/mymedia/{userId}")
	public ResponseEntity<PagedModel<MediaModel>> getMyMedia(@RequestParam(defaultValue="ALL",required=false) String mediaType
			,@PathVariable(name="userId") Integer userId, @PageableDefault(value = 30)Pageable pageable,@RequestParam Integer owner) {
		Page<Media> result=mediaService.getMyMedia(mediaType, userId,pageable,owner);
		System.out.println("i got the response ");
		PagedModel<MediaModel> collModel = pagedResourcesAssembler
                .toModel(result, mediaAssembler);
		collModel.add(linkTo(methodOn(MediaResource.class).getMyMedia(mediaType, userId, pageable,owner)).withSelfRel());
		return new ResponseEntity<PagedModel<MediaModel>>(collModel,HttpStatus.OK);
	}

	@GetMapping(value="/get/{mediaID}")
	public ResponseEntity<MediaModel> getMedia(@PathVariable(name="mediaID",required=true) String mediaID) throws IOException{
		Media result= mediaService.getMedia(mediaID);
		MediaModel mediaModel= mediaAssembler.toModel(result);
		return new ResponseEntity<MediaModel>(mediaModel,HttpStatus.OK);

	}
	
	@PutMapping(value="/update/{mediaId}",consumes="multipart/form-data")
	public ResponseEntity<Object> update(@ModelAttribute UploadMediaDTO dto, @PathVariable(name="mediaId") String mediaId) throws IOException{
		Media media=mediaService.updateMedia(dto, mediaId);
		URI uri=ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(media.getId()).toUri();
	  return ResponseEntity.created(uri).build();
	}
	
	@PatchMapping("/like/unlike")
	public ResponseEntity<String> updateLikeUnlike(@RequestParam String media,@RequestParam Integer userID,@RequestParam String type,@RequestParam String operation){
		
		String result=null;
		if(operation.equalsIgnoreCase("add"))
			result=mediaService.addLikeUnlike(media, userID, type);
		else if(operation.equalsIgnoreCase("remove"))
			result=mediaService.removesLikeUnlike(media, userID, type);
		return new ResponseEntity<>(result,HttpStatus.OK);
		
	}
	
	@PatchMapping("/defualt")
	public ResponseEntity<String> makingProfuleDefualt(@RequestParam String mediaId, @RequestParam Integer userID){
		String result=mediaService.updateDefualtProfile(mediaId, userID);
		return new ResponseEntity<>(result,HttpStatus.OK);
	}

}
