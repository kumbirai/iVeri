Option Explicit

Dim FSO, FLD, FIL
Dim strFolder

'Change as needed
strFolder = "C:\temp"

'Create the filesystem object
Set FSO = CreateObject("Scripting.FileSystemObject")
'Get a reference to the folder you want to search
set FLD = FSO.GetFolder(strFolder)

'loop through the folder and get the file names
For Each Fil In FLD.Files
	If LCase(Right(Fil.Name, 5)) = ".xlsm" Or LCase(Right(Fil.Name, 5)) = ".xlsx" Or LCase(Right(Fil.Name, 4)) = ".xls" Then
		'Workbooks(Fil.Name).RefreshAll
		'MsgBox Fil.Path
		Dim oExcel
		Set oExcel = CreateObject("Excel.Application")
		
		'Dim fullpath
		'fullpath = FSO.GetAbsolutePathName(Fil)
		'MsgBox fullpath
		
		oExcel.Visible = True
		oExcel.DisplayAlerts = False
		oExcel.AskToUpdateLinks = False
		oExcel.AlertBeforeOverwriting = False
		
		Dim oWorkbook
		Set oWorkbook = oExcel.Workbooks.Open(Fil)
		oWorkbook.RefreshAll
		oWorkbook.Save
		oWorkbook.Close
		
		oExcel.Quit
		Set oWorkbook = Nothing
		Set oExcel = Nothing
	End If
Next
	
'Clean up
Set FLD = Nothing
Set FSO = Nothing