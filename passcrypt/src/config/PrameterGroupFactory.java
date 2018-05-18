package config;

import config.ParamGroup;
import config.groups.BalancedParameters;
import config.groups.HeavyParameters;
import config.groups.LightParameters;

public final class PrameterGroupFactory {
    /**
     * Static class, no instances allowed.
     */
    private PrameterGroupFactory() {
    }
	
	public static ParamGroup create(PrameterGroupTypes type) {
        switch (type) {
		case BALANCED:
			return new BalancedParameters();
		case HEAVY:
			return new HeavyParameters();
		case LIGHT:
			return new LightParameters();
        default:
            throw new IllegalArgumentException("Invalid PrameterGroup type");
        }
	}
}
