package com.hoainam.service;

import java.util.List; 

import com.hoainam.entity.Videos;

public interface VideosService {
    List<Videos> findAll();

    Videos findById(Long id);

    Videos create(Videos video);

    Videos update(Videos video);

    void delete(Long id);

    List<Videos> search(String keyword);
}
