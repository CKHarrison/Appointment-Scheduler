package utilities;

/**
 * GeneralErrorMessage Interface Lambda. Responsible for generating an error message for any type of exception.
 * Use of the lambda allows for general use anywhere in the program.
 */
public interface GeneralErrorMessage {
    void errorMessage(Exception e);
}
