package com.alkaid.trip51.location.adapter;
import com.alkaid.trip51.model.SimpleCity;
import java.util.Comparator;

/**
 * 
 * @author jyz
 *
 */
public class CityComparator implements Comparator<SimpleCity> {

	@Override
	public int compare(SimpleCity c1, SimpleCity c2) {
		if (c1.getFirstLetter().equals("@")
				|| c2.getFirstLetter().equals("#")) {
			return -1;
		} else if (c1.getFirstLetter().equals("#")
				||c2.getFirstLetter().equals("@")) {
			return 1;
		} else {
			return c1.getFirstLetter().compareTo(c2.getFirstLetter());
		}
	}
}
