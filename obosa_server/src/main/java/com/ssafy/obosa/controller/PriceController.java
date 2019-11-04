package com.ssafy.obosa.controller;

import com.ssafy.obosa.model.dto.AuctionIdDto;
import com.ssafy.obosa.model.dto.PriceDto;
import com.ssafy.obosa.repository.BidRedisRepository;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class PriceController 
{
    private final BidRedisRepository bidRedisRepository;
    
    PriceController(final BidRedisRepository bidRedisRepository) {
        this.bidRedisRepository = bidRedisRepository;
    }
    @MessageMapping("/price")
    @SendTo("/topic/price")
    public PriceDto getHighPrice(AuctionIdDto id) throws Exception
    {
        //TODO : redis에서 가격 가져오기
         int highestPrice = bidRedisRepository.findById(id.getId()).get().getHighestBid();
        return new PriceDto(id.getId(), highestPrice);
    }
}