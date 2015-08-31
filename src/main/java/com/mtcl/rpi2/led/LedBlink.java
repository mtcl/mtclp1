package com.mtcl.rpi2.led;

/* Required imports for Pi4J */
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

/**
 * Created by Mukul Tripathi on 8/9/2015.
 */
public class LedBlink {
	public void blink(String color) throws IllegalArgumentException {

		boolean BLOCKING = true;
		boolean NONBLOCKING = false;

		System.out.println("===== Starting LED Blink =====");

		final GpioController gpio = GpioFactory.getInstance();

		/*
		 * Initialize all the pins as outputs, and pull them to a LOW on
		 * startup.
		 */
		final GpioPinDigitalOutput led1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, "LED1", PinState.LOW);

		final GpioPinDigitalOutput led2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "LED2", PinState.LOW);

		/*
		 * Add a shutdown hook so that the application can trap a Ctrl-C and
		 * handle it gracefully by ensuring that all the LEDs are turned off
		 * prior to exiting.
		 */
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				System.out.println("\n\nPROGRAM WAS INTERRUPTED. SHUTTING " + "DOWN!");
				led1.low();
				led2.low();
				gpio.shutdown();
			}
		});

		System.out.println("Pins initialized. All LEDs should be off.");

		/*
		 * Starting the main loop of our program. We put it in a try-catch since
		 * the various sleep's throw an exception and we need to catch those
		 * exceptions.
		 */
		try {
			System.out.println("Blocking Call: Each LED will be on for 1 sec" + " one after another.");
			Thread.sleep(5000);

			/*
			 * Blocking call: Execution proceeds to next line only after current
			 * line is finished. LEDs will blink one after the other.
			 */
			if (color.contains("red")) {

				System.out.println("Blinking Red LED for 5 seconds");
				led1.pulse(5000, BLOCKING);

			}

			if (color.contains("green")) {
				System.out.println("Blinking Green LED for 5 seconds");
				led2.pulse(1000, BLOCKING);
			}
		} catch (InterruptedException ie) {
			System.out.println("Execution Interrupted.");
			led1.low();
			led2.low();
			gpio.shutdown();
		}
	}
}
