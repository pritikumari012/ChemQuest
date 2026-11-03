package com.chemquest.model;

public record Element(int atomicNumber, String symbol, String name, int period, int group,
                      String category, String summary) {
}
