package com.financeiro.util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;

public class Util {

	public static boolean isEmptyOrNull(String value) {
		return value == null || value.trim().isEmpty();
	}

	public static boolean isEmptyOrNull(Collection<?> value) {
		return value == null || value.isEmpty();
	}

	public static boolean isZeroOrNull(BigDecimal value) {
		return value == null || (value.compareTo(BigDecimal.ZERO) == 0);
	}

	public static boolean isNumber(String value) {
		boolean numero = true;

		try {
			Long.parseLong(value);
		} catch (NumberFormatException e) {
			numero = false;
		}
		return numero;
	}

	public static boolean isDecimal(String value) {
		boolean numero = true;

		try {
			value = value.replace(',', '.');
			new BigDecimal(value);
		} catch (NumberFormatException e) {
			numero = false;
		}
		return numero;
	}

	public static boolean isDate(String value) {
		boolean numero = true;
		
		try {
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			format.parse(value);
		} catch (ParseException e) {
			numero = false;
		}
		
		return numero;
	}
}
