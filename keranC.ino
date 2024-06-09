#include <Wire.h>
#include <Firebase_ESP_Client.h>

#if defined(ESP32)
#include <WiFi.h>
#elif defined(ESP8266)
#include <ESP8266WiFi.h>
#endif

#include "addons/TokenHelper.h"
#include "addons/RTDBHelper.h"

#define WIFI_SSID "reihan"
#define WIFI_PASSWORD "123456789_"
#define API_KEY "AIzaSyCSH9wLbphH--ilg_7lSRnu_FIC0oo7SRA"
#define USER_EMAIL "ijhonkjr@gmail.com"
#define USER_PASSWORD "123456"
#define DATABASE_URL "https://pdam-1cace-default-rtdb.firebaseio.com/"

FirebaseData firebaseData;
FirebaseAuth auth;
FirebaseConfig config;

volatile int flow_frequency; // Measures flow sensor pulses
float vol = 0.0, l_minute;

unsigned char flowsensor = 34; // Sensor Input
unsigned long currentTime;
unsigned long cloopTime;
float volume_per_pulse = 0.004; // Calibrate and set the value of volume_per_pulse here

void IRAM_ATTR flow() {
    flow_frequency++;
}

void initWiFi() {
    WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
    Serial.print("Connecting to WiFi ..");
    while (WiFi.status() != WL_CONNECTED) {
        Serial.print('.');
        delay(1000);
    }
    Serial.println(WiFi.localIP());
    Serial.println();
}

void setup() {
    Serial.begin(115200);
    initWiFi();

    pinMode(flowsensor, INPUT);
    digitalWrite(flowsensor, HIGH); // Optional Internal Pull-Up

    attachInterrupt(digitalPinToInterrupt(flowsensor), flow, RISING); // Setup Interrupt
    currentTime = millis();
    cloopTime = currentTime;

    config.api_key = API_KEY;
    auth.user.email = USER_EMAIL;
    auth.user.password = USER_PASSWORD;
    config.database_url = DATABASE_URL;

    Firebase.reconnectWiFi(true);
    Firebase.begin(&config, &auth);
}

void loop() {
    currentTime = millis();
    if (currentTime >= (cloopTime + 1000)) {
        cloopTime = currentTime; // Updates cloopTime
        if (flow_frequency != 0) {
            l_minute = (flow_frequency * volume_per_pulse * 60);
            vol = vol + l_minute / 60;
            flow_frequency = 0; // Reset Counter
        } else {
            l_minute = 0;
        }

        double total_rate = 0;
        if (Firebase.RTDB.getDouble(&firebaseData, "waterflowData/waterflow1/rate1")) {
            total_rate += firebaseData.to<double>();
        } else {
            Serial.println("Failed to get rate1 from waterflow1: " + firebaseData.errorReason());
        }

        if (Firebase.RTDB.getDouble(&firebaseData, "waterflowData/waterflow2/rate1")) {
            total_rate += firebaseData.to<double>();
        } else {
            Serial.println("Failed to get rate1 from waterflow2: " + firebaseData.errorReason());
        }

        FirebaseJson json;
        json.set("/nama", "Keran C");
        json.set("/rate1", l_minute);
        json.set("/volume1", vol);
        json.set("/volume_mL", vol * 1000);
        json.set("/total_rate", total_rate);

        if (Firebase.RTDB.setJSON(&firebaseData, "waterflowData/waterflow3", &json)) { // Updated path to include waterflowData node
            Serial.println("Firebase write succeeded");
        } else {
            Serial.print("Firebase write failed: ");
            Serial.println(firebaseData.errorReason());
        }

        Serial.print("Rate: ");
        Serial.print(l_minute);
        Serial.println(" L/M");
        Serial.print("Vol: ");
        Serial.print(vol);
        Serial.println(" L");
        Serial.print("Vol: ");
        Serial.print(vol * 1000);
        Serial.println(" mL");
        Serial.print("Total Rate: ");
        Serial.print(total_rate);
        Serial.println(" L/M");
    }
    delay(10);
}