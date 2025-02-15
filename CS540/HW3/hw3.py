from scipy.linalg import eigh
import numpy as np
import matplotlib.pyplot as plt

def load_and_center_dataset(filename):
    # numpy load data
    x = np.load(filename)
    #mean
    mean_x = np.mean(x, axis=0)
    # x_i - mean_x
    center = x - mean_x
    
    return center

def get_covariance(dataset):
    # Your implementation goes here!
    n = len(dataset) #421
    if n == 0:
        raise ValueError("Dataset empty")
    
    #1. Transpose x_i -> x_i ^ T
    transpose_dataset = np.transpose(dataset)

    #2. Matrix Multiplication: Dot Product (x_i)(x_i)^T
    dot_product = np.dot(transpose_dataset, dataset)

    #3. Normalization: divide by n-1
    covariance_matrix = dot_product / (n-1)

    return covariance_matrix

def get_eig(S, m):
    # Your implementation goes here!
    eigenvalues, eigenvectors = eigh(S)

    #descending order to get largest m
    eigenvalues = eigenvalues[::-1]
    eigenvectors = eigenvectors[:, ::-1]

    #select m largest
    largest_eigvalues = eigenvalues[:m]
    largest_eigvectors = eigenvectors[:, :m]

    #make diag matrix for m largest values
    Lambda = np.diag(largest_eigvalues)

    #eigenvectors already arranged as columns
    U = largest_eigvectors
    return Lambda, U

def get_eig_prop(S, prop):
    # Your implementation goes here!
    # compute values using eig from covariance matrix S
    #same from above
    eigenvalues, eigenvectors = eigh(S)

    #descending order to get largest m
    eigenvalues = eigenvalues[::-1]
    eigenvectors = eigenvectors[:, ::-1]

    #now get sum of eigenvalues
    total_sum = sum(eigenvalues)

    #get cumulative sum , find # of eigenvalues to keep
    #cumsum is from numpy python - cumulative sum
    cum_sum = eigenvalues.cumsum()
    #to find m -> sum of first m largest eigenval >= p * total sum eigval
    m = np.argmax(cum_sum >= prop * total_sum) +1

    #Select m largest eigenvalues and their corresponding eigenvectors
    largest_eigenvalues = eigenvalues[:m]
    corresponding_eigvectors = eigenvectors[:, :m]

    #Now to return:
    #Make a diagonal matrix for selected eigenvalues
    Lambda = np.diag(largest_eigenvalues)

    #Make matrix for corresponding eigenvectors , column corresponds to eigenvector
    U = corresponding_eigvectors

    return Lambda, U

def project_image(image, U):
    # Your implementation goes here!
    raise NotImplementedError

def display_image(orig, proj):
    # Your implementation goes here!
    # Please use the format below to ensure grading consistency
    # fig, (ax1, ax2) = plt.subplots(figsize=(9,3), ncols=2)
    # return fig, ax1, ax2
    raise NotImplementedError

def main():
    #checking x for 5.1
    result = load_and_center_dataset(filename= 'Iris_64x64.npy')

    print(len(result))
    print(len(result[0]))
    print(np.average(result))

    covariance = get_covariance(result)
    print(covariance.shape)

    Lambda, U = get_eig(covariance, 2)
    print(Lambda)
    print(U)

    Lambda, U = get_eig_prop(covariance, 0.07)
    print(Lambda)
    print(U)


if __name__ == "__main__":
    main()