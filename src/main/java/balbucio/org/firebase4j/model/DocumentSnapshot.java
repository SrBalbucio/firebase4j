package balbucio.org.firebase4j.model;

import java.util.HashMap;
import java.util.Map;

public class DocumentSnapshot {

    private String name;
    private Map<String, Object> fields = new HashMap<>();
    private String creationTime;
    private String updateTime;
}
