package com.wind.common;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

@Accessors(chain = true)
@NoArgsConstructor
@Data
@ToString
public class PaginatedResult implements Serializable {
    private int currentPage; // Current page number
    private int count; // Number of total pages
    private Object data; // Paginated resources
    private Dictionary<String, Object> dictionary = new Hashtable<>(); // dictionary of related result
}
