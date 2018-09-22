/*
 * Copyright (c) 2018 Bull/Atos.
 * All rights reserved.
 */
package bull.internal.utils;

import bull.logger.Logger;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * [Class containing methods for writing to a csv file using RFC4180 standard formatting.]
 * @author mkyong.
 * @implNote https://www.mkyong.com/java/how-to-export-data-to-csv-file-java
 */
public class CSVUtils {
        /*Constructor*/
        private CSVUtils() {
            throw new IllegalStateException("Utility class");
        }

        /*Methods*/
        /**
         * [When formatting in CSV allows to respect the CSV standard.]
         * @implSpec https://tools.ietf.org/html/rfc4180 -> RFC4180.
         * @param value a string to normalize.
         * @return a normalized String which respect the CSV Standard.
         */
        private static String followCVSformat(String value) {

            String result = value;
            if (result.contains("\"")) {
                result = result.replace("\"", "\"\"");
            }
            return result;

        }

        /**
         * Write a single line (a tuple) in a file
         * @param w the writer object.
         * @param values a list of values from a tuple.
         */
        public static void writeLine(Writer w, List<String> values) {

            boolean first = true;

            StringBuilder sb = new StringBuilder();
            for (String value : values) {
                if (!first) {
                    sb.append(';');
                }
                    sb.append(followCVSformat(value));
                first = false;
            }
            sb.append("\n");
            try {
                w.append(sb.toString());
            } catch (IOException e) {
                Logger.getInstance().fatal("Error when using the append on Stringbuilder class, reason : ");
            }
        }
    }
