@MFBenchMark
Feature: Mutual Fund Performance Comparison with it's Category Average and Benchmark

  @BenchMarkGetData
  Scenario Outline: TS_01 Capture Fund Returns
    Given User is on ET Money "<FundType>" Fund Page
    Then Capture Trailing Returns for "1 Month" period for "<FundType>"
    Then Capture Trailing Returns for "3 Months" period for "<FundType>"
    Then Capture Trailing Returns for "6 Months" period for "<FundType>"
    Then Capture Trailing Returns for "1 Year" period for "<FundType>"
    Then Capture Trailing Returns for "3 Years" period for "<FundType>"
    Then Capture Trailing Returns for "5 Years" period for "<FundType>"
    Given User is on MoneyControl's Benchmark page of "<FundType>"
    Then Capture Absolute Returns for "1 Month" period for "<FundType>"
    Then Capture Absolute Returns for "3 Months" period for "<FundType>"
    Then Capture Absolute Returns for "6 Months" period for "<FundType>"
    Then Capture Absolute Returns for "1 Year" period for "<FundType>"
    Then Capture Absolute Returns for "3 Years" period for "<FundType>"
    Then Capture Absolute Returns for "5 Years" period for "<FundType>"

    Examples:
      | FundType |
      | SmallCap |
      | LargeCap |
      | FlexiCap |
      | Index    |
      | MidCap   |

  @SendEmail
  Scenario: TS_02 Send Email
    Then Send Email
    Then Close the Browser