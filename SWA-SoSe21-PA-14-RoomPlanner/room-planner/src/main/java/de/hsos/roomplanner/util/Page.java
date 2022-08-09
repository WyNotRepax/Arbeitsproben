package de.hsos.roomplanner.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Represents a page of results. Internally uses {@link java.util.ArrayList}.
 * 
 * @author Benno Steinkamp
 */
public class Page<T> {

    private List<T> data;
    private long totalCount;

    public Page(long totalCount, Collection<T> data) {
        this.data = new ArrayList<T>();
        this.data.addAll(data);
        this.totalCount = totalCount;
    }

    public List<T> getData() { return Collections.unmodifiableList(this.data); }

    public long getTotalCount() { return this.totalCount; }

}
