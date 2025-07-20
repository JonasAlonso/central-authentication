package com.baerchen.central.authentication.runtime.control;

/**
 * TO-DO implement this as a generic interface to validate DTOs
 *
 * @param <T>
 */
public interface Validator<T> {

    boolean validate();
}
