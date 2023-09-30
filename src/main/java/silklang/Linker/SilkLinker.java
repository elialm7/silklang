/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.Linker;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class SilkLinker {
    private List<String> fileContent;
    private File originFile;

    private String absPath;
    public SilkLinker(File descriptorFile) throws IOException {
        this.fileContent = Files.readAllLines(descriptorFile.toPath());
        this.originFile = descriptorFile.getParentFile();
    }

    public SilkLinker(URI uri) throws IOException{
        this.absPath = uri.getPath().substring(1);
        this.originFile = new File(absPath);
        this.fileContent = Files.readAllLines(originFile.toPath());
    }
    public List<String> getContent(){
        return this.fileContent;
    }

    public List<File> getDependencies() throws IOException {
        List<File> dependenciesFiles = new ArrayList<>();
        String parentPath = this.originFile.getParent()+"\\";
        for(String str: fileContent){
            if(str.endsWith(".silk") && !str.contains("#")) {
                dependenciesFiles.add(new File(parentPath + str));
            }
        }

        return dependenciesFiles;
    }
    public File getOrigin(){
        return this.originFile;
    }
}
