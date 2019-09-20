const firebase = require("firebase");
// Required for side-effects
require("firebase/firestore");

// Initialize Cloud Firestore through Firebase
firebase.initializeApp({
    apiKey: "AIzaSyDlUEzkxP5o2Fiv03o7gMylsNicuSzENaU",
	projectId: "ppl-auction-883c3"
  });
  
var db = firebase.firestore();

var batsman = [
 {
   "id": 1,
   "name": "A DOOLAN",
   "country": "AUS",
   "price": 5,
   "type": "LHB"
 },
 {
   "id": 2,
   "name": "A FINCH",
   "country": "AUS",
   "price": 5,
   "type": "RHB"
 },
 {
   "id": 3,
   "name": "B HODGE",
   "country": "AUS",
   "price": 5,
   "type": "RHB"
 },
 {
   "id": 4,
   "name": "C ROGERS",
   "country": "AUS",
   "price": 5,
   "type": "LHB"
 },
 {
   "id": 5,
   "name": "D WARNER",
   "country": "AUS",
   "price": 10,
   "type": "RHB"
 },
 {
   "id": 6,
   "name": "G BAILEY",
   "country": "AUS",
   "price": 10,
   "type": "RHB"
 },
 {
   "id": 7,
   "name": "M CLARKE",
   "country": "AUS",
   "price": 15,
   "type": "RHB"
 },
 {
   "id": 8,
   "name": "M HUSSEY",
   "country": "AUS",
   "price": 15,
   "type": "LHB"
 },
 {
   "id": 9,
   "name": "S MARSH",
   "country": "AUS",
   "price": 10,
   "type": "LHB"
 },
 {
   "id": 10,
   "name": "S SMITH",
   "country": "AUS",
   "price": 15,
   "type": "RHB"
 },
 {
   "id": 11,
   "name": "T IQBAL",
   "country": "BAN",
   "price": 10,
   "type": "LHB"
 },
 {
   "id": 12,
   "name": "A COOK",
   "country": "ENG",
   "price": 10,
   "type": "LHB"
 },
 {
   "id": 13,
   "name": "E MORGAN",
   "country": "ENG",
   "price": 10,
   "type": "LHB"
 },
 {
   "id": 15,
   "name": "I BELL",
   "country": "ENG",
   "price": 10,
   "type": "RHB"
 },
 {
   "id": 16,
   "name": "M CARBERRY",
   "country": "ENG",
   "price": 5,
   "type": "RHB"
 },
 {
   "id": 17,
   "name": "J ROOT",
   "country": "ENG",
   "price": 5,
   "type": "RHB"
 },
 {
   "id": 18,
   "name": "A RAHANE",
   "country": "IND",
   "price": 10,
   "type": "RHB"
 },
 {
   "id": 19,
   "name": "C PUJARA",
   "country": "IND",
   "price": 10,
   "type": "RHB"
 },
 {
   "id": 20,
   "name": "G GAMBHIR",
   "country": "IND",
   "price": 15,
   "type": "LHB"
 },
 {
   "id": 21,
   "name": "M BISLA",
   "country": "IND",
   "price": 5,
   "type": "RHB"
 },
 {
   "id": 22,
   "name": "M TIWARY",
   "country": "IND",
   "price": 5,
   "type": "LHB"
 },
 {
   "id": 23,
   "name": "M VIJAY",
   "country": "IND",
   "price": 5,
   "type": "RHB"
 },
 {
   "id": 24,
   "name": "R UTHAPPA",
   "country": "IND",
   "price": 5,
   "type": "RHB"
 },
 {
   "id": 25,
   "name": "S DHAWAN",
   "country": "IND",
   "price": 10,
   "type": "LHB"
 },
 {
   "id": 26,
   "name": "V KOHLI",
   "country": "IND",
   "price": 20,
   "type": "RHB"
 },
 {
   "id": 27,
   "name": "V SEHWAG",
   "country": "IND",
   "price": 20,
   "type": "RHB"
 },
 {
   "id": 28,
   "name": "H RUTHERFORD",
   "country": "NZ",
   "price": 5,
   "type": "RHB"
 },
 {
   "id": 29,
   "name": "K WILLIAMSON",
   "country": "NZ",
   "price": 5,
   "type": "RHB"
 },
 {
   "id": 30,
   "name": "M GUPTILL",
   "country": "NZ",
   "price": 10,
   "type": "RHB"
 },
 {
   "id": 31,
   "name": "R TAYLOR",
   "country": "NZ",
   "price": 10,
   "type": "RHB"
 },
 {
   "id": 32,
   "name": "T FULTON",
   "country": "NZ",
   "price": 5,
   "type": "RHB"
 },
 {
   "id": 33,
   "name": "A ALI",
   "country": "PAK",
   "price": 5,
   "type": "RHB"
 },
 {
   "id": 34,
   "name": "A SHEHZAD",
   "country": "PAK",
   "price": 5,
   "type": "RHB"
 },
 {
   "id": 35,
   "name": "S AHMED",
   "country": "PAK",
   "price": 5,
   "type": "RHB"
 },
 {
   "id": 36,
   "name": "S MAQSOOD",
   "country": "PAK",
   "price": 5,
   "type": "RHB"
 },
 {
   "id": 37,
   "name": "Y KHAN",
   "country": "PAK",
   "price": 20,
   "type": "RHB"
 },
 {
   "id": 38,
   "name": "A. PETERSON",
   "country": "RSA",
   "price": 5,
   "type": "LHB"
 },
 {
   "id": 39,
   "name": "D ELGAR",
   "country": "RSA",
   "price": 5,
   "type": "LHB"
 },
 {
   "id": 40,
   "name": "D MILLER",
   "country": "RSA",
   "price": 10,
   "type": "LHB"
 },
 {
   "id": 41,
   "name": "F DU PLESSIS",
   "country": "RSA",
   "price": 10,
   "type": "RHB"
 },
 {
   "id": 42,
   "name": "G.SMITH",
   "country": "RSA",
   "price": 15,
   "type": "LHB"
 },
 {
   "id": 43,
   "name": "H AMLA",
   "country": "RSA",
   "price": 15,
   "type": "RHB"
 },
 {
   "id": 44,
   "name": "J RUDOLPH",
   "country": "RSA",
   "price": 5,
   "type": "LHB"
 },
 {
   "id": 45,
   "name": "Q DE KOCK",
   "country": "RSA",
   "price": 10,
   "type": "LHB"
 },
 {
   "id": 46,
   "name": "D KARUNARATNE",
   "country": "SL",
   "price": 5,
   "type": "RHB"
 },
 {
   "id": 47,
   "name": "T DILSHAN",
   "country": "SL",
   "price": 15,
   "type": "RHB"
 },
 {
   "id": 48,
   "name": "T SAMARAWEERA",
   "country": "SL",
   "price": 5,
   "type": "RHB"
 },
 {
   "id": 49,
   "name": "U THARANGA",
   "country": "SL",
   "price": 10,
   "type": "LHB"
 },
 {
   "id": 50,
   "name": "C GAYLE",
   "country": "WI",
   "price": 15,
   "type": "LHB"
 },
 {
   "id": 51,
   "name": "K POWELL",
   "country": "WI",
   "price": 5,
   "type": "RHB"
 },
 {
   "id": 52,
   "name": "M SAMUELS",
   "country": "WI",
   "price": 5,
   "type": "RHB"
 }
]
var allrounders = [
 {
   "id": 1,
   "name": "S WATSON",
   "country": "AUS",
   "price": 15,
   "battype": "RHB",
   "bowltype": "MF"
 },
 {
   "id": 2,
   "name": "D CHRISTIAN",
   "country": "AUS",
   "price": 5,
   "battype": "RHB",
   "bowltype": "MF"
 },
 {
   "id": 3,
   "name": "J FAULKNAR",
   "country": "AUS",
   "price": 10,
   "battype": "RHB",
   "bowltype": "MF"
 },
 {
   "id": 4,
   "name": "M HENRIQUES",
   "country": "AUS",
   "price": 5,
   "battype": "RHB",
   "bowltype": "MF"
 },
 {
   "id": 5,
   "name": "G MAXWELL",
   "country": "AUS",
   "price": 10,
   "battype": "RHB",
   "bowltype": "S"
 },
 {
   "id": 6,
   "name": "C WHITE",
   "country": "AUS",
   "price": 10,
   "battype": "RHB",
   "bowltype": "S"
 },
 {
   "id": 7,
   "name": "A HAQUE",
   "country": "BAN",
   "price": 5,
   "battype": "RHB",
   "bowltype": "MF"
 },
 {
   "id": 8,
   "name": "S AL HASAN",
   "country": "BAN",
   "price": 15,
   "battype": "LHB",
   "bowltype": "S"
 },
 {
   "id": 9,
   "name": "M MORTAZA",
   "country": "BAN",
   "price": 10,
   "battype": "RHB",
   "bowltype": "MF"
 },
 {
   "id": 10,
   "name": "B STOKES",
   "country": "ENG",
   "price": 5,
   "battype": "LHB",
   "bowltype": "MF"
 },
 {
   "id": 11,
   "name": "R BOPARA",
   "country": "ENG",
   "price": 10,
   "battype": "RHB",
   "bowltype": "MF"
 },
 {
   "id": 12,
   "name": "T BRESNAN",
   "country": "ENG",
   "price": 5,
   "battype": "RHB",
   "bowltype": "MF"
 },
 {
   "id": 13,
   "name": "S BROAD",
   "country": "ENG",
   "price": 10,
   "battype": "LHB",
   "bowltype": "MF"
 },
 {
   "id": 14,
   "name": "K PIETERSEN",
   "country": "ENG",
   "price": 20,
   "battype": "RHB",
   "bowltype": "S"
 },
 {
   "id": 15,
   "name": "C WOAKES",
   "country": "ENG",
   "price": 5,
   "battype": "RHB",
   "bowltype": "MF"
 },
 {
   "id": 16,
   "name": "L WRIGHT",
   "country": "ENG",
   "price": 5,
   "battype": "LHB",
   "bowltype": "MF"
 },
 {
   "id": 17,
   "name": "R SHARMA",
   "country": "IND",
   "price": 15,
   "battype": "RHB",
   "bowltype": "S"
 },
 {
   "id": 18,
   "name": "R ASHWIN",
   "country": "IND",
   "price": 10,
   "battype": "RHB",
   "bowltype": "S"
 },
 {
   "id": 19,
   "name": "P CHAWLA",
   "country": "IND",
   "price": 5,
   "battype": "LHB",
   "bowltype": "S"
 },
 {
   "id": 20,
   "name": "R JADEJA",
   "country": "IND",
   "price": 10,
   "battype": "LHB",
   "bowltype": "S"
 },
 {
   "id": 21,
   "name": "Y PATHAN",
   "country": "IND",
   "price": 5,
   "battype": "RHB",
   "bowltype": "S"
 },
 {
   "id": 22,
   "name": "I PATHAN",
   "country": "IND",
   "price": 5,
   "battype": "LHB",
   "bowltype": "MF"
 },
 {
   "id": 23,
   "name": "S RAINA",
   "country": "IND",
   "price": 15,
   "battype": "LHB",
   "bowltype": "S"
 },
 {
   "id": 24,
   "name": "Y SINGH",
   "country": "IND",
   "price": 20,
   "battype": "LHB",
   "bowltype": "S"
 },
 {
   "id": 25,
   "name": "L R SUKLA",
   "country": "IND",
   "price": 5,
   "battype": "RHB",
   "bowltype": "MF"
 },
 {
   "id": 26,
   "name": "S K YADAV",
   "country": "IND",
   "price": 5,
   "battype": "RHB",
   "bowltype": "MF"
 },
 {
   "id": 27,
   "name": "S BINNY",
   "country": "IND",
   "price": 10,
   "battype": "RHB",
   "bowltype": "MF"
 },
 {
   "id": 28,
   "name": "R BHATIA",
   "country": "IND",
   "price": 5,
   "battype": "RHB",
   "bowltype": "M"
 },
 {
   "id": 29,
   "name": "C ANDERSON",
   "country": "NZ",
   "price": 10,
   "battype": "LHB",
   "bowltype": "MF"
 },
 {
   "id": 30,
   "name": "D VETTORI",
   "country": "NZ",
   "price": 10,
   "battype": "LHB",
   "bowltype": "S"
 },
 {
   "id": 31,
   "name": "J FRANKLIN",
   "country": "NZ",
   "price": 5,
   "battype": "RHB",
   "bowltype": "MF"
 },
 {
   "id": 32,
   "name": "N MCCULLUM",
   "country": "NZ",
   "price": 5,
   "battype": "RHB",
   "bowltype": "MF"
 },
 {
   "id": 33,
   "name": "A HAFEEZ",
   "country": "PAK",
   "price": 10,
   "battype": "RHB",
   "bowltype": "S"
 },
 {
   "id": 34,
   "name": "S AFRIDI",
   "country": "PAK",
   "price": 10,
   "battype": "RHB",
   "bowltype": "S"
 },
 {
   "id": 35,
   "name": "T AHEMED",
   "country": "PAK",
   "price": 5,
   "battype": "RHB",
   "bowltype": "MF"
 },
 {
   "id": 36,
   "name": "U AMIN",
   "country": "PAK",
   "price": 5,
   "battype": "RHB",
   "bowltype": "MF"
 },
 {
   "id": 37,
   "name": "A MORKEL",
   "country": "RSA",
   "price": 10,
   "battype": "LHB",
   "bowltype": "MF"
 },
 {
   "id": 38,
   "name": "F BEHARDIEN",
   "country": "RSA",
   "price": 5,
   "battype": "RHB",
   "bowltype": "MF"
 },
 {
   "id": 39,
   "name": "J P DUMINY",
   "country": "RSA",
   "price": 10,
   "battype": "LHB",
   "bowltype": "S"
 },
 {
   "id": 40,
   "name": "R MCLAREL",
   "country": "RSA",
   "price": 5,
   "battype": "RHB",
   "bowltype": "MF"
 },
 {
   "id": 41,
   "name": "J ONTONG",
   "country": "RSA",
   "price": 5,
   "battype": "RHB",
   "bowltype": "M"
 },
 {
   "id": 42,
   "name": "A MATTHEWS",
   "country": "SL",
   "price": 10,
   "battype": "RHB",
   "bowltype": "M"
 },
 {
   "id": 43,
   "name": "A DANANJAYA",
   "country": "SL",
   "price": 5,
   "battype": "RHB",
   "bowltype": "MF"
 },
 {
   "id": 44,
   "name": "A PERERA",
   "country": "SL",
   "price": 5,
   "battype": "RHB",
   "bowltype": "MF"
 },
 {
   "id": 45,
   "name": "D PRASAD",
   "country": "SL",
   "price": 5,
   "battype": "RHB",
   "bowltype": "MF"
 },
 {
   "id": 46,
   "name": "D BRAVO",
   "country": "WI",
   "price": 15,
   "battype": "RHB",
   "bowltype": "MF"
 },
 {
   "id": 47,
   "name": "D SAMMY",
   "country": "WI",
   "price": 10,
   "battype": "RHB",
   "bowltype": "MF"
 },
 {
   "id": 48,
   "name": "K POLLARD",
   "country": "WI",
   "price": 15,
   "battype": "RHB",
   "bowltype": "MF"
 },
 {
   "id": 49,
   "name": "C BOUGH",
   "country": "WI",
   "price": 5,
   "battype": "RHB",
   "bowltype": "MF"
 },
 {
   "id": 50,
   "name": "A RUSSEL",
   "country": "WI",
   "price": 15,
   "battype": "RHB",
   "bowltype": "MF"
 },
 {
   "id": 51,
   "name": "L SIMMONS",
   "country": "WI",
   "price": 10,
   "battype": "RHB",
   "bowltype": "MF"
 },
 {
   "id": 52,
   "name": "D SMITH",
   "country": "WI",
   "price": 10,
   "battype": "RHB",
   "bowltype": "MF"
 }
]
var bowlers = [
 {
   "id": 1,
   "name": "M JOHNSON",
   "country": "AUS",
   "price": 15,
   "type": "F"
 },
 {
   "id": 2,
   "name": "R HARRIS",
   "country": "AUS",
   "price": 5,
   "type": "MF"
 },
 {
   "id": 3,
   "name": "J PATTINSON",
   "country": "AUS",
   "price": 5,
   "type": "F"
 },
 {
   "id": 4,
   "name": "N LYON",
   "country": "AUS",
   "price": 5,
   "type": "S"
 },
 {
   "id": 5,
   "name": "P SIDDLE",
   "country": "AUS",
   "price": 10,
   "type": "F"
 },
 {
   "id": 6,
   "name": "J BIRD",
   "country": "AUS",
   "price": 5,
   "type": ""
 },
 {
   "id": 7,
   "name": "B HOGG",
   "country": "AUS",
   "price": 5,
   "type": "S"
 },
 {
   "id": 8,
   "name": "M C KAY",
   "country": "AUS",
   "price": 5,
   "type": "MF"
 },
 {
   "id": 9,
   "name": "M STARC",
   "country": "AUS",
   "price": 10,
   "type": "F"
 },
 {
   "id": 10,
   "name": "A RAZZAK",
   "country": "BAN",
   "price": 5,
   "type": "S"
 },
 {
   "id": 11,
   "name": "J ANDERSON",
   "country": "ENG",
   "price": 10,
   "type": "M"
 },
 {
   "id": 12,
   "name": "J TREDWELL",
   "country": "ENG",
   "price": 5,
   "type": "MF"
 },
 {
   "id": 13,
   "name": "S BORTHWICK",
   "country": "ENG",
   "price": 5,
   "type": ""
 },
 {
   "id": 14,
   "name": "S FINN",
   "country": "ENG",
   "price": 10,
   "type": "F"
 },
 {
   "id": 15,
   "name": "M PANASAR",
   "country": "ENG",
   "price": 5,
   "type": "S"
 },
 {
   "id": 16,
   "name": "B KUMAR",
   "country": "IND",
   "price": 10,
   "type": "MF"
 },
 {
   "id": 17,
   "name": "I SHARMA",
   "country": "IND",
   "price": 10,
   "type": "F"
 },
 {
   "id": 18,
   "name": "P OJHA",
   "country": "IND",
   "price": 5,
   "type": "S"
 },
 {
   "id": 19,
   "name": "V AARON",
   "country": "IND",
   "price": 10,
   "type": "F"
 },
 {
   "id": 20,
   "name": "L BALAJI",
   "country": "IND",
   "price": 5,
   "type": "MF"
 },
 {
   "id": 21,
   "name": "A DINDA",
   "country": "IND",
   "price": 10,
   "type": "MF"
 },
 {
   "id": 22,
   "name": "Z KHAN",
   "country": "IND",
   "price": 10,
   "type": "MF"
 },
 {
   "id": 23,
   "name": "H SINGH",
   "country": "IND",
   "price": 5,
   "type": "S"
 },
 {
   "id": 24,
   "name": "U YADAV",
   "country": "IND",
   "price": 5,
   "type": "F"
 },
 {
   "id": 25,
   "name": "A NEHRA",
   "country": "IND",
   "price": 10,
   "type": "F"
 },
 {
   "id": 26,
   "name": "M SHAMI",
   "country": "IND",
   "price": 10,
   "type": "F"
 },
 {
   "id": 27,
   "name": "J UNADKAT",
   "country": "IND",
   "price": 5,
   "type": "MF"
 },
 {
   "id": 28,
   "name": "M KARTIK",
   "country": "IND",
   "price": 5,
   "type": "S"
 },
 {
   "id": 29,
   "name": "T SOUTHEE",
   "country": "NZ",
   "price": 5,
   "type": "MF"
 },
 {
   "id": 30,
   "name": "N WAGNER",
   "country": "NZ",
   "price": 5,
   "type": ""
 },
 {
   "id": 31,
   "name": "T BOULT",
   "country": "NZ",
   "price": 5,
   "type": "F"
 },
 {
   "id": 32,
   "name": "I BUTLER",
   "country": "NZ",
   "price": 5,
   "type": "MF"
 },
 {
   "id": 33,
   "name": "G ELLIOT",
   "country": "NZ",
   "price": 5,
   "type": ""
 },
 {
   "id": 34,
   "name": "M GILLESPIE",
   "country": "NZ",
   "price": 5,
   "type": ""
 },
 {
   "id": 35,
   "name": "U  GUL",
   "country": "PAK",
   "price": 10,
   "type": "F"
 },
 {
   "id": 36,
   "name": "Z BABAR",
   "country": "PAK",
   "price": 5,
   "type": ""
 },
 {
   "id": 37,
   "name": "S AJMAL",
   "country": "PAK",
   "price": 10,
   "type": "S"
 },
 {
   "id": 38,
   "name": "S TANVIR",
   "country": "PAK",
   "price": 10,
   "type": "F"
 },
 {
   "id": 39,
   "name": "A AKMAL",
   "country": "PAK",
   "price": 5,
   "type": ""
 },
 {
   "id": 40,
   "name": "R PETERSON",
   "country": "RSA",
   "price": 5,
   "type": "S"
 },
 {
   "id": 41,
   "name": "V PHILANDER",
   "country": "RSA",
   "price": 5,
   "type": "MF"
 },
 {
   "id": 42,
   "name": "D STEYN",
   "country": "RSA",
   "price": 20,
   "type": "F"
 },
 {
   "id": 43,
   "name": "M MORKEL",
   "country": "RSA",
   "price": 15,
   "type": "F"
 },
 {
   "id": 44,
   "name": "K ABBOTT",
   "country": "RSA",
   "price": 5,
   "type": ""
 },
 {
   "id": 45,
   "name": "C MORRIS",
   "country": "RSA",
   "price": 10,
   "type": "F"
 },
 {
   "id": 46,
   "name": "W PARNELL",
   "country": "RSA",
   "price": 5,
   "type": "F"
 },
 {
   "id": 47,
   "name": "A PHANGISO",
   "country": "RSA",
   "price": 5,
   "type": ""
 },
 {
   "id": 48,
   "name": "I TAHIR",
   "country": "RSA",
   "price": 10,
   "type": "S"
 },
 {
   "id": 49,
   "name": "N KULASEKARA",
   "country": "SL",
   "price": 10,
   "type": "MF"
 },
 {
   "id": 50,
   "name": "S ERANGA",
   "country": "SL",
   "price": 5,
   "type": ""
 },
 {
   "id": 51,
   "name": "A MENDIS",
   "country": "SL",
   "price": 10,
   "type": "S"
 },
 {
   "id": 52,
   "name": "L MALINGA",
   "country": "SL",
   "price": 15,
   "type": "F"
 },
 {
   "id": 53,
   "name": "K ROACH",
   "country": "WI",
   "price": 5,
   "type": "F"
 },
 {
   "id": 54,
   "name": "S SHILLINGFORD",
   "country": "WI",
   "price": 5,
   "type": "S"
 },
 {
   "id": 55,
   "name": "T BEST",
   "country": "WI",
   "price": 10,
   "type": "F"
 },
 {
   "id": 56,
   "name": "S BADREE",
   "country": "WI",
   "price": 5,
   "type": "S"
 },
 {
   "id": 57,
   "name": "F EDWARDS",
   "country": "WI",
   "price": 10,
   "type": "F"
 },
 {
   "id": 58,
   "name": "J HOLDER",
   "country": "WI",
   "price": 10,
   "type": "F"
 },
 {
   "id": 59,
   "name": "S NARINE",
   "country": "WI",
   "price": 10,
   "type": "S"
 }
]
var keepers = [
 {
   "id": 1,
   "name": "B HADDIN",
   "country": "AUS",
   "price": 10,
   "type": "RHB"
 },
 {
   "id": 2,
   "name": "M RAHIM",
   "country": "BAN",
   "price": 5,
   "type": "RHB"
 },
 {
   "id": 3,
   "name": "J BUTTLER",
   "country": "ENG",
   "price": 5,
   "type": "RHB"
 },
 {
   "id": 4,
   "name": "M PRIOR",
   "country": "ENG",
   "price": 5,
   "type": "RHB"
 },
 {
   "id": 5,
   "name": "M. S. DHONI",
   "country": "IND",
   "price": 25,
   "type": "RHB"
 },
 {
   "id": 6,
   "name": "D KARTIK",
   "country": "IND",
   "price": 10,
   "type": "RHB"
 },
 {
   "id": 7,
   "name": "W SAHA",
   "country": "IND",
   "price": 5,
   "type": "RHB"
 },
 {
   "id": 8,
   "name": "B WATLING",
   "country": "NZ",
   "price": 5,
   "type": "RHB"
 },
 {
   "id": 9,
   "name": "B MCCULLUM",
   "country": "NZ",
   "price": 20,
   "type": "RHB"
 },
 {
   "id": 10,
   "name": "U AKMAL",
   "country": "PAK",
   "price": 10,
   "type": "RHB"
 },
 {
   "id": 11,
   "name": "K AKMAL",
   "country": "PAK",
   "price": 10,
   "type": "RHB"
 },
 {
   "id": 12,
   "name": "A DE VILLIERS",
   "country": "RSA",
   "price": 25,
   "type": "RHB"
 },
 {
   "id": 14,
   "name": "D CHANDIMAL",
   "country": "SL",
   "price": 10,
   "type": "RHB"
 },
 {
   "id": 15,
   "name": "D RAMDIN",
   "country": "WI",
   "price": 10,
   "type": "RHB"
 },
 {
   "id": 16,
   "name": "K SANGAKKARA",
   "country": "Sl",
   "price": 20,
   "type": "LHB"
 }
]


batsman.forEach(function(obj) {
    db.collection("/players").doc(obj.name).set({
        type: "batsman",
        name: obj.name,
        country: obj.country,
        price: obj.price,
		battype: obj.type,
		auctionID: 0,
		soldTo: null,
		isAuction: false
    }).then(function(docRef) {
        console.log("Document written with ID: ", obj.name);
    })
    .catch(function(error) {
        console.error("Error adding document: ", error);
    });
});

allrounders.forEach(function(obj) {
    db.collection("/players").doc(obj.name).set({
        type: "allrounder",
        name: obj.name,
        country: obj.country,
        price: obj.price,
		battype: obj.battype,
		bowltype: obj.bowltype,
		auctionID: 0,
		soldTo: null,
		isAuction: false
    }).then(function(docRef) {
        console.log("Document written with ID: ",obj.name );
    })
    .catch(function(error) {
        console.error("Error adding document: ", error);
    });
});

bowlers.forEach(function(obj) {
    db.collection("/players").doc(obj.name).set({
        type: "bowler",
        name: obj.name,
        country: obj.country,
        price: obj.price,
		bowltype: obj.type,
		auctionID: 0,
		soldTo: null,
		isAuction: false
    }).then(function(docRef) {
        console.log("Document written with ID: ", obj.name);
    })
    .catch(function(error) {
        console.error("Error adding document: ", error);
    });
});

keepers.forEach(function(obj) {
    db.collection("/players").doc(obj.name).set({
		type: "keepers",
		name: obj.name,
        country: obj.country,
        price: obj.price,
		battype: obj.type,
		auctionID: 0,
		soldTo: null,
		isAuction: false
    }).then(function(docRef) {
        console.log("Document written with ID: ", obj.name);
    })
    .catch(function(error) {
        console.error("Error adding document: ", error);
    });
});
