# ShamsiCalendar
A library to use shamsi calendar in your app as simple as possible

:app  is the sample application

:shamsicalendar  is the library source code

# Gradle Dependency (jCenter)

Just add the dependency to your build.gradle file
```gradle 
compile 'ir.mosobhani:shamsicalendar:0.2'
```
[ ![Download](https://api.bintray.com/packages/moso94/ShamsiCalendar/shamsi-calendar/images/download.svg) ](https://bintray.com/moso94/ShamsiCalendar/shamsi-calendar/_latestVersion)

### If jCenter is Having Issues (the library can't be resolved)

Add this to your app's build.gradle file:

```Gradle
repositories {
    maven { url 'https://dl.bintray.com/moso94/ShamsiCalendar' }
}
```

#Getting started

It's easy

-Add shamsi calendar to your xml file that you want to show calendar
```xml
<ir.mosobhani.shamsicalendar.views.CalendarView
        android:id="@+id/calendar"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
```

-add simple click listener for on date click
```java
CalendarView calendarView = (CalendarView) findViewById(R.id.calendar);
calendarView.setOnCalenderClick(new CalendarView.OnCalenderClick() {
            @Override
            public void handleClick(PersianCalendar clickedDay) {

            }
        });
```

#Contribution

### Questions

If you have any questions regarding ShamsiCalendar,create an Issue

### Feature request

To create a new Feature request, open an issue 

I'll try to answer as soon as I find the time.

### Pull requests welcome

Feel free to contribute to ShamsiCalendar.

Either you found a bug or have created a new and awesome feature, just create a pull request.


### Contact me
Feel free to contact me in [telegram](https://telegram.me/moso94).
