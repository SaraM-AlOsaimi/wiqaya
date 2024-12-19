## Endpoint
User-related Endpoints
1- getAllRequestForOneUser(Integer userId)
Retrieve all requests made by a specific user.
2- getTopRatedOffersForUser(Integer userId, int topN)
Get the top-rated offers for a user, based on service provider ratings.
3- getServiceProviderByRating(Double rating)
Fetch service providers with ratings eaqal or above a given reating.
//---------
Admin-related Endpoints
4- verifiedProvider
5- verifiedEng
6- getHousesByTypeAndCity
Get a list of houses based on their type and city
//---------
Service Provider-related Endpoints
7- ProviderGetPublishedReport(Integer providerId)
//---------
Engineer-related Endpoints
8- getReportsNumByEngId(Integer engineerId)
9- getAllRequestInspectionByEngId(Integer engineerId)
Retrieve all inspection requests assigned to a specific engineer.
10- checkMyStatus(Integer engineerId)

