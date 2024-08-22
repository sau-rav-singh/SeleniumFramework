@IndexAlert
Feature: Send Email When Market Falls more than threshold

  @CaptureIndexData
  Scenario Outline: TS_01 Capture Index Values
    Given User is on MoneyControl page of "<Index>"
    Then Capture Current Value and 52Wk High Value
    Then Send Email alert if Index Value is less then "10%" Threshold

    Examples:
      | Index            |
      | Index_MC         |
      | NiftySmallCap250 |

  @CloseBrowser
  Scenario: TS_02 Close Browser
    Then Close the Browser
