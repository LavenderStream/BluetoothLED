#include <Wire.h>   
#include "LiquidCrystal_I2C.h" 
LiquidCrystal_I2C lcd(0x27,16,2);

int LEDPIN = 7;
int HEIGHTPIN = 12;
String comdata = "";

void setup()
{
  Serial.begin(9600);
  pinMode(LEDPIN,OUTPUT);
  pinMode(HEIGHTPIN,OUTPUT);
 
  lcd.init(); 
  lcd.noBacklight();
  lcd.print("Temperatures: ");
}
 
void loop()
{
  while(Serial.available())
  { 
    if (Serial.available() > 0)
    {
      comdata += char(Serial.read());
      delay(2);
    }
  }
  if (comdata.length() > 0)
  {
    Serial.println(comdata);
    if(comdata == "1")
    {
      digitalWrite(LEDPIN,HIGH);
      digitalWrite(HEIGHTPIN,LOW);
      Serial.println("1");
    }
    else if (comdata == "0")
    {
      Serial.println("0");
      digitalWrite(LEDPIN,LOW);
      digitalWrite(HEIGHTPIN,LOW);
    }
    else if (comdata == "2")
    {
      Serial.println("2");
      digitalWrite(LEDPIN,HIGH);
      digitalWrite(HEIGHTPIN,HIGH);
    }
    else if (comdata == "4")
    {
      lcd.backlight();
      Serial.println("4");
    }
    else if (comdata == "5")
    {
      lcd.noBacklight();
      Serial.println("5");
    }
    else
    {
      String temp = "";
      temp = comdata;
      lcd.setCursor(1,1);
      lcd.print(temp);
    }
    comdata = "";
  }  
}


