@bikes
Feature: Upcoming Honda bikes under budget
  As a prospective Honda bike buyer in India
  I want to see upcoming Honda bikes priced under 4 Lakh
  So that I know what's launching soon within my budget

  Background:
    Given I am on the zigwheels home page

  @smoke @performance
  Scenario: Display upcoming Honda bikes priced under 4 Lakh
    When I extract the top navigation menu items into a list
    And the menu items list should not be empty
    And I display the menu items list
    And I navigate to the upcoming bikes page
    And I capture the page load performance metrics for the "Upcoming bikes" page
    And I extract the upcoming bikes filtered by manufacturer "Honda" and price ceiling "4.0" Lakh
    Then every returned bike should be a Honda model priced under "4.0" Lakh
    And each bike should have a name, a price and an expected launch date
    And I display the extracted bikes list
    And I navigate back to the home page
    Then I should be back on the zigwheels home page

  @accessibility
  Scenario: Basic accessibility checks on the upcoming bikes page
    When I navigate to the upcoming bikes page
    Then I scan the page with axe-core and report any critical or serious accessibility violations
    And a keyboard-only user can Tab through the first interactive elements on the page
