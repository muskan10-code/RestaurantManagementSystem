package com.rms.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rms.app.model.Staff;
import com.rms.app.model.User;



@Repository
public interface StaffRepo extends JpaRepository<Staff, Long>{

	@Query( value = "select * from staff where email = :email or username = :email", nativeQuery = true)
	Staff findbyEmail(@Param("email") String email);

	@Query( value = "select * from staff where id = :id", nativeQuery = true)
	Staff findStaffById(@Param("id") Long id);


}