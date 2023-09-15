package com.nttdata.bootcamp.s01accountservice.common;

import com.nttdata.bootcamp.s01accountservice.common.AccountException;

public class DuplicateAccountException extends AccountException {
	private static final long serialVersionUID = 1L;
    /**
     * Método de excepciones.
     *
     * @param message parametro de AccountNotFoundException.
     */
	public DuplicateAccountException(final String message) {
        super(message);
    }

    /**
     * Método de excepciones.
     *
     * @param message parametro de AccountNotFoundException.
     * @param cause parametro de AccountNotFoundException.
     */
    public DuplicateAccountException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
