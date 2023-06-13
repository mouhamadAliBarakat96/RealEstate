package org.RealEstate.restService;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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

}
