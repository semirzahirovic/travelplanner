
// Use Parse.Cloud.define to define as many cloud functions as you want.
// For example:
Parse.Cloud.define("deleteUserWithId", function(request, response) {
                   Parse.Cloud.useMasterKey();
                   var userId = request.params.userId;
                   var query = new Parse.Query(Parse.User);
                   query.get(userId).then(function(user) {
                                          return user.destroy();
                                          }).then(function() {
                                                  response.success("Success");
                                                  }, function(error) {
                                                  response.error(error);
                                                  });
                   });

Parse.Cloud.define("modifyUser", function(request, response) {
                   if (!request.user) {
                   response.error("Must be signed in to call this Cloud Function.")
                   return;
                   }
                   
                   // The rest of the function operates on the assumption that request.user is *authorized*
                   
                   Parse.Cloud.useMasterKey();
                   
                   // Query for the user to be modified by username
                   // The username is passed to the Cloud Function in a
                   // key named "username". You can search by email or
                   // user id instead depending on your use case.
                   
                   var query = new Parse.Query(Parse.User);
                   query.equalTo("objectId", request.params.userId);
                   
                   // Get the first user which matches the above constraints.
                   query.first({
                               success: function(anotherUser) {
                               // Successfully retrieved the user.
                               // Modify any parameters as you see fit.
                               // You can use request.params to pass specific
                               // keys and values you might want to change about
                               // this user.
                               anotherUser.set("firstname", request.params.firstname);
                               anotherUser.set("lastname", request.params.lastname);
                               anotherUser.set("username", request.params.username);
                               anotherUser.set("password", request.params.password);
                               anotherUser.set("email", request.params.email);


                               
                               // Save the user.
                               anotherUser.save(null, {
                                                success: function(anotherUser) {
                                                // The user was saved successfully.
                                                response.success(anotherUser);
                                                },
                                                error: function(error) {
                                                // The save failed.
                                                // error is a Parse.Error with an error code and description.
                                                response.error("Could not save changes to user.");
                                                }
                                                });
                               },
                               error: function(error) {
                               response.error("Could not find user.");
                               }
                               });
                   });

Parse.Cloud.define("addNewUser", function(request, response) {
                   
                   var firstName = request.params.firstName;
                   var lastName = request.params.lastname;
                   var username = request.params.username;
                   var email = request.params.email;
                   var password = request.params.password;
                   var userRole = request.params.userRole;

                   
                   var user = new Parse.User();
                   user.set("firstname", firstName);
                   user.set("lastname", lastName);
                   user.set("username", username);
                   user.set("email", email);
                   user.set("password", password);
                   user.set("userRole", userRole);

                   
                   Parse.Cloud.useMasterKey();
                   user.save(null, {
                                  success: function(user) {
                                  // Execute any logic that should take place after the object is saved.
                             response.success(user);
                             
                                  },
                                  error: function(user, error) {
                                  // Execute any logic that should take place if the save fails.
                                  // error is a Parse.Error with an error code and message.
                             response.error(error);
                                  }
                                  });
                   });

Parse.Cloud.define("addNewTrip", function(request, response) {
                   
                   var destination = request.params.destination;
                   var comment = request.params.comment;
                   var startDate = request.params.startDate;
                   var endDate = request.params.endDate;
                   var ownerId = request.params.ownerId;
                   
                   var Trip = new Parse.Object.extend("Trip");
                   var trip = new Trip();
                   trip.set("destination", destination);
                   trip.set("comment", comment);
                   trip.set("startDate", startDate);
                   trip.set("endDate", endDate);
                   var query = new Parse.Query(Parse.User);
                   query.equalTo("objectId", ownerId);
                   query.first({
                               success: function(anotherUser) {
                               trip.set("owner",anotherUser);
                               trip.save(null, {
                                         success: function(trip) {
                                         // Execute any logic that should take place after the object is saved.
                                         response.success(trip);
                                         
                                         },
                                         error: function(trip, error) {
                                         // Execute any logic that should take place if the save fails.
                                         // error is a Parse.Error with an error code and message.
                                         response.error(error);
                                         }
                                         });
                               },
                               error: function(error) {
                               response.error("Could not find user.");
                               }
                   });
                   });



Parse.Cloud.define("editTrip", function(request, response) {
                   if (!request.user) {
                   response.error("Must be signed in to call this Cloud Function.")
                   return;
                   }
                   var query = new Parse.Query("Trip");
                   query.equalTo("objectId", request.params.objectId);
                   
                   // Get the first trip which matches the above constraints.
                   query.first({
                               success: function(trip) {
                               var destination = request.params.destination;
                               var comment = request.params.comment;
                               var startDate = request.params.startDate;
                               var endDate = request.params.endDate;
                               trip.set("destination", destination);
                               trip.set("comment", comment);
                               trip.set("startDate", startDate);
                               trip.set("endDate", endDate);
                               // Save the trip.
                               trip.save(null, {
                                                success: function(trip) {
                                                // The trip was saved successfully.
                                                response.success(trip);
                                                },
                                                error: function(error) {
                                                // The save failed.
                                                // error is a Parse.Error with an error code and description.
                                                response.error("Could not save changes to trip.");
                                                }
                                                });
                               },
                               error: function(error) {
                               response.error("Could not find trip.");
                               }
                               });
                   });
