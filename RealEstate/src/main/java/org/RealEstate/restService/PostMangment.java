package org.RealEstate.restService;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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

	// add views
	@PUT
	@Path("/v1/{id}/{postType}")
	@Produces(MediaType.APPLICATION_JSON)

	public Response updatePostView(@PathParam("id") Long id, @PathParam("postType") String postType) {

		return postService.updatePostVieux(id, postType);

	}

	@GET
	@Path("/v1")
	@Produces(MediaType.APPLICATION_JSON)

	public Response findAllPostByUser(@QueryParam("userId") Long userId, @QueryParam("postType") String postType,
			@QueryParam("minPrice") int minPrice, @QueryParam("maxPrice") int maxPrice,
			@QueryParam("villageId") Long villageId, @QueryParam("page") int page, @QueryParam("size") int size,
			@QueryParam("bedRoom") int bedRoom, @QueryParam("bedRoomEq") boolean bedRoomEq,
			@QueryParam("bathRoom") int bathRoom, @QueryParam("bathRoomEq") boolean bathRoomEq,
			@QueryParam("districtId") Long districtId, @QueryParam("governorateId") Long governorateId

	) {

		return postService.findAllPostByUser(userId, postType, minPrice, maxPrice, villageId, page, size, bedRoom,
				bedRoomEq, bathRoom, bathRoomEq, districtId, governorateId);

	}

	// add call
	// add wpp call

	// find post by id

	// update post

}
