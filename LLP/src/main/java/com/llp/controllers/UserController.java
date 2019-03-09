package com.llp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.llp.models.PostsModel;
import com.llp.models.UsersModel;
import com.llp.services.UserService;
import com.llp.services.UtilService;
import com.llp.util.ApplicationProperties;
import com.llp.util.Result;

@RestController
@CrossOrigin(origins = ApplicationProperties.ORIGIN)
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UtilService utilService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
	public ResponseEntity<Result> getUserInfo() {
		return utilService.getResponse(userService.getUserInfo());
	}
	
	@RequestMapping(value = "/getHomeData", method = RequestMethod.POST)
	public ResponseEntity<Result> getHomeData(@RequestBody UsersModel usersModel) {
		return utilService.getResponse(userService.getHomeData(usersModel));
	}
	
	@RequestMapping(value = "/pinPost", method = RequestMethod.POST)
	public ResponseEntity<Result> pinPost(@RequestBody PostsModel postsModel) {
		return utilService.getResponse(userService.pinPost(postsModel));
	}
	
	@RequestMapping(value = "/unpinPost", method = RequestMethod.POST)
	public ResponseEntity<Result> unpinPost(@RequestBody PostsModel postsModel) {
		return utilService.getResponse(userService.unpinPost(postsModel));
	}
	
	@RequestMapping(value = "/addPostComment", method = RequestMethod.POST)
	public ResponseEntity<Result> addPostComment(@RequestBody PostsModel postsModel) {
		return utilService.getResponse(userService.addPostComment(postsModel));
	}
	
	@RequestMapping(value = "/getPostComments", method = RequestMethod.GET)
	public ResponseEntity<Result> getPostComments(@RequestParam Long postId, @RequestParam Integer start) {
		return utilService.getResponse(userService.getPostComments(postId, start));
	}
	
	@RequestMapping(value = "/editPostComment", method = RequestMethod.POST)
	public ResponseEntity<Result> editPostComment(@RequestBody PostsModel postsModel) {
		return utilService.getResponse(userService.editPostComment(postsModel));
	}
	
	@RequestMapping(value = "/deletePostComment", method = RequestMethod.POST)
	public ResponseEntity<Result> deletePostComment(@RequestBody PostsModel postsModel) {
		return utilService.getResponse(userService.deletePostComment(postsModel));
	}
	
	@RequestMapping(value = "/ratePost", method = RequestMethod.POST)
	public ResponseEntity<Result> ratePost(@RequestBody PostsModel postsModel) {
		return utilService.getResponse(userService.ratePost(postsModel));
	}
	
	@RequestMapping(value = "/getProfileData", method = RequestMethod.POST)
	public ResponseEntity<Result> getProfileData(@RequestBody UsersModel usersModel) {
		return utilService.getResponse(userService.getProfileData(usersModel));
	}
	
	@RequestMapping(value = "/getSearchCategories", method = RequestMethod.GET)
	public ResponseEntity<Result> getSearchCategories(@RequestParam(required = false) Long postId) {
		return utilService.getResponse(userService.getSearchCategories(postId));
	}
	
	@RequestMapping(value = "/addPost", method = RequestMethod.POST)
	public ResponseEntity<Result> addPost(@RequestBody PostsModel postsModel) {
		return utilService.getResponse(userService.addPost(postsModel));
	}
	
	@RequestMapping(value = "/editPost", method = RequestMethod.POST)
	public ResponseEntity<Result> editPost(@RequestBody PostsModel postsModel) {
		return utilService.getResponse(userService.editPost(postsModel));
	}
	
	@RequestMapping(value = "/deletePost", method = RequestMethod.POST)
	public ResponseEntity<Result> deletePost(@RequestBody PostsModel postsModel) {
		return utilService.getResponse(userService.deletePost(postsModel));
	}
	
	@RequestMapping(value = "/saveUserCategoriesSections", method = RequestMethod.POST)
	public ResponseEntity<Result> saveUserCategoriesSections(@RequestBody UsersModel usersModel) {
		return utilService.getResponse(userService.saveUserCategoriesSections(usersModel));
	}
	
}
