@googlelogin @negative-test @windows
Feature: Invalid Google login from zigwheels
  As a QA engineer validating negative paths
  I want to log in to zigwheels with Google using invalid account details
  So that I can confirm the correct warning/error message is shown

  # zigwheels' "Google" login option opens Google's sign-in in a separate
  # popup window, so this scenario is also the project's real
  # "Handling windows" example: switch into the popup, then back.

  Background:
    Given I am on the zigwheels home page

  @smoke
  Scenario: Log in to zigwheels with Google using invalid details and capture the error
    When I open the zigwheels login box
    And I choose to continue with Google
    Then a Google sign-in popup window should open
    When I attempt to log in with an invalid email "this.account.does.not.exist.qea.hackathon@gmail.com" and password "WrongPassword123!"
    Then Google should show an error message
    And I capture and display the warning message text
    When I close the Google popup and switch back to zigwheels
    Then I should be back on the zigwheels window
