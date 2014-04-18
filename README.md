<a name="top"></a>

## Index
0. [Application Features](#features) <br/>
   * [Current features](#current) <br/>
   * [Features being implemented](#coming) <br/>
   * [Future/planned features](#future) <br/>
1. [Setting up the environment](#setup) <br/>
   * [Ubuntu set-up](#ubuntusetup) <br/>
   * [Windows set-up](#windowssetup) <br/>
   * [MUST-DO](#mustdo) <br/>

---

[>>> Go Top](#top)
<a name="features"></a>
# Application Features (subject to change)
In this section, I will try to keep an updated list of current features, features being implemented, and future features. If there is something missing, message me on Facebook or by email emosong@ucsd.edu.

<a name="current"></a>
##Current features

<a name="coming"></a>
##Features being implemented

<a name="future"></a>
##Future/Planned Features

[>>> Go Top](#top)
<a name="setup"></a>
#Guide to setting up environment for Android Development (for our purposes)

Hey guys, since last meeting showed that most of us aren't ready to develop in android, I thought I'd set up a quick tutorial on getting the ESSENTIALS going so that everyone can start coding. This tutorial will be broken up in two parts: Windows and Ubuntu 13.10

######NOTE: Tomorrow (April 17, 2014) the newest Ubuntu LTS version, 14.04, will be available to everyone. I suggest that if you plan on moving to the newer Ubuntu, to wait till after updating to do these steps.

<a name="ubuntusetup"></a>
[>>> Go Top](#top)
## Ubuntu 13.10

#####Install Android Studio:
[Click here](http://developer.android.com/sdk/installing/linux-studio)
Extract the files to whenever you want your development area to be.

#####Check if you have JAVA
In your terminal (CTRL+ALT+T shortcut):
```
$ java -version
OpenJDK Runtime Environment (IcedTea 2.4.4) (7u51-2.4.4-0ubuntu0.13.10.1)
OpenJDK 64-Bit Server VM (build 24.45-b08, mixed mode)
```

As you can see, I have included an example output of how it would look if you do have Java.

If you don't have Java, do the following in your terminal.
######Option 1: Open-JDK (open-source)
```
$ sudo apt-get update
$ sudo apt-get install openjdk-7-jdk
```
And press "y" if you are prompted. Let it do its thing.

######Option 2: Oracle's JDK (propietary)
```
sudo add-apt-repository ppa:webupd8team/java
sudo apt-get update
sudo apt-get install oracle-java7-installer
```
Source: http://www.webupd8.org/2012/01/install-oracle-java-jdk-7-in-ubuntu-via.html (thanks Dilraj!)

NOTE: I do not use Oracle's JDK and, from the source website, I see that it shows Java Runtime Environment (JRE). I don't know if this includes JDK (Java Development Kit), which is needed to develop Java. If this doesn't work, either Google your problem or just use Open-JDK (above).

#####Install Git
In your terminal:
```
$ sudo apt-get update
$ sudo apt-get install git
```

At this point, you should be able to run Android Studio (if it doesn't, google your error or Facebook me) and can get started on Git cloning/pulling/etc. 

######Bonus: Netflix
######Run these commands in the terminal:
```
sudo apt-add-repository ppa:ehoover/compholio
sudo apt-get update
sudo apt-get install netflix-desktop
```

<a name="windowssetup"></a>
[>>> Go Top](#top)
## Windows

So for those of you who are running Windows...

#####Install JDK
Install JDK 7 [Click me](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html)

#####Install Git
Install Git [Click me](http://git-scm.com/download/win)

#####Set your environment variable PATH
This is where most of you were having problems. Open "My Computer" (shortcut: Windows key + E) and right click on your computer, then click on properties.

Click on "Advanced System Settings" and then click on Environment Variables (at the bottom of the new window). You will now see TWO lists (top and bottom), go to the bottom list and look for "PATH". Click on it and click "edit". 

Now, you will have to do these steps for both Git and the JDK. 

Go to the folder where you installed Git (C:\Program Files(x86)\Git\bin is default I THINK) and copy this url. Go back to the window where you can edit the PATH variable. Add a semicolon at the end of its value (if not already there) and paste the path for Git.

Do the same for JDK (might be more difficult, you will have to find the installation folder for JDK and go to /bin/ folder. This is the entire path you will want to put in the PATH variable.

Once you're done with this, open your command prompt (Windows-key+R, type cmd and ENTER) and test. Write "git" and hit ENTER. Write "javac" and hit ENTER. If either doesn't work, retrace your steps and see if you can find out your mistake.


<a name="mustdo"></a>
[>>> Go Top](#top)
##Must do:

To check that everyone has their environment set up correctly, I will ask that everyone (except the UX people, who are busy getting Use Cases and User Stories done by Friday) to do the following:

1. Clone a repository from my github account: [My Github](https://github.com/edwinmosong) 
   * The repository name is TestingApp.
2. Find a file called "modifyme.txt" and add a line with your name.
   * Note that the file is kinda hidden! A hint I can give you is that it is in the same folder that MainActivity.java is in. If you are using Android Studio (which you should) then this shouldn't be a problem.
3. I want you to PUSH this change to the repository. Make sure that you put your name!


### Make sure that your push is reflected in the repository by going to https://github.com/edwinmosong/TestingApp in the file modifyme.txt. I ask that EVERYONE (again, EXCEPT UX people) put their names in this file by SATURDAY 19 APRIL 2014, 8:00pm. This is so that we know that people can work on Sunday's meeting. If you have any questions, you can ask Jose, Dilraj, or me (Edwin). Good luck!
