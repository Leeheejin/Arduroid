# Project Title

Arduroid

---

# Description

A application of platform that helps block style coding without personal computer

---

**Badges will go here**

- devDependencies
- issues
- stars
- release version
- license

![Plat Form](https://img.shields.io/badge/Platform-Android-lightgrey.svg)
[![GitHub issues](https://img.shields.io/github/issues/Leeheejin/Arduroid.svg)](https://github.com/Leeheejin/Arduroid/issues)
[![GitHub stars](https://img.shields.io/github/stars/Leeheejin/Arduroid.svg)](https://github.com/Leeheejin/Arduroid/stargazers)
[![GitHub release](https://img.shields.io/badge/release-v1.2.5-green.svg)](https://github.com/Leeheejin/Arduroid)
![License: CC BY 4.0](https://img.shields.io/badge/license-CC%20BY%204.0%20%2F%20Apache--2.0-blue.svg)

---

## Getting Started

These instructions will get you a copy of the project up and running on your mobile device for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Example

you can release apk file on your IDE, or just see the below video!

#### previous version, Landscape mode
![build-test](https://media.giphy.com/media/9S5dmprXZC5uq1t6Mx/giphy.gif)

#### Lastest version.
![build-test](https://media.giphy.com/media/vNqIdtcyIASaFrajIL/giphy.gif)

---

### Clone

- Clone this repo to your local virtual mobile machine (also you can use your physical mobile phone) using [https://github.com/Leeheejin/Arduroid](https://github.com/Leeheejin/Arduroid "https://github.com/Leeheejin/Arduroid")

---

## Usage

#### Just drag blocks! and edit sentence! if you have arduino device, even can upload your source code on yours!

First, at start.py in assets, change settings.
```python
# Server IP and PORT settings
SERVER_IP = '175.195.42.157' #your IP
SERVER_PORT = 8000           #your port
```
Second, when you execute start.py ,then ServerCompilerSettings.ini created.
and you should edit this file like this.
```python
[Arduino_IDE]
arduino_exec_path = C:\Program Files (x86)\Arduino\arduino_debug.exe  
# your local arduino ide path, and please use "arduino_debug.exe"

arduino_board = Uno                                                   
# don't care.

arduino_serial_port = COM6                                            
# don't care, you can change in arduroid frontend html page.

[Arduino_Sketch]
sketch_name = ArdublocklySketch                                       
# name of your ino sketch file name.

sketch_directory = C:\Users\leina\Documents\GitHub\example2\app\src\main\assets\www
# your path of project. don't care.

[Ardublockly]
ide_load = verify
# don't care. it is dynamic value.
```
Third, in MainActivity, when you running start.py (It is a server! you can have your own server.) you can take url to webview, and can controll.
```java
mWebView.loadUrl("input your url");
```

as you see, when you running start.py you can see like....

<img src="https://github.com/Leeheejin/Arduroid/blob/master/app/src/main/res/drawable/executeimage.png?raw=true" width="90%"></img>

---


## Contributing

> To get started...

### Step 1

- **Option 1**
    - Fork this repo!

- **Option 2**
    - Clone this repo to your local machine using [https://github.com/Leeheejin/Arduroid.git](https://github.com/Leeheejin/Arduroid.git "https://github.com/Leeheejin/Arduroid.git")

### Step 2

- **HACK AWAY!**

### Step 3

- Create a new pull request using [https://github.com/Leeheejin/Arduroid/compare](https://github.com/Leeheejin/Arduroid/compare "https://github.com/Leeheejin/Arduroid/compare")

---


## Team

> Or Contributors/People

| Sungeun Kim | HeeJin Lee | Hyunho Moon |
| :---: | :---: | :---: |
| <img src="https://avatars3.githubusercontent.com/u/44923146?s=460&v=4" width="50%"></img> | <img src="https://avatars1.githubusercontent.com/u/9789023?s=460&v=4" width="50%"></img> | <img src="https://avatars3.githubusercontent.com/u/44826442?s=400&v=4" width="50%"></img> |
| [github.com/seongeunK](https://github.com/seongeunK "https://github.com/seongeunK") | [github.com/Leeheejin](https://github.com/Leeheejin "https://github.com/Leeheejin") | [github.com/gusgh9176](https://github.com/gusgh9176 "https://github.com/gusgh9176") |

- You can see team member and github profile
- You should probably find team member's lastest project

---

## Special thanks

> Contributors(unofficial)

None! hack ous!


## FAQ

- **How do I do *specifically* so and so?**
    - No problem! Just do this.

---


## To do

you can check weekly Todo list - see the [Todo.md](Todo.md "Todo.md") file for details

---


## License

Copyright (c) 2016 carlosperate https://github.com/carlosperate/

Unless stated otherwise, the source code of this projects is licensed under the Apache License, Version 2.0 (the "License"); you may not use any of the licensed files within this project except in compliance with the License.

The full document can be found in the LICENSE file.

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
