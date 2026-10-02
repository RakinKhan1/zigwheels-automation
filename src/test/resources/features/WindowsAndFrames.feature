@frames
Feature: Frame handling on the zigwheels home page
  As a QA engineer covering the "windows & frames" automation scope item
  I want to switch into one of zigwheels' own iframes and back
  So that frame handling is proven against the real site

  # The zigwheels home page loads its own iframes (Google consent and
  # advertising frames; 13 found when checked live on 23 Sep 2026).
  # Window handling is covered for real in GoogleInvalidLogin.feature,
  # where zigwheels opens Google's sign-in in a separate popup window.

  Background:
    Given I am on the zigwheels home page

  @smoke
  Scenario: Switch into a real iframe on the zigwheels home page and back
    When I extract the iframes on the page into a list
    Then the iframe list should not be empty
    When I switch into the first iframe on the page
    Then the driver should be inside the frame
    And I switch back to the main page content
    Then the driver should be back on the main page
