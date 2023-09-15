package com.nttdata.bootcamp.s01accountservice.common;
public class AccountException extends Exception {
	private static final long serialVersionUID = 1L;
	/**
	 * Método de excepciones.
	 *
	 * @param string parametro de AccountNotFoundException.
	 */
	public AccountException(final String string) {
		super(string);
	}

	/**
	 * Método de excepciones.
	 *
	 * @param message parametro de AccountNotFoundException.
	 * @param cause parametro de AccountNotFoundException.
	 */
    public AccountException(final String message, final Throwable cause) {
        super(message, cause);
    }
}

