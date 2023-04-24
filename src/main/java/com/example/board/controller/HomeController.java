package com.example.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.example.board.model.Restaurant;
import com.example.board.model.board.Reply;
import com.example.board.model.board.ReplyDto;
import com.example.board.model.member.Member;
import com.example.board.repository.RestaurantMapper;

@Slf4j
@RequestMapping("restaurant")
@RestController
public class HomeController {
	
	@Autowired
	private RestaurantMapper spotMapper;

	

	@GetMapping("")
    public String home(Model model) {
        return "index";
    }
    
    @PostMapping("/{restaurant_id}")
    public ResponseEntity<List<Restaurant>> findRestaurant(Long restaurant_id,Model model) {
    	log.info("실행");

    	
    	List<Restaurant> findAllRestaurant = spotMapper.findAllRestaurant();
    	

    	return ResponseEntity.ok(findAllRestaurant);
    	}


	}
