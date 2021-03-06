package com.ssafy.obosa.model.redis;

import com.ssafy.obosa.exception.LowerThanCurrentBidPriceException;
import com.ssafy.obosa.exception.TimeExpiredException;
import com.ssafy.obosa.repository.BidRedisRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class BidTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private BidRedisRepository bidRedisRepository;

    @After
    public void tearDown() throws Exception {
        bidRedisRepository.deleteAll();
    }

    @Test
    public void 기본_등록_조회기능() {
        // given
        String aid = "1";
        LocalDateTime endTime = LocalDateTime.now();
        int highestBid = 10000;
        int highestBidder = 1;

        Bid bid = Bid.builder()
                    .id(aid)
                    .highestBid(highestBid)
                    .highestBidder(highestBidder)
                    .endTime(endTime)
                    .build();

        // when
        bidRedisRepository.save(bid);

        // then
        Bid savedBid = bidRedisRepository.findById(aid).get();

        assertThat(savedBid.getHighestBid()).isEqualTo(10000);
        assertThat(savedBid.getHighestBidder()).isEqualTo(highestBidder);
    }

    @Test(expected = LowerThanCurrentBidPriceException.class)
    public void 입찰_금액_검사기능() {
        // given
        String aid = "1";
        LocalDateTime endTime = LocalDateTime.now();
        int highestBid = 10000;
        int highestBidder = 1;

        Bid bid = Bid.builder()
                .id(aid)
                .highestBid(highestBid)
                .highestBidder(highestBidder)
                .endTime(endTime)
                .build();

        bidRedisRepository.save(bid);
        Bid savedBid = bidRedisRepository.findById("1").get();

        // when
        savedBid.bid(5000, 2, LocalDateTime.now().minusDays(1));

        // then
    }

    @Test(expected = TimeExpiredException.class)
    public void 입찰_시간_검사기능() {
        // given
        String aid = "1";
        LocalDateTime endTime = LocalDateTime.now();
        int highestBid = 10000;
        int highestBidder = 1;

        Bid bid = Bid.builder()
                .id(aid)
                .highestBid(highestBid)
                .highestBidder(highestBidder)
                .endTime(endTime)
                .build();

        bidRedisRepository.save(bid);
        Bid savedBid = bidRedisRepository.findById("1").get();

        // when
        savedBid.bid(12000, 2, LocalDateTime.now().plusDays(1));

        // then
    }

    @Test
    public void 입찰_기능() {
        // given
        String aid = "1";
        LocalDateTime endTime = LocalDateTime.now();
        int highestBid = 10000;
        int highestBidder = 1;

        Bid bid = Bid.builder()
                .id(aid)
                .highestBid(highestBid)
                .highestBidder(highestBidder)
                .endTime(endTime)
                .build();

        bidRedisRepository.save(bid);
        Bid savedBid = bidRedisRepository.findById("1").get();

        // when
        savedBid.bid(12000, 2, LocalDateTime.now().minusDays(1));
        bidRedisRepository.save(savedBid);

        // then
        Bid afterBidding = bidRedisRepository.findById("1").get();
        assertThat(afterBidding.getHighestBid()).isEqualTo(12000);
        assertThat(afterBidding.getHighestBidder()).isEqualTo(2);
    }

    @Test
    public void 입찰_횟수_증가_기능() {
        // given
        String aid = "1";
        LocalDateTime endTime = LocalDateTime.now();
        int highestBid = 10000;
        int highestBidder = 1;

        Bid bid = Bid.builder()
                .id(aid)
                .highestBid(highestBid)
                .highestBidder(highestBidder)
                .endTime(endTime)
                .build();

        bidRedisRepository.save(bid);
        Bid savedBid = bidRedisRepository.findById("1").get();

        // when
        savedBid.bid(12000, 2, LocalDateTime.now().minusDays(1));
        bidRedisRepository.save(savedBid);

        // then
        Bid afterBidding = bidRedisRepository.findById("1").get();
        assertThat(afterBidding.getBidCount()).isEqualTo(1);
    }

    @Test
    public void 입찰_횟수순_정렬_기능() {
        // given
        LocalDateTime endTime = LocalDateTime.now();
        int highestBid = 10000;
        int highestBidder = 1;

        Bid bid = Bid.builder()
                .id("1")
                .highestBid(highestBid)
                .highestBidder(highestBidder)
                .endTime(endTime)
                .bidCount(1)
                .build();

        bidRedisRepository.save(bid);

        bid = Bid.builder()
                .id("2")
                .highestBid(highestBid)
                .highestBidder(highestBidder)
                .endTime(endTime)
                .bidCount(3)
                .build();

        bidRedisRepository.save(bid);

        bid = Bid.builder()
                .id("3")
                .highestBid(highestBid)
                .highestBidder(highestBidder)
                .endTime(endTime)
                .bidCount(2)
                .build();

        bidRedisRepository.save(bid);

//        // when
//        List<Bid> bids = bidRedisRepository.findAll();
//        System.out.println(bids);
//
//        // then
//        assertThat(bids.get(0).getBidCount()).isEqualTo(3);
//        assertThat(bids.get(1).getBidCount()).isEqualTo(2);
//        assertThat(bids.get(2).getBidCount()).isEqualTo(1);
    }
}