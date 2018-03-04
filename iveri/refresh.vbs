Option Explicit

Dim FSO, filePath, file
Dim strFolder

'Change as needed
'strFolder = "C:\temp"
If 1 <= WScript.Arguments.Count Then strFolder = WScript.Arguments(0)

'Create the filesystem object
Set FSO = CreateObject("Scripting.FileSystemObject")
'Get a reference to the folder you want to search
set filePath = FSO.GetFolder(strFolder)

'loop through the folder and get the file names
For Each file In filePath.Files
	If LCase(Right(file.Name, 5)) = ".xlsm" Or LCase(Right(file.Name, 5)) = ".xlsx" Or LCase(Right(file.Name, 4)) = ".xls" Then
		Dim excel
		Set excel = CreateObject("Excel.Application")
		
		excel.Visible = True
		excel.DisplayAlerts = False
		excel.AskToUpdateLinks = False
		excel.AlertBeforeOverwriting = False
		
		Dim workbook
		Set workbook = excel.Workbooks.Open(file)
		workbook.RefreshAll
		workbook.Save
		workbook.Close
		
		excel.Quit
		Set workbook = Nothing
		Set excel = Nothing
	End If
Next
	
'Clean up
Set filePath = Nothing
Set FSO = Nothing