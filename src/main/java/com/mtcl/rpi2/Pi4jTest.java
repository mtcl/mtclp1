package com.mtcl.rpi2;

/* Pi4J imports */
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class Pi4jTest {

	public static void main(String[] args) {
		System.out.println("Hello, Raspberry Pi!");

		/* Initialize pi4j */
		final GpioController gpio = GpioFactory.getInstance();

		/*
		 * Initialize GPIO 01 as an input pin called "MyButton" and set it low
		 * using the pull-down resistor.
		 */
		GpioPinDigitalInput myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_01, "MyButton",
				PinPullResistance.PULL_DOWN);

		/*
		 * Read the state (high or low) of the pin. Remember, it should be "low"
		 */
		PinState pinState = myButton.getState();
		System.out.println("GPIO 01 is set to " + pinState.getName());

		/* Close all open connections. */
		gpio.shutdown();
	}
}
