package com.rms.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rms.app.model.Cart;
import com.rms.app.model.Menu;
import com.rms.app.model.Review;
import com.rms.app.model.Tables;



@Repository
public interface ReviewRepo extends JpaRepository<Review, Long>{



}