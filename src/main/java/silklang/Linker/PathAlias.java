/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.Linker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.*;

public class PathAlias {


    private Map<String, Path> alias  = new HashMap<>();

    public PathAlias(){}
    public boolean isempty(){
        return alias.isEmpty();
    }

    public boolean exist(String aliaslookup){
        String file = "pair.txt";
        File check = new File(file);
        if(check.exists()){
            read();
            return alias.containsKey(aliaslookup);
        }else {
            return false;
        }
    }

    public void add(String alias, String path){
        Path newPath= new File(path).toPath();
        if(!exist(alias)){
            this.alias.put(alias, newPath);
            write();
        }
    }

    public Path getPath(String aliasstr){
        if(exist(aliasstr)){
            return alias.get(aliasstr);
        }
        return null;
    }

    private void read(){
        String filePath = "pair.txt";
        this.alias.clear();
        try (Scanner scanner = new Scanner(new File(filePath))) {
            String line = "";
            while(scanner.hasNext()){
                line = scanner.nextLine();
                String[] splittedLine = line.split(",");
                String key = splittedLine[0];
                String value = splittedLine[1];
                alias.put(key, new File(value).toPath());
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void write(){
        String filepath = "pair.txt";
        Set<Map.Entry<String, Path>> set = alias.entrySet();
        Iterator<Map.Entry<String, Path>> iterator = set.iterator();
        try {
            PrintWriter writer = new PrintWriter(filepath);
            while(iterator.hasNext()) {
                StringBuilder builder = new StringBuilder();
                Map.Entry mentry = (Map.Entry)iterator.next();
                builder.append(mentry.getKey()).append(",").append(((Path)mentry.getValue()).toString()).append("\n");
                writer.println(builder);
            }
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }


}
