package com.ibm.mediaservice.resources;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ibm.mediaservice.assembler.CommentAssembler;
import com.ibm.mediaservice.assembler.MediaAssembler;
import com.ibm.mediaservice.dto.UploadCommentDTO;
import com.ibm.mediaservice.dto.model.CommentModel;
import com.ibm.mediaservice.dto.model.MediaModel;
import com.ibm.mediaservice.model.Comment;
import com.ibm.mediaservice.model.Media;
import com.ibm.mediaservice.services.CommentService;

@RestController
@RequestMapping("/comment")
public class CommentResource {

	@Autowired
	private CommentService commentService;
	
	@Autowired
	private CommentAssembler commentAssembler;
	
	@Autowired
    private PagedResourcesAssembler<Comment> pagedResourcesAssembler;

	@PostMapping(value="/save")
	public ResponseEntity<Object> saveComments(@RequestBody UploadCommentDTO dto){
      Comment comment= commentService.saveComments(dto);
      URI location=ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(comment.getId()).toUri();
	return ResponseEntity.created(location).build();
	}
	
	@GetMapping(value="/get/media/{mediaId}")
	public ResponseEntity<PagedModel<CommentModel>> getCommentsByMedia(@PathVariable(name="mediaId", required=true) String mediaId,@PageableDefault(value = 30)Pageable pageable){
		Page<Comment> result=commentService.getCommentsByMediaId(mediaId,pageable);
		PagedModel<CommentModel> collModel = pagedResourcesAssembler
                .toModel(result, commentAssembler);
		collModel.add(linkTo(methodOn(CommentResource.class).getCommentsByMedia(mediaId, pageable)).withSelfRel());
		return new ResponseEntity<PagedModel<CommentModel>>(collModel,HttpStatus.OK);
	}
	
	@GetMapping(value="/get/comment/{parent}")
	public ResponseEntity<PagedModel<CommentModel>> getCommentsByParent(@PathVariable(name="parent", required=true) String parent,@PageableDefault(value = 30)Pageable pageable){
		Page<Comment> result=commentService.getCommentsByParent(parent,pageable);
		PagedModel<CommentModel> collModel = pagedResourcesAssembler
                .toModel(result, commentAssembler);
		collModel.add(linkTo(methodOn(CommentResource.class).getCommentsByParent(parent, pageable)).withSelfRel());
		return new ResponseEntity<PagedModel<CommentModel>>(collModel,HttpStatus.OK);
	}
	
	@PatchMapping("/like/unlike")
	public ResponseEntity<String> updateLikeUnlike(@RequestParam String commentId,@RequestParam Integer userID,@RequestParam String type,@RequestParam String operation){
		String result=null;
		if(operation.equalsIgnoreCase("add"))
			result=commentService.addLikeUnlike(commentId, userID, type);
		else if(operation.equalsIgnoreCase("remove"))
			result=commentService.removesLikeUnlike(commentId, userID, type);
		return new ResponseEntity<>(result,HttpStatus.OK);
		
	}
}
