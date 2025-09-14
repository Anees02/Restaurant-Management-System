package tech.zeta.utils;

import java.util.regex.Pattern;

/**
 * Utility class for validating email addresses.
 * Provides a method to check if a given string is a valid email.
 * This class cannot be instantiated.
 */
public class EmailValidator {
  private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
  private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

  private EmailValidator() {}

  /**
   * Checks whether a given email string is valid based on the standard email format.
   *
   * @param email the email address to validate
   * @return true if the email is valid, false otherwise
   */
  public static boolean isValid(String email) {
    if (email == null) return false;
    return EMAIL_PATTERN.matcher(email).matches();
  }
}
