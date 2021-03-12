# TinySquare

A tiny mobile app which displays nearby places around user using Foursquare API.

<img src="sample.gif" width=300></img>

## The scenario

This app:
- Displays the nearby places to the user based on the user current location in a list.
- Presents a detail page for each place. 
- Displays cached data if user moves by 100m from the last retrieved location. 
- Presents Offline data when the internet is unavailable or user is still at the same location or nearby places are the same.
- Updates the nearby places when user is replaced.
- Displays an endless list of nearby places.
- Checks the internet connection using NetworkStateBroadcastReceiver.



## The way of implementation

This Android application implemented using Clean Architecture containing 3 main layers:
1.  Presentation (used MVVM pattern for this layer)
2.  Data
3.  Domain



## The main dependencies:

- Koin
- Coroutines
- Moshi
- Room
- LiveData
- Coil (Image Loader)
- ViewBinding
- Google Play services location
- Leak canary (memory leak identifier)


## Author
Zahra Heydari