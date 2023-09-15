package com.nttdata.bootcamp.s01accountservice.common;
public class AccountCreationException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    /**
     * Método de excepciones.
     *
     * @param message parametro de AccountNotFoundException.
     */
	public AccountCreationException(final String message) {
        super(message);
    }

    /**
     * Método de excepciones.
     *
     * @param message parametro de AccountNotFoundException.
     * @param cause parametro de AccountNotFoundException.
     */
    public AccountCreationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
