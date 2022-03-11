Feature: Create a pet owner
    As a secretary, I want to be able to create a new pet owner profile in the petClinic system in order to store
    contact information relating to this person and enable its retrieval.

  Background:
    Given the following owners exist in the system:
      | first_name  | last_name   | address     | city | telephone  |
      | First       | Last        | 123 Street  | LA   | 5554443333 |

  Scenario: Create a new valid pet owner (Normal flow)
    When the following owner is created:
      | first_name  | last_name   | address     | city | telephone  |
      | NewFirst    | NewLast     | 123 Street  | LA   | 5554443333 |
    Then the following owner will exist 1 time in the system:
      | first_name  | last_name   | address     | city | telephone  |
      | NewFirst    | NewLast     | 123 Street  | LA   | 5554443333 |

  Scenario: Create a new valid owner with the same information as another (Alternate flow)
    When the following owner is created:
      | first_name  | last_name   | address     | city | telephone  |
      | First       | Last        | 123 Street  | LA   | 5554443333 |
    Then the following owner will exist 2 time in the system:
      | first_name  | last_name   | address     | city | telephone  |
      | First       | Last        | 123 Street  | LA   | 5554443333 |

  Scenario Outline: Try to create an invalid owner with missing information (Error flow)
    When an owner with name "<first_name>" "<last_name>", address "<address>" "<city>", and telephone "<telephone>" is created
    Then an owner with name "<first_name>" "<last_name>", address "<address>" "<city>", and telephone "<telephone>" will not exist
    And the error "<error_message>" shall be raised

    Examples:
      | first_name  | last_name   | address     | city | telephone  | error_message      |
      |             | NewLast     | 123 Street  | LA   | 5554443333 | must not be empty  |
      | NewFirst    |             | 123 Street  | LA   | 5554443333 | must not be empty  |
      | NewFirst    | NewLast     |             | LA   | 5554443333 | must not be empty  |
      | NewFirst    | NewLast     | 123 Street  |      | 5554443333 | must not be empty  |
      | NewFirst    | NewLast     | 123 Street  | LA   |            | must not be empty  |

  Scenario Outline: Try to create an invalid owner with an invalid phone number (Error flow)
    When an owner with name "<first_name>" "<last_name>", address "<address>" "<city>", and telephone "<telephone>" is created
    Then an owner with name "<first_name>" "<last_name>", address "<address>" "<city>", and telephone "<telephone>" will not exist
    And the error "<error_message>" shall be raised

    Examples:
      | first_name  | last_name   | address     | city | telephone    | error_message               |
      | NewFirst    | NewLast     | 123 Street  | LA   | 555444333    | numeric value out of bounds |
      | NewFirst    | NewLast     | 123 Street  | LA   | 55544433333  | numeric value out of bounds |
