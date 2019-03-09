package com.llp.services.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.llp.dao.UserDAO;
import com.llp.models.PostsModel;
import com.llp.models.UsersModel;
import com.llp.pojos.PostComments;
import com.llp.pojos.PostCommentsId;
import com.llp.pojos.PostRate;
import com.llp.pojos.PostRateId;
import com.llp.pojos.Posts;
import com.llp.pojos.PostsSearchCategoriesSections;
import com.llp.pojos.PostsSearchCategoriesSectionsId;
import com.llp.pojos.SearchCategories;
import com.llp.pojos.SearchCategoriesSections;
import com.llp.pojos.UserPinnedPosts;
import com.llp.pojos.UserPinnedPostsId;
import com.llp.pojos.Users;
import com.llp.pojos.UsersSearchCategoriesSections;
import com.llp.pojos.UsersSearchCategoriesSectionsId;
import com.llp.services.UserService;
import com.llp.shared.classes.UserInfo;
import com.llp.util.Helper;
import com.llp.util.QueryParam;
import com.llp.util.Result;
import com.llp.util.SystemConstants;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private HttpSession session;
	
	@Autowired
	private UserDAO userDAO;
	
	@Override
	public Result getUserInfo() {
		Result result = new Result();
		result.setReturnValue(new UserInfo((Users) session.getAttribute(SystemConstants.SessionVariables.USER_INFO)));
		return result;
	}
	
	@Override
	public Result getHomeData(UsersModel usersModel) {
		Result result = new Result();

		Integer start = usersModel.getStart();
		Integer length = usersModel.getLength();
		result = Helper.checkRequiredParams(result, start, length);
		if(!result.isExecutionSuccessful())
			return result;
		
		StringBuilder builder = new StringBuilder();
		builder.append('%');
		
		if(usersModel.get("query") != null) {
		    StringTokenizer tokenizer = new StringTokenizer(usersModel.get("query").toString());
		    while(tokenizer.hasMoreTokens())
			    builder.append(tokenizer.nextToken().toLowerCase()).append('%');
		}

	    usersModel.clear();

		Users currentUser = (Users) session.getAttribute(SystemConstants.SessionVariables.USER_INFO);
		
	    List<Posts> posts = userDAO.getRelatedPosts(currentUser.getUserId(), builder.toString(), start, length + 1);
	    if(posts != null && !posts.isEmpty()) {
    	    if(posts.size() > length) {
    	    	usersModel.put("nextFlag", true);
    	    	posts.remove(posts.size() - 1);
    	    }
	    	List<Long> userIdList = new ArrayList<Long>();
	    	List<Long> postIdList = new ArrayList<Long>();
	    	for(Posts post : posts) {
	    		userIdList.add(post.getUserId());
	    		postIdList.add(post.getId());
	    	}
	    	List<Users> users = userDAO.getUsers(userIdList);
	    	if(users != null && !users.isEmpty()) {
	    		List<Long> pinnedPostIdList = userDAO.getPinnedPostIdList(currentUser.getUserId(), postIdList);
	    		Set<Long> pinnedPostIdSet = new HashSet<Long>();
	    		if(pinnedPostIdList != null && !pinnedPostIdList.isEmpty())
	    			for(Long postId : pinnedPostIdList)
	    				pinnedPostIdSet.add(postId);
	    	    Map<Long, Users> usersMap = new HashMap<Long, Users>();
	    	    for(Users user : users)
	    	    	usersMap.put(user.getUserId(), user);
	    	    Map<Long, Byte> postRateMap = new HashMap<Long, Byte>();
	    	    List<PostRate> postRates = userDAO.getPostRates(currentUser.getUserId(), postIdList);
	    	    if(postRates != null && !postRates.isEmpty())
	    	    	for(PostRate postRate : postRates)
	    	    		postRateMap.put(postRate.getId().getPostId(), postRate.getRate());
	    	    List<Map<String, Object>> relatedPosts = new ArrayList<Map<String, Object>>();
	    	    Map<String, Object> relatedPost;
	    	    Users user;
	    	    List<PostComments> postComments;
	    	    List<Map<String, Object>> comments;
	    	    Map<String, Object> comment;
	    	    Map<Long, Users> commentsUsersMap = new HashMap<Long, Users>();
	    	    String rateFormat = "%.2f";
	    	    String rateStarsNumberFormat = "%.0f";
	    	    for(Posts post : posts) {
	    	    	relatedPost = new HashMap<String, Object>();
	    	    	relatedPost.put("id", post.getId());
	    	    	relatedPost.put("content", post.getContent());
	    	    	relatedPost.put("lastChangeDate", post.getUpdatedDate() == null ? Helper.formatDate(post.getCreatedDate(), "dd/MM/yyyy, HH:mm") : Helper.formatDate(post.getUpdatedDate(), "dd/MM/yyyy, HH:mm"));
	    	    	relatedPost.put("lastChangeDateAbbr", post.getUpdatedDate() == null ? Helper.formatDate(post.getCreatedDate(), "dd/MM/yyyy, HH:mm") : Helper.formatDate(post.getUpdatedDate(), "dd/MM/yyyy, HH:mm"));
	    	    	user = usersMap.get(post.getUserId());
	    	    	relatedPost.put("userInfo", new UserInfo(user));
	    	    	relatedPost.put("optionsList", createOptionList(user.getUserId().equals(currentUser.getUserId()), post.getId(), pinnedPostIdSet.contains(post.getId())));
	    	    	relatedPost.put("userRate", postRateMap.get(post.getId()));
	    	    	if(post.getRate() != null) {
	    	        	relatedPost.put("rate", String.format(rateFormat, (double) post.getRate() / (double) SystemConstants.Post.RATE_DIGITS));
	    	    	    relatedPost.put("rateStarsNumber", String.format(rateStarsNumberFormat, (double) post.getRate() / (double) SystemConstants.Post.RATE_DIGITS).charAt(0) - '0');
	    	    	}
	    	    	postComments = userDAO.getLastPostComments(post.getId(), 0, 4);
	    	    	if(postComments != null && !postComments.isEmpty()) {
	    	    		int i;
	    	    		if(postComments.size() == 4) {
	    	    			relatedPost.put("nextCommentFlag", true);
	    	    			i = 2;
	    	    		} else
	    	    			i = postComments.size() - 1;
	    	    		userIdList.clear();
	    				for(PostComments postComment : postComments)
	    					userIdList.add(postComment.getId().getUserId());
	    				users = userDAO.getUsers(userIdList);
	    				if(users != null && !users.isEmpty())
	    					for(Users _user : users)
	    						commentsUsersMap.put(_user.getUserId(), _user);
	    	    		comments = new ArrayList<Map<String, Object>>();
	    	    		while(i > -1) {
	    	    			comment = new HashMap<String, Object>();
	    	    			comment.put("commentId", postComments.get(i).getId().getCommentId());
	    	    			user = commentsUsersMap.get(postComments.get(i).getId().getUserId());
	    	    	    	comment.put("userInfo", new UserInfo(user));
	    	    			comment.put("comment", postComments.get(i).getComment());
	    	    			comment.put("currentUserFlag", postComments.get(i).getId().getUserId().equals(currentUser.getUserId()));
	    	    			comment.put("lastChangeDate", postComments.get(i).getUpdatedDate() == null ? Helper.formatDate(postComments.get(i).getCreatedDate(), "dd/MM/yyyy, HH:mm") : Helper.formatDate(postComments.get(i).getUpdatedDate(), "dd/MM/yyyy, HH:mm"));
	    	    			comments.add(comment);
	    	    			i--;
	    	    		}
	    				commentsUsersMap.clear();
	    	    		relatedPost.put("commentsList", comments);
	    	    	}
	    	    	relatedPosts.add(relatedPost);
	    	    }
	    	    usersModel.put("relatedPosts", relatedPosts);
	    	}
	    }

	    usersModel.put("userInfo", new UserInfo(currentUser));
	    result.setReturnValue(usersModel);
		return result;
	}
	
	private List<Map<String, Object>> createOptionList(boolean currentUserFlag, Long postId, boolean unpinFlag) {
		List<Map<String, Object>> optionsList = new ArrayList<Map<String, Object>>();
		
		Map<String, Object> option = new HashMap<String, Object>();
		if(unpinFlag) {
		    option.put("name", "Unpin post");
 	        option.put("description", "Remove this from pinned posts");
 	        option.put("uriSuffix", "/user/unpinPost");
 	    } else {
 	    	option.put("name", "Pin post");
 	        option.put("description", "Add this to your pinned posts");
 	        option.put("uriSuffix", "/user/pinPost");
 	    }
		option.put("imgSrc", "../../../../../../../assets/img/post/pin-post.png");
	    Map<String, Object> body = new HashMap<String, Object>();
	    body.put("postId", postId);
	    option.put("body", body);
	    optionsList.add(option);
	    
	    if(currentUserFlag) {
	    	option = new HashMap<String, Object>();
	    	option.put("name", "Edit");
 	        option.put("description", "Edit post content");
 	        option.put("imgSrc", "../../../../../../../assets/img/post/pin-post.png");
 	        optionsList.add(option);
 	        
 	        option = new HashMap<String, Object>();
 	        option.put("name", "Delete");
	        option.put("description", "Delete this post");
	        option.put("uriSuffix", "/user/deletePost");
 	        option.put("imgSrc", "../../../../../../../assets/img/post/pin-post.png");
	        option.put("body", body);
	        optionsList.add(option);
	    }
		
		return optionsList;
	}
	
	@Override
	public Result pinPost(PostsModel postsModel) {
		Result result = new Result();
		
		Long postId = postsModel.getPostId();
		if(postId == null)
			return Helper.getResultWithErrorCode(result, "0000");
		
		Long userId = ((Users) session.getAttribute(SystemConstants.SessionVariables.USER_INFO)).getUserId();
		
		if(userDAO.isExists("USER_PINNED_POSTS", new QueryParam("USER_ID", userId), new QueryParam("POST_ID", postId)))
			return Helper.getResultWithErrorCode(result, "2000");
		if(!userDAO.isExists("POSTS", new QueryParam("ID", postId)))
			return Helper.getResultWithErrorCode(result, "0001");
		
		userDAO.savePojo(new UserPinnedPosts(new UserPinnedPostsId(userId, postId)));
		
		Map<String, Object> option = new HashMap<String, Object>();
		option.put("name", "Unpin post");
        option.put("description", "Remove this from pinned posts");
        option.put("uriSuffix", "/user/unpinPost");
        postsModel.clear();
        postsModel.put("option", option);
        result.setReturnValue(postsModel);
        
		return Helper.getResultWithSuccessCode(result, "2000");
	}
	
	@Override
	public Result unpinPost(PostsModel postsModel) {
		Result result = new Result();
		
		Long postId = postsModel.getPostId();
		if(postId == null)
			return Helper.getResultWithErrorCode(result, "0000");
		
		UserPinnedPosts userPinnedPosts = userDAO.getUserPinnedPosts(((Users) session.getAttribute(SystemConstants.SessionVariables.USER_INFO)).getUserId(), postId);
		if(userPinnedPosts == null)
			return Helper.getResultWithErrorCode(result, "0001");
		
		userDAO.deletePojo(userPinnedPosts);
		
		Map<String, Object> option = new HashMap<String, Object>();
		option.put("name", "Pin post");
        option.put("description", "Add this to your pinned posts");
        option.put("uriSuffix", "/user/pinPost");
        postsModel.clear();
        postsModel.put("option", option);
        result.setReturnValue(postsModel);
		
		return Helper.getResultWithSuccessCode(result, "2001");
	}
	
	@Override
	public Result addPostComment(PostsModel postsModel) {
		Result result = new Result();
		
		Long postId = postsModel.getPostId();
		String comment = postsModel.getComment();
		result = Helper.checkRequiredParams(result, postId, comment);
		if(!result.isExecutionSuccessful())
			return result;
		
		if(comment.length() > SystemConstants.MySQL.DataTypes.TEXT_MAX_LENGTH)
			return Helper.getResultWithErrorCode(result, "2001");	
		
		if(!userDAO.isExists("POSTS", new QueryParam("ID", postId)))
			return Helper.getResultWithErrorCode(result, "0001");
		
		Users user = ((Users) session.getAttribute(SystemConstants.SessionVariables.USER_INFO));
		
		Long nextPostCommentId = userDAO.getNextPostCommentId(user.getUserId(), postId);
		Date now = new Date();
		userDAO.savePojo(new PostComments(new PostCommentsId(nextPostCommentId, ((Users) session.getAttribute(SystemConstants.SessionVariables.USER_INFO)).getUserId(), postId), comment, now));
		
		postsModel.clear();
		postsModel.setCommentId(nextPostCommentId);
		postsModel.put("lastChangeDate", Helper.formatDate(now, "dd/MM/yyyy, HH:mm"));
		postsModel.put("userInfo", new UserInfo(user));
		result.setReturnValue(postsModel);
		
		return Helper.getResultWithSuccessCode(result, "2002");
	}
	
	@Override
	public Result getPostComments(Long postId, Integer start) {
		Result result = new Result();
		
		Map<String, Object> model = new HashMap<String, Object>();
		
		List<PostComments> postComments = userDAO.getLastPostComments(postId, start, 11);
		if(postComments != null && !postComments.isEmpty()) {
			if(postComments.size() == 11) {
				model.put("nextCommentFlag", true);
				postComments.remove(postComments.size() - 1);
			} else
				model.put("nextCommentFlag", false);
			
			List<Long> userIdList = new ArrayList<Long>();
			for(PostComments postComment : postComments)
				userIdList.add(postComment.getId().getUserId());
			List<Users> users = userDAO.getUsers(userIdList);
			Map<Long, Users> usersMap = new HashMap<Long, Users>();
			if(users != null && !users.isEmpty())
				for(Users user : users)
					usersMap.put(user.getUserId(), user);
			
			Long userId = ((Users) session.getAttribute(SystemConstants.SessionVariables.USER_INFO)).getUserId();
			List<Map<String, Object>> comments = new ArrayList<Map<String, Object>>();
    	    Map<String, Object> comment;
    	    Users user;
    	    for(PostComments postComment : postComments) {
    	    	comment = new HashMap<String, Object>();
    			comment.put("commentId", postComment.getId().getCommentId());
    			user = usersMap.get(postComment.getId().getUserId());
    			comment.put("userInfo", new UserInfo(user));
    			comment.put("comment", postComment.getComment());
    			comment.put("currentUserFlag", postComment.getId().getUserId().equals(userId));
    			comment.put("lastChangeDate", postComment.getUpdatedDate() == null ? Helper.formatDate(postComment.getCreatedDate(), "dd/MM/yyyy, HH:mm") : Helper.formatDate(postComment.getUpdatedDate(), "dd/MM/yyyy, HH:mm"));
    			comments.add(comment);
    	    }
    	    model.put("commentsList", comments);
		} else
			model.put("nextCommentFlag", false);
		
		result.setReturnValue(model);
		return result;
	}
	
	@Override
	public Result editPostComment(PostsModel postsModel) {
		Result result = new Result();
		
		Long postId = postsModel.getPostId();
		Long commentId = postsModel.getCommentId();
		String comment = postsModel.getComment();
		result = Helper.checkRequiredParams(result, postId, commentId, comment);
		if(!result.isExecutionSuccessful())
			return result;
		
		if(comment.length() > SystemConstants.MySQL.DataTypes.TEXT_MAX_LENGTH)
			return Helper.getResultWithErrorCode(result, "2001");
		
		PostComments postComment = userDAO.getPostComment(((Users) session.getAttribute(SystemConstants.SessionVariables.USER_INFO)).getUserId(), postId, commentId);
		if(postComment == null)
			return Helper.getResultWithErrorCode(result, "0001");
		postComment.setComment(comment);
		postComment.setUpdatedDate(new Date());
		userDAO.updatePojo(postComment);
		
		postsModel.clear();
		postsModel.put("lastChangeDate", Helper.formatDate(postComment.getUpdatedDate(), "dd/MM/yyyy, HH:mm"));
		result.setReturnValue(postsModel);
		
		return Helper.getResultWithSuccessCode(result, "2003");
	}
	
	@Override
	public Result deletePostComment(PostsModel postsModel) {
		Result result = new Result();
		
		Long postId = postsModel.getPostId();
		Long commentId = postsModel.getCommentId();
		result = Helper.checkRequiredParams(result, postId, commentId);
		if(!result.isExecutionSuccessful())
			return result;
		
		userDAO.delete("POST_COMMENTS", new QueryParam("USER_ID", ((Users) session.getAttribute(SystemConstants.SessionVariables.USER_INFO)).getUserId()), new QueryParam("POST_ID", postId), new QueryParam("COMMENT_ID", commentId));
		
		return Helper.getResultWithSuccessCode(result, "2004");
	}
	
	@Override
	public Result ratePost(PostsModel postsModel) {
		Result result = new Result();
		
		Long postId = postsModel.getPostId();
		Byte rate = postsModel.getRate();
		result = Helper.checkRequiredParams(result, postId, rate);
		if(!result.isExecutionSuccessful())
			return result;
		if(rate < 1 || rate > 5)
			return Helper.getResultWithErrorCode(result, "0000");
		
		Posts post = userDAO.getPost(postId);
		if(post == null)
			return Helper.getResultWithErrorCode(result, "0001");
		
		Long userId = ((Users) session.getAttribute(SystemConstants.SessionVariables.USER_INFO)).getUserId();
		
		PostRate postRate = userDAO.getPostRate(postId, userId);
		if(postRate == null) {
			userDAO.savePojo(new PostRate(new PostRateId(postId, userId), rate));
//			if(post.getRate() != null)
//				post.setRate((((int) rate * SystemConstants.Post.RATE_DIGITS) + post.getRate()) / 2);
//			else
//				post.setRate((int) rate * SystemConstants.Post.RATE_DIGITS);
			///
			post.setRate(userDAO.calculatePostRate(postId));
			///
			userDAO.updatePojo(post);
		} else {
//			if(post.getRate() != null && userDAO.isExistAnotherPostRate(postId))
//			    post.setRate((((post.getRate() * 2) - ((int) postRate.getRate() * SystemConstants.Post.RATE_DIGITS)) + ((int) rate * SystemConstants.Post.RATE_DIGITS)) / 2);
//			else
//				post.setRate((int) rate * SystemConstants.Post.RATE_DIGITS);
			postRate.setRate(rate);
			userDAO.updatePojo(postRate);
			///
			post.setRate(userDAO.calculatePostRate(postId));
			///
			userDAO.updatePojo(post);
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("rate", String.format("%.2f", (double) post.getRate() / (double) SystemConstants.Post.RATE_DIGITS));
		model.put("rateStarsNumber", String.format("%.0f", (double) post.getRate() / (double) SystemConstants.Post.RATE_DIGITS).charAt(0) - '0');
		result.setReturnValue(model);
		
		return Helper.getResultWithSuccessCode(result, "2005");
	}
	
	@Override
	public Result getProfileData(UsersModel usersModel) {
		Result result = new Result();
		
		Integer start = usersModel.getStart();
		Integer length = usersModel.getLength();
		result = Helper.checkRequiredParams(result, start, length);
		if(!result.isExecutionSuccessful())
			return result;

		usersModel.clear();
		
		Users currentUser = (Users) session.getAttribute(SystemConstants.SessionVariables.USER_INFO);
		
		List<Posts> posts = userDAO.getUserPosts(currentUser.getUserId(), start, length + 1);
		if(posts != null && !posts.isEmpty()) {
    	    if(posts.size() > length) {
    	    	usersModel.put("nextFlag", true);
    	    	posts.remove(posts.size() - 1);
    	    }
			List<Long> postIdList = new ArrayList<Long>();
			for(Posts post : posts)
				postIdList.add(post.getId());
			Map<Long, Byte> postRateMap = new HashMap<Long, Byte>();
    	    List<PostRate> postRates = userDAO.getPostRates(currentUser.getUserId(), postIdList);
    	    if(postRates != null && !postRates.isEmpty())
    	    	for(PostRate postRate : postRates)
    	    		postRateMap.put(postRate.getId().getPostId(), postRate.getRate());
    	    List<Long> pinnedPostIdList = userDAO.getPinnedPostIdList(currentUser.getUserId(), postIdList);
    		Set<Long> pinnedPostIdSet = new HashSet<Long>();
    		if(pinnedPostIdList != null && !pinnedPostIdList.isEmpty())
    			for(Long postId : pinnedPostIdList)
    				pinnedPostIdSet.add(postId);
    	    List<Map<String, Object>> userPosts = new ArrayList<Map<String, Object>>();
    	    Map<String, Object> userPost;
    	    UserInfo userInfo = new UserInfo(currentUser);
    	    String rateFormat = "%.2f";
    	    String rateStarsNumberFormat = "%.0f";
    	    List<PostComments> postComments;
    	    List<Long> userIdList = new ArrayList<Long>();
    	    List<Users> users;
    	    Map<Long, Users> commentsUsersMap = new HashMap<Long, Users>();
    	    List<Map<String, Object>> comments;
    	    Map<String, Object> comment;
    	    Users user;
    	    for(Posts post : posts) {
    	    	userPost = new HashMap<String, Object>();
    	    	userPost.put("id", post.getId());
    	    	userPost.put("content", post.getContent());
    	    	userPost.put("lastChangeDate", post.getUpdatedDate() == null ? Helper.formatDate(post.getCreatedDate(), "dd/MM/yyyy, HH:mm") : Helper.formatDate(post.getUpdatedDate(), "dd/MM/yyyy, HH:mm"));
    	    	userPost.put("lastChangeDateAbbr", post.getUpdatedDate() == null ? Helper.formatDate(post.getCreatedDate(), "dd/MM/yyyy, HH:mm") : Helper.formatDate(post.getUpdatedDate(), "dd/MM/yyyy, HH:mm"));
    	    	userPost.put("userInfo", userInfo);
    	    	userPost.put("optionsList", createOptionList(true, post.getId(), pinnedPostIdSet.contains(post.getId())));
    	    	userPost.put("userRate", postRateMap.get(post.getId()));
    	    	if(post.getRate() != null) {
    	    		userPost.put("rate", String.format(rateFormat, (double) post.getRate() / (double) SystemConstants.Post.RATE_DIGITS));
    	    		userPost.put("rateStarsNumber", String.format(rateStarsNumberFormat, (double) post.getRate() / (double) SystemConstants.Post.RATE_DIGITS).charAt(0) - '0');
    	    	}
    	    	postComments = userDAO.getLastPostComments(post.getId(), 0, 4);
    	    	if(postComments != null && !postComments.isEmpty()) {
    	    		int i;
    	    		if(postComments.size() == 4) {
    	    			userPost.put("nextCommentFlag", true);
    	    			i = 2;
    	    		} else
    	    			i = postComments.size() - 1;
    	    		userIdList.clear();
    				for(PostComments postComment : postComments)
    					userIdList.add(postComment.getId().getUserId());
    				users = userDAO.getUsers(userIdList);
    				if(users != null && !users.isEmpty())
    					for(Users _user : users)
    						commentsUsersMap.put(_user.getUserId(), _user);
    	    		comments = new ArrayList<Map<String, Object>>();
    	    		while(i > -1) {
    	    			comment = new HashMap<String, Object>();
    	    			comment.put("commentId", postComments.get(i).getId().getCommentId());
    	    			user = commentsUsersMap.get(postComments.get(i).getId().getUserId());
    	    	    	comment.put("userInfo", new UserInfo(user));
    	    			comment.put("comment", postComments.get(i).getComment());
    	    			comment.put("currentUserFlag", postComments.get(i).getId().getUserId().equals(currentUser.getUserId()));
    	    			comment.put("lastChangeDate", postComments.get(i).getUpdatedDate() == null ? Helper.formatDate(postComments.get(i).getCreatedDate(), "dd/MM/yyyy, HH:mm") : Helper.formatDate(postComments.get(i).getUpdatedDate(), "dd/MM/yyyy, HH:mm"));
    	    			comments.add(comment);
    	    			i--;
    	    		}
    				commentsUsersMap.clear();
    				userPost.put("commentsList", comments);
    	    	}
    	    	userPosts.add(userPost);
    	    }
    	    usersModel.put("userPosts", userPosts);
		}

	    usersModel.put("userInfo", new UserInfo(currentUser));
		result.setReturnValue(usersModel);
		return result;
	}
	
	@Override
	public Result getSearchCategories(Long postId) {
		Result result = new Result();
		
		List<SearchCategories> searchCategories = userDAO.getSearchCategories();
		List<SearchCategoriesSections> searchCategoriesSections = userDAO.getSearchCategoriesSections();
		if(searchCategories == null || searchCategories.isEmpty() || searchCategoriesSections == null || searchCategoriesSections.isEmpty())
			return Helper.getResultWithErrorCode(result, "0001");

		Map<String, Object> model = new HashMap<String, Object>();
		
		Map<String, Object> tmpMap;
		
		Map<Byte, List<Map<String, Object>>> searchCategoriesSectionsMap = new HashMap<Byte, List<Map<String, Object>>>();
		if(postId == null)
    		for(SearchCategoriesSections section : searchCategoriesSections) {
	    		if(searchCategoriesSectionsMap.get(section.getSearchCategoryId()) == null)
		    		searchCategoriesSectionsMap.put(section.getSearchCategoryId(), new ArrayList<Map<String, Object>>());
			    tmpMap = new HashMap<String, Object>();
    			tmpMap.put("id", section.getId());
	    		tmpMap.put("name", section.getName());
		     	searchCategoriesSectionsMap.get(section.getSearchCategoryId()).add(tmpMap);
		    } 
		else {
			List<PostsSearchCategoriesSections> postsSearchCategoriesSections = userDAO.getPostsSearchCategoriesSections(postId);
			Set<Short> searchCategoriesSectionsSet = new HashSet<Short>();
			for(PostsSearchCategoriesSections postsSearchCategoriesSection : postsSearchCategoriesSections)
				searchCategoriesSectionsSet.add(postsSearchCategoriesSection.getId().getSearchCategorySectionId());
			model.put("categoriesSectionsCount", searchCategoriesSectionsSet.size());
			for(SearchCategoriesSections section : searchCategoriesSections) {
	    		if(searchCategoriesSectionsMap.get(section.getSearchCategoryId()) == null)
		    		searchCategoriesSectionsMap.put(section.getSearchCategoryId(), new ArrayList<Map<String, Object>>());
			    tmpMap = new HashMap<String, Object>();
    			tmpMap.put("id", section.getId());
	    		tmpMap.put("name", section.getName());
	    		if(searchCategoriesSectionsSet.contains(section.getId()))
	    			tmpMap.put("chosenFlag", true);
		    	searchCategoriesSectionsMap.get(section.getSearchCategoryId()).add(tmpMap);
		    }
		}
		
		List<Map<String, Object>> searchCategoriesList = new ArrayList<Map<String, Object>>();
		
		List<Map<String, Object>> searchCategorySectionsList;
		for(SearchCategories searchCategory : searchCategories) {
			searchCategorySectionsList = searchCategoriesSectionsMap.get(searchCategory.getId());
			if(searchCategorySectionsList == null)
				continue;
			tmpMap = new HashMap<String, Object>();
			tmpMap.put("name", searchCategory.getName());
			tmpMap.put("sections", searchCategorySectionsList);
			searchCategoriesList.add(tmpMap);
		}
		
		if(searchCategoriesList.isEmpty())
			return Helper.getResultWithErrorCode(result, "0001");
		
		model.put("searchCategoriesList", searchCategoriesList);
		result.setReturnValue(model);
		return result;
	}
	
	@Override
	public Result addPost(PostsModel postsModel) {
		Result result = new Result();
		
		String content = postsModel.getContent();
		List<Short> searchCategoriesSectionsIds = postsModel.getSearchCategoriesSectionsIds();
		if(content == null || content.isEmpty() || searchCategoriesSectionsIds == null || searchCategoriesSectionsIds.isEmpty())
			return Helper.getResultWithErrorCode(result, "0000");
		if(content.length() > SystemConstants.MySQL.DataTypes.TEXT_MAX_LENGTH)
			return Helper.getResultWithErrorCode(result, "2002");
		
		if(userDAO.getSearchCategoriesSectionsCount(searchCategoriesSectionsIds) < searchCategoriesSectionsIds.size())
			return Helper.getResultWithErrorCode(result, "2003");
		
		Users user = (Users) session.getAttribute(SystemConstants.SessionVariables.USER_INFO);
		Date now = new Date();
		
		userDAO.savePojo(new Posts(null, user.getUserId(), content, now));
		
		Long postId = userDAO.getMaxPostId(user.getUserId());
		for(Short searchCategorySectionId : searchCategoriesSectionsIds)
			userDAO.savePojo(new PostsSearchCategoriesSections(new PostsSearchCategoriesSectionsId(postId, searchCategorySectionId)));
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("id", postId);
		model.put("userInfo", new UserInfo(user));
		model.put("lastChangeDate", Helper.formatDate(now, "dd/MM/yyyy, HH:mm"));
		model.put("lastChangeDateAbbr", Helper.formatDate(now, "dd/MM/yyyy, HH:mm"));
		model.put("content", content);
		model.put("nextCommentFlag", false);
		model.put("optionsList", createOptionList(true, postId, false));
		
		result.setReturnValue(model);
		return Helper.getResultWithSuccessCode(result, "2006");
	}
	
	@Override
	public Result editPost(PostsModel postsModel) {
		Result result = new Result();
		
		Long postId = postsModel.getPostId();
		String content = postsModel.getContent();
		result = Helper.checkRequiredParams(result, postId, content);
		if(!result.isExecutionSuccessful())
			return result;
		List<Short> searchCategoriesSectionsIds = postsModel.getSearchCategoriesSectionsIds();
		if(searchCategoriesSectionsIds == null || searchCategoriesSectionsIds.isEmpty())
			return Helper.getResultWithErrorCode(result, "0000");
		if(content.length() > SystemConstants.MySQL.DataTypes.TEXT_MAX_LENGTH)
			return Helper.getResultWithErrorCode(result, "2002");
		
		Posts post = userDAO.getPost(postId, ((Users) session.getAttribute(SystemConstants.SessionVariables.USER_INFO)).getUserId());
		if(post == null)
			return Helper.getResultWithErrorCode(result, "0001");
		if(userDAO.getSearchCategoriesSectionsCount(searchCategoriesSectionsIds) < searchCategoriesSectionsIds.size())
			return Helper.getResultWithErrorCode(result, "2003");
		
		post.setContent(content);
		userDAO.updatePojo(post);
		
		List<PostsSearchCategoriesSections> postsSearchCategoriesSections = userDAO.getPostsSearchCategoriesSections(postId);
		if(postsSearchCategoriesSections != null && !postsSearchCategoriesSections.isEmpty()) {
			Set<Short> searchCategorySectionIdSet = new HashSet<Short>();
			for(Short searchCategoriesSectionsId : searchCategoriesSectionsIds)
				searchCategorySectionIdSet.add(searchCategoriesSectionsId);
			for(PostsSearchCategoriesSections postsSearchCategoriesSection : postsSearchCategoriesSections)
				if(!searchCategorySectionIdSet.remove(postsSearchCategoriesSection.getId().getSearchCategorySectionId()))
					userDAO.deletePojo(postsSearchCategoriesSection);
			for(Short searchCategorySectionId : searchCategorySectionIdSet)
				userDAO.savePojo(new PostsSearchCategoriesSections(new PostsSearchCategoriesSectionsId(postId, searchCategorySectionId)));
		} else {
			for(PostsSearchCategoriesSections postsSearchCategoriesSection : postsSearchCategoriesSections)
				userDAO.deletePojo(postsSearchCategoriesSection);
			for(Short searchCategorySectionId : searchCategoriesSectionsIds)
				userDAO.savePojo(new PostsSearchCategoriesSections(new PostsSearchCategoriesSectionsId(postId, searchCategorySectionId)));
		}
		
		return Helper.getResultWithSuccessCode(result, "2007");
	}
	
	@Override
	public Result deletePost(PostsModel postsModel) {
		Result result = new Result();
		
		Long postId = postsModel.getPostId();
		if(postId == null)
			return Helper.getResultWithErrorCode(result, "0000");
		
		Posts post = userDAO.getPost(postId, ((Users) session.getAttribute(SystemConstants.SessionVariables.USER_INFO)).getUserId());
		if(post == null)
			return Helper.getResultWithErrorCode(result, "0001");
		deletePost(post);
		
		return Helper.getResultWithSuccessCode(result, "2008");
	}
	
	private void deletePost(Posts post) {
		QueryParam param = new QueryParam("POST_ID", post.getId());
		userDAO.delete("POSTS_SEARCH_CATEGORIES_SECTIONS", param);
		userDAO.delete("POST_COMMENTS", param);
		userDAO.delete("POST_RATE", param);
		userDAO.delete("USER_PINNED_POSTS", param);
		userDAO.deletePojo(post);
	}
	
	@Override
	public Result saveUserCategoriesSections(UsersModel usersModel) {
		Result result = new Result();
		
		List<Short> searchCategoriesSectionsIds = usersModel.getSearchCategoriesSectionsIds();
		if(searchCategoriesSectionsIds == null || searchCategoriesSectionsIds.isEmpty())
			return Helper.getResultWithErrorCode(result, "0000");
		
		Long userId = ((Users) session.getAttribute(SystemConstants.SessionVariables.USER_INFO)).getUserId();
		List<UsersSearchCategoriesSections> usersSearchCategoriesSections = userDAO.getUsersSearchCategoriesSections(userId);
		if(usersSearchCategoriesSections == null || usersSearchCategoriesSections.isEmpty())
			for(Short searchCategorySectionId : searchCategoriesSectionsIds)
				userDAO.savePojo(new UsersSearchCategoriesSections(new UsersSearchCategoriesSectionsId(userId, searchCategorySectionId)));
		else {
			Set<Short> searchCategorySectionIdSet = new HashSet<Short>();
			for(Short searchCategorySectionId : searchCategoriesSectionsIds)
				searchCategorySectionIdSet.add(searchCategorySectionId);
			for(UsersSearchCategoriesSections userSection : usersSearchCategoriesSections)
				if(!searchCategorySectionIdSet.remove(userSection.getId().getSearchCategorySectionId()))
					userDAO.deletePojo(userSection);
			for(Short searchCategorySectionId : searchCategorySectionIdSet)
				userDAO.savePojo(new UsersSearchCategoriesSections(new UsersSearchCategoriesSectionsId(userId, searchCategorySectionId)));
		}
		
		return Helper.getResultWithSuccessCode(result, "2009");
	}
	
}
