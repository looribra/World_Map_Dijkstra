package com.example.project3;

import com.example.project3.graph.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Dijkstra<T> {

    Graph<T> graph;
    Graph<T> graphTime;
    Graph<T> graphCost;

    HashMap<T, Double> distances;
    HashMap<T, Vertex<T>> previous;

    //constructor
    public Dijkstra(Graph<T> g) {
        if (g == null) throw new IllegalArgumentException("Graph cannot be null");
        if (!g.isWeighted()) throw new IllegalArgumentException("Graph must be weighted");

        this.distances = new HashMap<>();
        this.previous = new HashMap<>();
        this.graph = g;
        this.graphTime = g;
        this.graphCost = g;
    }

    private void dijkstraAlgo(Vertex<T> start, Graph<T> graph) {
        distances = new HashMap<>();
        previous = new HashMap<>();

        PriorityQueue<QueueObject<T>> queue = new PriorityQueue<>();
        queue.add(new QueueObject<>(start, 0.0));

        // Initialize distances and previous
        for (Vertex<T> vertex : graph.getVertices().values()) {
            distances.put(vertex.getData(), Double.POSITIVE_INFINITY);
            previous.put(vertex.getData(), null);
        }

        distances.put(start.getData(), 0.0);

        while (!queue.isEmpty()) {
            Vertex<T> v = queue.poll().vertex();

            for (Edge<T> e : v.getEdges()) {
                if (e.getWeight() == null) continue; // Skip null weights

                double alt = distances.get(v.getData()) + e.getWeight();
                Vertex<T> to = e.getTo();

                if (alt < distances.get(to.getData())) {
                    distances.put(to.getData(), alt);
                    previous.put(to.getData(), v);
                    queue.add(new QueueObject<>(to, distances.get(to.getData())));
                }
            }
        }
    }



    public LinkedList<Vertex<T>> getShortestPath(Vertex<T> start, Vertex<T> end) {
        if (start == null || end == null) throw new IllegalArgumentException("Start and end vertices cannot be null");
        if (start.equals(end)) throw new IllegalArgumentException("Start and end vertices cannot be the same");

        dijkstraAlgo(start, graph); // Use distance graph

        return buildPath(start, end);
    }

    public LinkedList<Vertex<T>> getShortestPathTime(Vertex<T> start, Vertex<T> end) {
        if (start == null || end == null) throw new IllegalArgumentException("Start and end vertices cannot be null");
        if (start.equals(end)) throw new IllegalArgumentException("Start and end vertices cannot be the same");

        dijkstraAlgo(start, graphTime); // Use time graph

        return buildPath(start, end);
    }

    public LinkedList<Vertex<T>> getShortestPathCost(Vertex<T> start, Vertex<T> end) {
        if (start == null || end == null) throw new IllegalArgumentException("Start and end vertices cannot be null");
        if (start.equals(end)) throw new IllegalArgumentException("Start and end vertices cannot be the same");

        dijkstraAlgo(start, graphCost); // Use cost graph

        return buildPath(start, end);
    }


    private LinkedList<Vertex<T>> buildPath(Vertex<T> start, Vertex<T> end) {
        LinkedList<Vertex<T>> path = new LinkedList<>();

        Vertex<T> current = end;
        while (current != null) {
            path.addFirst(current);
            current = previous.get(current.getData());
        }

        if (path.getFirst().equals(start)) return path;
        return null;
    }

}
