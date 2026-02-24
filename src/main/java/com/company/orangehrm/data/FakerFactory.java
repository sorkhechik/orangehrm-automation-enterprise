package com.company.orangehrm.data;

import net.datafaker.Faker;

import java.util.Locale;

public final class FakerFactory {
	private static final Faker faker = new Faker(Locale.ENGLISH);

	private FakerFactory() {
	}

	public static Faker get() {
		return faker;
	}

	public static String randomFirstName() {
		return faker.name().firstName();
	}

	public static String randomLastName() {
		return faker.name().lastName();
	}

	public static String randomEmail() {
		return faker.internet().emailAddress();
	}

	public static String randomPassword() {
		return "Test@" + faker.number().digits(6);
	}
}
