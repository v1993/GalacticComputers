package com.v1993.galacticcomputers.utils;

/*
 * We still want to override default drivers from OC,
 * so even the lowest priority is > 0. Feel free to use
 * them like `GENERIC+5` when more values are needed.
 */

public class ComponentPriority {
	/// For very non-specific interfaces (e.g. electricity)
	public static final int GENERIC = 1;
	/// For more specific interfaces (e.g. oxygen)
	public static final int SPECIFIC_INTERFACE = 50;
	/// For interfaces belonging to specific machines, supposed to be final
	public static final int SPECIFIC_MACHINE = 1000;
}
