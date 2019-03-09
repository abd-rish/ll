package com.llp.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.llp.dao.UserDAO;
import com.llp.pojos.PostComments;
import com.llp.pojos.PostRate;
import com.llp.pojos.Posts;
import com.llp.pojos.PostsSearchCategoriesSections;
import com.llp.pojos.SearchCategories;
import com.llp.pojos.SearchCategoriesSections;
import com.llp.pojos.UserPinnedPosts;
import com.llp.pojos.Users;
import com.llp.pojos.UsersSearchCategoriesSections;
import com.llp.util.SystemConstants;

@Repository
public class UserDAOImpl extends AbstractDAOImpl implements UserDAO {

	@Override
	public List<Posts> getRelatedPosts(Long userId, String content, Integer start, Integer length) {
		List<Short> searchCategorySectionIds = createQuery("select id.searchCategorySectionId from UsersSearchCategoriesSections where id.userId = :userId").setParameter("userId", userId).getResultList();
		if(searchCategorySectionIds != null && !searchCategorySectionIds.isEmpty())
			return createQuery("from Posts where lower(content) like :content and id in (select id.postId from PostsSearchCategoriesSections where id.searchCategorySectionId in (:searchCategorySectionIds)) order by rate desc, createdDate desc")
					.setParameter("content", content).setParameter("searchCategorySectionIds", searchCategorySectionIds)
					.setStart(start).setLength(length)
					.getResultList();
		return createQuery("from Posts where lower(content) like :content order by rate desc, createdDate desc")
				.setParameter("content", content)
				.setStart(start).setLength(length)
				.getResultList();
	}
	
	@Override
	public List<Users> getUsers(List<Long> userIdList) {
		return createQuery("from Users where userId in (:userIdList)").setParameter("userIdList", userIdList).getResultList();
	}
	
	@Override
	public UserPinnedPosts getUserPinnedPosts(Long userId, Long postId) {
		return createQuery("from UserPinnedPosts where id.userId = :userId and id.postId = :postId").setParameter("userId", userId).setParameter("postId", postId).getSingleResult();
	}
	
	@Override
	public List<Long> getPinnedPostIdList(Long userId, List<Long> postIdList) {
		return createQuery("select id.postId from UserPinnedPosts where id.userId = :userId and id.postId in (:postIdList)").setParameter("userId", userId).setParameter("postIdList", postIdList).getResultList();
	}
	
	@Override
	public Long getNextPostCommentId(Long userId, Long postId) {
		Object nextIdObject = createQuery("select max(id.commentId) from PostComments where id.userId = :userId and id.postId = :postId")
				.setParameter("userId", userId).setParameter("postId", postId)
				.getSingleResult();
		return nextIdObject == null ? 1L : nextIdObject instanceof Long ? (Long) nextIdObject + 1L : Long.parseLong(nextIdObject.toString()) + 1L;
	}
	
	@Override
	public List<PostComments> getLastPostComments(Long postId, Integer start, Integer length) {
		return createQuery("from PostComments where id.postId = :postId order by createdDate desc")
				.setParameter("postId", postId)
				.setStart(start).setLength(length)
				.getResultList();
	}
	
	@Override
	public PostComments getPostComment(Long userId, Long postId, Long commentId) {
		return createQuery("from PostComments where id.userId = :userId and id.postId = :postId and id.commentId = :commentId")
				.setParameter("userId", userId).setParameter("postId", postId).setParameter("commentId", commentId)
				.getSingleResult();
	}
	
	@Override
	public Posts getPost(Long id) {
		return createQuery("from Posts where id = :id").setParameter("id", id).getSingleResult();
	}
	
	@Override
	public Posts getPost(Long id, Long userId) {
		return createQuery("from Posts where id = :id and userId = :userId").setParameter("id", id).setParameter("userId", userId).getSingleResult();
	}
	
	@Override
	public PostRate getPostRate(Long postId, Long userId) {
		return createQuery("from PostRate where id.postId = :postId and id.userId = :userId")
				.setParameter("postId", postId).setParameter("userId", userId)
				.getSingleResult();
	}
	
	@Override
	public List<PostRate> getPostRates(Long userId, List<Long> postIdList) {
		return createQuery("from PostRate where id.userId = :userId and id.postId in (:postIdList)")
				.setParameter("userId", userId).setParameter("postIdList", postIdList)
				.getResultList();
	}
	
	@Override
	public boolean isExistAnotherPostRate(Long postId) {
		Object countObject = createQuery("select count(*) from PostRate where id.postId = :postId").setParameter("postId", postId).getSingleResult();
		return countObject != null && (countObject instanceof Long ? (Long) countObject : Long.parseLong(countObject.toString())) > 1L;
	}
	
	@Override
	public Integer calculatePostRate(Long postId) {
		Object avgObject = createSQLQuery("SELECT ROUND(AVG(RATE * " + SystemConstants.Post.RATE_DIGITS + "), 0) FROM POST_RATE WHERE POST_ID = :postId").setParameter("postId", postId).getSingleResult();
		return avgObject == null ? 0 : avgObject instanceof Integer ? (Integer) avgObject : Integer.parseInt(avgObject.toString());
	}
	
	@Override
	public List<Posts> getUserPosts(Long userId, Integer start, Integer length) {
		return createQuery("from Posts where userId = :userId order by createdDate desc")
				.setParameter("userId", userId)
				.setStart(start).setLength(length)
				.getResultList();
	}
	
	@Override
    public List<SearchCategories> getSearchCategories() {
		return createQuery("from SearchCategories").getResultList();
	}
	
	@Override
	public List<SearchCategoriesSections> getSearchCategoriesSections() {
		return createQuery("from SearchCategoriesSections").getResultList();
	}
	
	@Override
	public List<PostsSearchCategoriesSections> getPostsSearchCategoriesSections(Long postId) {
		return createQuery("from PostsSearchCategoriesSections where id.postId = :postId").setParameter("postId", postId).getResultList();
	}
	
	@Override
	public Short getSearchCategoriesSectionsCount(List<Short> idList) {
		Object countObject = createQuery("select count(*) from SearchCategoriesSections where id in (:idList)").setParameter("idList", idList).getSingleResult();
		return countObject == null ? 0 : countObject instanceof Short ? (Short) countObject : Short.parseShort(countObject.toString());
	}
	
	@Override
	public Long getMaxPostId(Long userId) {
		Object maxPostIdObject = createQuery("select max(id) from Posts where userId = :userId").setParameter("userId", userId).getSingleResult();
		return maxPostIdObject == null ? null : maxPostIdObject instanceof Long ? (Long) maxPostIdObject : Long.parseLong(maxPostIdObject.toString());
	}
	
	@Override
	public List<UsersSearchCategoriesSections> getUsersSearchCategoriesSections(Long userId) {
		return createQuery("from UsersSearchCategoriesSections where id.userId = :userId").setParameter("userId", userId).getResultList();
	}
	
}
