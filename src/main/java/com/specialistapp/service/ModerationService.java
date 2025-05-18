package com.specialistapp.service;

import com.specialistapp.model.entity.Organization;
import com.specialistapp.model.entity.User;
import com.specialistapp.model.repository.OrganizationRepository;
import com.specialistapp.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ModerationService {
    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Organization> getPendingOrganizations() {
        return organizationRepository.findByApprovedFalse();
    }

    public List<User> getAllUsers() {
        return userRepository.findAllRegularUsers();
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public void blockUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setBlocked(true);
        userRepository.save(user);
    }

    @Transactional
    public void unblockUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setBlocked(false);
        userRepository.save(user);
    }
}