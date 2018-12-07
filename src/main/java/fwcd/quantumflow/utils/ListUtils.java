package fwcd.quantumflow.utils;

import java.util.List;
import java.util.Optional;

public final class ListUtils {
	private ListUtils() {}
	
	public static <T> Optional<T> listGet(List<T> list, int index) {
		if (index >= 0 && index < list.size()) {
			return Optional.of(list.get(index));
		} else {
			return Optional.empty();
		}
	}
}
