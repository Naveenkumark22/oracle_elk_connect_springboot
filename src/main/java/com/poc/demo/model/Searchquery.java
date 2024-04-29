package com.poc.demo.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Searchquery {
    private String size;
    private String from;
    private String minScore;
    private Set<String> sourceFilter;
    private Map<String, Object> query;
    private String scroll;
    private String scrollId;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMinScore() {
        return minScore;
    }

    public void setMinScore(String minScore) {
        this.minScore = minScore;
    }

    public Set<String> getSourceFilter() {
        return sourceFilter;
    }

    public void setSourceFilter(Set<String> sourceFilter) {
        this.sourceFilter = sourceFilter;
    }

    public Map<String, Object> getQuery() {
        return query;
    }

    public void setQuery(Map<String, Object> query) {
        this.query = query;
    }

    public String getScroll() {
        return scroll;
    }

    public void setScroll(String scroll) {
        this.scroll = scroll;
    }

    public String getScrollId() {
        return scrollId;
    }

    public void setScrollId(String scrollId) {
        this.scrollId = scrollId;
    }
}
