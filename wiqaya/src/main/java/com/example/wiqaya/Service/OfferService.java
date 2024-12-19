package com.example.wiqaya.Service;

import com.example.wiqaya.ApiResponse.ApiException;
import com.example.wiqaya.DTO.IN.OfferDTOIN;
import com.example.wiqaya.DTO.OUT.OfferDTOOUT;
import com.example.wiqaya.Model.*;
import com.example.wiqaya.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OfferService {

    private final OfferRepository offerRepository;
    private final HouseRepository houseRepository;
    private final ReportRepository reportRepository;
    private final ServiceProviderRepository serviceProviderRepository;
    private final UserRepository userRepository;

    public List<OfferDTOOUT> getAllOffers() {
        List<Offer>offers=offerRepository.findAll();
        if(offers.isEmpty() || offers==null)throw  new ApiException("there is no offers yet");

        List<OfferDTOOUT> dtos=new ArrayList<>();
        for(Offer o:offers){
            OfferDTOOUT offerDTOOUT =new OfferDTOOUT(o.getId(),o.getDescription(),o.getStatus(),o.getPrice(),o.getServiceProvider().getId());
            dtos.add(offerDTOOUT);
        }

        return  dtos;
    }


    public void updateOffer(Integer id,Offer offer) {
        Offer oldOffer = offerRepository.findOfferById(id);

        if (oldOffer != null) {
            oldOffer.setDescription(offer.getDescription());
            oldOffer.setPrice(offer.getPrice());
            oldOffer.setStatus(offer.getStatus());
            offerRepository.save(oldOffer);
        }else throw new ApiException("Offer Not Found");
    }
    public void deleteOffer(Integer id) {
        Offer oldOffer = offerRepository.findOfferById(id);
        if (oldOffer != null) {
            offerRepository.delete(oldOffer);
        }else throw new ApiException("Offer Not Found");
    }

    // Endpoint No.14
    //hadeel
    public void sendOffer(Integer serviceProviderid, Integer reportid, OfferDTOIN offerDTOIN){
        //check report
        Report report=reportRepository.findReportById(reportid);
        if(report==null)throw new ApiException("not found report");
        if(!report.getIsPublished())throw new ApiException("report not Published yet");

        //check service provider
        ServiceProvider serviceProvider=serviceProviderRepository.findServiceProviderById(serviceProviderid);
        if(serviceProvider==null)throw new ApiException("not found service provider");
        if(serviceProvider.getStatus().equalsIgnoreCase("Inactive"))
            throw new ApiException("service provider Inactive");

        List<Offer> offerList=offerRepository.findOffersByReport(report);
        for (Offer o:offerList){
            if(o.getServiceProvider().getId().equals(serviceProviderid)){
                if(o.getStatus().equalsIgnoreCase("Pending"));
                throw new ApiException("this service provider has pending offer on this report");
            }
        }

        //add this offer to offer  list
        Offer offer=new Offer(null,offerDTOIN.getDescription(),offerDTOIN.getPrice(),"Pending",serviceProvider,report);
        offerRepository.save(offer);


    }


    // Endpoint No.15
    //hadeel
    public List<OfferDTOOUT> getOffersByReport(Integer userid, Integer reportid){
        Report report =reportRepository.findReportById(reportid);
        if(report==null)throw new ApiException("no report found with this id");

        House house=houseRepository.findHouseById(report.getHouse().getId());
        if(!(house.getUser().getId().equals(userid))){
            throw  new ApiException("user not authorize to this report");
        }

       List<Offer>offers=offerRepository.findOffersByReport(report);
        if(offers.isEmpty() || offers==null)throw  new ApiException("there is no offers yet");

        List<OfferDTOOUT> dtos=new ArrayList<>();
        for(Offer o:offers){
            OfferDTOOUT offerDTOOUT =new OfferDTOOUT(o.getId(),o.getDescription(),o.getStatus(),o.getPrice(),o.getServiceProvider().getId());
            dtos.add(offerDTOOUT);
        }

        return  dtos;
    }


    // Endpoint No.16
    //hadeel
    public void acceptOffer(Integer userid,Integer offerid){
        //check user
        User user=userRepository.findUserById(userid);
        if(user==null)throw new ApiException("user not found");
        if(!user.getRole().equalsIgnoreCase("user")) throw new ApiException("admin can not accept user offer");
        //check offer if exist
        Offer offer=offerRepository.findOfferById(offerid);
        if(user==null)throw new ApiException("offer not found");

        //check if the user own this report
        Report report =reportRepository.findReportById(offer.getReport().getId());
        House house=houseRepository.findHouseById(report.getHouse().getId());
       if(!house.getUser().getId().equals(userid))
           throw new ApiException("the user doesn't own this report");

       if(offer.getStatus().equalsIgnoreCase("Accepted")|| (offer.getStatus().equalsIgnoreCase("Rejected"))) throw new ApiException("you already accept one of the offer");


        offer.setStatus("Accepted");
       offerRepository.save(offer);

        List<Offer>offers=offerRepository.findOffersByReport(report);
        for(Offer o:offers){
            if(o.getId()!=offer.getId()){
                o.setStatus("Rejected");
                offerRepository.save(o);
            }
        }
        report.setIsPublished(false);
        reportRepository.save(report);
        house.setStatus("inProgress");
        houseRepository.save(house);
    }


    // Endpoint No.24
    //sara
    //user get the most famous serviceProvider by offerReserved
    // user filter serviceProviders who send offer to him to get the most famous Provider based rating
    // take userId , Number of top serProvider he went like top 3 or top 5 ..
    public List<Offer> getTopRatedOffersForUser(Integer userId, int topN) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new ApiException("User not found");
        }
        List<Offer> filteredOffers = offerRepository.findOffersByUserId(userId);
        if (filteredOffers == null || filteredOffers.isEmpty()) {
            throw new ApiException("No offers found");
        }
        // Sort the offers based on the rating of the service provider in descending order
        List<Offer> sortedOffers = filteredOffers.stream()
                .sorted((offer1, offer2) -> Double.compare(
                        offer2.getServiceProvider().getAverageRating(),
                        offer1.getServiceProvider().getAverageRating()
                ))
                .collect(Collectors.toList());
        // Return only the top N offers
        return sortedOffers.stream().limit(topN).collect(Collectors.toList());
    }

    // Endpoint No.25
    //mohammed
    // get cheapest offer by the offers received by id
    public Offer getCheapestOffer(Integer userId, Integer reportId){
        User user=userRepository.findUserById(userId);
        if(user==null)throw new ApiException("user not found");

        Report report =reportRepository.findReportById(reportId);
        if(report==null)throw new ApiException("no report found with this id");

        if (!report.getRequestInspection().getHouse().getUser().getId().equals(userId)) {
            throw new ApiException("User with ID " + userId + " does not own the house associated with this report.");
        }

        List<Offer> offers = offerRepository.findOffersByReportId(reportId);
        if (offers.isEmpty()) {
            throw new ApiException("No offers found for report with ID: " + reportId);
        }
        Offer cheapestOffer = offers.get(0);
        for (Offer offer : offers) {
            if (offer.getPrice() < cheapestOffer.getPrice()) {
                cheapestOffer = offer;
            }
        }

        return cheapestOffer;
    }



    // get Offers Status For Service Provider
    public List<OfferDTOOUT> getOffersStatusForServiceProvider(Integer serviceProviderId) {
        // Check if the service provider exists
        if (!serviceProviderRepository.existsById(serviceProviderId)) {
            throw new ApiException("Service provider not found");
        }

        // Fetch offers for the service provider
        List<Offer> offers = offerRepository.findOffersByServiceProviderId(serviceProviderId);

        if (offers.isEmpty()) {
            throw new ApiException("No offers found for this service provider");
        }

        // Map offers to DTOs
        return offers.stream()
                .map(offer -> new OfferDTOOUT(
                        offer.getId(),
                        offer.getDescription(),
                        offer.getStatus(),
                        offer.getPrice(),
                        serviceProviderId
                ))
                .collect(Collectors.toList());
    }


}
