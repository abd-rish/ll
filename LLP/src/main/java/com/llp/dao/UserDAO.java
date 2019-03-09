package com.llp.dao;

import java.util.List;

import com.llp.pojos.PostComments;
import com.llp.pojos.PostRate;
import com.llp.pojos.Posts;
import com.llp.pojos.PostsSearchCategoriesSections;
import com.llp.pojos.SearchCategories;
import com.llp.pojos.SearchCategoriesSections;
import com.llp.pojos.UserPinnedPosts;
import com.llp.pojos.Users;
import com.llp.pojos.UsersSearchCategoriesSections;

public interface UserDAO extends AbstractDAO {

	public List<Posts> getRelatedPosts(Long userId, String content, Integer start, Integer length);
	
	public List<Users> getUsers(List<Long> userIdList);
	
	public UserPinnedPosts getUserPinnedPosts(Long userId, Long postId);
	
	public List<Long> getPinnedPostIdList(Long userId, List<Long> postIdList);
	
	public Long getNextPostCommentId(Long userId, Long postId);
	
	public List<PostComments> getLastPostComments(Long postId, Integer start, Integer length);
	
	public PostComments getPostComment(Long userId, Long postId, Long commentId);
	
	public Posts getPost(Long id);
	
	public Posts getPost(Long id, Long userId);
	
	public PostRate getPostRate(Long postId, Long userId);
	
	public List<PostRate> getPostRates(Long userId, List<Long> postIdList);
	
	public boolean isExistAnotherPostRate(Long postId);
	
	public Integer calculatePostRate(Long postId);
	
	public List<Posts> getUserPosts(Long userId, Integer start, Integer length);
	
	public List<SearchCategories> getSearchCategories();
	
	public List<SearchCategoriesSections> getSearchCategoriesSections();
	
	public Short getSearchCategoriesSectionsCount(List<Short> idList);
	
	public Long getMaxPostId(Long userId);

	public List<PostsSearchCategoriesSections> getPostsSearchCategoriesSections(Long postId);
	
	public List<UsersSearchCategoriesSections> getUsersSearchCategoriesSections(Long userId);
	
}
