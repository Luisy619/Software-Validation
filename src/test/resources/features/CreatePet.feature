Feature: Create a pet
    As a secretary, I want to be able to add a pet to the profile of an specific pet owner already in the petClinic
    system in order to store the pet information and a link to its owner.

  Background:
    Given the following owners exist in the system:
      | first_name  | last_name   | address     | city | telephone  |
      | First       | Last        | 123 Street  | LA   | 5554443333 |
    Given the following pets exist for owner "Last":
      | name    | birth_date | type  |
      | Sprigs  | 2022-03-03 | cat   |

  Scenario: Create a valid pet with a given owner (Normal flow)
    When the following pet is added to the owner "Last":
      | name    | birth_date | type  |
      | Noodle  | 2017-03-10 | snake |
    Then the following pet will exist for owner "Last":
      | name    | birth_date | type  |
      | Noodle  | 2017-03-10 | snake |

  Scenario: Create an invalid pet with a duplicate name for a given owner (Error flow)
    When the following pet is added to the owner "Last":
      | name    | birth_date | type  |
      | Sprigs  | 2022-03-03 | cat   |
    Then the following pet will not exist for owner "Last":
      | name    | birth_date | type  |
      | Noodle  | 2017-03-10 | snake |
    And the error "is already in use" shall be raised

  Scenario Outline: Try to create an invalid pet with missing information (Error flow)
    When a pet with name "<name>", birthdate "<birth_date>", and type "<type>" is created for owner "Last"
    Then a pet with name "<name>", birthdate "<birth_date>", and type "<type>" will not exist for owner "Last"
    And the error "<error_message>" shall be raised

    Examples:
      | name    | birth_date | type  | error_message  |
      |         | 2017-03-10 | snake | is required    |
      | Noodle  |            | snake | is required    |
      | Noodle  | 2017-03-10 |       | is required    |

  Scenario Outline: Try to create an invalid pet with an invalid birth date (Error flow)
    When a pet with name "<name>", birthdate "<birth_date>", and type "<type>" is created for owner "Last"
    Then a pet with name "<name>", birthdate "<birth_date>", and type "<type>" will not exist for owner "Last"
    And the error "<error_message>" shall be raised

    Examples:
      | name    | birth_date  | type  | error_message |
      | Noodle  | 99999-03-10 | snake | is required   |
      | Noodle  | 10/10/1999  | snake | is required   |

  Scenario: Try to create an invalid pet with an invalid type (Error flow)
    When a pet with name "Noodle", birthdate "2017-03-10", and type "gecko" is created for owner "Last"
    Then a pet with name "Noodle", birthdate "2017-03-10", and type "gecko" will not exist for owner "Last"
    And the error "must be a valid type" shall be raised
