package com.example.discordbot.repository;

import com.example.discordbot.model.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<UserProfile, String> {
}
