# Phishing SMS Detector Application
<img src="https://img.shields.io/badge/code_style-standard-brightgreen.svg"> <img src="https://img.shields.io/badge/architecture-MVVM-blue"> 
<img src="https://img.shields.io/badge/minSdk-21-orange"> <img src="https://img.shields.io/badge/targetSdk-31-lightgrey"> 
<img src="https://img.shields.io/badge/version-1-yellow">

An Android application that detects `phishing` in incoming texts and alerts the user through `notification` in real time. The user may also examine the legitimacy of any message by accessing it in the app and viewing it in graphical form. Our API is powered by a dynamic `machine learning model` that improves over time as our consumers use it. Using `YouTube's API`, we also give the relevant spam videos that have been reported. The user receives a warning and has the option of reporting/forwarding any questionable messages to 1909.

![Slide 4_3 - 1 (2)](https://user-images.githubusercontent.com/54764235/152403588-6d0a895b-fdc6-44d3-a869-235ded5545e7.png)


---
## Requirements

For development, you will only need:

* Android Studio
* Android Device or Emulator installed along with Android Studio
* Minimum supported Android SDK


## Getting started

- [Install Android Studio](https://developer.android.com/studio/install.html)
- Download the project 
```bash
 git clone https://github.com/The-Fuse/SMS-Phishing-Detection.git
```
- Open the project into Android Studio.
- Build the project and run the sample. You may need to update gradle and library versions. 
- Follow the guidance provided by Android Studio. 
- If you still not able to build the project try installing the [APK](https://drive.google.com/file/d/1Oaez71_q2Gw7kAWW7VLm0AviuIqHHKo6/view?usp=sharing) of the applicaiton


## Built With 🛠
- [Kotlin](https://kotlinlang.org/) - First class and official programming language for Android development.
- [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - For asynchronous and more..
- [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-flow/) - A cold asynchronous data stream that sequentially emits values and completes normally or with an exception.
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture) - Collection of libraries that help you design robust, testable, and maintainable apps.
  - [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - Data objects that notify views when the underlying database changes.
  - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Stores UI-related data that isn't destroyed on UI changes.
  - [ViewBinding](https://developer.android.com/topic/libraries/view-binding) - Generates a binding class for each XML layout file present in that module and allows you to more easily write code that interacts with views.
- [Retrofit](https://square.github.io/retrofit/) - A type-safe HTTP client for Android and Java.
- [Material Components for Android](https://github.com/material-components/material-components-android) - Modular and customizable Material Design UI components for Android.

## License
[MIT](https://choosealicense.com/licenses/mit/)


## ML Model Source Code: 
Check out this github repo: https://github.com/cnarte/cred_avenue_sms_api
