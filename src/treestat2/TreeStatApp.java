/*
 * TreeStatApp.java
 *
 * Copyright (C) 2002-2013 Alexei Drummond and Andrew Rambaut
 *
 * This file is part of BEAST2.
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership and licensing.
 *
 * BEAST2 is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 *  BEAST is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with BEAST; if not, write to the
 * Free Software Foundation, Inc., 51 Franklin St, Fifth Floor,
 * Boston, MA  02110-1301  USA
 */

package treestat2;

import beast.base.core.Log;
import beast.pkgmgmt.BEASTClassLoader;
import jam.framework.SingleDocApplication;
import jam.mac.Utils;
import treestat2.statistics.SummaryStatisticDescription;
import treestat2.statistics.TreeSummaryStatistic;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TreeStatApp extends SingleDocApplication {

    public TreeStatApp(String nameString, String aboutString, Icon icon, String websiteURLString, String helpURLString) {
        super(nameString, aboutString, icon, websiteURLString, helpURLString);
    }

    @SuppressWarnings("unchecked")
    private static Class<? extends TreeSummaryStatistic> loadStatisticClass(String name) throws ClassNotFoundException {
        try {
            return (Class<? extends TreeSummaryStatistic>) BEASTClassLoader.forName(name);
        } catch (ClassNotFoundException e1) {
            // Fall back tries to use statistics in this package
            String fallback = "treestat2.statistics." + name;
            try {
                return (Class<? extends TreeSummaryStatistic>) BEASTClassLoader.forName(fallback);
            } catch (ClassNotFoundException e2) {
                throw e2; // propagate to outer try-catch
            }
        }
    }

    private static TreeSummaryStatistic createStatistic(String name, String value) throws ClassNotFoundException {
        Class<? extends TreeSummaryStatistic> tssClass = loadStatisticClass(name);
        SummaryStatisticDescription ssd = TreeSummaryStatistic.getSummaryStatisticDescription(tssClass);
        TreeSummaryStatistic stat;
        try {
            stat = tssClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        if (value != null) {
            if (ssd.allowsDouble()) {
                stat.setDouble(Double.parseDouble(value));
            } else if (ssd.allowsInteger()) {
                stat.setInteger(Integer.parseInt(value));
            } else if (ssd.allowsString()) {
                stat.setString(value);
            } else {
                Log.err("[TreeStatApp]: Statistic '" + name + "' does not accept a value, but one was provided: " + value);
            }
        }

        return stat;
    }

    private static List<TreeSummaryStatistic> parseCliStats(List<String> statNames) {
        List<TreeSummaryStatistic> statistics = new ArrayList<>();
        for (int i = 0; i < statNames.size(); i++) {
            String name = statNames.get(i);
            String value = null;
            try {
                loadStatisticClass(name);
                // Look ahead for optional value
                if (i + 1 < statNames.size()) {
                    String potentialValue = statNames.get(i + 1);
                    try {
                        // If next token is also a stat, do not consume it
                        loadStatisticClass(potentialValue);
                    } catch (ClassNotFoundException e) {
                        // Next token is not a class – treat as a value
                        value = potentialValue;
                        i++; // Consume the value
                    }
                }
                TreeSummaryStatistic stat = createStatistic(name, value);
                statistics.add(stat);
            } catch (ClassNotFoundException e) {
                Log.err("[TreeStatApp]: Class not found " + name + ".\n\tUse --list to see accepted classes.");
            } catch (Exception e) {
                Log.err("[TreeStatApp]: Failed to create statistic '" + name + "': " + e.getMessage());
                e.printStackTrace();
            }
        }

        return statistics;
    }

    private static List<TreeSummaryStatistic> loadStatisticsFromControlFile(String controlFileName) throws IOException {
        Log.info("Processing control file: " + controlFileName);
        List<TreeSummaryStatistic> statistics = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(controlFileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("\\s+");

                if (parts.length > 2) {
                    Log.err("[TreeStatApp]: Only one optional value is supported per statistic and remainder will be ignored." +
                            "Invalid input: " + line);
                }

                TreeSummaryStatistic stat = null;
                try {
                    String name = parts[0];
                    String value = parts.length == 2 ? parts[1] : null;
                    stat = createStatistic(name, value);
                } catch (ClassNotFoundException ignored) {
                    Log.err("[TreeStatApp]: Class not found " + parts[0] + ".\n\tUse --list to see accepted classes.");
                }

                if (stat != null) {
                    Log.info("  Adding statistic: " + stat.getName());
                    statistics.add(stat);
                }
            }
        }

        return statistics;
    }

    // Print Help static
    private static void printHelp() {
        System.out.println("TreeStatApp - Compute summary statistics from BEAST tree files");
        System.out.println();
        System.out.println("Usage:");
        System.out.println("  applauncher TreeStatApp [options]");
        System.out.println();
        System.out.println("Options:");
        System.out.println("  --control-file <path>, -c <path>");
        System.out.println("      Path to a control file specifying statistics and options.");
        System.out.println();
        System.out.println("  --stats <stat1> [stat2 ...], -s <stat1> [stat2 ...]");
        System.out.println("      Directly specify one or more statistic names to compute. Use this instead of --control-file.");
        System.out.println();
        System.out.println("  --tree-file <path>, --tree-files <path1> [path2 ...], -t <path1> [path2 ...]");
        System.out.println("      One or more input tree files to analyze.");
        System.out.println();
        System.out.println("  --out-file <path>, -o <path>");
        System.out.println("      (Single input only) Output file path. Will not be used with multiple input files.");
        System.out.println();
        System.out.println("  --out-tag <tag>, -g <tag>");
        System.out.println("      Suffix or tag to append to input filenames for output files.");
        System.out.println("      Used when processing multiple input files. Default: \"-treestats.log\"");
        System.out.println();
        System.out.println("  --ccd-burn-in <double>, -b <double> (optional)");
        System.out.println("      Burn-in used to construct CCDs with, should be between 0 and 1.");
        System.out.println("      Defaults to 0.1 for 10% burn-in, CCD0 has a default minimum of 0.1!");
        System.out.println();
        System.out.println("  --list, -list, -l");
        System.out.println("      Print all available tree statistic classes.");
        System.out.println();
        System.out.println("  --help, -help, -h");
        System.out.println("      Show this help message.");
        System.out.println();
    }

    // Main entry point
    static public void main(String[] args) throws IOException {

        if (args.length > 0) {
            String controlFileName = null;
            List<String> treeFileNames = new ArrayList<>();
            String outFileName = null;
            String outFileSuffix = "-treestats.log";  // default suffix for files
            List<String> statNames = new ArrayList<>();
            String ccdBurnInInput = null;  // default ccdBurnIn for CLI only!

            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "--help":
                    case "-help":
                    case "-h":
                        printHelp();
                        return;
                    case "--list":
                    case "-list":
                    case "-l":
                        TreeStatisticRegistry.printAvailableOptions();
                        return;
                    case "--control-file":
                    case "-c":
                        if (i + 1 < args.length) {
                            controlFileName = args[++i]; // get next arg
                        } else {
                            System.err.println("Error: --control-file requires a file path.");
                            return;
                        }
                        break;
                    case "--stats":
                    case "-s":
                        while (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                            statNames.add(args[++i]);
                        }
                        break;
                    case "--tree-file":
                    case "--tree-files":
                    case "-t":
                        i++;
                        while (i < args.length && !args[i].startsWith("-")) {
                            treeFileNames.add(args[i++]);
                        }
                        i--;
                        if (treeFileNames.isEmpty()) {
                            System.err.println("Error: --tree-file(s) requires at least one file.");
                            return;
                        }
                        break;
                    case "--out-file":
                    case "-o":
                        if (i + 1 < args.length) {
                            outFileName = args[++i]; // get next arg
                        } else {
                            System.err.println("Error: --out-file requires a file path.");
                            return;
                        }
                        break;
                    case "--out-tag":
                    case "-g":
                        if (i + 1 < args.length) {
                            outFileSuffix = args[++i];
                        } else {
                            System.err.println("Error: --out-suffix requires a suffix string.");
                            return;
                        }
                        break;
                    case "--ccd-burn-in":
                    case "-b":
                        if (i + 1 < args.length) {
                            ccdBurnInInput = args[++i];
                        } else {
                            System.err.println("Error: --ccd-burn-in requires a double as input.");
                            return;
                        }
                        break;
                    default:
                        System.err.println("Unknown option: " + args[i]);
                        printHelp();
                        return;
                }
            }

            boolean multipleInputs = treeFileNames.size() > 1;

            if (treeFileNames.isEmpty()) {
                throw new IllegalArgumentException("[TreeStatApp]: Error: --tree-file(s) requires at least one input file.");
            }
            if (multipleInputs && outFileName != null) {
                // Will only throw warning, but ignore and fall back to using --out-tag
                Log.warning("[TreeStatApp]: Error: --out-file cannot be used when processing multiple input files. Use --out-tag instead!");
            }

            List<TreeSummaryStatistic> statistics;

            if (controlFileName != null) {
                // Parse input stats from control file
                statistics = loadStatisticsFromControlFile(controlFileName);
            } else if (!statNames.isEmpty()) {
                // Parse input stats and values from cli input
                statistics = parseCliStats(statNames);
            } else {
                Log.err("[TreeStatApp]: No input provided. Use --help for usage.");
                return;
            }

            // Parsing ccdBurnIn value
            double ccdBurnIn = 0.1; // default ccd burnin
            if (ccdBurnInInput != null && !ccdBurnInInput.trim().isEmpty()) {
                try {
                    double parsed = Double.parseDouble(ccdBurnInInput);
                    if (parsed >= 0.0 && parsed < 1.0) {
                        ccdBurnIn = parsed;
                    } else {
                        Log.warning("Burn-in value must be between 0.0 (inclusive) and 1.0 (exclusive). Using default: " + ccdBurnIn);
                    }
                } catch (NumberFormatException e) {
                    Log.warning("Invalid burn-in value: '" + ccdBurnInInput + "', using default: " + ccdBurnIn);
                }
            }

            ProcessTreeFileListener listener = new ProcessTreeFileListener() {
                @Override
                public void startProcessing() {
                }

                @Override
                public void processingHalted() {
                    Log.info("  Processing halted!");
                }

                @Override
                public void processingComplete(int numTreesProcessed) {
                    Log.info("  Processed " + numTreesProcessed + " trees.");
                }

                @Override
                public boolean warning(String message) {
                    Log.warning(message);
                    return true;
                }

                @Override
                public void error(String errorTitle, String errorMessage) {
                    Log.err(errorTitle + ": " + errorMessage);
                }

                @Override
                public void progress(String progress) {
                }
            };

            for (String treeFileName : treeFileNames) {
                File inFile = new File(treeFileName);

                File outFile;
                if (multipleInputs || outFileName == null) {
                    // Always use suffix for multiple inputs or if outFileName is not provided
                    String baseName;
                    int lastDot = treeFileName.lastIndexOf('.');
                    if (lastDot > 0) {
                        baseName = treeFileName.substring(0, lastDot);
                    } else {
                        baseName = treeFileName; // No extension found
                    }
                    outFile = new File(baseName + outFileSuffix);
                } else {
                    // Use explicit output file name for single file
                    outFile = new File(outFileName);
                }

                TreeStatUtils.processTreeFile(inFile, outFile, listener, statistics, ccdBurnIn);
            }
        } else {

            // There is a major issue with languages that use the comma as a decimal separator.
            // To ensure compatibility between programs in the package, enforce the US locale.
            Locale.setDefault(Locale.US);

            if (Utils.isMacOSX()) {
                if (Utils.getMacOSXVersion().startsWith("10.5")) {
                    System.setProperty("apple.awt.brushMetalLook", "true");
                }
                System.setProperty("apple.laf.useScreenMenuBar", "true");
                System.setProperty("apple.awt.showGrowBox", "true");
                UIManager.put("SystemFont", new Font("Lucida Grande", Font.PLAIN, 13));
                UIManager.put("SmallSystemFont", new Font("Lucida Grande", Font.PLAIN, 11));
            }

            try {

                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                URL url = TreeStatApp.class.getResource("images/TreeStat.png");
                Icon icon = null;

                if (url != null) {
                    icon = new ImageIcon(url);
                }

                String nameString = "TreeStat2";
                String aboutString = "<html><center><p>Tree Statistic Calculation Tool<br>" +
                        "<p>by<br>" +
                        "Andrew Rambaut and Alexei J. Drummond</p>" +
                        "<p>Institute of Evolutionary Biology, University of Edinburgh<br>" +
                        "<a href=\"mailto:a.rambaut@ed.ac.uk\">a.rambaut@ed.ac.uk</a></p>" +
                        "<p>Department of Computer Science, University of Auckland<br>" +
                        "<a href=\"mailto:alexei@cs.auckland.ac.nz\">alexei@cs.auckland.ac.nz</a></p>" +
                        "<p>Visit the BEAST page:<br>" +
                        "<a href=\"http://beast.bio.ed.ac.uk/\">http://beast.bio.ed.ac.uk/</a></p>" +
                        "</center></html>";

                String websiteURLString = "https://github.com/alexeid/TreeStat2/";
                String helpURLString = "https://github.com/alexeid/TreeStat2/";

                TreeStatApp app = new TreeStatApp(nameString, aboutString, icon,
                        websiteURLString, helpURLString);
                app.setDocumentFrame(new TreeStatFrame(app, nameString));
                app.initialize();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(new JFrame(), "Fatal exception: " + e,
                        "Please report this to the authors",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}
