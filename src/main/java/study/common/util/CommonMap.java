package study.common.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CommonMap extends HashMap<String, Object> {

	public CommonMap() {
		super();
	}

	public CommonMap(Map<String, Object> map) {
        this.putAll(map);
	}

	public Object put(String key, Object value) {
        return super.put((key).toLowerCase(), Objects.requireNonNullElse(value, ""));
	}

}
