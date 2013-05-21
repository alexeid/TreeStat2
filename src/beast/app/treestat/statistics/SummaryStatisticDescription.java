/*
 * SummaryStatisticDescription.java
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

package beast.app.treestat.statistics;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to provide basic details about each summary statistic.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SummaryStatisticDescription {

	String name();

    String description();

    String valueName() default "";

    boolean allowsNonultrametricTrees() default true;

    boolean allowsPolytomies() default false;

	boolean allowsUnrootedTrees() default false;

    boolean allowsWholeTree() default true;

    boolean allowsCharacter() default false;

    boolean allowsCharacterState() default false;

    boolean allowsTaxonList() default false;

    boolean allowsString() default false;

    boolean allowsInteger() default false;

    boolean allowsDouble() default false;

    Category category() default Category.GENERAL;

	public enum Category {

		TREE_SHAPE("Tree shape"),
		PHYLOGENETIC("Phylogenetic"),
		POPULATION_GENETIC("Population genetic"),
		GENERAL("General"),
		SPECIATION("Speciation/Birth-death");

		private Category(String name) {
			this.name = name;
		}

        public String getPrettyName() {
            return name;
        }

        private String name;
	}

}

