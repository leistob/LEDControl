    // color swirl! connect an RGB LED to the PWM pins as indicated
    // in the #defines
    // public domain, enjoy!
     
    #define REDPIN 6
    #define GREENPIN 5
    #define BLUEPIN 3
     
    #define FADESPEED 10     // make this higher to slow down

    const int buttonPin = 2; 
    int buttonCounter = 0;  
    int buttonState = 0;     
    
    void setup() {
      pinMode(REDPIN, OUTPUT);
      pinMode(GREENPIN, OUTPUT);
      pinMode(BLUEPIN, OUTPUT);
      Serial.begin(9600);
      pinMode(buttonPin, INPUT);
    }
     
     
    void loop() {      

      buttonState = digitalRead(buttonPin);

      if(buttonState == HIGH) {        
        showColor(buttonCounter%4);
        buttonCounter++;
        delay(500);
      }
      
    }

    void showColor(int col) {
      Serial.print(col);
      Serial.print("\n");
      analogWrite(REDPIN, 0);        
      analogWrite(BLUEPIN, 0);       
      analogWrite(GREENPIN, 0);

      switch(col) {
        case 0:
          analogWrite(REDPIN, 255);
          break;
        case 1:
          analogWrite(GREENPIN, 255);
          break;
        case 2:
          analogWrite(BLUEPIN, 255);
          break;
        case 3:
          break;
      }
      
    }

