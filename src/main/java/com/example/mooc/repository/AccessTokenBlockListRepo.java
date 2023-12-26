package com.example.mooc.repository;

import com.example.mooc.model.AccessTokenBlockList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessTokenBlockListRepo extends CrudRepository<AccessTokenBlockList, String> {

}


