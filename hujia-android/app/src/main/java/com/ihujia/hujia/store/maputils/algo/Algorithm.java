package com.ihujia.hujia.store.maputils.algo;

import com.ihujia.hujia.store.maputils.Cluster;
import com.ihujia.hujia.store.maputils.ClusterItem;

import java.util.Collection;
import java.util.Set;

/**
 * Logic for computing clusters
 */
public interface Algorithm<T extends ClusterItem> {

	void addItem(T item);

	void addItems(Collection<T> items);

	void clearItems();

	void removeItem(T item);

	Set<? extends Cluster<T>> getClusters(double zoom);

	Collection<T> getItems();
}