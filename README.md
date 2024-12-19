## Model , Service , Controller , CRUD , DTOS : 
* User
* Review
* House


### **User-related Endpoints**
1. **`getAllRequestForOneUser(Integer userId)`** - Retrieve all requests made by a specific user.  
2. **`getTopRatedOffersForUser(Integer userId, int topN)`** - Get the top-rated offers for a user, based on service provider ratings.  
3. **`getServiceProviderByRating(Double rating)`** - Fetch service providers with ratings equal to or above a given rating.

---

### **Admin-related Endpoints**
4. **`verifiedProvider(Integer providerId)`** - Check if a service provider is verified.  
5. **`verifiedEng(Integer engineerId)`** - Check if an engineer is verified.  
6. **`getHousesByTypeAndCity(Integer adminId, String city, String type)`** - Get a list of houses based on their type and city.

---

### **Service Provider-related Endpoints**
7. **`ProviderGetPublishedReport(Integer providerId)`** - Retrieve the published report for a specific service provider.

---

### **Engineer-related Endpoints**
8. **`getReportsNumByEngId(Integer engineerId)`** - Get the number of reports created by a specific engineer.  
9. **`getAllRequestInspectionByEngId(Integer engineerId)`** - Retrieve all inspection requests assigned to a specific engineer.  
10. **`checkMyStatus(Integer engineerId)`** - Check the current status of an engineer.

---

**Diagram:**

![wiqaya drawio](https://github.com/user-attachments/assets/6f8e7a7c-b2da-4fd5-8e3f-38b716dd97dd)
