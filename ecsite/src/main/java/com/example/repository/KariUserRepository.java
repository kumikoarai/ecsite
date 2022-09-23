package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.KariUser;

public interface KariUserRepository extends JpaRepository<KariUser, Integer> {
	public KariUser findByUserEmailLike(String email);
}
