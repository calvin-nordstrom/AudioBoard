#include <Keypad.h>

const byte ROWS = 4;
const byte COLS = 4;

char keys[ROWS][COLS] = {
  {'0','1','2','3'},
  {'4','5','6','7'},
  {'8','9','A','B'},
  {'C','D','E','F'}
};

byte rowPins[ROWS] = {4,5,6,7};
byte colPins[COLS] = {8,9,10,16};

Keypad keypad = Keypad(makeKeymap(keys), rowPins, colPins, ROWS, COLS);

void sendEvent(char source, char event, char key) {
  Serial.print(source);
  Serial.print(event);
  Serial.println(key);
}

void setup() {
  Serial.begin(115200);
  delay(2000);
}

void loop() {
  if (keypad.getKeys()) {
    for (int i = 0; i < LIST_MAX; i++) {
      if (!keypad.key[i].stateChanged) {
        continue;
      }

      char event;

      switch (keypad.key[i].kstate) {
        case PRESSED:
          event = 'P';
          break;
        case RELEASED:
          event = 'R';
          break;
        default:
          continue;
      }

      sendEvent('K', event, keypad.key[i].kchar);
    }
  }
}
