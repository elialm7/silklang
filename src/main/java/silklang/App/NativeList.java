/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.App;

import com.diogonunes.jcolor.Attribute;

import java.util.ArrayList;
import java.util.List;

import static com.diogonunes.jcolor.Ansi.colorize;

public class NativeList {
    private List<String> functionalities = new ArrayList<>();

    public NativeList() {
    }

    public void addTitle(String title){
        functionalities.add(colorize(title, Attribute.MAGENTA_TEXT()));
    }
    public void addSubTitle(String sub){
        functionalities.add(colorize(sub, Attribute.WHITE_TEXT()));
    }

    public void addFunction(String functionname, String returntype, String description){
        StringBuilder builder = new StringBuilder();
        builder.append(colorize(functionname, Attribute.BLUE_TEXT()));
        builder.append(colorize(" -> ", Attribute.BRIGHT_GREEN_TEXT()));
        builder.append(colorize(returntype, Attribute.YELLOW_TEXT()));
        builder.append(colorize(" | ", Attribute.BRIGHT_GREEN_TEXT()));
        builder.append(colorize(description, Attribute.GREEN_TEXT()));
        functionalities.add(builder.toString());
    }

    public void print(){
        functionalities.forEach(System.out::println);
    }


}
