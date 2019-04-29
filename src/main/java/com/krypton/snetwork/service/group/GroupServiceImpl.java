package com.krypton.snetwork.service.group;

import com.krypton.snetwork.model.Image;
import com.krypton.snetwork.model.User;
import com.krypton.snetwork.model.group.Group;
import com.krypton.snetwork.model.group.Post;
import com.krypton.snetwork.repository.GroupRepository;
import com.krypton.snetwork.service.image.ImageServiceImpl;
import com.krypton.snetwork.service.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupServiceImpl implements GroupService {

	@Autowired
	private GroupRepository groupRepository;
	
	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private ImageServiceImpl imageService;
	
	@Override
	public boolean groupExist(String name) {
		return getGroup(name) != null;
	}
	
	@Override
	public void insertGroup(String name, String email) {
		// get admin entity from database
		User admin  	 = userService.getUser(email);
		// get group photo entity from database
		Image image 	 = imageService.getImage(name);
		// get group background entity bytes from database 
		Image background = imageService.getBackground(name);
		// create group entity and return as object
		Group group 	 = createGroup(name, admin, image, background);
		// save new group entity to admin
		saveGroupMember(group, admin);
		// save admin entity to group
		userService.saveMemberGroup(group, admin);
	}

	@Override
	public Group createGroup(String name, User admin, Image image, Image background) {
		// group entity
		return new Group(name, admin, image, background);
	}
	
	@Override
	public void saveGroupMember(Group group, User member) {
		group.getMembers().add(member);
		groupRepository.save(group);
	}
	
	@Override
	public Group getGroup(String name) {
		return groupRepository.findByName(name);
	}
	
	@Override
	public Group getGroup(Long id) {
		return groupRepository.findById(id).get();
	}
	
	@Override
    public Post getPost(String groupId, String postId) {
    	final Post[] posts = new Post[1];
		// get group where that contains post
		getGroup(Long.valueOf(groupId))
			// find post by id
			.getPosts().forEach(post ->  {
				if (post.getId().equals(Long.valueOf(postId))) {
					posts[0] = post;
				}
			});
		return posts[0];
    }
}