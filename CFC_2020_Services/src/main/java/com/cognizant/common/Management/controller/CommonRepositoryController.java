/**
 * 
 */
package com.cognizant.common.Management.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.common.Management.dao.CommPost;
import com.cognizant.common.Management.dao.CommonRepository;

/**
 * @author 487245
 *
 */
@RestController
public class CommonRepositoryController {

	@Autowired
	private CommonRepository commonRepository;
	
	@RequestMapping("/common/view")
    public @ResponseBody ResponseEntity<String> example() {
        List<String> list = new ArrayList<>();
        list.add("Table data...");
        for(CommPost commPost: commonRepository.findAll()){
            list.add(commPost.toString());
        }
        return new ResponseEntity<String>(list.toString(), HttpStatus.OK);
    }

}