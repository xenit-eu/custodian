package eu.xenit.custodian.adapters.gradle.buildsystem.asserts.file;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

public class GradleBuildParser {

    static String extractSection(String name, String content) {
        if (content == null) {
            return "";
        }

        int openPluginsIndex = content.indexOf(name + " {");
        if (openPluginsIndex < 0) {
            return "";
        }

        content = content.substring(openPluginsIndex + name.length() + " {".length()).trim();

        // now 'content' contains the start of the section
        // we start tracking the 'depth', to support nested sections
        //
        // ASSUMPTION: the matching closing brace is:
        // 1. at the end of a line (after trimming)
        // 2. alone on a new line (is that the same as case (1) ?
        // This is to avoid running into variables in strings like "${foo}"

        List<String> section = new ArrayList<>();
        int depth = 1;
        for (String line : content.split(System.lineSeparator())) {

            if (line.trim().matches("^\\w+\\s\\{.*$")) {
                depth++;
            }

            if (line.trim().endsWith("}")) {
                depth--;

                if (depth == 0) {

                    if (!line.trim().equalsIgnoreCase("}"))
                    {
                        // there is useful content before the closing '}'
                        // Example: maven { url 'https://artifacts.alfresco.com/nexus/content/groups/public/' }

                        section.add(line.substring(0, line.indexOf("}")).trim());
                    }
                    return String.join(System.lineSeparator(), section);
                }
            }

            section.add(line);
        }

        // if we end up here, something went wrong
        // the matching closing brace was not found ?!
        throw new IllegalStateException("matching closing brace not found for section " + name);

    }



}
