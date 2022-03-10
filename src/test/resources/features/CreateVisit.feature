Feature: Create a visit
    As a secretary, I want to be able to add a visit to a specific pet profile already in the petClinic system in order
    to keep track of past and scheduled clinic visits.

  Background:
    Given the following owners exist in the system:
      | first_name  | last_name   | address     | city | telephone  |
      | First       | Last        | 123 Street  | LA   | 5554443333 |
    Given the following pets exist for owner "Last":
      | name    | birth_date | type  |
      | Sprigs  | 2022-03-03 | cat   |

  Scenario: Create a valid visit for the pet of an owner (Normal flow)
    When a visit with description "A visit" and date "2022-03-10" is created for pet "Sprigs" of owner "Last"
    Then a visit with description "A visit" and date "2022-03-10" will exist for pet "Sprigs" of owner "Last"

  Scenario: Create a valid visit without a date for the pet of an owner (Alternate flow)
    When a visit with description "A visit" and date "" is created for pet "Sprigs" of owner "Last"
    Then a visit with description "A visit" and date "" will exist for pet "Sprigs" of owner "Last"

  Scenario: Try to create an invalid visit without a description for the pet of an owner (Error flow)
    When a visit with description "" and date "2022-03-10" is created for pet "Sprigs" of owner "Last"
    Then the error "must not be empty" shall be raised
    And no visit with description "A visit" and date "" will exist for pet "Sprigs" of owner "Last"

  Scenario Outline: Try to create an invalid visit with an invalid date for the pet of an owner (Normal flow)
    When a visit with description "<description>" and date "<date>" is created for pet "Sprigs" of owner "Last"
    Then the error "<error_message>" shall be raised
    And no visit with description "<description>" and date "<date>" will exist for pet "Sprigs" of owner "Last"

    Examples:
      | description | date       | error_message                                                              |
      | A visit     | 1000-03-28 | Please match the required format: Enter a date in this format: YYYY-MM-DD. |
      | A visit     | 9999-03-28 | Please match the required format: Enter a date in this format: YYYY-MM-DD. |
      | A visit     | 10/10/1999 | Please match the required format: Enter a date in this format: YYYY-MM-DD. |
