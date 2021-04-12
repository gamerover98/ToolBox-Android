## ToolBox Android application

ToolBox Android Application was made for the Android exam of the Informatic technologies for the software prodution of Uniba Italy University.

Authors (MAGR group):
- [gamerover98](https://github.com/gamerover98 "gamerover98") (PM and java developer)
- [RaffSStein](https://github.com/RaffSStein "RaffSStein") (java developer)
- [Gip94](https://github.com/Gip94 "Gip94") (Material Design and user interface)
- [zack-97](https://github.com/zack-97 "zack-97") (Material Design and user interface)

### Introduction
ToolBox is an Android Application that offers to the user the opportunity to make the most of the sensors of its own device. You can use a tool to make a measure and save it into your device or remotely on the google firebase cloud. It uses the remote firebase database to keep registered accounts (or google logins) and user data.

Supported *tools* at this time:
- **Ruler** (no sensors, just a ruler)
- **Magnetometer**
- **Barometer**
- **Luxmeter**
- **Compass**
- **Step count**
------------

### READ BEFORE CLONE IT
To compile and run this application you have to use Android Studio (canary versions) and Gradle 4.x.x (in date 2021/04/12 if you compile it with Gradle 7, it throws an exception) and besides, **you must install the IntelliJ [Lombok Plugin](https://plugins.jetbrains.com/plugin/6317-lombok "Lombok Plugin") into your IDE**.

Useful things:
- SDK target: 28+ (Android 9+)
- Color palette from [Immuni Covid Android App](https://github.com/immuni-app/immuni "Immuni Covid Android application") under GNU public license.

#### Rules

This software has a free license but you have to comply with the following rules:

**1.** The use of the entire source or a piece of it needs to be reported into your project description files.
**2.** You can't sell this project or a derivative.
**3.** You can't use it for your exam. You must **fork** it and you have to create significant changes. Besides, you must respect the first rule.

##### Dependencies
- **Android X**: constraint layout, preference, navigation: fragment and UI, drawer layout, Room (DAO per SQLite)
- **Material Design** components by Google
- **MP Android Chart**: https://github.com/PhilJay/MPAndroidChart
- **Vector Child Finder**: https://github.com/devendroid/VectorChildFinder
- **Firebase**: Auth, UI-Auth, database
- **Lombok**: https://plugins.jetbrains.com/plugin/6317-lombok and https://projectlombok.org/

Jetbrains annotations
------------

### Screenshots and explains

![](https://i.imgur.com/emu0iUJ.png)  ![](https://i.imgur.com/eaZV03g.png) ![](https://i.imgur.com/F0FsF1D.png)
> The authentication, sign in (login) and register activities. From the auth activity, you can log in as a guest (without authentication)

------------

![](https://i.imgur.com/gcAcO8Q.png) ![](https://i.imgur.com/pTlKLQt.png) ![](https://i.imgur.com/Jd1HCKI.png) ![](https://i.imgur.com/U9pHPoh.png)
> The main page of the application. Here you can see your saves.
> 1. By clicking a card, the edit interface will be opened.
> 2. By swipe a card from left to right, it will be deleted.
> 3. By clicking the FAB (right-down corner) the tool list will be opened.
> 4. By clicking the hamburger (left-up corner) or swipe from left to right side, the navigation drawer will be shown.
> 5. The settings. Available languages: english and italian.

------------

![](https://i.imgur.com/O9D1StO.png)
> The tool list interface. In this screenshot the step counter is missing, the emulator can't emulate its sensor.

------------

![](https://i.imgur.com/RtzL4pN.png) ![](https://i.imgur.com/Wx1pS9X.png) ![](https://i.imgur.com/SmCWME6.png)
![](https://i.imgur.com/kkf5JmY.png) ![](https://i.imgur.com/v8V1VhR.png)
> Some tool doesn't require or does not implement the save button.

------------

![](https://i.imgur.com/3LRRJOY.png) ![](https://i.imgur.com/NGYpTNU.png)
> The save and edit interfaces.

------------

### Prospects for the future (create a pull request or fork it)
- The dark theme.
- New tools also working with external wireless devices.
- Other supported languages.
- Missing saving for some tool.
- Guest saves needs to be synced into the firebase realtime database of the next authenticated user.

### Known issues and bugs
- The main interface requires a lot of time to perform firebase requests (just for authenticated users).
- After entering the save measure fragment, when you change the application language, it will crash.
- The ruler measure is not very accurate (very small values) but it works fine.
- If your device is offline and you save a measure, it will be saved locally (SQLite DB) but obviously it doesn't be synced to the firebase realtime database. If you close the application, turn the device online and re-open the application, the local saves won't be synced.
