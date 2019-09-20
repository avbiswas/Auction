const firebase = require("firebase");
// Required for side-effects
require("firebase/firestore");

// Initialize Cloud Firestore through Firebase
firebase.initializeApp({
    apiKey: "AIzaSyDlUEzkxP5o2Fiv03o7gMylsNicuSzENaU",
	projectId: "ppl-auction-883c3"
  });
  
var db = firebase.firestore();

var players = [
 {
   "legend": "",
   "name": "Virat Kohli",
   "country": "IND",
   "role": "BAT",
   "bowltype": "",
   "battype": "RHB",
   "batovr": 92,
   "ballovr": 12,
   "price": 27
 },
 {
   "legend": "",
   "name": "Rohit Sharma",
   "country": "IND",
   "role": "BAT",
   "bowltype": "",
   "battype": "RHB",
   "batovr": 89,
   "ballovr": 5,
   "price": 25
 },
 {
   "legend": "",
   "name": "Shikhar Dhawan",
   "country": "IND",
   "role": "BAT",
   "bowltype": "",
   "battype": "LHB",
   "batovr": 86,
   "ballovr": 19,
   "price": 20
 },
 {
   "legend": "",
   "name": "KL Rahul",
   "country": "IND",
   "role": "BAT",
   "bowltype": "",
   "battype": "RHB",
   "batovr": 80,
   "ballovr": 31,
   "price": 15
 },
 {
   "legend": "",
   "name": "MS Dhoni",
   "country": "IND",
   "role": "BOWL",
   "bowltype": "",
   "battype": "RHB",
   "batovr": 88,
   "ballovr": 16,
   "price": 25
 }
]

players.forEach(function(obj) {
    db.collection("/players").doc(obj.name).set({
        type: obj.role,
        name: obj.name,
        country: obj.country,
        price: obj.price,
		battype: obj.battype,
		bowltype: obj.bowltype,
		batovr: obj.batovr,
		ballovr: obj.ballovr,
    }).then(function(docRef) {
        console.log("Document written with ID: ", obj.name);
    })
    .catch(function(error) {
        console.error("Error adding document: ", error);
    });
});
