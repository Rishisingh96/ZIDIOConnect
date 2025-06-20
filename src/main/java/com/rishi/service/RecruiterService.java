package com.rishi.service;

import com.rishi.entity.RecruiterProfile;

import java.util.Optional;

public interface RecruiterService {
    public Optional<RecruiterProfile> getByCompanyName(String name);
}
