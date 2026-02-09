package com.url.shortner.repository;

import com.url.shortner.models.ClickEvent;
import com.url.shortner.models.UrlMapping;
import com.url.shortner.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClickEventRepository extends JpaRepository<ClickEvent, Long> {
    //what jpa will do is filter all the data with the url mapping and between these dates
    List<ClickEvent> findByUrlMappingAndClickDateBetween(UrlMapping mapping, LocalDateTime startDate, LocalDateTime endDate);
    //when we want to get total clicks that the user has got
    //so we will send all the url's that user has in his account along with date information
    List<ClickEvent> findByUrlMappingInAndClickDateBetween(List<UrlMapping> urlMappings, LocalDateTime startDate, LocalDateTime endDate);
}
