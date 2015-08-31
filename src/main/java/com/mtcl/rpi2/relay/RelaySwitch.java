package com.mtcl.rpi2.relay;

/* Required imports for Pi4J */
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

/**
 * Created by Mukul Tripathi on 8/9/2015.
 */
public class RelaySwitch {
	public static void main(String[] args) {

		boolean BLOCKING = true;
		boolean NONBLOCKING = false;

		System.out.println("===== Starting Relay Blink =====");

		final GpioController gpio = GpioFactory.getInstance();

		/*
		 * Initialize all the pins as outputs, and pull them to a LOW on
		 * startup.
		 */
		final GpioPinDigitalOutput relay1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "RELAY1", PinState.LOW);
		final GpioPinDigitalOutput relay2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "RELAY2", PinState.LOW);
		final GpioPinDigitalOutput relay3 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, "RELAY3", PinState.LOW);
		final GpioPinDigitalOutput relay4 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_26, "RELAY4", PinState.LOW);

		/*
		 * Add a shutdown hook so that the application can trap a Ctrl-C and
		 * handle it gracefully by ensuring that all the LEDs are turned off
		 * prior to exiting.
		 */
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				System.out.println("\n\nPROGRAM WAS INTERRUPTED. SHUTTING DOWN!");
				relay1.low();
				relay2.low();
				relay3.low();
				relay4.low();
				gpio.shutdown();
			}
		});

		System.out.println("Pins initialized. All Relays should be off.");

		/*
		 * Starting the main loop of our program. We put it in a try-catch since
		 * the various sleep's throw an exception and we need to catch those
		 * exceptions.
		 */
		try {
			System.out.println("Blocking Call: Each Relay will be on for 1 sec" + " one after another.");
			Thread.sleep(5000);

			/*
			 * Blocking call: Execution proceeds to next line only after current
			 * line is finished. LEDs will blink one after the other.
			 */
			relay1.pulse(1000, BLOCKING);
			relay2.pulse(1000, BLOCKING);
			relay3.pulse(1000, BLOCKING);
			relay4.pulse(1000, BLOCKING);

			System.out.println("Non-Blocking Call: Each LED will be on for 1 sec, but simultaneously.");
			Thread.sleep(5000);

			/*
			 * Non-blocking call: Each LED should turn on and off
			 * simultaneously. Execution does not wait for a statement to finish
			 * before moving to next statement.
			 */
			relay1.pulse(1000, NONBLOCKING);
			relay2.pulse(1000, NONBLOCKING);
			relay3.pulse(1000, NONBLOCKING);
			relay4.pulse(1000, NONBLOCKING);

			System.out.println("Blinking LED2 for 10 seconds, every second.");
			Thread.sleep(5000);

			/*
			 * Blink LED2 for a total of 10 seconds, with each cycle being 1 sec
			 * long. LED2 will be on for 500 ms and off for 500 ms.
			 */
			relay2.blink(500, 10000);

			Thread.sleep(10000);

			System.out.println("Blinking LED3 forever, every 2 seconds. Press Ctrl-C to abort the program.");

			/*
			 * Another way to blink a GPIO pin is to set it high and low
			 * alternately, with a sleep.
			 */
			for (;;) {
				relay3.high();
				System.out.print(".");
				Thread.sleep(1000);
				relay3.low();
				System.out.print(" ");
				Thread.sleep(1000);
			}
		} catch (InterruptedException ie) {
			System.out.println("Execution Interrupted.");
			relay1.low();
			relay2.low();
			relay3.low();
			relay4.low();
			gpio.shutdown();
		}
	}
}
