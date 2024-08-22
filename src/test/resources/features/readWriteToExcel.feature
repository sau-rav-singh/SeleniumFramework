Feature: Read Data from Excel

  Scenario Outline: Place an order
    Given A Workbook named "secTypes" and sheetname as"equity" and Row number as <RowNumber> is read and to write Data
    When Read symbol as "<SYMBOL>" and orderType as "<ORDERTYPE>" and print the values

    Examples: 
      | RowNumber | SYMBOL | ORDERTYPE |
      |         1 | SYMBOL | ORDERTYPE |
      |         2 | SYMBOL | ORDERTYPE |
