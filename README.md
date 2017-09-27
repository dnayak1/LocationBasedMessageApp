# LocationBasedMessageApp

An app that is based on messaging system where one user can send message to another with Location constraint so the receiver can only unlock the message if he is in the range of the respective region. The regions are marked using iBeacons.
The main challenge was to handle the intermittent flickering on BLE device signal.
The app has functionalities like marked read, compose, reply and unlock.
The api for the app is developed in node js and database is created in MySql and both are hosted in Amazon Web Service so the app can be accessed globally anytime.
