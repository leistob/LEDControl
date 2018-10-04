// Serial Parameters: COM11 9600 8 N 1 
// \r or \n to end command line
// Bluetooth is on Pin 0 & 1 @ 9600 speed

// Command structure
// CMD RED|GREEN|YELLOW=ON|OFF

#define REDPIN 6
#define GREENPIN 5
#define BLUEPIN 3

int maxSeconds = 10; // send status message every maxSeconds

String inputString = "";
String command = "";
String value = "";
boolean stringComplete = false;

void setup(){  
  Serial.begin(9600);
  Serial.print("Test");

  inputString.reserve(50);
  command.reserve(50);
  value.reserve(50);
  
  pinMode(REDPIN, OUTPUT); 
  pinMode(GREENPIN, OUTPUT); 
  pinMode(BLUEPIN, OUTPUT); 
  digitalWrite(REDPIN, LOW);
  digitalWrite(GREENPIN, LOW);
  digitalWrite(BLUEPIN, LOW);

}

// interpret and execute command when received
// then report status if flag raised by timer interrupt
void loop(){
  int intValue = 0;
  
  
  //fade(20);
  //police2();
  pulse(255, 0, 0);
  
  
  /*
  if (stringComplete) {
    Serial.println(inputString);
    boolean stringOK = false;
    if (inputString.startsWith("CMD ")) {
      inputString = inputString.substring(4);
      int pos = inputString.indexOf('=');
      if (pos > -1) {
        command = inputString.substring(0, pos);
        value = inputString.substring(pos+1, inputString.length()-1);  // extract command up to \n exluded
        //Serial.println(command);
        //Serial.println(value);
        if (command.equals("RED")) { // RED=ON|OFF
          value.equals("ON") ? digitalWrite(REDPIN, HIGH) : digitalWrite(REDPIN, LOW);
          stringOK = true;
        }
        else if (command.equals("GREEN")) { // GREEN=ON|OFF
          value.equals("ON") ? digitalWrite(GREENPIN, HIGH) : digitalWrite(GREENPIN, LOW);
          stringOK = true;
        }
        else if (command.equals("BLUE")) { // BLUE=ON|OFF
          value.equals("ON") ? digitalWrite(BLUEPIN, HIGH) : digitalWrite(BLUEPIN, LOW);
          stringOK = true;
        }
        else if (command.equals("TMAX")) { // TMAX=value
          
        }
        else if (command.equals("SECONDS")) { // SECONDS=value
          
        }
      } // pos > -1
      else if (inputString.startsWith("STATUS")) {
        /*Serial.print("STATUS RED=");
        Serial.println(digitalRead(led3Pin));
        Serial.print("STATUS GREEN=");
        Serial.println(digitalRead(led2Pin));
        Serial.print("STATUS YELLOW=");
        Serial.println(digitalRead(led1Pin));
        Serial.print("STATUS TMAX=");
        Serial.println(maxTemp);
        Serial.print("STATUS SECONDS=");
        Serial.println(maxSeconds);
        Serial.print("STATUS TEMP=");
        Serial.println(temperature);
        Serial.print("STATUS THIGH=");
        Serial.println(tempHigh);
        stringOK = true;*/
      //} // inputString.startsWith("STATUS")
    //} // inputString.startsWith("CMD ")
    /*    stringOK ? Serial.println("Command Executed") : Serial.println("Invalid Command");
    
    
    
    inputString = "";
    stringComplete = false;
    
  }
  */
}

/*
  SerialEvent occurs whenever a new data comes in the
 hardware serial RX.  This routine is run between each
 time loop() runs, so using delay inside loop can delay
 response.  Multiple bytes of data may be available.
 */
void serialEvent() {
  while (Serial.available()) {
    // get the new byte:
    char inChar = (char)Serial.read();    
    inputString += inChar;    
    if (inChar == '\n' || inChar == '\r') {
      stringComplete = true;
    } 
  }
}

void enlightRGB(int red, int green, int blue) {
  analogWrite(REDPIN, red);
  analogWrite(GREENPIN, green);
  analogWrite(BLUEPIN, blue);
}

void fade(int speed) {
  int r=255, g, b; 
  
  //Red to light green
  for(g=0; g<255; g++) {
    enlightRGB(r, g, b);
    delay(speed+10);
  }  
  //Green
  for(r=255; r>0; r--) {
    enlightRGB(r, g, b); 
    delay(speed);
  }
  
  for(b=0; b<255; b++) {
    enlightRGB(r, g, b);
    delay(speed);
  }
  
  for(g=255; g>0; g--) {
    enlightRGB(r, g, b); 
    delay(speed);
  }
  
  for(r=0; r<255; r++) {
    enlightRGB(r, g, b);
    delay(speed);
  }
  
  for(b=255; b>0; b--) {
    enlightRGB(r, g, b); 
    delay(speed);
  }
  
}

void police() {
  int r, g, b;
  
  for(b=255; b>100; b--) {
    enlightRGB(r, g, b);
    delay(5);
  }
  b=0;
  for(r=255; r>100; r--) {
    enlightRGB(r, g, b);
    delay(5);
  }
  
}

void police2() {
  
  enlightRGB(0, 0, 255);
  delay(100);
  enlightRGB(0, 0, 0);
  delay(100);
  enlightRGB(0, 0, 255);
  delay(100);
  enlightRGB(0, 0, 0);
  delay(100);
  enlightRGB(255, 0, 0);
  delay(100);
  enlightRGB(0, 0, 0);
  delay(100);
  enlightRGB(255, 0, 0);
  delay(100);
  
  
}

void pulse(int r, int g, int b) {
  
  int maxVal = r>g ? (r>b ? r : b) : (g>b ? g : b);
  
  for(maxVal=maxVal; maxVal>maxVal-50; maxVal--) {
    enlightRGB(r--, g--, b--);
    delay(50);
    
  }
  
  
}


