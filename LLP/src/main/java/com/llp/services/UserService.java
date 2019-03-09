package com.llp.services;

import com.llp.models.PostsModel;
import com.llp.models.UsersModel;
import com.llp.util.Result;

public interface UserService {
	
	public Result getUserInfo();
	
	public Result getHomeData(UsersModel usersModel);
	
	public Result pinPost(PostsModel postsModel);
	
	public Result unpinPost(PostsModel postsModel);
	
	public Result addPostComment(PostsModel postsModel);
	
	public Result getPostComments(Long postId, Integer start);
	
	public Result editPostComment(PostsModel postsModel);

	public Result deletePostComment(PostsModel postsModel);

	public Result ratePost(PostsModel postsModel);
	
	public Result getProfileData(UsersModel usersModel);
	
	public Result getSearchCategories(Long postId);
	
	public Result addPost(PostsModel postsModel);
	
	public Result editPost(PostsModel postsModel);

	public Result deletePost(PostsModel postsModel);

	public Result saveUserCategoriesSections(UsersModel usersModel);
	
}
