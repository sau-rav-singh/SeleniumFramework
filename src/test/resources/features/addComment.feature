@RestAPI
Feature: Adding Comment to Jira Issues

  Background:
    Given A Workbook named "addComment" and sheetname as"Sheet1" is read

  Scenario Outline: Add Comment to Jira Bug with Valid Data
    Given Row number as<RowNumber> is read
    Then Read comment as"<COMMENT>" to create a valid addCommentToBug payload as"addCommentToBug"
    When The "AddComment" request is sent with the "Post" HTTP method on IssueID as "<ISSUEID>"
    Then Validate that the response status code is "201"
    And Validate the following fields from the response:
      | Field                       | Value                         |
      | Display Name                | <DISPLAY_NAME>                |
      | Comment                     | <COMMENT>                     |
      | Update Author Email Address | <UPDATE_AUTHOR_EMAIL_ADDRESS> |

    Examples:
      | RowNumber | ISSUEID | DISPLAY_NAME | UPDATE_AUTHOR_EMAIL_ADDRESS | COMMENT |
      | 1         | ISSUEID | DISPLAY_NAME | UPDATE_AUTHOR_EMAIL_ADDRESS | COMMENT |
      | 2         | ISSUEID | DISPLAY_NAME | UPDATE_AUTHOR_EMAIL_ADDRESS | COMMENT |

  Scenario Outline: Add Comment to Jira Bug with Valid Data
    Given A Workbook named "addComment" and sheetname as"Sheet1" and Row number as <RowNumber> is read
    Then Read comment as"<COMMENT>" to create a valid addCommentToBug payload as"addCommentToBug"
    When The "AddComment" request is sent with the "Post" HTTP method on IssueID as "<ISSUEID>"
    Then Validate that the response status code is "201"
    And Validate the following fields from the response:
      | Field                       | Value                         |
      | Display Name                | <DISPLAY_NAME>                |
      | Comment                     | <COMMENT>                     |
      | Update Author Email Address | <UPDATE_AUTHOR_EMAIL_ADDRESS> |

    Examples:
      | RowNumber | ISSUEID | DISPLAY_NAME | UPDATE_AUTHOR_EMAIL_ADDRESS | COMMENT |
      | 3         | ISSUEID | DISPLAY_NAME | UPDATE_AUTHOR_EMAIL_ADDRESS | COMMENT |

  Scenario Outline: Add Comment to Jira Bug with Invalid Data
    Given Row number as<RowNumber> is read
    Then Read comment as"<COMMENT>" to create an invalid addCommentToBug payload as"invalidAddComment"
    When The "AddComment" request is sent with the "Post" HTTP method on IssueID as "<ISSUEID>"
    Then Validate that the response status code is "400"

    Examples:
      | RowNumber | ISSUEID | COMMENT |
      | 3         | ISSUEID | COMMENT |

  Scenario Outline: Add Comment to Non-Existing Jira Bug
    Given Row number as<RowNumber> is read
    Then Read comment as"<COMMENT>" to create a valid addCommentToBug payload as"addCommentToBug"
    When The "AddComment" request is sent with the "Post" HTTP method on a non-existing IssueID as "<ISSUEID>"
    Then Validate that the response status code is "404"

    Examples:
      | RowNumber | ISSUEID | COMMENT |
      | 4         | ISSUEID | COMMENT |
