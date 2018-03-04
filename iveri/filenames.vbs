Option Explicit
Dim objArgs, strArg, strArgs



Set objArgs = WScript.Arguments

For Each strArg in objArgs
    strArgs = strArgs &  strArg & vbcrlf
Next 

MsgBox strArgs