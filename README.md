<p align="center">
<img src="Horologium.png" alt="Horologium"/>
</p>

# *Horologium*

*Horologium* is a Latin word whose meaning is *Clock*. ***{College project}***

The main purpose of this application to contain functionalities of clock such as time, alarm, reminder, stopwatch with additional functionalities like weather info, time zone etc.
In short this application integrates basic features of a clock and a weather application with more compatibility so **it can be installed on smartphones having android version 4.4.2 (Kitkat) or above**.

**You can also go through 'Output screens' directory to see how this looks.**

**Requirements:**
- Compatibility - Android 4.4.2 (Kitkat) or above
- API level - 19 or above
- Development platform: Linux (Ubuntu 16.04 LTS), Windows
- Programming language - Java 8
- Backend - SQLite (For storage of user data)
- IDE - Android studio 3.x or above
- Other API: OpenWeatherMap (To fetch weather info) (See - https://openweathermap.org/)
- Internet access - To fetch weather info for particular city

**TODO:**
* Currently alarm trigger is vibrate only, still we can not select a custom audio file as a alarmtone.
* We can not select specific date for alarm, rather we can select only day from days in a week.

***NOTE:*** <br />
- Please create your account on https://openweathermap.org/ and get your own key. Then add this key to **'open_weather_maps_app_id'** tag in **strings.xml** of the project then build your APK.
