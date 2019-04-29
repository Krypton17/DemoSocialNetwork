package com.krypton.snetwork.service.user;

import com.krypton.snetwork.model.Image;
import com.krypton.snetwork.model.group.Group;
import com.krypton.snetwork.model.user.User;
import com.krypton.snetwork.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public boolean userExist(String email) {
		return userRepository.findEmail(email).isPresent();
	}

	@Override
	public void saveMemberGroup(Group group, User member) {
		// add group to member groups list
		member.getGroups().add(group);
		// update member
		userRepository.save(member);
	}

	@Override
	public User getUser(String email) {
		// user entity
		return userRepository.findByEmail(email);
	}

	@Override
	public User getUser(Long id) {
		// user entity
		return userRepository.findById(id).get();
	}

	@Override
	public void saveUser(String username, String email, String password, Image image) {
		// save user to database
		userRepository.save(new User(username,email,password,image,null));
	}
}