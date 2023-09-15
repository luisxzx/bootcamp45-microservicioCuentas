package com.nttdata.bootcamp.s01accountservice.common;

import com.nttdata.bootcamp.s01accountservice.common.AccountCreationException;

public class AccountNotFoundException extends AccountCreationException {
	private static final long serialVersionUID = 1L;
    /**
     * Método de excepciones.
     *
     * @param message parametro de AccountNotFoundException.
     */
	public AccountNotFoundException(final String message) {
        super(message);
    }

    /**
     * Método de excepciones.
     *
     * @param message parametro de AccountNotFoundException.
     * @param cause parametro de AccountNotFoundException.
     */
    public AccountNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
