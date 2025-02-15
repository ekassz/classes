import csv
import numpy as np
import matplotlib.pyplot as plt
from scipy.cluster.hierarchy import dendrogram

#this website helped me understand more about clustering:
#https://www.mltut.com/hierarchical-clustering-in-python-step-by-step-complete-guide/
#https://anakin297.medium.com/clustering-with-python-hierarchical-clustering-a60688396945

def load_data(filepath):
    with open(filepath, encoding='utf-8') as f:
        csv_read = csv.DictReader(f)
        countries_dict = [dict(row) for row in csv_read]
    return countries_dict

def calc_features(row):
    features = []
    stats = ['Population', 'Net migration', 'GDP ($ per capita)', 'Literacy (%)', 'Phones (per 1000)', 'Infant mortality (per 1000 births)']

    #converting to float
    for stat in stats:
        try:
            features.append(float(row[stat].replace(',', '')))
        except ValueError:
            #test case default?
            features.append(0.0)

    features_dimension = np.array(features,dtype= np.float64)
    return features_dimension


def hac(features):
    n = len(features)
    #0s array for numPy array
    Z = np.zeros((n-1, 4))

    #1 distance matrix with euclidean distance
    distance_matrix = np.full((n,n), np.inf)

    for i in range(n):
        for j in range(i+1, n):
            distance = np.linalg.norm(features[i] - features[j])
            distance_matrix[i,j] = distance_matrix[j,i] = distance
    
    #2 cluster initialization
    clusters = list(range(n))
    clusters_size = np.ones(n,dtype=int)

    #3 cluster loop
    for k in range(n-1):
        #find closest pair of clusters, m=1 to skip cluster that starts
        i,j = np.unravel_index(np.argmin(distance_matrix), distance_matrix.shape)
        min_dist = distance_matrix[i, j]


        #update linkage matrix, distance, new size
        Z[k,0], Z[k,1] = sorted([clusters[i], clusters[j]])
        Z[k,2] = min_dist
        Z[k,3] = clusters_size[i] + clusters_size[j]

        #update cluster sizes
        clusters_size[i] += clusters_size[j]
        clusters_size[j] = 0

        #update distance matrix
        clusters[i] = n + k
        clusters[j] = -1  # Invalidate the merged cluster

        # Update distances for the new cluster
        for m in range(n):
            if m != i and m != j:
                distance_matrix[i, m] = distance_matrix[m, i] = max(distance_matrix[i, m], distance_matrix[j, m])
        distance_matrix[:, j] = distance_matrix[j, :] = np.inf

    Z[:, :2] = np.sort(Z[:, :2], axis=1)


    return Z

#These helped to understand what dendrogram was + how to use it
#https://www.geeksforgeeks.org/scipy-cluster-hierarchy-dendrogram/
#https://stackoverflow.com/questions/35873273/display-cluster-labels-for-a-scipy-dendrogram
def fig_hac(Z, names):
    fig = plt.figure()
    dn = dendrogram(Z, above_threshold_color="blue", 
                    leaf_rotation=60, labels=names)
    plt.show()
    return fig

def normalize_features(features):
    dimension = np.array(features, dtype=np.float64)
    means = np.mean(dimension, axis=0)
    s_d = np.std(dimension, axis= 0)
    normalize = (dimension - means) / s_d

    normalize_dimension = [np.array(vector, dtype=np.float64) for vector in normalize]

    return normalize_dimension

"""
def main():
    data = load_data('countries.csv')
    country_names = [row['Country'] for row in data]
    features = [calc_features(row) for row in data]
    features_normalized = normalize_features(features)
    n = 20
    Z_raw = hac(features[:n])
    Z_normalized = hac(features_normalized[:n])
    fig = fig_hac(Z_normalized, country_names[:n])
    plt.show()
    
if __name__ == "__main__":
    main()
"""