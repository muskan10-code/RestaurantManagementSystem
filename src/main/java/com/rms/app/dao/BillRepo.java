package com.rms.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rms.app.model.Bill;
import com.rms.app.model.Cart;
import com.rms.app.model.Menu;



@Repository
public interface BillRepo extends JpaRepository<Bill, Long>{




}