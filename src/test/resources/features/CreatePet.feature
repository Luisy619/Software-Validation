Feature: Create a pet
    As a secretary, I want to be able to add a pet to the profile of an specific pet owner already in the petClinic
    system in order to store the pet information and a link to its owner.

Background:
  Given the following owners exist in the system:
    | first_name  | last_name   | address     | city | telephone  |
    | First       | Last        | 123 Street  | LA   | 5554443333 |
  Given the following pets exist for owner "Last":
    | name    | birth_date | type  |
    | Sprigs  | 2022-03-03 | Cat   |

Scenario: Create a valid pet with a given owner (Normal flow)
  When the following pet is added to the owner "Last":
    | name    | birth_date | type  |
    | Noodle  | 2017-03-28 | Snake |
  Then the following pet will exist for owner "Last":
    | name    | birth_date | type  |
    | Noodle  | 2017-03-28 | Snake |

Scenario: Create an invalid pet with a duplicate name for a given owner (Error flow)
  When the following pet is added to the owner "Last":
    | name    | birth_date | type  |
    | Sprigs  | 2022-03-03 | Cat   |
  Then the error "..." shall be raised
  And the following pet will not exist for owner "Last":
    | name    | birth_date | type  |
    | Noodle  | 2017-03-28 | Snake |

Scenario Outline: Try to create an invalid pet with missing information (Error flow)
  When a pet with name "<name>", birthdate "<birth_date>", and type "<type>" is created
  Then the error "<error_message>" shall be raised
  And And a pet with name "<name>", birthdate "<birth_date>", and type "<type>" will not exist

  Examples:
    | name    | birth_date | type  | error_message |
    |         | 2017-03-28 | Snake | ...           |
    | Noodle  |            | Snake | ...           |
    | Noodle  | 2017-03-28 |       | ...           |

Scenario Outline: Try to create an invalid pet with an invalid birth date (Error flow)
  When a pet with name "<name>", birthdate "<birth_date>", and type "<type>" is created
  Then the error "<error_message>" shall be raised
  And a pet with name "<name>", birthdate "<birth_date>", and type "<type>" will not exist

  Examples:
    | name    | birth_date | type  | error_message |
    | Noodle  | 1000-03-28 | Snake | ...           |
    | Noodle  | 9999-03-28 | Snake | ...           |
    | Noodle  | 10/10/1999 | Snake | ...           |

Scenario: Try to create an invalid pet with an invalid type (Error flow)
  When a pet with name "Noodle", birthdate "2017-03-28", and type "Gecko" is created
  Then the error "..." shall be raised
  And a pet with name "Noodle", birthdate "2017-03-28", and type "Gecko" will not exist
