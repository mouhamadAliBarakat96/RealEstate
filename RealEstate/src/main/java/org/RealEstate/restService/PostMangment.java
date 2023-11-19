package org.RealEstate.restService;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.RealEstate.enumerator.ExchangeRealEstateType;
import org.RealEstate.service.PostService;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

@Path("/post")
public class PostMangment {

	@EJB
	private PostService postService;

	@POST
	@Path("/v1")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)

	public Response savePost(@MultipartForm MultipartFormDataInput input) {

		return postService.mangmentAddPost(input);

	}

	// update post

	@PUT
	@Path("/v1")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)

	public Response updatePost(@MultipartForm MultipartFormDataInput input) {

		return postService.mangmentUpdatePost(input);

	}

	@PUT
	@Path("/v1/remove-picture-post")

	public Response removePciture(@QueryParam("id") Long id, List<String> imagesToDelete) {

		return postService.removePostImage(id, imagesToDelete);
	}

	@PUT
	@Path("/v1/update-image-post")

	public Response updatePostImage(@QueryParam("id") Long id, @MultipartForm MultipartFormDataInput input) {

		return postService.updatePostImage(id, input);

	}

	@PUT
	@Path("/v1/update-image-chalet")

	public Response updateChaletImage(@QueryParam("id") Long id, @MultipartForm MultipartFormDataInput input) {

		return postService.updateChaletImage(id, input);

	}

	@PUT
	@Path("/v1/remove-picture-chalet")
	public Response removePcitureChalet(@QueryParam("id") Long id, List<String> images) {

		return postService.removePostImageChalet(id, images);

	}

	// add images

	// post update image

	// add views
	@PUT
	@Path("/v1/add-view/{id}/{postType}")
	@Produces(MediaType.APPLICATION_JSON)

	public Response updatePostView(@PathParam("id") Long id, @PathParam("postType") String postType) {

		return postService.updatePostVieux(id, postType);

	}

	// add like
	@PUT
	@Path("/v1/add-like/{id}/{postType}")
	@Produces(MediaType.APPLICATION_JSON)

	public Response updatePostLike(@PathParam("id") Long id, @PathParam("postType") String postType) {

		return postService.updatePostVieux(id, postType);

	}

	@GET
	@Path("/v1")
	@Produces(MediaType.APPLICATION_JSON)

	/*
	 * remove
	 * bedRoomEq 
	 * 
	 * remove
	 * bathRoomEq
	 * 
	 * bedRoom String as 1,2,3
	 * 
	 * bathRoom 1,2,3
	 */
	public Response findAllPostByUser(@QueryParam("userId") Long userId, @QueryParam("allPost") boolean isAllPost , @QueryParam("postType") String postType,
			@QueryParam("minPrice") int minPrice, @QueryParam("maxPrice") int maxPrice,
			@QueryParam("villageId") Long villageId, @QueryParam("page") int page, @QueryParam("size") int size,
			@QueryParam("bedRoom") String bedRoom,
			@QueryParam("bathRoom") String bathRoom,
			@QueryParam("districtId") Long districtId, @QueryParam("governorateId") Long governorateId , @QueryParam("exchangeRealEstateType")  String exchangeRealEstateType
			, @QueryParam("sort")  List<String> sort
			

	) {
		
		
		
		return postService.findPosts(userId, isAllPost , postType, minPrice, maxPrice, villageId, page, size, bedRoom, 
				bathRoom, districtId, governorateId , exchangeRealEstateType , sort);

	}

	@GET
	@Path("/chalet/v1")
	@Produces(MediaType.APPLICATION_JSON)

	public Response findAllChalet(@QueryParam("userId") Long userId, @QueryParam("minPrice") int minPrice,
			@QueryParam("maxPrice") int maxPrice,

			@QueryParam("villageId") Long villageId, @QueryParam("page") int page, @QueryParam("size") int size,
			@QueryParam("pool") Boolean pool, @QueryParam("chimney") Boolean chimney,
			@QueryParam("districtId") Long districtId, @QueryParam("governorateId") Long governorateId

	) {

		return postService.findChalet(userId, minPrice, maxPrice, villageId, size, page, pool, chimney, governorateId,
				districtId);

	}

	// find post by id
	@GET
	@Path("/v1/{id}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)

	public Response findPostById(@PathParam("id") Long id) {

		return postService.findPostById(id);

	}

	@PUT
	@Path("/v1/call-post/{id}/{postType}")
	@Produces(MediaType.APPLICATION_JSON)

	public Response updateCallPost(@PathParam("id") Long id, @PathParam("postType") String postType) {

		return postService.updateCallPost(id, postType);

	}

}
