@usedcars
Feature: Popular used car models in Chennai
  As a used-car buyer researching the Chennai market
  I want to see the popular used car models listed on zigwheels
  So that I know what's commonly available before I search further

  Background:
    Given I am on the zigwheels home page

  @smoke @performance
  Scenario: Extract all popular used car models for Chennai into a list
    When I navigate to the used cars page for "Chennai"
    And I capture the page load performance metrics for the "Used cars Chennai" page
    Then I extract all the popular models into a list
    And the popular models list should not be empty
    And I display the popular models list
    And I navigate back to the home page
    Then I should be back on the zigwheels home page

  @accessibility
  Scenario: Basic accessibility checks on the used cars Chennai page
    When I navigate to the used cars page for "Chennai"
    Then I scan the page with axe-core and report any critical or serious accessibility violations
    And a keyboard-only user can Tab through the first interactive elements on the page
