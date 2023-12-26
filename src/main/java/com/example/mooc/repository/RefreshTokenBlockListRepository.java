package com.example.mooc.repository;

import com.example.mooc.model.RefreshTokenBlockList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenBlockListRepository extends CrudRepository<RefreshTokenBlockList, String> {

}


