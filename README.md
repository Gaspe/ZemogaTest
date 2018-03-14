# ZemogaTest

This app corresponds to the zemoga mobile test.

HOW TO RUN THE APP

In order to run the app in a device you need:

- Android Studio 2.3.3 or higher
- Android SDK updated to a recent version (API 27 or higher)
- Android mobile device (Emulated or physical)

After cloning this repository into your Android Studio projects folder, open it using the wizard.

Note: Depending on the OS, it might ask for confirmation to replace de SDK directory. In that case, just click Ok and it will start the project.

Then, connect your device via USB (or start the emulated phone) and click on the green "Play" button on the top part of your screen.

It will prompt a list of devices to run the app in, pick one and click ok.

Now, it should be running in your device.

LIBRARIES USED

In this project were used 4 third-party libraries

- Eventbus: This is a small and powerfull tool that helps in the communication between components (Activities, fragments, etc), saving a lot of developing time taking away the need of implementing callback interfaces.

- Retrofit2: This is one of the most used libraries in terms of sending requests to API's because it gives a lot of options and flexibility to build your requests. Also, it has great compatibility with the last 2 libraries which makes data binding much simpler.

- Gson and Gson Converter: This is the official library released by Google to handle Json files and responses and, as it was stated before, works perfectly with Retrofit 2, making the process of binding the response data to the models very straightforward. 

Note: In order to keep consistency, I used TinyDB class. This is an implementation of Android SharedPreferences that provides simpler methods to save and retrieve data. 

