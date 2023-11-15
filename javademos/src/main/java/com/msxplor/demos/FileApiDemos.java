package com.msxplor.demos;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileApiDemos {

    public static void usingTransferToFunction() throws IOException {
        // for efficient file copy you can use Files.Copy
        // transferTo API demonstrate copying any inputsteam to output stream
        
        try( var input = Files.newInputStream(Paths.get("some_path")); 
            var output = Files.newOutputStream(Paths.get("some_out_put_file")) 
            ) {
                   input.transferTo(output) ;
            }
    }
    public static void usingFileMismatch() throws IOException {
        long mismatch = Files.mismatch(Paths.get("filea"),Paths.get("fileb"));
        Files.isSameFile(Paths.get("filea"),Paths.get("fileb"));
    }
    
}
