/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.Linker;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class PathResolver {


    public PathResolver(){}


    public static Path resolvePath(String from, String target){
        List<String> result = new ArrayList<>();
        try {
            Files.walkFileTree(Paths.get(from), EnumSet.noneOf(FileVisitOption.class), Integer.MAX_VALUE,
                    new SimpleFileVisitor<>() {
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                            if (file.getFileName().toString().equals(target)) {
                                result.add(file.toString());
                            }
                            return FileVisitResult.CONTINUE;
                        }

                        @Override
                        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                            // Handle file visit failure
                            return FileVisitResult.CONTINUE;
                        }
                    });
        } catch (IOException e) {
            return null;
        }
        return new File(result.get(0)).toPath();
    }



}
