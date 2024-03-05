package com.app.service;

import com.app.dto.*;
import com.app.model.User;
import com.app.repository.UserRepository;
import com.app.util.CommonUtil;
import com.app.util.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public AppResponse authenticateUser(LoginRequest loginRequest) {
        try {
            if(!RequestValidator.isLoginUpRequestValid(loginRequest)){
                return new AppResponse(false, "Invalid value provided !");
            }

            Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
            if(userOptional.isEmpty()){
                return new AppResponse(false, "Wrong Credentials !");
            }

            if(!CommonUtil.isPasswordValid(loginRequest.getPassword(), userOptional.get().getPassword())){
                return new AppResponse(false, "Wrong Credentials !");
            }

            if(userOptional.get().getStatus().equals("Pending")){
                return new AppResponse(false, "Admin approval needed !");
            }

            return new AppResponse(true, "Login Success.");
        }
        catch (Exception e) {
            e.printStackTrace();
            return new AppResponse(false, "Login Failed !");
        }
    }

    public AppResponse doRegistration(SignupRequest signupRequest) {
        try {
            if(!RequestValidator.isSignUpRequestValid(signupRequest)){
                return new AppResponse(false, "Invalid value provided !");
            }

            Optional<User> existingUser = userRepository.findByEmail(signupRequest.getEmail());
            if(existingUser.isPresent()) {
                return new AppResponse(false, "Email already exists !");
            }

            User user = new User();
            user.setFirstName(signupRequest.getFirstName());
            user.setLastName(signupRequest.getLastName());
            user.setGender(signupRequest.getGender());
            user.setAddress(signupRequest.getAddress());
            user.setPostCode(signupRequest.getPostCode());
            user.setCity(signupRequest.getCity());
            user.setTelephone(signupRequest.getTelephone());
            user.setEmail(signupRequest.getEmail());
            user.setPassword(CommonUtil.getEncodedPassword(signupRequest.getPassword()));

            if(signupRequest.getRegistrationType().equals("USER")){
                user.setRole("USER");
                user.setStatus("Approved");
            }
            else {
                user.setRole("CLEANER");
                user.setStatus("Pending");
            }
            user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            userRepository.save(user);
            return new AppResponse(true, "Registration Success. Login Below");
        }
        catch (Exception e) {
            e.printStackTrace();
            return new AppResponse(false, "Registration Failed !");
        }
    }

    public AppResponse approveCleaner(CleanerDTO cleanerDTO, User loggedInUser) {
        try {
            if(!RequestValidator.isCleanerApprovalRequestValid(cleanerDTO)){
                return new AppResponse(false, "Invalid value provided !");
            }

            Optional<User> cleanerOptional = userRepository.findById(Long.valueOf(cleanerDTO.getId()));
            if(cleanerOptional.isEmpty()) {
                return new AppResponse(false, "Invalid value provided !");
            }

            User user = cleanerOptional.get();
            user.setStatus("Approved");
            user.setUpdatedBy(loggedInUser.getUserId());
            user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

            userRepository.save(user);
            return new AppResponse(true, "Cleaner Approved.");
        }
        catch (Exception e) {
            e.printStackTrace();
            return new AppResponse(false, "Failed to approve cleaner !");
        }
    }

    public User getUserByEmail(String email) {
        try {
            Optional<User> userOptional = userRepository.findByEmail(email);
            return userOptional.orElse(null);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<ListVo> getActiveCleanerList() {
        try {
            List<User> users = userRepository.findByRoleAndStatus("CLEANER", "Approved");
            List<ListVo> cleanerList = new ArrayList<ListVo>();
            cleanerList.add(new ListVo("", "--select one--"));
            for(User user : users) {
                ListVo listVo = new ListVo();
                listVo.setKey(String.valueOf(user.getUserId()));
                listVo.setValue(CommonUtil.getFormattedName(user));
                cleanerList.add(listVo);
            }
            return cleanerList;
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<CleanerDTO> getCleanerList() {
        try {
            List<User> users = userRepository.findByRole("CLEANER");
            List<CleanerDTO> cleanerList = new ArrayList<CleanerDTO>();
            int counter = 1;
            for(User user : users) {
                CleanerDTO cleanerDTO = new CleanerDTO();
                cleanerDTO.setSl(String.valueOf(counter++));
                cleanerDTO.setId(String.valueOf(user.getUserId()));
                cleanerDTO.setName(CommonUtil.getFormattedName(user));
                cleanerDTO.setStatus(user.getStatus());
                cleanerList.add(cleanerDTO);
            }
            return cleanerList;
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public CleanerDTO getCleanerById(Long id) {
        try {
            Optional<User> userOptional= userRepository.findById(id);
            if(userOptional.isEmpty()) {
                return new CleanerDTO();
            }
            User user = userOptional.get();
            CleanerDTO cleanerDTO = new CleanerDTO();
            cleanerDTO.setId(String.valueOf(user.getUserId()));
            cleanerDTO.setName(CommonUtil.getFormattedName(user));
            cleanerDTO.setStatus(user.getStatus());
            return cleanerDTO;
        }
        catch (Exception e) {
            e.printStackTrace();
            return new CleanerDTO();
        }
    }

    public AppResponse changePassword(PasswordChangeDTO passwordChangeDTO, User loggedInUser) {
        try {
            Optional<User> userOptional = userRepository.findById(loggedInUser.getUserId());
            if(userOptional.isEmpty()){
                return new AppResponse(false, "Invalid user !");
            }

            if(!RequestValidator.isPasswordChangeRequestValid(passwordChangeDTO)){
                return new AppResponse(false, "Invalid input !");
            }

            User user = userOptional.get();
            user.setPassword(CommonUtil.getEncodedPassword(passwordChangeDTO.getNewPassword()));
            user.setUpdatedBy(loggedInUser.getUserId());
            user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

            userRepository.save(user);
            return new AppResponse(true, "Password changed successfully");
        }
        catch (Exception e) {
            e.printStackTrace();
            return new AppResponse(false, "Failed to approve cleaner !");
        }
    }

    public ProfileDTO fetchProfileInfo(User loggedInUser) {
        try {
            Optional<User> userOptional = userRepository.findById(loggedInUser.getUserId());
            if(userOptional.isEmpty()){
                return new ProfileDTO();
            }

            User user = userOptional.get();
            return getProfileInfo(user);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ProfileDTO();
        }
    }

    private ProfileDTO getProfileInfo(User user) {
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setFirstName(user.getFirstName());
        profileDTO.setLastName(user.getLastName());
        profileDTO.setGender(user.getGender());

        profileDTO.setAddress(user.getAddress());
        profileDTO.setCity(user.getCity());
        profileDTO.setTelephone(user.getTelephone());
        profileDTO.setPostCode(user.getPostCode());

        return profileDTO;
    }

    public List<ListVo> getGenderList() {
        List<ListVo> list = new ArrayList<>();
        list.add(new ListVo("", "--select one--"));
        list.add(new ListVo("MALE", "Male"));
        list.add(new ListVo("FEMALE", "Female"));
        list.add(new ListVo("OTHER", "Other"));
        return list;
    }

    public AppResponse updateProfile(ProfileDTO profileDTO, User loggedInUser) {
        try {
            Optional<User> userOptional = userRepository.findById(loggedInUser.getUserId());
            if(userOptional.isEmpty()){
                return new AppResponse(false, "Invalid user !");
            }

            User user = userOptional.get();
            user.setFirstName(profileDTO.getFirstName());
            user.setLastName(profileDTO.getLastName());
            user.setGender(profileDTO.getGender());

            user.setAddress(profileDTO.getAddress());
            user.setCity(profileDTO.getCity());
            user.setPostCode(profileDTO.getPostCode());
            user.setTelephone(profileDTO.getTelephone());

            user.setUpdatedBy(loggedInUser.getUserId());
            user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

            userRepository.save(user);
            return new AppResponse(true, "Profile updated successfully");
        }
        catch (Exception e) {
            e.printStackTrace();
            return new AppResponse(false, "Failed to update profile !");
        }
    }
}
