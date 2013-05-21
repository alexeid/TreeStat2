/*
 * TreeStatData.java
 *
 * Copyright (C) 2002-2006 Alexei Drummond and Andrew Rambaut
 *
 * This file is part of BEAST.
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership and licensing.
 *
 * BEAST is free software; you can redistribute it and/or modify
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

package beast.app.treestat;

import beast.app.treestat.statistics.SummaryStatisticDescription;
import beast.app.treestat.statistics.TreeSummaryStatistic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TreeStatData {
	public static final String version = "1.0";

	public TreeStatData() {
	}

	// Data options
	public Set<String> allTaxa = new HashSet<String>();
	public List<TaxonSet> taxonSets = new ArrayList<TaxonSet>();
	public List<Character> characters = new ArrayList<Character>();
	public List<TreeSummaryStatistic> statistics = new ArrayList<TreeSummaryStatistic>();

	public static class TaxonSet {
		String name;
		List taxa;
		public String toString() { return name; }
	}

	public static class Character {
		String name;
		List<TreeStatData.State> states;
		public String toString() { return name; }
	}

	public static class State {
		String name;
		String description;
		List<String> taxa;
		public String toString() { return name; }
	}
}

