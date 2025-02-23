package com.example.project3;

import com.example.project3.graph.Vertex;

//distance as priority
public record QueueObject<T>(Vertex<T> vertex, Double distance) implements Comparable<QueueObject<T>> {
    @Override
    public int compareTo(QueueObject<T> other) {
        return this.distance.compareTo(other.distance());
    }
}

