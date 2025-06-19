package com.bad.batch.Controller;

import com.bad.batch.Model.Profile;
import com.bad.batch.Enum.ExperienceLevel;
import com.bad.batch.Enum.ProfileVisibility;
import com.bad.batch.Repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/profiles")
public class ProfileController {
    @Autowired
    private ProfileRepository profileRepository;

    @GetMapping
    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profile> getProfileById(@PathVariable Long id) {
        Optional<Profile> profile = profileRepository.findById(id);
        return profile.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Profile createProfile(@RequestBody Profile profile) {
        if (profile.getExperienceLevel() == null) {
            profile.setExperienceLevel(ExperienceLevel.JUNIOR);
        }
        if (profile.getVisibility() == null) {
            profile.setVisibility(ProfileVisibility.PUBLIC);
        }
        return profileRepository.save(profile);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Profile> updateProfile(@PathVariable Long id, @RequestBody Profile details) {
        Optional<Profile> optional = profileRepository.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Profile profile = optional.get();
        profile.setBio(details.getBio());
        profile.setLocation(details.getLocation());
        profile.setGithubUrl(details.getGithubUrl());
        profile.setLinkedinUrl(details.getLinkedinUrl());
        profile.setPersonalWebsite(details.getPersonalWebsite());
        profile.setExperienceLevel(details.getExperienceLevel());
        profile.setVisibility(details.getVisibility());
        profile.setTechnologies(details.getTechnologies());
        profile.setInterests(details.getInterests());
        profile.setObjectives(details.getObjectives());
        return ResponseEntity.ok(profileRepository.save(profile));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable Long id) {
        if (!profileRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        profileRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

