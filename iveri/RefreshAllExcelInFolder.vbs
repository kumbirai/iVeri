Option Explicit

Dim fso
Dim ObjFolder
Dim ObjFiles
Dim ObjFile
Dim objExcel

'Creating File System Object
Set fso = CreateObject("Scripting.FileSystemObject")
'Getting the Folder Object
Set ObjFolder = fso.GetFolder("C:\temp")

'Getting the list of Files
Set ObjFiles = ObjFolder.Files
'On Error Resume Next
For Each ObjFile In ObjFiles
	If LCase(Right(ObjFile.Name, 5)) = ".xlsx" Or LCase(Right(ObjFile.Name, 4)) = ".xls" Then
		MsgBox(ObjFile.Name)
		Workbooks.Open(ObjFile).Activate
		ActiveWorkbook.RefreshAll
		ActiveWorkbook.Save
		ActiveWorkbook.Close
	End If
Next
