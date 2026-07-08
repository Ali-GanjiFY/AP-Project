package org.example.frontend.advertisement;

public class CategoryOption {
    private final Long id;
    private final String name;

    public CategoryOption(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() { return id; }
    public String getName() { return name; }

    @Override
    public String toString() {
        return name;
    }
}