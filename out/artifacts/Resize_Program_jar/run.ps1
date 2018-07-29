param (
    [string]$p0 = "",
    [string]$p1 = "",
    [string]$p2 = "",
    [string]$p3 = "",
    [string]$p4 = "",
    [string]$p5 = "",
    [string]$p6 = "",
    [string]$p7 = "",
    [string]$p8 = "",
    [string]$p9 = ""
)
function Find-JavaExe {
    [CmdletBinding()]
    param ()

    $JavaExeSuffix = 'bin\java.exe'

    if ($env:JAVAHOME -eq $null) {
        Write-Debug "`$env:JAVAHOME doesn't exist, going to look elsewhere"
    }
    else {
        $JavaHomeBasedPath = Join-Path $env:JAVAHOME $JavaExeSuffix
        Write-Debug "Testing for $JavaHomeBasedPath, based on `$env:JAVAHOME"
        if (Test-Path $JavaHomeBasedPath) {
            Write-Debug "Found $JavaExePath"
            return $JavaExePath
        }
    }

    $RegistrySearchPaths = @('HKLM:\SOFTWARE\JavaSoft\Java Runtime Environment\', 'HKLM:\SOFTWARE\Wow6432Node\JavaSoft\Java Runtime Environment\')
    $JavaExePath = $RegistrySearchPaths |
        where { Test-Path $_ } |
        % {
            $CurrentVersion = (Get-ItemProperty $_).CurrentVersion
            Write-Debug "Current Java version is $CurrentVersion, based on $($_)"
            $VersionKey = Join-Path $_ $CurrentVersion
            $JavaHomeBasedPath = Join-Path (Get-ItemProperty $VersionKey).JavaHome $JavaExeSuffix
            Write-Debug "Testing for $JavaHomeBasedPath, based on $VersionKey\JavaHome"
            if (Test-Path $JavaHomeBasedPath) { $JavaHomeBasedPath }
        } |
        select -First 1

    if ($JavaExePath -ne $null) {
        Write-Debug "Found $JavaExePath"
        return $JavaExePath
    }

}

#$path_java = Find-JavaExe
$path_java = "java"
$java_param =  " -jar "
$path_prog = "E:\Projekty\Java\Resize_Program\out\artifacts\Resize_Program_jar"
$program_name = "\ResizeProgram.jar"
$path =  "$p0 " + "$p1 " + "$p2 " + "$p3 " + "$p4 " + "$p5 " + "$p6 " + "$p7 " + "$p8 " + "$p9 "
$path = $path -replace '\s*$',''
$command = "$path_java" + $java_param + $path_prog+$program_name + " ""$path"""

iex "$command"
#Read-Host -Prompt "Click to exit"