package com.example.wiqaya.Service;

import com.example.wiqaya.ApiResponse.ApiException;
import com.example.wiqaya.DTO.OUT.ReviewDTOOUT;
import com.example.wiqaya.Model.Review;
import com.example.wiqaya.Repository.ReviewRepository;
import com.example.wiqaya.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
//    private final ServiceProviderRepository serviceProviderRepository;

    public List<ReviewDTOOUT> getAllReviews(){
     List<Review> reviews = reviewRepository.findAll();
     List<ReviewDTOOUT> reviewDTOOUTS = new ArrayList<>();

     for (Review r : reviews){
       String username = r.getUser().getUsername();
       ReviewDTOOUT reviewDTOOUT = new ReviewDTOOUT(r.getRating(),r.getComment(),username);
       reviewDTOOUTS.add(reviewDTOOUT);
     }
     return reviewDTOOUTS;
    }

        public void addReview(Integer user_id,Integer provider_id,Review review){
//        User user = userRepository.findUserById(user_id);
//        ServiceProvider serviceProvider = serviceProviderRepository.findServiceProviderById(provider_id);
//        if(user==null || serviceProvider==null){
//            throw new ApiException("can't review");
//        }
//        user.getReviews().add(review);
//        serviceProvider.getReviews().add(review);
//        reviewRepository.save(review);
//        userRepository.save(user);
//        serviceProviderRepository.save(serviceProvider);
    }

    public void updateReview(Integer id,Review review){
      Review olReview = reviewRepository.findReviewById(id);
      if(olReview==null){
          throw new ApiException("Review not found!");
      }
      olReview.setComment(review.getComment());
      olReview.setRating(review.getRating());
      reviewRepository.save(review);
    }

    public void deleteReview(Integer id){
      Review review = reviewRepository.findReviewById(id);
      if (review==null){
          throw new ApiException("Review not found");
      }
      reviewRepository.delete(review);
    }

}