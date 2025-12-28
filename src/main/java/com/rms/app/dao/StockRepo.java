package com.rms.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rms.app.model.Menu;
import com.rms.app.model.Stock;



@Repository
public interface StockRepo extends JpaRepository<Stock, Long>{



}