# MyPhone

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Usage](#usage)
- [SMS Commands](#sms-commands)
- [Firebase Integration](#firbase-integration)
- [Contributing](#contributing)


## Introduction

 This is an android application which is useful in finding missing phones by accessing location of the mobile using another mobile through sms services and also have some other interesting features like accessing contacts, ringing the phone and can lock phone by just sending an sms.
## Features

- **Location Tracking**: Send an SMS command to retrieve the lost phone's current location using GPS.
- **Contacts Access**: Send an SMS command to receive a list of contacts stored on the lost phone.
- **Remote Ringing**: Make the lost phone ring at maximum volume using an SMS command.
- **Remote Locking**: Lock the lost phone remotely by sending an SMS command.
- **User-Friendly Interface**: Intuitive UI/UX design for seamless navigation and operation.

## Prerequisites

Before you begin, ensure you have the following:

- Android Studio installed.
- A basic understanding of Java programming and XML layout design.

## Installation

1. Clone or download the repository:

```bash
git clone https://github.com/MVKarthikReddy/MyPhone.git
```

2. Open the project in Android Studio.

## Configuration

1. Customize the SMS keywords used for each command in the app's codebase.
2. Implement the necessary permissions for location tracking and SMS services in the AndroidManifest.xml file.

## Usage

1. Launch the app on your Android device.
2. Configure the app settings and SMS keywords.
3. In case of a missing phone, send the appropriate SMS command to the lost device to trigger the desired action.

## SMS Commands

To interact with the lost phone remotely, send SMS commands to the lost device:

- **Location**: Send the location command keyword to receive the current GPS coordinates.
        ![ContactFormat](https://imgur.com/fQvxXeU)
- **Contacts**: Send the contacts command keyword to receive a list of stored contacts.
- **Ring**: Send the ring command keyword to make the phone ring at maximum volume.
- **Lock**: Send the lock command keyword to remotely lock the phone.

## Firebase Integration

The app uses Firebase for authentication and SMS services. Make sure to set up the required Firebase services and integrate them into the app.

## Contributing

Contributions are welcome! If you find any issues or want to enhance the app, feel free to create a pull request.

1. Fork the repository.
2. Create a new branch: `git checkout -b feature/your-feature-name`.
3. Commit your changes: `git commit -m "Add feature XYZ"`.
4. Push to the branch: `git push origin feature/your-feature-name`.
5. Create a pull request explaining your changes.
